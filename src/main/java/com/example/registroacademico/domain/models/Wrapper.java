package com.example.registroacademico.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wrapper<T> {

    private List<T> result;
    private int totalElements;
    private int totalPages;
    private int page;
    private int size;
}
