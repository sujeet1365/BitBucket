package com.example.notefactory.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notefactory.Activity.MainActivity;
import com.example.notefactory.Fragment.ShowNoteFragment;
import com.example.notefactory.Model.NoteItems;
import com.example.notefactory.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<NoteItems> list;

    public RecyclerViewAdapter(Context context, List<NoteItems> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_notes_layout,viewGroup,false);
        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_title.setText(list.get(position).title);
        holder.tv_body.setText(list.get(position).body);
        holder.tv_timestamp.setText(list.get(position).timestamp);

        holder.cv_openNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id",list.get(position).id);
                bundle.putString("title",list.get(position).title);
                bundle.putString("body",list.get(position).body);
                bundle.putString("timestamp",list.get(position).timestamp);
                bundle.putString("image_path",list.get(position).image_path);
                ShowNoteFragment showNoteFragment = new ShowNoteFragment();
                showNoteFragment.setArguments(bundle);
                MainActivity.fragmentManager.beginTransaction()
                                            .addToBackStack(new ShowNoteFragment().getClass().getName())
                                            .replace(R.id.home_fragment_container,showNoteFragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_body,tv_timestamp;
        CardView cv_openNotes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            tv_body = (TextView)itemView.findViewById(R.id.tv_body);
            tv_timestamp = (TextView)itemView.findViewById(R.id.tv_timestamp);
            cv_openNotes = (CardView) itemView.findViewById(R.id.cv_openNotes);
        }
    }
}
