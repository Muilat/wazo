package com.muilat.android.wazo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.muilat.android.wazo.data.Categories;
import com.muilat.android.wazo.data.CategoryContract;
import com.muilat.android.wazo.data.DictionaryContract;
import com.muilat.android.wazo.data.WazoDbHelper;
import com.muilat.android.wazo.data.WordContract;

import java.util.ArrayList;
import java.util.Locale;

public class AddWordActiviity extends AppCompatActivity {

    EditText yorubaTxt, englishTxt, hausaTxt;
//    Spinner categorySpinner;
    Button addBtn;
    private ArrayAdapter categoryAdapter;
//    private SQLiteDatabase mDb;
//    private WazoDbHelper dbHelper;

    public int category_id = 0;//not needed but our dictionary of is Word


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        yorubaTxt = findViewById(R.id.new_word_yoruba);
        englishTxt = findViewById(R.id.new_word_english);
        hausaTxt = findViewById(R.id.new_word_hausa);
//        categorySpinner = findViewById(R.id.new_word_category);
        addBtn = findViewById(R.id.add_btn);

//        dbHelper = new WazoDbHelper(this);
//        mDb= dbHelper.getReadableDatabase();

//        String countQuery = "SELECT  * FROM " + CategoryContract.CategoryEntry.TABLE_NAME ;
//        Cursor cursor = mDb.rawQuery(countQuery, null);
//        ArrayList<Categories> categories = new ArrayList<>();
//
//        while(cursor.moveToNext()){
//            Categories category = new Categories(cursor);
//            category.getDisplayName(this);//this will also set the dusplayname
//            categories.add(category);
//        }
//
//
//        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
//        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        categorySpinner.setAdapter(categoryAdapter);
//
//        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                // Here you get the current item (a Categories object) that is selected by its position
//                Categories category = (Categories)categoryAdapter.getItem(position);
//                // Here you can do the action you want to...
//                category_id = category.getId();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapter) {
//            }
//        });



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String yoruba, hausa, english, id;

                hausa = hausaTxt.getText().toString();
                english = englishTxt.getText().toString();
                yoruba = yorubaTxt.getText().toString();

                if (!yoruba.equals("")) {

                    if (hausa.equals("") && english.equals("")){
                        hausaTxt.setError(/*getString*/("You have to enter either english or hausa text"));
                        englishTxt.setError(/*getString*/("You have to enter either english or hausa text"));
                        Snackbar.make(yorubaTxt, R.string.complete_form, Snackbar.LENGTH_LONG).show();
                        return;

                    }
                    if (hausa.equals(""))
                        hausa = "H";
                    else if(english.equals(""))
                        english = "E";

                    ContentValues values = new ContentValues();
                    values.put(WordContract.WordEntry.COLUMN_CATEGORY_ID, category_id);
                    values.put(WordContract.WordEntry.COLUMN_YORUBA, yoruba);
                    values.put(WordContract.WordEntry.COLUMN_ENGLISH, english);
                    values.put(WordContract.WordEntry.COLUMN_HAUSA, hausa);


            getContentResolver().insert(DictionaryContract.CONTENT_URI, values);
                    finish();
                } else {
                    yorubaTxt.setError(/*getString*/("Yoruba meaning cannot be empty"));
                    Snackbar.make(yorubaTxt, R.string.complete_form, Snackbar.LENGTH_LONG).show();
                }

            }
        });



    }

    public void closeSearch(View view) {
        finish();
    }
}
