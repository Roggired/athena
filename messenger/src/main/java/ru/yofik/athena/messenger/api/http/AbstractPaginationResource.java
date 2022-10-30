package ru.yofik.athena.messenger.api.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import ru.yofik.athena.common.domain.Page;

import java.util.stream.Collectors;

@Component
public abstract class AbstractPaginationResource {
    @Autowired
    protected ConversionService conversionService;


    protected <T, U> Page<U> mapPage(Page<T> page, Class<U> uClass) {
        return new Page<>(
                page.getMeta(),
                page.getContent()
                        .stream()
                        .map(el -> conversionService.convert(el, uClass))
                        .collect(Collectors.toList())
        );
    }
}
