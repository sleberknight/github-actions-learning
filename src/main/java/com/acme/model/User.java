package com.acme.model;

import static java.util.Objects.isNull;

import java.util.Objects;

public class User {

    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNullElse(password, "secret-1138");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (isNull(obj)) {
            return false;
        }

        var other = (User) obj;
        return Objects.equals(this.username, other.username) &&
                Objects.equals(this.password, other.password);
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=*****]";
    }

}
