package com.shodaqa.helpers.convertor;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public interface FormConvertToDomain<T, R> extends Function<T, R> {
	default List<R> convertToList(final List<T> input) {
		return input.stream().map(this::apply).collect(toList());
	}
}
