package com.tassioauad.moviecheck.view;

import com.tassioauad.moviecheck.model.entity.Crew;
import com.tassioauad.moviecheck.model.entity.Genre;

import java.util.Date;
import java.util.List;

public interface MovieDetailView {
    void showVoteCount(long voteCount);

    void showVoteAverage(float voteAverage);

    void showOverview(String overview);

    void showTagline(String tagline);

    void showReleaseDate(Date releaseDate);

    void showPoster(String posterUrl);

    void showBackdrop(String backdropUrl);

    void showLoadingGenres();

    void warnFailedOnLoadGenres();

    void showGenres(List<Genre> genreList);

    void warnAnyGenreFounded();

    void hideLoadingGenres();

    void hideLoadingDirectors();

    void showLoadingDirector();

    void showDirectors(List<Crew> directorList);

    void warnAnyFoundedDirectors();

    void warnFailedToLoadDirectors();

    void disableToCheckInterest();

    void enableToCheckInterest();

    void checkInterest();

    void uncheckInterest();

    void showUserClassification(Float classification);

    void enableToClassify();

    void warnRemovedFromWatched();

    void warnAddedAsWatched();

    void warmAddedAsInteresting();

    void removedFromInteresting();
}
