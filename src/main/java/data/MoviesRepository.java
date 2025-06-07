package data;

import models.Movie;

import java.util.Collection;

public interface MoviesRepository {
    Movie findById(long id);
    Collection<Movie> findAll();
    void saveOrUpdate(Movie movie);
}
