package com.parceiroferramentas.api.parceiro_api.model;

import java.util.List;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class DataConverter {

    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <O, D> D convert(O source, Class<D> targetClass) {
        return mapper.map(source, targetClass);
    }

    public static <O, D> List<D> convert(List<O> source, Class<D> targetClass) {
        return source.stream()
                .map(element -> DataConverter.convert(element, targetClass))
                .toList();
    }
}
