package com.tassioauad.moviecheck.model.api.asynctask;

import android.content.Context;

import com.tassioauad.moviecheck.model.api.asynctask.exception.BadRequestException;
import com.tassioauad.moviecheck.model.api.resource.PersonResource;
import com.tassioauad.moviecheck.model.entity.Person;

import java.util.List;

import retrofit.Response;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by imamsulthon on 15/12/17.
 */

public class ListPopularPersonAsyncTask extends GenericAsyncTask<Void, Void, List<Person>> {

    private PersonResource personResource;
    private Integer page;

    public ListPopularPersonAsyncTask(Context context, PersonResource personResource, Integer page) {
        super(context);
        this.personResource = personResource;
        this.page = page;
    }

    @Override
    protected AsyncTaskResult<List<Person>> doInBackground(Void... voids) {
        try {
            Response<List<Person>> response = personResource.listPopularPerson(getApiKey(), page).execute();
            switch (response.code()) {
                case HTTP_OK:
                    return new AsyncTaskResult<>(response.body());
                default:
                    return new AsyncTaskResult<>(new BadRequestException());
            }
        } catch (Exception ex) {
            return new AsyncTaskResult<>(new BadRequestException());
        }
    }
}
