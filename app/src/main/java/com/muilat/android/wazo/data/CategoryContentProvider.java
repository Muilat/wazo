package com.muilat.android.wazo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.muilat.android.wazo.data.CategoryContract.CategoryEntry.TABLE_NAME;


public class CategoryContentProvider  extends ContentProvider {

    public static final int WORDS = 100;
    public static final int CATEGORIES = 110;
    public static final int DICTIONARIES = 120;
    public static final int WORD_WITH_ID = 101;
    public static final int WORD_WITH_CATEGORY_ID = 102;
    public static final int CATEGORY_WITH_ID = 111;
    public static final int DICTIONARY_WITH_ID = 121;


    // Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = CategoryContentProvider.class.getSimpleName();

    // Define a static buildUriMatcher method that associates URI's with their int match
    /**
     Initialize a new matcher object without any matches,
     then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    /*
      All paths added to the UriMatcher have a corresponding int.
      For each kind of uri you may want to access, add the corresponding match with addURI.
      The two calls below add matches for the task directory and a single item by ID.
     */
        uriMatcher.addURI(CategoryContract.AUTHORITY, CategoryContract.PATH_CATEGORIES, CATEGORIES);
        uriMatcher.addURI(CategoryContract.AUTHORITY, CategoryContract.PATH_CATEGORIES + "/#", CATEGORY_WITH_ID);
        uriMatcher.addURI(WordContract.AUTHORITY, WordContract.PATH_WORDS, WORDS);
        uriMatcher.addURI(WordContract.AUTHORITY, WordContract.PATH_WORDS + "/#", WORD_WITH_ID);
        uriMatcher.addURI(WordContract.AUTHORITY, WordContract.PATH_WORDS + "/category/#", WORD_WITH_CATEGORY_ID);
        uriMatcher.addURI(WordContract.AUTHORITY, DictionaryContract.PATH_DICTIONARIES, DICTIONARIES);
        uriMatcher.addURI(WordContract.AUTHORITY, DictionaryContract.PATH_DICTIONARIES + "/#", DICTIONARY_WITH_ID);

        return uriMatcher;
    }

    // Member variable for a WazoDbHelper that's initialized in the onCreate() method
    private WazoDbHelper mWazoDbHelper;

    /* onCreate() is where you should initialize anything you’ll need to setup
    your underlying data source.
    In this case, you’re working with a SQLite database, so you’ll need to
    initialize a DbHelper to gain access to it.
     */
    @Override
    public boolean onCreate() {
        // Complete onCreate() and initialize a WazoDbhelper on startup
        // [Hint] Declare the DbHelper as a global variable

        Context context = getContext();
        mWazoDbHelper = new WazoDbHelper(context);
        return true;
    }


    // Implement insert to handle requests to insert a single new row of data
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mWazoDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the categories directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        long id;
        switch (match) {
            case CATEGORIES:
                // Insert new values into the database
                // Inserting values into categories table
                id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(CategoryContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case WORDS:
                // Insert new values into the database
                // Inserting values into words table
                id = db.insert(WordContract.WordEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(WordContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case DICTIONARIES:
                // Insert new values into the database
                // Inserting values into dictionaries table
                id = db.insert(DictionaryContract.DictionaryEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(DictionaryContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }


    // Implement query to handle requests for data by URI
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mWazoDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        String id;

        // Query for the categories directory and a default case
        switch (match) {
            // Query for the categories directory
            case CATEGORIES:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Handle the single category case, recognized by the ID included in the URI path
            case CATEGORY_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                retCursor =  db.query(TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            // Query for the words directory
            case WORDS:
                retCursor =  db.query(WordContract.WordEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Handle the single word case, recognized by the ID included in the URI path
            case WORD_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                retCursor =  db.query(WordContract.WordEntry.TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
                //get wordsby cat id
            case WORD_WITH_CATEGORY_ID:
                // Get the category ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                retCursor =  db.query(WordContract.WordEntry.TABLE_NAME,
                        projection,
                        "category_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            // Query for the words directory
            case DICTIONARIES:
                retCursor =  db.query(DictionaryContract.DictionaryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }


    // Implement delete to delete a single row of data
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mWazoDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted dictionaries
        int dictionariesDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case DICTIONARY_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                dictionariesDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (dictionariesDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of dictionaries deleted
        return dictionariesDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mWazoDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of updated category
        int itemsUpdated; // starts as 0
        String id;

        // [Hint] Use selections to update an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case CATEGORY_WITH_ID:
                // Get the category ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                itemsUpdated = db.update(CategoryContract.CategoryEntry.TABLE_NAME,values, "_id=?", new String[]{id});
                break;

            case WORD_WITH_ID:
                // Get the category ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                itemsUpdated = db.update(WordContract.WordEntry.TABLE_NAME,values, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (itemsUpdated != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return itemsUpdated;
    }


    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

}



