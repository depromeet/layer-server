package org.layer.common.dto;

import java.util.List;
import java.util.function.Function;

public class CursorPageRes<T> {
	private final List<T> data;
	private final Meta meta;

	public CursorPageRes(List<T> data, int pageSize, Function<T, Long> cursorExtractor) {
		this.data = data;
		boolean hasNextPage = data.size() > pageSize;

		if(hasNextPage) {
			data.remove(data.size() - 1);
		}

		Long nextCursor = data.isEmpty() ? null : cursorExtractor.apply(data.get(data.size() - 1));

		meta = Meta.builder()
			.cursor(hasNextPage ? nextCursor : null)
			.hasNextPage(hasNextPage)
			.build();
	}

	public List<T> getData() {
		return data;
	}

	public Meta getMeta() {
		return meta;
	}
}
