package com.vpesotskii.repository;

import com.vpesotskii.model.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findPosts(String search, Integer pageSize, Integer offset);

    Post getPostById(Long postId);

    Long save(Post post);

    void updateImagePath(Long postId, String imagePath);

    void updateLikesCount(Long postId, Integer likesCount);

    void update(Post post);

    void deleteById(Long postId);
}
