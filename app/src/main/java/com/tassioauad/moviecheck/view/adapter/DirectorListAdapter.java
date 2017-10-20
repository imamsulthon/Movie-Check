package com.tassioauad.moviecheck.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tassioauad.moviecheck.R;
import com.tassioauad.moviecheck.model.entity.Crew;

import java.util.List;

/**
 * Created by imamsulthon on 17/10/17.
 */

public class DirectorListAdapter extends RecyclerView.Adapter<DirectorListAdapter.ViewHolder> implements View.OnClickListener {

    private List<Crew> crewList;
    private OnItemClickListener<Crew> directorOnItemClickListener;

    public DirectorListAdapter(List<Crew> crewList, OnItemClickListener<Crew> crewOnItemClickListener) {
        this.crewList = crewList;
        this.directorOnItemClickListener = crewOnItemClickListener;
    }

    @Override
    public DirectorListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewitem_director, parent, false);
        view.setOnClickListener(this);
        return new DirectorListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Crew crew = crewList.get(position);

        holder.itemView.setTag(crew);
        holder.textViewName.setText(crew.getName());
        holder.progressBar.setVisibility(View.VISIBLE);
        String posterUrl = holder.itemView.getContext().getString(R.string.imagetmdb_baseurl) + crew.getProfilePath();
        Picasso.with(holder.itemView.getContext()).load(posterUrl).placeholder(R.drawable.noimage).
                into(holder.imageViewProfile, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.progressBar.setVisibility(View.GONE);
                Picasso.with(holder.itemView.getContext()).load(R.drawable.noimage).into(holder.imageViewProfile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }

    @Override
    public void onClick(View view) {
        Crew crew = (Crew) view.getTag();
        directorOnItemClickListener.onClick(crew, view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewProfile;
        private TextView textViewName;
        private ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewProfile = (ImageView) itemView.findViewById(R.id.imageview_profile);
            textViewName = (TextView) itemView.findViewById(R.id.textview_name);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }
}
