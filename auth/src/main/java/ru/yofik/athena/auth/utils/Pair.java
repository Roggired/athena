package ru.yofik.athena.auth.utils;

public final class Pair<T, R> {
    private final T first;
    private final R second;

    private Pair(T first, R second) {
        this.first = first;
        this.second = second;
    }

    public static <T, R> Pair<T, R> of(T first, R second) {
        return new Pair<>(first, second);
    }

    public T left() { return first; }
    public T first() { return first; }
    public R right() { return second; }
    public R second() { return second; }
}
