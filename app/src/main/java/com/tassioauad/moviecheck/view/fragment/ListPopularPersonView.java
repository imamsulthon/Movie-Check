package com.tassioauad.moviecheck.view.fragment;

import com.tassioauad.moviecheck.model.entity.Person;

import java.util.List;

/**
 * Created by imamsulthon on 15/12/17.
 */

public interface ListPopularPersonView {

    void showLoadingPersons();

    void warnAnyPersonFounded();

    void showPerson(List<Person> personList);

    void hideLoadingPersons();

    void warnFailedToLoadPersons();
}
