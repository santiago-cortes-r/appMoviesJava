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

        Collection<Movie> moviesGenreSelect = moviesRepository.findAll().stream().filter(Movie -> Movie.getGenre() == genre).collect(Collectors.toList());
        return moviesGenreSelect;
    }


    public Collection<Movie> findMoviesByDuratinTime(int duration) {
        Collection<Movie> moviesDurationSelect = moviesRepository.findAll().stream().filter(Movie -> Movie.getMinutes() <= duration).collect(Collectors.toList());
        return moviesDurationSelect;
    }
}
