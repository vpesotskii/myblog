package com.vpesotskii.controller;

import com.vpesotskii.model.Page;
import com.vpesotskii.model.Post;
import com.vpesotskii.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String findPosts(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            Model model) {

        List<Post> posts = postService.findPosts(search, pageNumber, pageSize);
        model.addAttribute("posts", posts);
        model.addAttribute("search", search);
        model.addAttribute("paging", new Page(pageNumber, pageSize, posts.size()));

        return "posts";
    }

    /**
     * Страница с постом
     *
     * @param postId Идентификатор поста
     * @param model  Модель
     * @return Шаблон "post.html"
     */
    @GetMapping("/{id}")
    public String getPostById(
            @PathVariable("id") Long postId,
            Model model) {

        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);

        return "post";
    }

    /**
     * Добавление поста
     *
     * @param title Название поста
     * @param text  Текст поста
     * @param image Файл картинки поста
     * @param tags  Список тегов поста (по умолчанию, пустая строка)
     * @return Редирект на созданный "/posts/{id}"
     */
    @PostMapping
    public String addPost(
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestPart(name = "image", required = false) String image,
            @RequestParam(name = "tags", required = false) String tags) {

        Long postId = postService.addPost(title, text, image, tags);

        return "redirect:/posts/" + postId;
    }

    /**
     * Увеличение/уменьшение числа лайков поста
     *
     * @param postId Идентификатор поста
     * @param like   Если true, то +1 лайк, если "false", то -1 лайк
     * @return Редирект на "/posts/{id}"
     */
    @PostMapping("/{id}/like")
    public String likePostById(
            @PathVariable("id") Long postId,
            @RequestParam("like") Boolean like
    ) {

        postService.likePostById(postId, like);
        return "redirect:/posts/" + postId;
    }

    /**
     * Страница редактирования поста
     *
     * @param model  Модель
     * @param postId Идентификатор поста
     * @return Редирект на форму редактирования поста "add-post.html"
     */
//    @GetMapping("/{id}/edit")
//    public String getEditingForm(
//            Model model,
//            @PathVariable("id") Long postId
//    ) {
//        EditPostDto post = postService.getEditPostDtoById(postId);
//        model.addAttribute("post", post);
//
//        return "add-post";
//    }

    /**
     * Редактирование пост
     *
     * @param postId Идентификатор поста
     * @param title  Название поста
     * @param text   Текст поста
     * @param image  Файл картинки поста (класс MultipartFile, может быть null - значит, остается прежним)
     * @param tags   Список тегов поста (по умолчанию, пустая строка)
     * @return Редирект на отредактированный "/posts/{id}"
     */
    @PostMapping("{id}")
    public String editPost(
            @PathVariable("id") Long postId,
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestPart(name = "image", required = false) String image,
            @RequestParam(name = "tags", required = false) String tags) {

        postService.editPost(postId, title, text, image, tags);

        return "redirect:/posts/" + postId;
    }

    /**
     * Эндпоинт удаления поста
     *
     * @param postId Идентификатор поста
     * @return Редирект на "/posts"
     */
    @PostMapping(value = "/{id}/delete")
    public String deletePost(@PathVariable("id") Long postId) {
        postService.deletePost(postId);

        return "redirect:/posts";
    }
}