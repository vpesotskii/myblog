package com.vpesotskii.repository;

import com.vpesotskii.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import static io.micrometer.common.util.StringUtils.isEmpty;

public class JdbcPostRepository implements PostRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcPostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findPosts(String search, Integer pageSize, Integer offset) {
        return jdbcTemplate.query(
                "SELECT id, title, image_path, text, likes_count, tags FROM posts "
                        + (isEmpty(search) ? "" : "WHERE tags like " + search )
                        + "LIMIT ? "
                        + "OFFSET ? ",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("image_path"),
                        rs.getString("text"),
                        rs.getLong("likes_count"),
                        rs.getString("tags").split(","),
                        Collections.EMPTY_LIST
                ),
                pageSize,
                offset);
    }

    @Override
    public Post getPostById(Long postId) {
            return jdbcTemplate.queryForObject(
                    "SELECT id, title, image_path, text, likes_count, tags FROM posts "
                            + "WHERE id = ? ",
                    (rs, rowNum) -> new Post(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("image_path"),
                            rs.getString("text"),
                            rs.getLong("likes_count"),
                            rs.getString("tags").split(","),
                            Collections.EMPTY_LIST
                    ),
                    postId
            );
    }

    @Override
    public Long save(Post post) {
        String sqlQuery = "INSERT INTO posts (title, image_path, text, likes_count, tags) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getImagePath());
            ps.setString(3, post.getText());
            ps.setLong(4, post.getLikesCount());
            ps.setArray(5, connection.createArrayOf("VARCHAR", post.getTags()));
            return ps;
        }, keyHolder);
        return (Long) keyHolder.getKeyList().get(0).get("id");
    }

    @Override
    public void updateImagePath(Long postId, String imagePath) {
        jdbcTemplate.update("UPDATE posts SET image_path = ? WHERE id = ?", imagePath, postId);
    }

    @Override
    public void updateLikesCount(Long postId, Integer likesCount) {
        jdbcTemplate.update("UPDATE posts SET likes_count = likes_count + ? WHERE id = ?", likesCount, postId);
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update(
                "UPDATE posts SET title = ?, image_path = ?, text = ?, tags = string_to_array(?,',') WHERE id = ?",
                post.getTitle(),
                post.getImagePath(),
                post.getText(),
                String.join(",", post.getTags()),
                post.getId()
        );
    }

    @Override
    public void deleteById(Long postId) {
        jdbcTemplate.update(
                "DELETE FROM posts WHERE id = ?",
                postId
        );
    }
}
