package com.example.registroacademico.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wrapper<T> {

    private List<T> results;
    private int totalElements;
    private int totalPages;
    private int size;
    private int numberOfElements;
}
