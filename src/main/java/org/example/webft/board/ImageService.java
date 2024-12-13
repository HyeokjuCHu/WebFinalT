package org.example.webft.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    @Autowired
    ImageDAO imageDAO;

    public List<ImageVO> getImageList() {
        return imageDAO.getImageList();
    }

    public int insertImage(ImageVO vo) {
        return imageDAO.insertImage(vo);
    }

    public int deleteImage(int id) {
        return imageDAO.deleteImage(id);
    }

    public int updateImage(ImageVO vo) {
        return imageDAO.updateImage(vo);
    }

    public ImageVO getImage(int id) {
        return imageDAO.getImage(id);
    }

    public int getTotalCnt() {
        return imageDAO.getTotalCnt();
    }

    public void incrementViewCount(int id) {
        imageDAO.incrementViewCount(id);
    }

    public ImageVO getPostById(int id) {
        return imageDAO.getPostById(id);
    }

    public void incrementLikes(int postId) {
        imageDAO.incrementLikes(postId); // DAO에서 좋아요 수 증가
    }

    public String getFilenameById(int id) {
        return imageDAO.getFilenameById(id);
    }

    public List<ImageVO> searchImages(String keyword) {
        return imageDAO.searchImages(keyword);
    }

    public int getSearchResultCount(String keyword) {
        return imageDAO.getSearchResultCount(keyword);
    }

}