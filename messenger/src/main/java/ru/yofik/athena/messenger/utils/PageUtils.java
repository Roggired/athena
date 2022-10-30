package ru.yofik.athena.messenger.utils;

import ru.yofik.athena.common.domain.Page;

import java.util.ArrayList;

public final class PageUtils {
    public static <T> Page<T> fromSpringPage(org.springframework.data.domain.Page<T> springPage) {
        return new Page<>(
                new Page.Meta(
                        springPage.getPageable().getPageNumber(),
                        springPage.getPageable().getPageSize()
                ),
                new ArrayList<>(springPage.getContent())
        );
    }
}
