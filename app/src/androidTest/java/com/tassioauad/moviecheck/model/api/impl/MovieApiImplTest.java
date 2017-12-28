package com.tassioauad.moviecheck.model.api.impl;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tassioauad.moviecheck.R;
import com.tassioauad.moviecheck.model.api.ItemTypeAdapterFactory;
import com.tassioauad.moviecheck.model.api.asynctask.ApiResultListener;
import com.tassioauad.moviecheck.model.api.resource.MovieResource;
import com.tassioauad.moviecheck.model.entity.Movie;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class MovieApiImplTest extends AndroidTestCase {

    MovieApiImpl movieApi;
    Integer page = 2;

    public void setUp() throws Exception {
        super.setUp();
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .setDateFormat("yyyy'-'MM'-'dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getContext().getString(R.string.themoviedbapi_baseurl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        movieApi = new MovieApiImpl(getContext(), retrofit.create(MovieResource.class));
    }

    public void testListUpcomingMovies() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        movieApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                assertNotNull(object);
                assertTrue(((List<Movie>) object).size() > 0);
                signal.countDown();
            }

            @Override
            public void onException(Exception exception) {
                fail("Exception has happened");
                signal.countDown();
            }
        });

        movieApi.listUpcomingMovies();
        signal.await();

    }

    public void testListUpcomingMoviesWithPage() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        movieApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                assertNotNull(object);
                assertTrue(((List<Movie>) object).size() > 0);
                signal.countDown();
            }

            @Override
            public void onException(Exception exception) {
                fail("Exception has happened");
                signal.countDown();
            }
        });

        movieApi.listUpcomingMovies(page);
        signal.await();
    }

    public void testListPopularMovies() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        movieApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                assertNotNull(object);
                assertTrue(((List<Movie>) object).size() > 0);
                signal.countDown();
            }

            @Override
            public void onException(Exception exception) {
                fail("Exception has happened");
                signal.countDown();
            }
        });

        movieApi.listPopularMovies();
        signal.await();
    }

    public void testListPopularMoviesWithPage() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        movieApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                assertNotNull(object);
                assertTrue(((List<Movie>) object).size() > 0);
                signal.countDown();
            }

            @Override
            public void onException(Exception exception) {
                fail("Exception has happened");
                signal.countDown();
            }
        });

        movieApi.listPopularMovies(page);
        signal.await();
    }

    public void testListTopRatedMovies() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        movieApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                assertNotNull(object);
                assertTrue(((List<Movie>) object).size() > 0);
                signal.countDown();
            }

            @Override
            public void onException(Exception exception) {
                fail("Exception has happened");
                signal.countDown();
            }
        });

        movieApi.listTopRatedMovies();
        signal.await();
    }

    public void testListTopRatedMoviesWithPage() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        movieApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                assertNotNull(object);
                assertTrue(((List<Movie>) object).size() > 0);
                signal.countDown();
            }

            @Override
            public void onException(Exception exception) {
                fail("Exception has happened");
                signal.countDown();
            }
        });

        movieApi.listTopRatedMovies(page);
        signal.await();
    }

    public void testListNowPlayingMovies() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        movieApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                assertNotNull(object);
                assertTrue(((List<Movie>) object).size() > 0);
                signal.countDown();
            }

            @Override
            public void onException(Exception exception) {
                fail("Exception has happened");
                signal.countDown();
            }
        });

        movieApi.listNowPlayingMovies();
        signal.await();
    }

    public void testListNowPlayingMoviesWithPage() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        movieApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                assertNotNull(object);
                assertTrue(((List<Movie>) object).size() > 0);
                signal.countDown();
            }

            @Override
            public void onException(Exception exception) {
                fail("Exception has happened");
                signal.countDown();
            }
        });

        movieApi.listNowPlayingMovies(page);
        signal.await();
    }

    public void testCancelAllService_ListNowPlayingMovies() {
        movieApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                fail("Request not cancelled");
            }

            @Override
            public void onException(Exception exception) {
                fail("Exception has happened");
            }
        });
        movieApi.listNowPlayingMovies();

        movieApi.cancelAllServices();
    }
    
    public void testCancelAllService_ListTopRatedMovies() {
        movieApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                fail("Request not cancelled");
            }

            @Override
            public void onException(Exception exception) {
                fail("Exception has happened");
            }
        });
        movieApi.listTopRatedMovies();

        movieApi.cancelAllServices();
    }
    
    public void testCancelAllService_ListUpcomingMovies() {
        movieApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                fail("Request not cancelled");
            }

            @Override
            public void onException(Exception exception) {
                fail("Exception has happened");
            }
        });
        movieApi.listUpcomingMovies();

        movieApi.cancelAllServices();
    }
    
    public void testCancelAllService_ListPopularMovies() {
        movieApi.setApiResultListener(new ApiResultListener() {
            @Override
            public void onResult(Object object) {
                fail("Request not cancelled");
            }

            @Override
            public void onException(Exception exception) {
                fail("Exception has happened");
            }
        });
        movieApi.listPopularMovies();

        movieApi.cancelAllServices();
    }

}