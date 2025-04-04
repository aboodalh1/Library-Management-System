package com.library.book.response;

import java.util.List;

public class BookResponsePagination {

    public List<BooksResponse> getBooks() {
        return books;
    }

    public void setBooks(List<BooksResponse> books) {
        this.books = books;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    private List<BooksResponse> books;
    private int page;
    private int perPage;
    private long total;
}
