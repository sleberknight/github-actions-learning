package com.acme.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void shouldRequireUsername() {
        assertThatNullPointerException().isThrownBy(() -> new User(null, "THX1138"));
    }

    @Test
    void shouldProvideDefaultPasswordIfNotProvided() {
        var u = new User("jyn", null);
        assertThat(u.getPassword()).isEqualTo("secret-1138");
    }

    @Test
    void shouldEqual() {
        var u1 = new User("bob", "monkey-123");
        var u2 = new User("bob", "monkey-123");
        var u3 = new User("alice", "basura");

        assertThat(u1).isEqualTo(u2).isNotEqualTo(u3);
    }

    @Test
    void shouldHash() {
        var u1 = new User("alice", "basura");
        var u2 = new User("alice", "basura");
        var u3 = new User("bob", "monkey-123");

        assertThat(u1).hasSameHashCodeAs(u2);
        assertThat(u1.hashCode()).isNotEqualTo(u3.hashCode());
    }
}
