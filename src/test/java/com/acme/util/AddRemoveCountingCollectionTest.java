package com.acme.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class AddRemoveCountingCollectionTest {

    @Test
    void shouldTrackSingleAddCount() {
        var c = new AddRemoveCountingCollection<>(new ArrayList<Integer>());
        c.add(1);
        c.add(42);
        c.add(84);
        assertThat(c.getAddCount()).isEqualTo(3);
    }

    @Test
    void shouldTrackMultipleAddCount() {
        var c = new AddRemoveCountingCollection<>(new ArrayList<Integer>());
        c.addAll(List.of(1, 4, 10));
        c.addAll(List.of());
        c.addAll(List.of(42));
        assertThat(c.getAddCount()).isEqualTo(4);
    }

    @Test
    void shouldTrackSingleRemoveCount() {
        var c = new AddRemoveCountingCollection<>(new ArrayList<String>());
        c.add("a");
        c.add("b");
        c.add("c");
        c.remove("a");
        c.remove("c");
        assertThat(c.getRemoveCount()).isEqualTo(2);
    }

    @Test
    void shouldTrackRemoveCountEvenWhenNothingRemoved() {
        var c = new AddRemoveCountingCollection<>(new ArrayList<String>());
        c.remove("foo");
        c.remove("bar");
        assertThat(c.getRemoveCount()).isEqualTo(2);
    }
}
