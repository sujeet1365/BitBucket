package com.example.notefactory.Fragment;


import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notefactory.Activity.MainActivity;
import com.example.notefactory.R;
import com.example.notefactory.Utility.SQLiteHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowNoteFragment extends Fragment {

    ImageView img_edit_btn,img_delete_btn,image_note;
    TextView tv_note_title,tv_note_body,tv_noteTimestamp;
    String title,body,timestamp,imagepath;
    int id;

    public ShowNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_note, container, false);

        image_note = (ImageView)view.findViewById(R.id.image_note);
        img_edit_btn = (ImageView)view.findViewById(R.id.img_edit_btn);
        img_delete_btn = (ImageView)view.findViewById(R.id.img_delete_btn);
        tv_note_title = (TextView) view.findViewById(R.id.tv_note_title);
        tv_note_body = (TextView) view.findViewById(R.id.tv_note_body);
        tv_noteTimestamp = (TextView) view.findViewById(R.id.tv_noteTimestamp);

        id = getArguments().getInt("id");
        title = getArguments().getString("title");
        body = getArguments().getString("body");
        timestamp = getArguments().getString("timestamp");
        imagepath = getArguments().getString("image_path");

        tv_note_title.setText(title);
        tv_note_body.setText(body);
        tv_noteTimestamp.setText(timestamp);

        if (imagepath != null) {
            image_note.setVisibility(View.VISIBLE);
            image_note.setImageBitmap(BitmapFactory.decodeFile(imagepath));
        }else {
            image_note.setVisibility(View.GONE);
        }

        image_note.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);

                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                image_note.setColorFilter(filter);
                return false;
            }
        });

        img_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.edit_note_layout,null);
                final EditText edt_edit_title = (EditText)view1.findViewById(R.id.edt_edit_title);
                final EditText edt_edit_body = (EditText)view1.findViewById(R.id.edt_edit_body);
                edt_edit_title.setText(title);
                edt_edit_body.setText(body);
                Button btn_update = (Button)view1.findViewById(R.id.btn_update);

                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = edt_edit_title.getText().toString().trim();
                        String body = edt_edit_body.getText().toString().trim();
                        if (!title.equals("") && !body.equals("")) {
                            SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
                            SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
                            db.execSQL("UPDATE " + SQLiteHelper.TABLE_NAME + " SET " + SQLiteHelper.Column_TITLE + " = " +"\""+ title +"\"" + " WHERE " + SQLiteHelper.Column_Number + " = " + id);
                            db.execSQL("UPDATE " + SQLiteHelper.TABLE_NAME + " SET " + SQLiteHelper.Column_BODY + " = "+"\"" + body +"\""+ " WHERE " + SQLiteHelper.Column_Number + " = " + id);
                            db.close();
                            tv_note_title.setText(title);
                            tv_note_body.setText(body);
                            Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(getContext(), "Empty field can't be saved!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.setView(view1);
                dialog.show();
            }
        });

        img_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
                SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
                db.delete(SQLiteHelper.TABLE_NAME, SQLiteHelper.Column_Number + " = ?",
                        new String[]{String.valueOf(id)});
                db.close();
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.home_fragment_container,new HomeFragment()).commit();
            }
        });

        return view;
    }

}
