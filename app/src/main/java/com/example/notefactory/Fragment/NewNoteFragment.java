package com.example.notefactory.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notefactory.Activity.MainActivity;
import com.example.notefactory.Model.NoteItems;
import com.example.notefactory.R;
import com.example.notefactory.Utility.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewNoteFragment extends Fragment {

    EditText edt_title,edt_body;
    TextView tv_save,tv_attach;
    ImageView img_note;
    String pic_path;

    public NewNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);

        tv_save = (TextView)view.findViewById(R.id.tv_save);
        edt_title = (EditText) view.findViewById(R.id.edt_title);
        edt_body = (EditText) view.findViewById(R.id.edt_body);
        tv_attach = (TextView)view.findViewById(R.id.tv_attach);
        img_note = (ImageView) view.findViewById(R.id.img_note);

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_title.getText().equals("") || edt_body.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Please save Note with title!!", Toast.LENGTH_SHORT).show();
                }else {
                    saveNote(edt_title.getText().toString().trim(),edt_body.getText().toString().trim());
                }
            }
        });

        tv_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDocuments();
            }
        });

        return view;
    }

    public void openDocuments(){
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent,1);
    }

    private void saveNote(String title,String body){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        try {
            progressDialog.show();
            SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
            SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");
            String timestamp = simpleDateFormat.format(new Date());

            db.execSQL("INSERT INTO " + SQLiteHelper.TABLE_NAME + " (" + SQLiteHelper.Column_TITLE + " ," +
                    SQLiteHelper.Column_BODY + " ," + SQLiteHelper.Column_Timestamp+" ," + SQLiteHelper.Column_Image+ " ) " +
                    " VALUES(" +"\""+ title+"\"" + "," +"\""+ body+"\""+" , " +"\""+ timestamp+ "\""+" , "+"\""+pic_path+"\""+");");
            db.close();

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            Toast.makeText(getContext(), "Note Saved", Toast.LENGTH_SHORT).show();
            edt_body.setText("");
            edt_title.setText("");
            MainActivity.fragmentManager.beginTransaction()
                                        .replace(R.id.home_fragment_container,new HomeFragment()).commit();
        }catch (Exception e){
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            pic_path = cursor.getString(columnIndex);
            img_note.setImageBitmap(BitmapFactory.decodeFile(pic_path));
            cursor.close();
        }
    }
}
