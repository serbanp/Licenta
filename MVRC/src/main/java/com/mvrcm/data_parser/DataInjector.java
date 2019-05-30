package com.mvrcm.data_parser;

import com.google.gson.Gson;
import com.mvrcm.data_parser.data_wrappers.CastWrapper;
import com.mvrcm.data_parser.data_wrappers.CrewWrapper;
import com.mvrcm.data_parser.data_wrappers.GenresWrapper;
import com.mvrcm.model.Actor;
import com.mvrcm.model.Director;
import com.mvrcm.model.Genre;
import com.mvrcm.model.Movie;
import com.mvrcm.model.Utils.RegisterForm;
import com.mvrcm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class DataInjector {
    private final String SCHEME_CAST_CREW = "movie_id,title,cast,crew";
    private final String SCHEME_MOVIE_GENRE = "budget,genres,homepage,id,keywords,original_language,original_title,overview,popularity,production_companies,production_countries,release_date,revenue,runtime,spoken_languages,status,tagline,title,vote_average,vote_count";
    private final String CAST_CREW_FILE_PATH = "D:\\Licenta\\MVRC\\src\\main\\resources\\movie_data\\tmdb_5000_credits.csv";
    private final String MOVIE_GENRE_FILE_PATH = "D:\\Licenta\\MVRC\\src\\main\\resources\\movie_data\\tmdb_5000_movies.csv";
    private final List<String> GENRE_NAMES = new ArrayList<>(Arrays.asList("Action", "Adventure", "Animation", "Comedy", "Crime", "Drama", "Documentary", "Family", "Fantasy", "Foreign", "History", "Horror", "Mystery", "Music", "Romance", "Science Fiction", "Thriller", "TV Movie", "War", "Western"));
    private List<String[]> CastCrewData;
    private List<String[]> MovieGenreData;
    private Map<String, Set<Actor>> movieIdsAndActors = new HashMap<>();
    private Map<String, Director> movieIdsAndDirectors = new HashMap<>();

    @Autowired
    private ActorService actorService;

    @Autowired
    private DirectorService directorService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserMovieRatingService userMovieRatingService;

    @Autowired
    private UserService userService;

    public DataInjector() {
        this.CastCrewData = CsvParser.getParsedData(CAST_CREW_FILE_PATH);
        this.MovieGenreData = CsvParser.getParsedData(MOVIE_GENRE_FILE_PATH);
    }

    @PostConstruct
    public void init() {
        if (genreService.getAll().size()==0) {
            injectGenres();
            injectActorsDirectors();
            injectMovies();
        }
        //injectUsers();
        injectRatings();
    }

    private void injectUsers() {
        userService.create(new RegisterForm("a1","user1","jj"));
        userService.create(new RegisterForm("a2","user2","jj"));
        userService.create(new RegisterForm("a3","user3","jj"));
        userService.create(new RegisterForm("a4","user4","jj"));
        userService.create(new RegisterForm("a5","user5","jj"));
        userService.create(new RegisterForm("a6","user6","jj"));
        userService.create(new RegisterForm("a7","user7","jj"));
        userService.create(new RegisterForm("a8","user8","jj"));
        userService.create(new RegisterForm("a9","user9","jj"));
        userService.create(new RegisterForm("a10","user10","jj"));
    }
    private void injectRatings() {
        userMovieRatingService.create(1L,1L,(short)3);
        userMovieRatingService.create(1L,2L,(short)2);
        userMovieRatingService.create(1L,3L,(short)5);
        userMovieRatingService.create(1L,7L,(short)5);
        userMovieRatingService.create(1L,8L,(short)3);

        userMovieRatingService.create(2L,3L,(short)5);
        userMovieRatingService.create(2L,4L,(short)4);
        userMovieRatingService.create(2L,5L,(short)1);
        userMovieRatingService.create(2L,6L,(short)4);

        userMovieRatingService.create(3L,3L,(short)5);
        userMovieRatingService.create(3L,4L,(short)4);
        userMovieRatingService.create(3L,5L,(short)2);
        userMovieRatingService.create(3L,6L,(short)3);
        userMovieRatingService.create(3L,9L,(short)4);

        userMovieRatingService.create(4L,8L,(short)4);
        userMovieRatingService.create(4L,10L,(short)3);
        userMovieRatingService.create(4L,11L,(short)3);
        userMovieRatingService.create(4L,12L,(short)3);

        userMovieRatingService.create(5L,10L,(short)3);
        userMovieRatingService.create(5L,11L,(short)3);
        userMovieRatingService.create(5L,12L,(short)3);

        userMovieRatingService.create(6L,7L,(short)5);
        userMovieRatingService.create(6L,8L,(short)2);
        userMovieRatingService.create(6L,10L,(short)3);
        userMovieRatingService.create(6L,11L,(short)3);

        userMovieRatingService.create(7L,2L,(short)2);
        userMovieRatingService.create(7L,3L,(short)5);

        userMovieRatingService.create(8L,4L,(short)4);
        userMovieRatingService.create(8L,5L,(short)2);

        userMovieRatingService.create(9L,6L,(short)2);
        userMovieRatingService.create(9L,7L,(short)4);

        userMovieRatingService.create(10L,1L,(short)3);
        userMovieRatingService.create(10L,2L,(short)2);
        userMovieRatingService.create(10L,3L,(short)4);
        userMovieRatingService.create(10L,8L,(short)2);
    }

    private void injectActorsDirectors() {
        for (String[] csvRow : CastCrewData) {
            Gson gson = new Gson();
            CastWrapper[] castWrappers = gson.fromJson(csvRow[2], CastWrapper[].class);
            CrewWrapper[] crewWrappers = gson.fromJson(csvRow[3], CrewWrapper[].class);
            String movieId = csvRow[0];
            Set<Actor> actors = new HashSet<>();
            movieIdsAndActors.put(movieId, actors);
            for (int i = 0; i < 5 && i < castWrappers.length; i++) {
                String fullName = castWrappers[i].name;
                Actor actor = new Actor(fullName);
                try {
                    actorService.create(actor);
                } catch (ResponseStatusException e) {
                    actor.setId(actorService.getByFullName(fullName).getId());
                } finally {
                    actors.add(actor);
                }
            }
            for (CrewWrapper crewWrapper : crewWrappers)
                if (crewWrapper.job.equals("Director")) {
                    String fullName = crewWrapper.name;
                    Director director = new Director(fullName);
                    try {
                        directorService.create(director);
                    } catch (ResponseStatusException e) {
                        director.setId(directorService.getByFullName(fullName).getId());
                    } finally {
                        movieIdsAndDirectors.put(movieId, director);
                    }
                    break;
                }
        }
    }

    private void injectGenres() {
        for (String genreName : GENRE_NAMES)
            genreService.create(new Genre(genreName));
    }

    private void injectMovies() {
        for (String[] csvRow : MovieGenreData) {
            Gson gson = new Gson();
            GenresWrapper[] genresWrappers = gson.fromJson(csvRow[1], GenresWrapper[].class);
            String bio = csvRow[7];
            String title = csvRow[17];
            String movieId = csvRow[3];
            Movie movie = new Movie();
            movie.setTitle(title);
            for (GenresWrapper genreWrapper : genresWrappers) {
                movie.addGenre(genreService.getByTitle(genreWrapper.name));
            }
            movie.setDirector(movieIdsAndDirectors.get(movieId));
            movie.setActors(movieIdsAndActors.get(movieId));
            if (movie.getDirector() != null)
                movieService.create(movie);
        }
    }
}
