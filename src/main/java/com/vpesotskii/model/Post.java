package com.vpesotskii.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private Long id;
    private String title;
    private String imagePath;
    private String text;
    private Long likesCount;
    private String[] tags;
    private List<Comment> comments;
}
