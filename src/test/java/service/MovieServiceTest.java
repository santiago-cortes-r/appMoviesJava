package service;

import data.MoviesRepository;
import models.Genre;
import models.Movie;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MoviesRepository moviesRepository = Mockito.mock(MoviesRepository.class);

        Mockito.when(moviesRepository.findAll()).thenReturn(
                Arrays.asList(
                        new Movie(1, "Dark Knight", 152, Genre.ACTION),
                        new Movie(2, "Memento", 113, Genre.THRILLER),
                        new Movie(3, "There's Something About Marty", 119, Genre.COMEDY),
                        new Movie(4, "Super 8", 112, Genre.THRILLER),
                        new Movie(5, "Scream", 111, Genre.HORROR),
                        new Movie(6, "Home Alone", 103, Genre.COMEDY),
                        new Movie(7, "Matrix", 136, Genre.ACTION)
                )
        );

        movieService = new MovieService(moviesRepository);
    }



    @Test
    void return_movies_by_genre() {


        Collection<Movie> movies = movieService.findMoviesByGenre(Genre.ACTION);

        List<Integer> moviesIds = movies.stream().map(movie -> movie.getId()).collect(Collectors.toList());

        assertThat(moviesIds, CoreMatchers.is(Arrays.asList(1,7)));
    }

    @Test
    void return_movies_by_duration_time() {

        Collection<Movie> movies = movieService.findMoviesByDuratinTime(113);

        List<Integer> moviesDuration = movies.stream().map(movie -> movie.getId()).collect(Collectors.toList());

        assertThat(moviesDuration, CoreMatchers.is(Arrays.asList(2,4,5,6)));
    }
}