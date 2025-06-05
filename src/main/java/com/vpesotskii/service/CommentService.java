package com.vpesotskii.service;

import com.vpesotskii.repository.CommentRepository;

public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void addComment(Long postId, String text) {
        commentRepository.save(postId, text);
    }

    public void editComment(Long commentId, String text) {
        commentRepository.update(commentId, text);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
