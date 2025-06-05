package com.vpesotskii.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Page {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalSize;

    public Boolean hasNext() {
        return (pageNumber - 1) * pageSize < totalSize;
    }

    public Boolean hasPrevious() {
        return (pageNumber - 1) * pageSize > totalSize;
    }
}
