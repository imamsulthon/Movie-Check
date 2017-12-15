package com.tassioauad.moviecheck;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tassioauad.moviecheck.dagger.ListPopularPersonViewModule;
import com.tassioauad.moviecheck.model.entity.Person;
import com.tassioauad.moviecheck.presenter.ListPopularPersonPresenter;
import com.tassioauad.moviecheck.view.activity.PersonProfileActivity;
import com.tassioauad.moviecheck.view.adapter.ListViewAdapterWithPagination;
import com.tassioauad.moviecheck.view.adapter.OnItemClickListener;
import com.tassioauad.moviecheck.view.adapter.OnShowMoreListener;
import com.tassioauad.moviecheck.view.adapter.PopularPersonListAdapter;
import com.tassioauad.moviecheck.view.fragment.ListPopularPersonView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListPopularPersonActivity extends AppCompatActivity implements ListPopularPersonView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview_actor)
    RecyclerView recyclerViewPersons;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;
    @Bind(R.id.linearlayout_anyfounded)
    LinearLayout linearLayoutAnyFounded;
    @Bind(R.id.linearlayout_loadfailed)
    LinearLayout linearLayoutLoadFailed;

    @Inject
    ListPopularPersonPresenter presenter;
    private List<Person> personList;
    private ListViewAdapterWithPagination listViewAdapter;
    private Integer page = 1;
    private Integer columns = 3;
    private int scrollToItem;
    private static final String BUNDLE_KEY_PERSONLIST = "bundle_key_personlist";
    private static final String BUNDLE_KEY_PAGE = "bundle_key_page";
    private final int itemsPerPage = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_popular_person);

        ButterKnife.bind(this);

        ((MovieCheckApplication) getApplication()).getObjectGraph().plus(new ListPopularPersonViewModule(this)).inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Popular Person");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            presenter.loadPersons(page);
        } else {
            List<Person> personList = savedInstanceState.getParcelableArrayList(BUNDLE_KEY_PERSONLIST);
            if (personList == null) {
                presenter.loadPersons(page);
            } else if (personList.size() == 0) {
                warnAnyPersonFounded();
            } else {
                page = savedInstanceState.getInt(BUNDLE_KEY_PAGE);
                showPerson(personList);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tracker defaultTracker = ((MovieCheckApplication) getApplication()).getDefaultTracker();
        defaultTracker.setScreenName("List of Popular Person Screen");
        defaultTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (personList != null) {
            outState.putParcelableArrayList(BUNDLE_KEY_PERSONLIST, new ArrayList<Parcelable>(personList));
        }
        outState.putInt(BUNDLE_KEY_PAGE, page);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoadingPersons() {
        progressBar.setVisibility(View.VISIBLE);
        linearLayoutAnyFounded.setVisibility(View.GONE);
        linearLayoutLoadFailed.setVisibility(View.GONE);
    }

    @Override
    public void warnAnyPersonFounded() {
        if (personList == null) {
            linearLayoutAnyFounded.setVisibility(View.VISIBLE);
            linearLayoutLoadFailed.setVisibility(View.GONE);
            recyclerViewPersons.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, getString(R.string.general_anyfounded), Toast.LENGTH_SHORT).show();
            listViewAdapter.withShowMoreButton(false);
            recyclerViewPersons.setAdapter(listViewAdapter);
        }
    }

    @Override
    public void showPerson(List<Person> newPersonList) {
        if (personList == null) {
            personList = newPersonList;
        } else {
            personList.addAll(newPersonList);
        }
        linearLayoutAnyFounded.setVisibility(View.GONE);
        linearLayoutLoadFailed.setVisibility(View.GONE);
        recyclerViewPersons.setVisibility(View.VISIBLE);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, columns, GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position >= personList.size() ? columns : 1;
            }
        });
        recyclerViewPersons.setLayoutManager(layoutManager);
        recyclerViewPersons.setItemAnimator(new DefaultItemAnimator());
        listViewAdapter = new ListViewAdapterWithPagination(new PopularPersonListAdapter(personList, new OnItemClickListener<Person>() {
            @Override
            public void onClick(Person person, View view) {
                startActivity(PersonProfileActivity.newIntent(ListPopularPersonActivity.this, person), ActivityOptionsCompat.makeSceneTransitionAnimation(ListPopularPersonActivity.this, view.findViewById(R.id.imageview_photo), "personPhoto").toBundle());
            }

            @Override
            public void onLongClick(Person person, View view) {

            }
        }), new OnShowMoreListener() {
            @Override
            public void showMore() {
                scrollToItem = layoutManager.findFirstVisibleItemPosition();
                presenter.loadPersons(++page);
            }
        }, itemsPerPage);
        recyclerViewPersons.setAdapter(listViewAdapter);
        recyclerViewPersons.scrollToPosition(scrollToItem);
    }

    @Override
    public void hideLoadingPersons() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void warnFailedToLoadPersons() {
        if (personList == null) {
            linearLayoutAnyFounded.setVisibility(View.GONE);
            linearLayoutLoadFailed.setVisibility(View.VISIBLE);
            linearLayoutLoadFailed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.loadPersons(page);
                }
            });
            recyclerViewPersons.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, getString(R.string.general_failedtoload), Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ListPopularPersonActivity.class);
    }
}
