package com.tassioauad.moviecheck.presenter;


import com.tassioauad.moviecheck.model.api.CrewApi;
import com.tassioauad.moviecheck.model.api.GenreApi;
import com.tassioauad.moviecheck.model.api.asynctask.ApiResultListener;
import com.tassioauad.moviecheck.model.dao.MovieInterestDao;
import com.tassioauad.moviecheck.model.dao.MovieWatchedDao;
import com.tassioauad.moviecheck.model.dao.UserDao;
import com.tassioauad.moviecheck.model.entity.Crew;
import com.tassioauad.moviecheck.model.entity.Genre;
import com.tassioauad.moviecheck.model.entity.Movie;
import com.tassioauad.moviecheck.model.entity.MovieInterest;
import com.tassioauad.moviecheck.model.entity.MovieWatched;
import com.tassioauad.moviecheck.view.MovieDetailView;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailPresenter {

    private MovieDetailView view;
    private GenreApi genreApi;
    private MovieInterestDao movieInterestDao;
    private MovieWatchedDao movieWatchedDao;
    private UserDao userDao;
    private Movie movie;
    private CrewApi crewApi;

    public MovieDetailPresenter(MovieDetailView view, GenreApi genreApi, CrewApi crewApi, MovieInterestDao movieInterestDao,
                                MovieWatchedDao movieWatchedDao, UserDao userDao) {
        this.view = view;
        this.genreApi = genreApi;
        this.crewApi = crewApi;
        this.movieInterestDao = movieInterestDao;
        this.movieWatchedDao = movieWatchedDao;
        this.userDao = userDao;

    }

    public void init(Movie movie) {
        this.movie = movie;
        view.showVoteCount(movie.getVoteCount());
        view.showVoteAverage(movie.getVoteAverage());
        view.showOverview(movie.getOverview());
        if (movie.getReleaseDate() != null) {
            view.showReleaseDate(movie.getReleaseDate());
        }
        view.showPoster(movie.getPosterUrl());
        view.showBackdrop(movie.getBackdropUrl());

        if (userDao.getLoggedUser() != null) {
            view.enableToClassify();
        }

        if (userDao.getLoggedUser() == null || movieWatchedDao.findByMovie(movie, userDao.getLoggedUser()) != null) {
            view.disableToCheckInterest();
            if (userDao.getLoggedUser() != null && movieWatchedDao.findByMovie(movie, userDao.getLoggedUser()) != null) {
                view.showUserClassification(movieWatchedDao.findByMovie(movie, userDao.getLoggedUser()).getVote());
            }
        } else {
            view.enableToCheckInterest();
            if (movieInterestDao.findByMovie(movie, userDao.getLoggedUser()) != null) {
                view.checkInterest();
            }
        }
    }

    public void loadDirector(Movie movie) {
        view.showLoadingDirector();
        crewApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                List<Crew> crewList = (List<Crew>) object;
                if (crewList == null || crewList.size() == 0) {
                    view.warnAnyFoundedDirectors();
                } else {
                    Crew crew = new Crew();
                    List<Crew> directorList = new ArrayList<Crew>();
                    for (int i = 0; i<crewList.size(); i++) {
                        crew = crewList.get(i);
                        if (crew.getJob().equals("Director")) {
                            directorList.add(crewList.get(i));
                        }
                    }
                    view.showDirectors(directorList);
                }
                view.hideLoadingDirectors();
            }

            @Override
            public void onException(Exception exception) {
                view.warnFailedToLoadDirectors();
                view.hideLoadingDirectors();
            }
        });
        crewApi.listAllByMovie(movie);
    }

    public void loadGenres() {
        if (movie.getGenreId() == null || movie.getGenreId().size() == 0) {
            view.warnAnyGenreFounded();
            return;
        }

        view.showLoadingGenres();
        genreApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                List<Genre> genreList = (List<Genre>) object;
                if (genreList == null || genreList.size() == 0) {
                    view.warnAnyGenreFounded();
                } else {
                    List<Genre> genresOfTheMovieList = new ArrayList<>();
                    for (Genre genre : genreList) {
                        if (movie.getGenreId().contains(genre.getId())) {
                            genresOfTheMovieList.add(genre);
                        }
                    }
                    view.showGenres(genresOfTheMovieList);
                }
                view.hideLoadingGenres();
            }

            @Override
            public void onException(Exception exception) {
                view.warnFailedOnLoadGenres();
                view.hideLoadingGenres();
            }
        });
        genreApi.listAllOfMovie();
    }

    public void stop() {
        genreApi.cancelAllServices();
    }

    public void checkInterest() {
        MovieInterest movieInterest = movieInterestDao.findByMovie(movie, userDao.getLoggedUser());
        if (movieInterest == null) {
            movieInterest = new MovieInterest();
            movieInterest.setMovie(movie);
            movieInterest.setUser(userDao.getLoggedUser());

            movieInterestDao.insert(movieInterest);
            view.checkInterest();
            view.warmAddedAsInteresting();
        } else {
            movieInterestDao.remove(movieInterest);
            view.uncheckInterest();
            view.removedFromInteresting();
        }
    }

    public void informUserClassification(float rating) {
        MovieWatched movieWatched = movieWatchedDao.findByMovie(movie, userDao.getLoggedUser());
        if (movieWatched == null) {
            movieWatched = new MovieWatched();
            movieWatched.setMovie(movie);
            movieWatched.setUser(userDao.getLoggedUser());
        }
        movieWatched.setVote(rating);
        movieWatchedDao.save(movieWatched);
        movieInterestDao.remove(movieWatched.getMovie(), userDao.getLoggedUser());
        view.disableToCheckInterest();
        view.showUserClassification(rating);
        view.warnAddedAsWatched();
    }

    public void removeClassification() {
        movieWatchedDao.remove(movie, userDao.getLoggedUser());
        view.enableToCheckInterest();
        view.uncheckInterest();
        view.showVoteAverage(movie.getVoteAverage());
        view.warnRemovedFromWatched();
    }
}
