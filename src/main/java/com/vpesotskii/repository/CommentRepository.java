package com.vpesotskii.repository;

import com.vpesotskii.model.Comment;

import java.util.List;

public interface CommentRepository {
    void save(Long postId, String text);
    void update(Long commentId, String text);
    void deleteById(Long commentId);
    List<Comment> findByPostId(Long postId);
}
