package com.vpesotskii.repository;

import com.vpesotskii.model.Comment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Long postId, String text) {
        jdbcTemplate.update(
                "INSERT INTO comments (post_id, text) VALUES (?, ?)",
                postId,
                text
        );
    }

    @Override
    public void update(Long commentId, String text) {
        jdbcTemplate.update(
                "UPDATE comments SET text = ? WHERE id = ?",
                text,
                commentId
        );
    }

    @Override
    public void deleteById(Long commentId) {
        jdbcTemplate.update(
                "DELETE FROM comments WHERE id = ?",
                commentId
        );
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return jdbcTemplate.query(
                "SELECT * FROM comments WHERE post_id = ?",
                BeanPropertyRowMapper.newInstance(Comment.class),
                postId
        );
    }
}
