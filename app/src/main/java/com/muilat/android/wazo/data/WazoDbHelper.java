package com.muilat.android.wazo.data;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.muilat.android.wazo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.muilat.android.wazo.data.CategoryContract.CREATE_CATEGORIES_TABLE;
import static com.muilat.android.wazo.data.CategoryContract.*;
import static com.muilat.android.wazo.data.WordContract.CREATE_WORDS_TABLE;
import static com.muilat.android.wazo.data.WordContract.*;
import static com.muilat.android.wazo.data.DictionaryContract.CREATE_DICTIONARIES_TABLE;

public class WazoDbHelper  extends SQLiteOpenHelper {

    private static final String TAG = WazoDbHelper.class.getName();
    private static final String DB_NAME = "wazo.db";
    private static final int DB_VERSION = 1;

    private Resources mResources;

    public WazoDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mResources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        Log.e(TAG, "attemmpting to create");

        db.execSQL(CREATE_CATEGORIES_TABLE);/*create words table*/

        db.execSQL(CREATE_WORDS_TABLE);/*create words table*/

        db.execSQL(CREATE_DICTIONARIES_TABLE);/*create dictionaries table*/

        Log.e(TAG, "Db created");

        //only isert the asset from colourValue.json if the db is being created for the first time
        addInitWords(db);


        addInitCategories(db);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Query for altering table here
        final String ALTER_TABLE = "";

        if(ALTER_TABLE.equals("")){
            db.execSQL(ALTER_TABLE);
        }
        else
        {
            //no alter query so drop the existing database
            //drop words table
            db.execSQL("DROP TABLE IF EXISTS " + WordEntry.TABLE_NAME);

            //drop words table
            db.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);


            //create a new database
            onCreate(db);

        }

    }

    public static String getColumnString(Cursor cursor, String name) {
        return cursor.getString(cursor.getColumnIndex(name));
    }

    public static int getColumnInt(Cursor cursor, String name) {
        return cursor.getInt(cursor.getColumnIndex(name));
    }

    /**
     * save init categories into database
     */
    private void addInitWords(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            try {
                readWordsFromResources(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Unable to pre-fill database", e);
        }
    }

    /**
     * save init categories into database
     */
    private void addInitCategories(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            try {
                readCategoriesFromResources(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Unable to pre-fill database with categories", e);
        }
    }


    /**
     * load init words from {@link raw/wazodata.json}
     */
    private void readWordsFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.wazodata);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        JSONObject root = new JSONObject(builder.toString());


        JSONArray arr = root.getJSONArray("words");


        for (int i = 0; i<arr.length(); i++){
//        for (int i = 0; i<jsonObject.length(); i++){
            JSONObject obj = arr.getJSONObject(i);
            ContentValues contentValues = new ContentValues();
            // Put the insect  into the ContentValues
            contentValues.put(WordEntry.COLUMN_ENGLISH, obj.getString("english"));
            contentValues.put(WordEntry.COLUMN_HAUSA, obj.getString("hausa"));
            contentValues.put(WordEntry.COLUMN_YORUBA, obj.getString("yoruba"));
            contentValues.put(WordEntry.COLUMN_CATEGORY_ID, obj.getInt("category_id"));

            long id =db.insert(WordEntry.TABLE_NAME,null,contentValues);

            Log.e(TAG, "including data: word "+id);

        }
    }

    /**
     * load init categories from {@link raw/wazodata.json}
     */
    private void readCategoriesFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.wazodata);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        JSONObject root = new JSONObject(builder.toString());


        JSONArray arr = root.getJSONArray("categories");


        for (int i = 0; i<arr.length(); i++){
//        for (int i = 0; i<jsonObject.length(); i++){
            JSONObject obj = arr.getJSONObject(i);
            ContentValues contentValues = new ContentValues();
            // Put the insect  into the ContentValues
            contentValues.put(CategoryEntry.COLUMN_NAME, obj.getString("name"));
            contentValues.put(CategoryEntry.COLUMN_HAUSA, obj.getString("hausa"));
            contentValues.put(CategoryEntry.COLUMN_IMAGE_URL, obj.getString("imageUrl"));

            long id =db.insert(CategoryEntry.TABLE_NAME,null,contentValues);

            Log.e(TAG, "including data: category "+id);

        }
    }



}

