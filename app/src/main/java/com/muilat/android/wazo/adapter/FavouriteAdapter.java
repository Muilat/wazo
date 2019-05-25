package com.muilat.android.wazo.adapter;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muilat.android.wazo.DictionaryActivity;
import com.muilat.android.wazo.FavouriteActivity;
import com.muilat.android.wazo.R;
import com.muilat.android.wazo.data.DictionaryContract;
import com.muilat.android.wazo.data.Word;
import com.muilat.android.wazo.data.WordContract;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    //    private ArrayList<Word> mCursor;
//    ArrayList<Word> mWordArrayList;
    private final String TAG = WordAdapter.class.getSimpleName();

    Context mContext;

    private ArrayList<Word> wordsArrayList;

    //    String user_lang_pref;
    static Drawable d;





    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final  ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.word_item, parent, false));




//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                int position = viewHolder.getAdapterPosition();
////                Word word = getItem(position);
//
//
//
//            }
//        });
//        d = ContextCompat.getDrawable(mContext,R.drawable.first_letter_circle);

        return viewHolder;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(FavouriteAdapter.ViewHolder holder, final int position) {

        final Word word = wordsArrayList.get(position);


//        wordsArrayList.add(word);


//        d.setColorFilter(Utils.generateColor(), PorterDuff.Mode.DARKEN);

//        holder.icon.setBackground(d);
        holder.name.setText(word.getDisplayName(mContext));
        holder.yoruba.setText(word.getYoruba());

        holder.icons_parent.setVisibility(View.GONE);
        holder.deleteIcon.setVisibility(View.VISIBLE);

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mContext instanceof FavouriteActivity)
                    removeFav(view, word, position);
                else if(mContext instanceof DictionaryActivity)
                    removeDictionary(view, word, position);



            }
        });




//        Log.e(TAG, word.getTitle()+" is here");

    }

    private void removeFav(View view, final Word word, final int position) {
        //remove from favoirites
        String stringId = Long.toString(word.getID());
        Uri uri = WordContract.CONTENT_URI;
        final Uri appended_uri = uri.buildUpon().appendPath(stringId).build();

        final ContentValues contentValues = new ContentValues();
        contentValues.put(WordContract.WordEntry.COLUMN_IS_FAVOURITE, 0);
        mContext.getContentResolver().update(appended_uri,contentValues,null,null);

        /*This removes the item from the recycler view*/
        wordsArrayList.remove(word);
        if (wordsArrayList.isEmpty()){
            FavouriteActivity.favouriteEmpty();
        }
        notifyDataSetChanged();

        Snackbar snackbar = Snackbar.make(view,word.getDisplayName(mContext)+ " deleted from favorites!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO:resave the item to db
                contentValues.put(WordContract.WordEntry.COLUMN_IS_FAVOURITE, 1);
                mContext.getContentResolver().update(appended_uri,contentValues,null,null);
                wordsArrayList.add(position, word);
                if (wordsArrayList.size()==1){
                    FavouriteActivity.favouriteNotEmpty();
                }
                notifyDataSetChanged();
            }
        });
        snackbar.setActionTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }


    private void removeDictionary(View view, final Word word, final int position) {
        //remove from dictionary
        String stringId = Long.toString(word.getID());
        Uri uri = DictionaryContract.CONTENT_URI;
        final Uri appended_uri = uri.buildUpon().appendPath(stringId).build();

        mContext.getContentResolver().delete(appended_uri,null,null);

        /*This removes the item from the recycler view*/
        wordsArrayList.remove(word);
        if (wordsArrayList.isEmpty()){
            DictionaryActivity.dictionaryEmpty();
        }
        notifyDataSetChanged();

        Snackbar snackbar = Snackbar.make(view,word.getDisplayName(mContext)+ " deleted from My Dictionaries!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO:resave the item to db
                ContentValues contentValues = new ContentValues();
                contentValues.put(DictionaryContract.DictionaryEntry.COLUMN_CATEGORY_ID, word.getCategoryID());
                contentValues.put(DictionaryContract.DictionaryEntry.COLUMN_ENGLISH, word.getEnglish());
                contentValues.put(DictionaryContract.DictionaryEntry.COLUMN_HAUSA, word.getHausa());
                contentValues.put(DictionaryContract.DictionaryEntry.COLUMN_YORUBA, word.getYoruba());
                contentValues.put(DictionaryContract.DictionaryEntry.COLUMN_IS_FAVOURITE, word.isFavourite());
                mContext.getContentResolver().insert(DictionaryContract.CONTENT_URI,contentValues);
                wordsArrayList.add(position, word);
                if (wordsArrayList.size()==1){
                    DictionaryActivity.dictionaryNotEmpty();
                }
                notifyDataSetChanged();
            }
        });
        snackbar.setActionTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

    @Override
    public int getItemCount() {
        if (wordsArrayList == null) {
            return 0;
        }
        return wordsArrayList.size();
    }

    /**
     * Return a {@link Word} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position ArrayList<Word> item position
     * @return A new {@link Word}
     */
    public Word getItem(int position) {
        if(position < wordsArrayList.size() || position > wordsArrayList.size()){
            return wordsArrayList.get(position);
        }
        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(ArrayList<Word> data) {
        Log.e(TAG, "Swapping Word");
        wordsArrayList = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, yoruba;
        //            CardView word_card;
        LinearLayout icons_parent;
        ImageView deleteIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.word_text);
            yoruba = itemView.findViewById(R.id.word_convert);
            icons_parent = itemView.findViewById(R.id.icons_parent);
            deleteIcon = itemView.findViewById(R.id.delete_fav);

        }
    }

}

