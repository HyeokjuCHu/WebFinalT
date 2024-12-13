package org.example.webft.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    UserServiceImpl service;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login"; // 로그인 페이지로 이동
    }

    @RequestMapping(value = "/loginOk", method = RequestMethod.POST)
    public String loginCheck(HttpSession session, UserVO vo) {
        String returnURL = "";
        if (session.getAttribute("login") != null) {
            session.removeAttribute("login"); // 기존 세션 제거
        }
        UserVO loginVO = service.getUser(vo);
        if (loginVO != null) { // 로그인 성공
            System.out.println("로그인 성공!");
            session.setAttribute("login", loginVO); // 세션에 로그인 정보 저장
            returnURL = "redirect:/board/list"; // 게시글 목록 페이지로 리다이렉트
        } else { // 로그인 실패
            System.out.println("로그인 실패!");
            returnURL = "redirect:/login/login";
        }
        return returnURL;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login/login"; // 로그인 페이지로 이동
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup"; // 회원가입 폼 JSP
    }

    @PostMapping("/signupOk")
    public String signup(UserVO user, RedirectAttributes rttr) {
        int i = service.insertUser(user); // 삽입된 행의 수 반환

        if (i > 0) { // 성공적으로 회원가입된 경우
            rttr.addFlashAttribute("msg", "회원가입이 완료되었습니다.");
            return "redirect:/login/login"; // 로그인 페이지로 이동
        } else { // 실패한 경우
            rttr.addFlashAttribute("msg", "회원가입에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/signup"; // 회원가입 페이지로 다시 이동
        }
    }

}