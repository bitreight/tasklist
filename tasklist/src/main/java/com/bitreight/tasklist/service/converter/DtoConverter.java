package com.bitreight.tasklist.service.converter;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DtoConverter<E, D> {

    public abstract E convertDto(D dto);

    public abstract D convertEntity(E entity);

    public List<E> convertDtos(List<D> dtos) {
        return dtos.stream()
                .map(this::convertDto)
                .collect(Collectors.toList());
    }

    public List<D> convertEntities(List<E> entities) {
        return entities.stream()
                .map(this::convertEntity)
                .collect(Collectors.toList());
    }
}