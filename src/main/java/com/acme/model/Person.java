package com.acme.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import java.time.LocalDate;

@Value
@Builder
public class Person {

    @With
    Long id;

    @NonNull
    String firstName;

    @NonNull
    String lastName;

    @NonNull
    LocalDate dateOfBirth;
}
