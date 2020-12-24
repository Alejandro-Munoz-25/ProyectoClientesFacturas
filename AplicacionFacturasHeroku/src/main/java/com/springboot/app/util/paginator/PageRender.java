package com.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {
	private String url;
	private Page<T> page;
	private int totalPages;
	private int numItemsPerPage;
	private int actualPage;
	private List<PageItem> pages;

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<PageItem>();
		this.numItemsPerPage = page.getSize();
		this.totalPages = page.getTotalPages();
		this.actualPage = page.getNumber() + 1;
		int since, until;

		if (totalPages <= numItemsPerPage) {
			since = 1;
			until = totalPages;
		} else {
			if (actualPage <= numItemsPerPage / 2) {
				since = 1;
				until = numItemsPerPage;
			} else if (actualPage >= totalPages - numItemsPerPage / 2) {
				since = totalPages - numItemsPerPage + 1;
				until = numItemsPerPage;
			} else {
				since = actualPage - numItemsPerPage / 2;
				until = numItemsPerPage;
			}
		}
		for (int i = 0; i < until; i++) {
			pages.add(new PageItem(since + i, actualPage == since + i));
		}
	}

	public String getUrl() {
		return url;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getActualPage() {
		return actualPage;
	}

	public List<PageItem> getPages() {
		return pages;
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
}
