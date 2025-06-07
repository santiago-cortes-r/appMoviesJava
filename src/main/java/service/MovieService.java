package service;

import data.MoviesRepository;
import models.Genre;
import models.Movie;

import java.util.Collection;
import java.util.stream.Collectors;

public class MovieService {

    private MoviesRepository moviesRepository;

    public MovieService(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    public Collection findMoviesByGenre(Genre genre) {

        Collection<Movie> allMovies = moviesRepository.findAll().stream().toList().stream().filter(Movie -> Movie.getGenre() == genre).collect(Collectors.toList());
        return allMovies;
    }


}
