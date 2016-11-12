package com.bitreight.tasklist.service.converter;

import java.util.List;
import java.util.stream.Collectors;

public interface GenericDtoConverter<E, D> {

    E convertDto(D dto);

    D convertEntity(E entity);

    default List<E> convertDtos(List<D> dtos) {
        return dtos.stream()
                .map(this::convertDto)
                .collect(Collectors.toList());
    }

    default List<D> convertEntities(List<E> entities) {
        return entities.stream()
                .map(this::convertEntity)
                .collect(Collectors.toList());
    }
}