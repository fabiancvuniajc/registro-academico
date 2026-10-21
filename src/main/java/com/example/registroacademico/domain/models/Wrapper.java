package com.example.registroacademico.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wrapper<T> implements Serializable {

    private List<T> results;
    private int totalElements;
    private int totalPages;
    private int size;
    private int numberOfElements;
}
