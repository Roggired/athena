package ru.yofik.athena.messenger.api.http;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import ru.yofik.athena.common.domain.NewPage;
import ru.yofik.athena.common.domain.Page;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public abstract class AbstractPaginationResource {
    protected final ConversionService conversionService;


    protected <T, U> NewPage<U> mapPage(NewPage<T> page, Class<U> uClass) {
        return new NewPage<>(
                page.getMeta(),
                page.getContent()
                        .stream()
                        .map(el -> conversionService.convert(el, uClass))
                        .collect(Collectors.toList())
        );
    }
}
