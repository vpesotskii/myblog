package com.vpesotskii.service;

import com.vpesotskii.model.Comment;
import com.vpesotskii.model.Post;
import com.vpesotskii.repository.CommentRepository;
import com.vpesotskii.repository.PostRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public List<Post> findPosts(String search, Integer pageNumber, Integer pageSize) {
        Integer offset = (pageNumber - 1) * pageSize;

        return postRepository.findPosts(search, pageSize, offset);
    }


    public Post getPostById(Long postId) {
        return postRepository.getPostById(postId);

    }


//    public EditPostDto getEditPostDtoById(Long postId) {
//        return postMapper.toEditPostDto(getPostById(postId));
//    }

    public Long addPost(String title, String text, String image, String tags) {

        Post post = Post.builder()
                .title(title)
                .text(text)
                .likesCount(0L)
                .tags(tags.split(","))
                .build();

        Long postId = postRepository.save(post);

        return postId;
    }

    public void likePostById(Long postId, Boolean like) {
        postRepository.updateLikesCount(postId, like ? 1 : -1);
    }

    public void editPost(Long postId, String title, String text, String imagePath, String tags) {


        Post post = Post.builder()
                .id(postId)
                .title(title)
                .imagePath(imagePath)
                .text(text)
                .tags(tags.split(","))
                .build();

        postRepository.update(post);
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    private Post enrichPost(Post post) {
        List<Comment> comments = commentRepository.findByPostId(post.getId());
        post.setComments(comments);
        return post;
    }

}
