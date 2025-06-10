package data;

import models.Genre;
import models.Movie;
import org.h2.tools.Script;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MoviesRepositoryIntegretionTest {

    private MoviesRepositoryJdbc moviesRepositoryJdbc;
    private DriverManagerDataSource dataSource;

    @BeforeEach
    void setUp() throws SQLException {
        String dbName = "test" + System.nanoTime(); // Nombre único para cada test
        dataSource = new DriverManagerDataSource("jdbc:h2:mem:" + dbName + ";MODE=MYSQL", "sa", "sa");

        ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("sql-scripts/test-data.sql"));

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        moviesRepositoryJdbc =  new MoviesRepositoryJdbc(jdbcTemplate);
    }


    @Test
    void load_all_movies() throws SQLException {

        Collection<Movie> movies = moviesRepositoryJdbc.findAll();

        assertThat(movies, is(Arrays.asList(
                new Movie(1, "Dark Knight", 152, Genre.ACTION),
                new Movie(2, "Memento", 113, Genre.THRILLER),
                new Movie(3, "Matrix", 136, Genre.ACTION)
        )));
    }

    @Test
    void load_movie_by_id(){

        Movie movieFromDB =  moviesRepositoryJdbc.findById(2);

        assertThat(movieFromDB, is(new Movie(2,"Memento",113,Genre.THRILLER)));

    }


}