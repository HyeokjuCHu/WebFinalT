package org.example.webft.board;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageDAO {

    @Autowired
    private SqlSession sqlSession;

    // 게시글 삽입
    public int insertImage(ImageVO vo) {
        return sqlSession.insert("Image.insertImage", vo);
    }

    // 게시글 수정
    public int updateImage(ImageVO vo) {
        return sqlSession.update("Image.updateImage", vo);
    }

    // 게시글 삭제
    public int deleteImage(int seq) {
        return sqlSession.delete("Image.deleteImage", seq);
    }

    // 특정 게시글 조회
    public ImageVO getImage(int seq) {
        return sqlSession.selectOne("Image.getImage", seq);
    }

    // 게시글 총 개수 조회
    public int getTotalCnt() {
        return sqlSession.selectOne("Image.getImageCnt");
    }

    // 게시글 목록 조회
    public List<ImageVO> getImageList() {
        return sqlSession.selectList("Image.getImageList");
    }

    public void incrementViewCount(int seq) {
        sqlSession.update("Image.incrementViewCount", seq);
    }

    public ImageVO getPostById(int seq) {
        return sqlSession.selectOne("Image.getPostById", seq);
    }

    public void incrementLikes(int seq) {
        sqlSession.update("Image.incrementLikes", seq);
    }

    public String getFilenameById(int seq) {
        return sqlSession.selectOne("Image.getFilenameById", seq);
    }

    public List<ImageVO> searchImages(String keyword) {
        return sqlSession.selectList("Image.searchImages", keyword);
    }

    public int getSearchResultCount(String keyword) {
        return sqlSession.selectOne("Image.getSearchResultCount", keyword);
    }

}