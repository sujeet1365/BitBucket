package com.example.notefactory.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.notefactory.Activity.MainActivity;
import com.example.notefactory.Adapter.RecyclerViewAdapter;
import com.example.notefactory.Model.NoteItems;
import com.example.notefactory.R;
import com.example.notefactory.Utility.SQLiteHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView rvNotes;
    List<NoteItems> items;
    TextView tv_no_notes;
    RecyclerViewAdapter recyclerViewAdapter;
    FloatingActionButton fab_add;

    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvNotes = (RecyclerView)view.findViewById(R.id.rvNotes);
        tv_no_notes = (TextView)view.findViewById(R.id.tv_no_notes);
        items = new ArrayList<>();
        fab_add = (FloatingActionButton)view.findViewById(R.id.fab_add);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_add.setVisibility(View.GONE);
                MainActivity.fragmentManager.beginTransaction()
                        .addToBackStack(new HomeFragment().getClass().getName())
                        .replace(R.id.home_fragment_container,new NewNoteFragment()).commit();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvNotes.setLayoutManager(layoutManager);
        getSavedNotes();


        return view;
    }
    private void getSavedNotes(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+SQLiteHelper.Column_Number+" , "+SQLiteHelper.Column_TITLE+" , "+SQLiteHelper.Column_BODY+" , "+SQLiteHelper.Column_Timestamp+" , "+SQLiteHelper.Column_Image+" FROM " + SQLiteHelper.TABLE_NAME,null);
        while (cursor.moveToNext()){
            try {

                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String body = cursor.getString(2);
                String timestamp = cursor.getString(3);
                String path = cursor.getString(4);
                items.add(new NoteItems(id,title,body,path,timestamp));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        db.close();

        if (progressDialog.isShowing())
            progressDialog.dismiss();

        if (items.size() <= 0){
            tv_no_notes.setVisibility(View.VISIBLE);
        }else {
            tv_no_notes.setVisibility(View.GONE);
            Collections.reverse(items);
        }
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), items);
        rvNotes.setAdapter(recyclerViewAdapter);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onResume() {
        super.onResume();
        fab_add.setVisibility(View.VISIBLE);
    }
}
