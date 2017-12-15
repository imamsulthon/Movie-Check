package com.tassioauad.moviecheck.presenter;

import com.tassioauad.moviecheck.model.api.PersonApi;
import com.tassioauad.moviecheck.model.api.asynctask.ApiResultListener;
import com.tassioauad.moviecheck.model.entity.Person;
import com.tassioauad.moviecheck.view.fragment.ListPopularPersonView;

import java.util.List;

/**
 * Created by imamsulthon on 15/12/17.
 */

public class ListPopularPersonPresenter {

    private ListPopularPersonView view;
    private PersonApi personApi;

    public ListPopularPersonPresenter(ListPopularPersonView listPersonView, PersonApi personApi) {
        this.view = listPersonView;
        this.personApi = personApi;
    }

    public void loadPersons(Integer page) {
        view.showLoadingPersons();
        personApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                List<Person> personList = (List<Person>) object;
                if (personList == null || personList.size() == 0) {
                    view.warnAnyPersonFounded();
                } else {
                    view.showPerson(personList);
                }
                view.hideLoadingPersons();
            }

            @Override
            public void onException(Exception exception) {
                view.warnFailedToLoadPersons();
                view.hideLoadingPersons();
            }
        });

        personApi.listPopularPerson(page);
    }

    public void onStop() {
        personApi.cancelAllServices();
    }
}
