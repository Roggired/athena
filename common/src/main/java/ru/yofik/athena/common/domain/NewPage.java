package ru.yofik.athena.common.domain;

import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
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
public class NewPage<T>{
    private final Meta meta;
    private final List<T> content;


    public Stream<T> stream() {
        return content.stream();
    }

    public <U> NewPage<U> map(Function<T, U> mapper) {
        return new NewPage<>(
                meta,
                content.stream()
                        .map(mapper)
                        .collect(Collectors.toList())
        );
    }

    public NewPage<T> sort(Comparator<T> comparator) {
        content.sort(comparator);
        return this;
    }

    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Meta {
        public List<String> sort = Collections.emptyList();
        @PositiveOrZero
        public int sequentialNumber;
        @Positive
        public int size;
        public long elementsTotal;
    }
}
