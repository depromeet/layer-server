package org.layer.common.dto;

public class CursorPageReq {
	private final long cursorId;
	private final int pageSize;

	public CursorPageReq(long cursorId, int pageSize) {
		if(pageSize <= 0) {
			pageSize = 1;
		}

		this.cursorId = cursorId;
		this.pageSize = pageSize;
	}

	public boolean isFirstPage() {
		return cursorId == 0L;
	}

	public long getCursorId() {
		return cursorId;
	}

	/**
	 * 클라이언트가 요청한 페이지 사이즈
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 쿼리에 사용할 실제 조회 크기 (hasNext 판단을 위해 +1)
	 */
	public int getFetchSize() {
		return pageSize + 1;
	}
}
