package data;

import models.Genre;
import models.Movie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;

public class MoviesRepositoryJdbc implements MoviesRepository {

    private JdbcTemplate jdbcTemplate;

    public MoviesRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Movie findById(long id) {
        Object[] args = {id};

        return jdbcTemplate.queryForObject("select * from movies where id = ?", args, movieMapper);
    }

    @Override
    public Collection<Movie> findAll() {


        return jdbcTemplate.query("select * from movies", movieMapper);
    }

    @Override
    public void saveOrUpdate(Movie movie) {

        jdbcTemplate.update("insert into movies (name, minutes, genre, director) values (?, ?, ?, ?)",
                movie.getName(), movie.getMinutes(), movie.getGenre().toString(), movie.getDirector());

    }

    public Collection<Movie> findByKeywordOrKeyChar(String s){

         String value = "%"+s+"%";

        return jdbcTemplate.query("SELECT * FROM movies WHERE LOWER(name) LIKE LOWER(?)", movieMapper, value);
        // uso el lower debido que los test se corren con una db en memoria generada por h2 que no tiene
        // la configuracion utf8_general_ci, por lo que es sensible a minusculas y mayusculas

    }
    public Collection<Movie>  findByDirector(String director) {

        String value = "%"+director+"%";

        return jdbcTemplate.query("SELECT * FROM movies WHERE LOWER(director) LIKE LOWER(?)", movieMapper, value);
    }

    private static RowMapper<Movie> movieMapper = (rs, rowNum) ->
            new Movie(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("minutes"),
                    Genre.valueOf(rs.getString("genre")),
                    rs.getString("director"));
}





