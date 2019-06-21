package com.mvrcm.data_parser;

import com.google.gson.Gson;
import com.mvrcm.data_parser.data_wrappers.*;
import com.mvrcm.model.*;
import com.mvrcm.model.Utils.RegisterForm;
import com.mvrcm.repository.MovieRepository;
import com.mvrcm.repository.TagRepository;
import com.mvrcm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
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
    private final String URI = "https://api.themoviedb.org/3/movie/";
    private final String API_KEY = "?api_key=47e31ee1b860f25b933e4a0fef104c9e";
    private final String INITIAL_POSTER_PATH = "http://image.tmdb.org/t/p/w185";
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
    private TagService tagService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserMovieRatingService userMovieRatingService;

    @Autowired
    private UserService userService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TagRepository tagRepository;

    public DataInjector() {
        this.CastCrewData = CsvParser.getParsedData(CAST_CREW_FILE_PATH);
        this.MovieGenreData = CsvParser.getParsedData(MOVIE_GENRE_FILE_PATH);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        if (genreService.getAll().size() == 0) {
            //injectGenres();
            injectActorsDirectors();
            injectMovies();
            injectUsers();
            injectRatings();
            System.out.println("DATA INJECTION COMPLETED!!!");
        }
        //addTagsAndReleaseDate();
    }

    private void injectUsers() {
        userService.create(new RegisterForm("a1", "user1", "1"));
        userService.create(new RegisterForm("a2", "user2", "jj"));
        userService.create(new RegisterForm("a3", "user3", "jj"));
        userService.create(new RegisterForm("a4", "user4", "jj"));
        userService.create(new RegisterForm("a5", "user5", "jj"));
        userService.create(new RegisterForm("a6", "user6", "jj"));
        userService.create(new RegisterForm("a7", "user7", "jj"));
        userService.create(new RegisterForm("a8", "user8", "jj"));
        userService.create(new RegisterForm("a9", "user9", "jj"));
        userService.create(new RegisterForm("a10", "user10", "jj"));
    }

    private void injectRatings() {
        userMovieRatingService.create(1L, 1L, (short) 3);
        userMovieRatingService.create(1L, 2L, (short) 2);
        userMovieRatingService.create(1L, 3L, (short) 5);
        userMovieRatingService.create(1L, 7L, (short) 5);
        userMovieRatingService.create(1L, 8L, (short) 3);

        userMovieRatingService.create(2L, 3L, (short) 5);
        userMovieRatingService.create(2L, 4L, (short) 4);
        userMovieRatingService.create(2L, 5L, (short) 1);
        userMovieRatingService.create(2L, 6L, (short) 4);

        userMovieRatingService.create(3L, 3L, (short) 5);
        userMovieRatingService.create(3L, 4L, (short) 4);
        userMovieRatingService.create(3L, 5L, (short) 2);
        userMovieRatingService.create(3L, 6L, (short) 3);
        userMovieRatingService.create(3L, 9L, (short) 4);

        userMovieRatingService.create(4L, 8L, (short) 4);
        userMovieRatingService.create(4L, 10L, (short) 3);
        userMovieRatingService.create(4L, 11L, (short) 3);
        userMovieRatingService.create(4L, 12L, (short) 3);

        userMovieRatingService.create(5L, 10L, (short) 3);
        userMovieRatingService.create(5L, 11L, (short) 3);
        userMovieRatingService.create(5L, 12L, (short) 3);

        userMovieRatingService.create(6L, 7L, (short) 5);
        userMovieRatingService.create(6L, 8L, (short) 2);
        userMovieRatingService.create(6L, 10L, (short) 3);
        userMovieRatingService.create(6L, 11L, (short) 3);

        userMovieRatingService.create(7L, 2L, (short) 2);
        userMovieRatingService.create(7L, 3L, (short) 5);

        userMovieRatingService.create(8L, 4L, (short) 4);
        userMovieRatingService.create(8L, 5L, (short) 2);

        userMovieRatingService.create(9L, 6L, (short) 2);
        userMovieRatingService.create(9L, 7L, (short) 4);

        userMovieRatingService.create(10L, 1L, (short) 3);
        userMovieRatingService.create(10L, 2L, (short) 2);
        userMovieRatingService.create(10L, 3L, (short) 4);
        userMovieRatingService.create(10L, 8L, (short) 2);
    }

    private void injectActorsDirectors() {
        for (String[] csvRow : CastCrewData) {
            Gson gson = new Gson();
            CastWrapper[] castWrappers = gson.fromJson(csvRow[2], CastWrapper[].class);
            CrewWrapper[] crewWrappers = gson.fromJson(csvRow[3], CrewWrapper[].class);
            String movieId = csvRow[0];
            System.out.println("MOVIE ID "+ movieId);
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

    private void addTagsAndReleaseDate() {
        for (String[] csvRow : MovieGenreData) {
            Gson gson = new Gson();
            String title = csvRow[17];
            Movie movie = movieService.getByTitle(title);
            if (movie != null) {
                String releaseDate = csvRow[11];
                TagsWrapper[] tagsWrappers = gson.fromJson(csvRow[4], TagsWrapper[].class);
                for (TagsWrapper tagsWrapper : tagsWrappers) {
                    Tag currentTag = tagService.getByTitle(tagsWrapper.name.toLowerCase());
                    if (currentTag != null)
                        movie.addTag(currentTag);
                    else {
                        Tag newTag = new Tag(tagsWrapper.name.toLowerCase());
                        tagRepository.save(newTag);
                        System.out.println("newTag title" + newTag.getTitle());
                        movie.addTag(newTag);
                    }
                }
                    movie.setReleaseDate(releaseDate);
                    System.out.println("TITLU "+movie.getTitle());
                    movieRepository.save(movie);

            }
        }
    }

    private void injectMovies() {
        int count = 0;
        final int MAX_COUNT = 40;
        for (String[] csvRow : MovieGenreData) {
            Gson gson = new Gson();
            GenresWrapper[] genresWrappers = gson.fromJson(csvRow[1], GenresWrapper[].class);
            TagsWrapper[] tagsWrappers = gson.fromJson(csvRow[4], TagsWrapper[].class);
            String bio = csvRow[7];
            String title = csvRow[17];
            String movieId = csvRow[3];
            System.out.println("PE MOVIE ID 2"+ movieId);
            String releaseDate = csvRow[11];
            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setReleaseDate(releaseDate);
            try {
                movie.setImgSrc(INITIAL_POSTER_PATH + getPosterPathCompletion(movieId));
            } catch (Exception e) {
                movie.setImgSrc(null);
            }
            movie.setBio(bio);
            for (GenresWrapper genresWrapper:genresWrappers)  {
                Genre currentGenre=genreService.getByTitle(genresWrapper.name.toLowerCase());
                if (currentGenre!=null) {
                    movie.addGenre(currentGenre);
                }
                else {
                    Genre newGenre=new Genre(genresWrapper.name.toLowerCase());
                    genreService.create(newGenre);
                    movie.addGenre(newGenre);
                }
            }
            for (TagsWrapper tagsWrapper : tagsWrappers) {
                Tag currentTag = tagService.getByTitle(tagsWrapper.name.toLowerCase());
                if (currentTag != null)
                    movie.addTag(currentTag);
                else {
                    Tag newTag = new Tag(tagsWrapper.name.toLowerCase());
                    tagRepository.save(newTag);
                    movie.addTag(newTag);
                }
            }
            movie.setDirector(movieIdsAndDirectors.get(movieId));
            movie.setActors(movieIdsAndActors.get(movieId));
            if (movie.getDirector() != null && movie.getImgSrc() != null)
                movieService.create(movie);
            count++;
            if (count == MAX_COUNT) {
                try {
                    Thread.sleep(6000);
                    count = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private String getPosterPathCompletion(String movieId) throws Exception {
        final String uri = URI + movieId + API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(uri, String.class);
            Gson gson = new Gson();
            PosterWrapper posterWrapper = gson.fromJson(response, PosterWrapper.class);
            return posterWrapper.poster_path;
        } catch (HttpClientErrorException e) {
            throw new Exception("Poster not found");
        }
    }

}
