package ru.yofik.athena.common.domain;

import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Page<T>{
    private final Meta meta;
    private final List<T> content;


    public Stream<T> stream() {
        return content.stream();
    }

    public <U> Page<U> map(Function<T, U> mapper) {
        return new Page<>(
                meta,
                content.stream()
                        .map(mapper)
                        .collect(Collectors.toList())
        );
    }

    public Page<T> sort(Comparator<T> comparator) {
        content.sort(comparator);
        return this;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Meta {
        @PositiveOrZero
        private final int sequentialNumber;
        @Positive
        private final int size;
    }
}
