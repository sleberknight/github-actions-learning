package com.acme.model;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

class PersonTest {

    @Test
    void shouldRequireFirstName() {
        assertThatNullPointerException().isThrownBy(() ->
                Person.builder().lastName("Smith").dateOfBirth(LocalDate.of(1973, Month.DECEMBER, 15)).build());
    }

    @Test
    void shouldRequireLastName() {
        assertThatNullPointerException().isThrownBy(() ->
                Person.builder().firstName("Bob").dateOfBirth(LocalDate.of(1977, Month.JULY, 17)).build());
    }

    @Test
    void shouldRequireDateOfBirth() {
        assertThatNullPointerException().isThrownBy(() ->
                Person.builder().firstName("Bob").lastName("Jones").build());
    }
}
