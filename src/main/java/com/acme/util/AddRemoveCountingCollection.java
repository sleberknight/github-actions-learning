package com.acme.util;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.util.Collection;

@RequiredArgsConstructor
public class AddRemoveCountingCollection<E> implements Collection<E> {

    private interface CountingExclusions<E> {
        boolean add(E item);
        boolean addAll(Collection<? extends E> c);
        boolean remove(E item);
        boolean removeAll(Collection<?> c);
    }

    @NonNull
    @Delegate(excludes = CountingExclusions.class)
    private final Collection<E> collection;

    @Getter
    private int addCount;

    @Getter
    private int removeCount;

    @Override
    public boolean add(E e) {
        ++addCount;
        return collection.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return collection.addAll(c);
    }

    @Override
    public boolean remove(Object e) {
        ++removeCount;
        return collection.remove(e);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        removeCount += c.size();
        return collection.removeAll(c);
    }

    public int getAddRemoveCount() {
        return addCount + removeCount;
    }
}
