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
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MoviesRepositoryIntegretionTest {

    private MoviesRepositoryJdbc moviesRepositoryJdbc;
    private DriverManagerDataSource dataSource;

    @BeforeEach
    void setUp() throws SQLException {
        String dbName = "test" + System.nanoTime(); // Nombre único para cada test
        dataSource = new DriverManagerDataSource("jdbc:h2:mem:"+dbName+";MODE=MYSQL", "sa", "sa");

        ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("sql-scripts/test-data.sql"));

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        moviesRepositoryJdbc =  new MoviesRepositoryJdbc(jdbcTemplate);
    }


//  En caso de no crear una base nueva para cada test se hace un drop para tener una base limpia homogena para cada test
//    @AfterEach
//    void tearDown() throws SQLException {
//        final Statement s = dataSource.getConnection().createStatement();
//        s.execute("drop all objects delete files");
//    }


    @Test
    void load_all_movies() throws SQLException {

        List<Movie> movies = new ArrayList<>(moviesRepositoryJdbc.findAll());

        assertEquals(3, movies.size());
        List<Movie> expectedMovies = Arrays.asList(
                new Movie(1, "Dark Knight", 152, Genre.ACTION),
                new Movie(2, "Memento", 113, Genre.THRILLER),
                new Movie(3, "Matrix", 136, Genre.ACTION));
        for (int i = 0; i < expectedMovies.size(); i++) {
            assertEquals( expectedMovies.get(i).getId(), movies.get(i).getId());
            assertEquals( expectedMovies.get(i).getName(), movies.get(i).getName());
            assertEquals( expectedMovies.get(i).getMinutes(), movies.get(i).getMinutes());
            assertEquals( expectedMovies.get(i).getGenre(), movies.get(i).getGenre());
        }

//      el assertThat compara el objecto completo a traves de su hash y por lo tanto al momento de un debug
//      no se logra apreciar que propiedad es la diferente
//        assertThat(movies, is(Arrays.asList(
//                new Movie(1, "Dark Knight", 152, Genre.ACTION),
//                new Movie(2, "Memento", 113, Genre.THRILLER),
//                new Movie(3, "Matrix", 136, Genre.ACTION)
//        )));
    }

    @Test
    void load_movie_by_id(){

        Movie movieFromDB =  moviesRepositoryJdbc.findById(2);

        assertEquals( 2, movieFromDB.getId());
        assertEquals("Memento", movieFromDB.getName());
        assertEquals(113, movieFromDB.getMinutes());
        assertEquals(Genre.THRILLER, movieFromDB.getGenre());
        // assertThat(movieFromDB, is(new Movie(2,"Memento",113,Genre.THRILLER)));

    }

    @Test
    void insert_movie() {
        Movie movie = new Movie("Super 8", 112, Genre.THRILLER);
        moviesRepositoryJdbc.saveOrUpdate(movie);

        Movie movieFromDB =  moviesRepositoryJdbc.findById(4);

        assertEquals( 4, movieFromDB.getId());
        assertEquals("Super 8", movieFromDB.getName());
        assertEquals(112, movieFromDB.getMinutes());
        assertEquals(Genre.THRILLER, movieFromDB.getGenre());
    }
}