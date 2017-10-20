package com.tassioauad.moviecheck.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tassioauad.moviecheck.R;
import com.tassioauad.moviecheck.model.entity.Crew;

import java.util.List;

/**
 * Created by eventlink on 19/10/17.
 */

public class DirectedByAdapter extends RecyclerView.Adapter<DirectedByAdapter.ViewHolder> implements View.OnClickListener {

    private List<Crew> directorList;
    private OnItemClickListener<Crew> directorOnItemClickListener;

    public DirectedByAdapter(List<Crew> directorList, OnItemClickListener<Crew> directorOnItemClickListener) {
        this.directorList = directorList;
        this.directorOnItemClickListener = directorOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewitem_directedby, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Crew director = directorList.get(position);

        holder.itemView.setTag(director);
        holder.textViewName.setText(director.getName());
    }

    @Override
    public int getItemCount() {
        return directorList.size();
    }

    @Override
    public void onClick(View view) {
        Crew crew = (Crew) view.getTag();
        directorOnItemClickListener.onClick(crew, view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textview_name);
        }
    }
}
