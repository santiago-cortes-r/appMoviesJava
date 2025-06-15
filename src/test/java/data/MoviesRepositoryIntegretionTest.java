package data;

import models.Genre;
import models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
                new Movie(1, "Dark Knight", 152, Genre.ACTION, "carlos"),
                new Movie(2, "Memento", 113, Genre.THRILLER, "carlos"),
                new Movie(3, "Matrix", 136, Genre.ACTION, "santiago"));
        for (int i = 0; i < expectedMovies.size(); i++) {
            assertEquals( expectedMovies.get(i).getId(), movies.get(i).getId());
            assertEquals( expectedMovies.get(i).getName(), movies.get(i).getName());
            assertEquals( expectedMovies.get(i).getMinutes(), movies.get(i).getMinutes());
            assertEquals( expectedMovies.get(i).getGenre(), movies.get(i).getGenre());
            assertEquals(expectedMovies.get(i).getDirector(), movies.get(i).getDirector());
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
        assertEquals("carlos", movieFromDB.getDirector());

        // assertThat(movieFromDB, is(new Movie(2,"Memento",113,Genre.THRILLER)));

    }

    @Test
    void insert_movie() {
        Movie movie = new Movie("Super 8", 112, Genre.THRILLER, "pepito");
        moviesRepositoryJdbc.saveOrUpdate(movie);

        Movie movieFromDB =  moviesRepositoryJdbc.findById(4);

        assertEquals( 4, movieFromDB.getId());
        assertEquals("Super 8", movieFromDB.getName());
        assertEquals(112, movieFromDB.getMinutes());
        assertEquals(Genre.THRILLER, movieFromDB.getGenre());
        assertEquals("pepito", movieFromDB.getDirector());
    }

    @Test
    void load_movie_by_keywordOrKeyChar(){

        List<Movie> moviesByKeyChar = new ArrayList<>(moviesRepositoryJdbc.findByKeywordOrKeyChar("m"));

        List<Movie> expectedMovies = Arrays.asList(
                new Movie(2, "Memento", 113, Genre.THRILLER, "carlos"),
                new Movie(3, "Matrix", 136, Genre.ACTION, "santiago"));

        assertEquals(expectedMovies.size(), moviesByKeyChar.size(), "Size mismatch");

        for (int i = 0; i < expectedMovies.size(); i++) {
            assertEquals( expectedMovies.get(i).getId(), moviesByKeyChar.get(i).getId());
            assertEquals( expectedMovies.get(i).getName(), moviesByKeyChar.get(i).getName());
            assertEquals( expectedMovies.get(i).getMinutes(), moviesByKeyChar.get(i).getMinutes());
            assertEquals( expectedMovies.get(i).getGenre(), moviesByKeyChar.get(i).getGenre());
            assertEquals( expectedMovies.get(i).getDirector(), moviesByKeyChar.get(i).getDirector());
        }
         // assertThat(movieKeyChar, is(new Movie(2,"Memento",113,Genre.THRILLER)));
    }

    @Test
    void findByDirector() {

        List<Movie> moviesByDirector = new ArrayList<>(moviesRepositoryJdbc.findByDirector("carlos"));

        List<Movie> expectedMovies = Arrays.asList(
                new Movie(1, "Dark Knight", 152, Genre.ACTION, "carlos"),
                new Movie(2, "Memento", 113, Genre.THRILLER, "carlos"));

        assertEquals(expectedMovies.size(), moviesByDirector.size(), "Size mismatch");

        for (int i = 0; i < expectedMovies.size(); i++) {
            assertEquals( expectedMovies.get(i).getId(), moviesByDirector.get(i).getId());
            assertEquals( expectedMovies.get(i).getName(), moviesByDirector.get(i).getName());
            assertEquals( expectedMovies.get(i).getMinutes(), moviesByDirector.get(i).getMinutes());
            assertEquals( expectedMovies.get(i).getGenre(), moviesByDirector.get(i).getGenre());
            assertEquals( expectedMovies.get(i).getDirector(), moviesByDirector.get(i).getDirector());
        }
    }

    @Test
    void find_movies_by_genre_and_max_duration() {
        List<Movie> result = new ArrayList<>(moviesRepositoryJdbc.findByGenreAndMaxDuration(Genre.THRILLER, 115));

        List<Movie> expected = List.of(
                new Movie(2, "Memento", 113, Genre.THRILLER, "carlos")
        );

        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId(), result.get(i).getId());
            assertEquals(expected.get(i).getName(), result.get(i).getName());
            assertEquals(expected.get(i).getMinutes(), result.get(i).getMinutes());
            assertEquals(expected.get(i).getGenre(), result.get(i).getGenre());
            assertEquals(expected.get(i).getDirector(), result.get(i).getDirector());
        }
    }

    @Test
    void find_movies_by_director_and_max_duration() {
        List<Movie> result = new ArrayList<>(moviesRepositoryJdbc.findByDirectorAndMaxDuration("carlos", 115) );

        List<Movie> expected = List.of(
                new Movie(2, "Memento", 113, Genre.THRILLER, "carlos")
        );

        assertEquals(expected.size(), result.size());
        // Comparación de campos...
    }

    @Test
    void find_movies_by_director_and_genre() {
        List<Movie> result = new ArrayList<>(moviesRepositoryJdbc.findByDirectorAndGenre("carlos", Genre.THRILLER));

        List<Movie> expected = List.of(
                new Movie(2, "Memento", 113, Genre.THRILLER, "carlos")
        );

        assertEquals(expected.size(), result.size());
        // Comparación de campos...
    }





}