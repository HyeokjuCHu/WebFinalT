package org.example.webft.board;

import org.example.webft.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/board/list", method = RequestMethod.GET)
    public String boardList(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 검색어가 있을 경우, 검색 결과 반환
            model.addAttribute("list", imageService.searchImages(keyword));
            model.addAttribute("totalcnt", imageService.getSearchResultCount(keyword));
        } else {
            // 검색어가 없을 경우, 전체 리스트 반환
            model.addAttribute("list", imageService.getImageList());
            model.addAttribute("totalcnt", imageService.getTotalCnt());
        }
        return "posts";
    }

    @RequestMapping(value = "/board/add", method = RequestMethod.GET)
    public String boardAdd() {
        return "addpostform";
    }

    @RequestMapping(value = "/board/addok", method = RequestMethod.POST)
    public String boardAddOK(HttpSession session,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) throws IOException {

        // 세션에서 로그인 사용자 정보 가져오기
        UserVO loginVO = (UserVO) session.getAttribute("login");
        ImageVO vo = new ImageVO();
        vo.setUserid(loginVO.getUserid());

        // MultipartHttpServletRequest 사용하기
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

        // 파일 받기
        MultipartFile image = multiRequest.getFile("image");

        // 업로드 파일이 저장될 실제 경로 가져오기
        String realPath = request.getServletContext().getRealPath("/upload");

        // 경로에 해당하는 디렉토리가 존재하지 않으면 생성
        File dir = new File(realPath);
        if (!dir.exists()) {
            dir.mkdirs();  // 디렉토리 생성
        }

        // 이미지 파일 처리
        if (!image.isEmpty()) {
            // 파일 이름 중복 방지 (UUID 또는 타임스탬프 추가)
            String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();  // 예: 1617876567000_cake-8233676.jpg

            // 저장될 파일 경로 (upload 디렉토리 내)
            File destFile = new File(realPath + "/" + filename);  // 실제 파일이 저장될 경로
            image.transferTo(destFile);  // 파일 업로드 처리

            // DB에 저장할 파일 정보 설정
            vo.setFilename(filename);  // 파일 이름
            vo.setImagePath("/upload/" + filename);  // 파일 경로 (DB에 저장될 경로)
        }

        // 제목, 설명 등 다른 입력 값 설정
        vo.setTitle(multiRequest.getParameter("title"));
        vo.setDescription(multiRequest.getParameter("description"));

        // 데이터베이스에 게시글 추가
        int i = imageService.insertImage(vo);
        if (i == 0) {
            System.out.println("데이터 추가 실패!");
        } else {
            System.out.println("데이터 추가 성공!");
        }

        redirectAttributes.addFlashAttribute("message", "게시글 추가 성공");
        return "redirect:/board/list";
    }

    @RequestMapping(value = "/board/edit/{id}", method = RequestMethod.GET)
    public String boardEdit(@PathVariable("id") Integer id, HttpSession session, Model model) {
        ImageVO vo = imageService.getImage(id);

        // 로그인한 사용자가 작성한 글인지 확인
        UserVO loginVO = (UserVO) session.getAttribute("login");
        if (vo.getUserid() == null || !vo.getUserid().equals(loginVO.getUserid())) {
            System.out.println("권한이 없습니다! 작성자만 수정할 수 있습니다.");
            return "redirect:/board/list"; // 권한 없으면 리스트로 리다이렉트
        }

        model.addAttribute("imageVO", vo);
        return "editform";
    }

    @RequestMapping(value = "/board/editok", method = RequestMethod.POST)
    public String boardEditOk(HttpSession session,
                              MultipartHttpServletRequest multiRequest,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest request) throws IOException {

        // 세션에서 로그인한 사용자 정보 가져오기
        UserVO loginVO = (UserVO) session.getAttribute("login");

        // edit 페이지에서 id 파라미터를 가져오기
        int id = Integer.parseInt(multiRequest.getParameter("id"));

        // 게시글 정보 가져오기
        ImageVO vo = imageService.getImage(id);

        // 로그인한 사용자가 작성한 글인지 확인
        if (vo.getUserid() == null || !vo.getUserid().equals(loginVO.getUserid())) {
            System.out.println("권한이 없습니다! 작성자만 수정할 수 있습니다.");
            return "redirect:/board/list"; // 권한 없으면 리스트로 리다이렉트
        }

        vo.setUserid(loginVO.getUserid()); // 세션에서 사용자 ID 설정
        vo.setId(id);  // 게시글 ID 설정

        // 제목과 설명 파라미터 가져오기 (MultipartHttpServletRequest로 처리)
        String title = multiRequest.getParameter("title");
        String description = multiRequest.getParameter("description");

        vo.setTitle(title);  // 제목 설정
        vo.setDescription(description);  // 설명 설정

        // 업로드 파일이 저장될 실제 경로 가져오기
        String realPath = request.getServletContext().getRealPath("/upload");

        // 경로에 해당하는 디렉토리가 존재하지 않으면 생성
        File dir = new File(realPath);
        if (!dir.exists()) {
            dir.mkdirs();  // 디렉토리 생성
        }

        // 새로운 이미지 파일이 업로드되었을 경우 처리
        MultipartFile image = multiRequest.getFile("image");

        if (image != null && !image.isEmpty()) {
            // 파일 이름 중복 방지 (UUID 또는 타임스탬프 추가)
            String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();  // 예: 1617876567000_cake-8233676.jpg

            // 저장될 파일 경로 (upload 디렉토리 내)
            File destFile = new File(realPath + "/" + filename);  // 실제 파일이 저장될 경로
            image.transferTo(destFile);  // 파일 업로드 처리

            // 새 이미지 경로 설정
            vo.setFilename(filename);  // 파일 이름
            vo.setImagePath("/upload/" + filename);  // 새 이미지 경로 (DB에 저장될 경로)
        } else {
            // 이미지가 업로드되지 않으면 기존의 이미지 경로를 그대로 사용
            vo.setImagePath(multiRequest.getParameter("currentPhoto"));  // 기존 이미지 경로 사용
        }

        // 데이터베이스에 게시글 수정
        int i = imageService.updateImage(vo);
        if (i == 0) {
            System.out.println("데이터 수정 실패!");
        } else {
            System.out.println("데이터 수정 성공!");
        }

        redirectAttributes.addFlashAttribute("message", "게시글 수정 성공");
        return "redirect:/board/list";
    }

    @RequestMapping(value = "/board/delete/{id}", method = RequestMethod.GET)
    public String boardDelete(@PathVariable("id") Integer id, HttpSession session) {
        ImageVO vo = imageService.getImage(id);

        // 로그인한 사용자가 작성한 글인지 확인
        UserVO loginVO = (UserVO) session.getAttribute("login");

        // 작성자와 로그인한 사용자가 일치하는지 확인
        if (vo.getUserid() == null || !vo.getUserid().equals(loginVO.getUserid())) {
            System.out.println("권한이 없습니다! 작성자만 삭제할 수 있습니다.");
            return "redirect:/board/list"; // 권한이 없으면 리스트로 리다이렉트
        }

        int i = imageService.deleteImage(id);
        if (i == 0) {
            System.out.println("데이터 삭제 실패!");
        } else {
            System.out.println("데이터 삭제 성공!");
        }

        return "redirect:/board/list";
    }

    @GetMapping("/board/view/{id}")
    public String viewPost(@PathVariable("id") int id, Model model) {
        // 조회수 증가
        imageService.incrementViewCount(id);

        // 게시물 조회
        ImageVO post = imageService.getPostById(id);
        model.addAttribute("post", post);

        return "view"; // view.jsp로 이동
    }

    @PostMapping("board/view/like/{id}")
    public String likePost(@PathVariable("id") int id) {
        // 게시글의 좋아요 수 증가
        imageService.incrementLikes(id);
        return "redirect:/board/view/" + id; // 좋아요 후 해당 게시글로 리다이렉트
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") int id, HttpServletRequest request) {
        // 데이터베이스에서 파일 이름 가져오기
        String filename = imageService.getFilenameById(id);

        if (filename == null || filename.isEmpty()) {
            throw new RuntimeException("File not found for this post");
        }

        try {
            // 업로드 디렉토리의 실제 경로 가져오기
            String realPath = request.getServletContext().getRealPath("/upload");

            // 파일 경로 설정
            Path filePath = Paths.get(realPath).resolve(filename).normalize();

            // 파일 존재 여부 확인
            if (!Files.exists(filePath)) {
                throw new RuntimeException("File not found: " + filename);
            }

            // 파일 리소스 가져오기
            Resource resource = new UrlResource(filePath.toUri());

            // 파일 다운로드 응답 반환
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("Error while downloading file", e);
        }
    }

}