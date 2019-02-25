package com.nbvarnado.popularmovies.model;

import java.util.List;

/**
 * Created by Nick Varnado on 2/24/2019.
 */

public class Page {

    private int page;
    private List<Movie> results;

    /**
     * Constructor
     */
    public Page() {

    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

}
