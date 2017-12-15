package com.tassioauad.moviecheck.dagger;

import com.tassioauad.moviecheck.ListPopularPersonActivity;
import com.tassioauad.moviecheck.model.api.PersonApi;
import com.tassioauad.moviecheck.presenter.ListPopularPersonPresenter;
import com.tassioauad.moviecheck.view.fragment.ListPopularPersonView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by imamsulthon on 15/12/17.
 */

@Module(library = true, includes = ApiModule.class, injects = ListPopularPersonActivity.class)
public class ListPopularPersonViewModule {

    ListPopularPersonView view;

    public ListPopularPersonViewModule(ListPopularPersonView view) {
        this.view = view;
    }

    @Provides
    public ListPopularPersonPresenter provideListPopularPersonPresenter(PersonApi personApi) {
        return new ListPopularPersonPresenter(view, personApi);
    }

}
