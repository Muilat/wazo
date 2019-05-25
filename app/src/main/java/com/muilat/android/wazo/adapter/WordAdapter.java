package com.muilat.android.wazo.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.muilat.android.wazo.CategoryViewActivity;
import com.muilat.android.wazo.R;
import com.muilat.android.wazo.data.Word;
import com.muilat.android.wazo.data.WordContract;
import com.muilat.android.wazo.utils.Utils;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    private Cursor mCursor;
    private final String TAG = WordAdapter.class.getSimpleName();

    Context mContext;
    private int pref = -1;


    @Override
        public WordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            final  ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.word_item, parent, false));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = viewHolder.getAdapterPosition();
                    Word word = getItem(position);


                }
            });


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final WordAdapter.ViewHolder holder, int position) {

            mCursor.moveToPosition(position); // get to the right location in the cursor

            final Word word = new Word(mCursor);

//            Log.e(TAG, word.getEnglish());

            switch (pref){
                case 0://yoruba
                    int lang = Utils.getLangPref(mContext);

                    if (lang == Utils.ENGLISH_YORUBA)
                        holder.yoruba.setText(word.getEnglish());
                    else
                        holder.yoruba.setText(word.getHausa());

                    holder.name.setText(word.getYoruba());
                    break;
                case Utils.ENGLISH_YORUBA:
                    holder.name.setText(word.getEnglish());
                    holder.yoruba.setText(word.getYoruba());
                    break;
                case Utils.HAUSA_YORUBA:
                    holder.name.setText(word.getHausa());
                    holder.yoruba.setText(word.getYoruba());
                    break;
                default:
                    holder.name.setText(word.getDisplayName(mContext));
                    holder.yoruba.setText(word.getYoruba());
                    break;

            }




            holder.icons_parent.setTag(word);

            if(word.isFavourite()){
                holder.favouriteIcon.setLiked(true);
            }

            holder.favouriteIcon.setOnLikeListener( new OnLikeListener(){
                @Override
                public void liked(LikeButton likeButton) {
                    String stringId = Long.toString(word.getID());
                    Uri uri = WordContract.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(stringId).build();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(WordContract.WordEntry.COLUMN_IS_FAVOURITE, 1);
                    Toast.makeText(mContext, word.getDisplayName(mContext)+" is added to favourite", Toast.LENGTH_SHORT).show();
                    word.setIsFavourite("1");
                    mContext.getContentResolver().update(uri,contentValues,null,null);

                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    String stringId = Long.toString(word.getID());
                    Uri uri = WordContract.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(stringId).build();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(WordContract.WordEntry.COLUMN_IS_FAVOURITE, 0);
                    Toast.makeText(mContext, word.getDisplayName(mContext)+" is removed from favourite", Toast.LENGTH_SHORT).show();
                    word.setIsFavourite("0");
                    mContext.getContentResolver().update(uri,contentValues,null,null);

                }

//                new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String stringId = Long.toString(word.getID());
//                    Uri uri = WordContract.CONTENT_URI;
//                    uri = uri.buildUpon().appendPath(stringId).build();
//
//                    ContentValues contentValues = new ContentValues();
//                    if(word.isFavourite()){
//                        contentValues.put(WordContract.WordEntry.COLUMN_IS_FAVOURITE, 0);
//                        Toast.makeText(mContext, word.getDisplayName(mContext)+" is removed from favourite", Toast.LENGTH_SHORT).show();
//                        holder.favouriteIcon.setImageResource(R.drawable.ic_favorite_border_black_24dp);
//                        word.setIsFavourite("0");
//
//                    }
//                    else {
//                        contentValues.put(WordContract.WordEntry.COLUMN_IS_FAVOURITE, 1);
//                        Toast.makeText(mContext, word.getDisplayName(mContext)+" is added to favourite", Toast.LENGTH_SHORT).show();
//                        holder.favouriteIcon.setImageResource(R.drawable.ic_favorite_24dp);
//                        word.setIsFavourite("1");
//
//                    }
//                    mContext.getContentResolver().update(uri,contentValues,null,null);
//
//                }
            });

            holder.copyIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    String textToShare = word.getDisplayName(mContext)+" means "+word.getYoruba()+" in yoruba";
                    textToShare += "\nCheckout more from Wazo app\nInstall from http://play.google.com/store/apps/details?id=" + mContext.getPackageName();

                    Intent shareIntent = ShareCompat.IntentBuilder.from((Activity)mContext)
                            .setText(textToShare)
                            .setChooserTitle("Share Wazo text(s) with")
                            .setSubject(mContext.getString(R.string.app_name))
                            .setType("text/plain")
                            .createChooserIntent();

                    if(shareIntent.resolveActivity(mContext.getPackageManager()) != null){
                        mContext.startActivity(shareIntent);
                    }
                }

            });

            holder.copyIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String textToCopy = word.getYoruba();
                    if(Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB){
                        android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setText(textToCopy);
                    }
                    else {
                        android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clipData = ClipData.newPlainText("Copied text" ,textToCopy);
                        clipboardManager.setPrimaryClip(clipData);
                    }
                    Toast.makeText(mContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
        }

        @Override
        public int getItemCount() {
            if (mCursor == null) {
                return 0;
            }
            return mCursor.getCount();
        }

        /**
         * Return a {@link Word} represented by this item in the adapter.
         * Method is used to run machine tests.
         *
         * @param position Cursor item position
         * @return A new {@link Word}
         */
        public Word getItem(int position) {
            if (mCursor.moveToPosition(position)) {
                return new Word(mCursor);
            }
            return null;
        }

        /**
         * @param data update cursor
         */
        public void swapCursor(Cursor data) {
            Log.e(TAG, "Swapping cursor");
            mCursor = data;
            notifyDataSetChanged();
        }

    /**
     * @param data update cursor for searchActivity
     * @pref language set pref
     */
    public void swapCursor(Cursor data, int pref) {
        Log.e(TAG, "Swapping cursor");
        mCursor = data;
        this.pref= pref;
        notifyDataSetChanged();
    }

        /**
         * An Recycler item view
         */
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView name, yoruba;
//            CardView word_card;
            LinearLayout icons_parent;
            ImageView copyIcon, shareIcon;
            LikeButton favouriteIcon;

            public ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.word_text);
                yoruba = itemView.findViewById(R.id.word_convert);
                icons_parent = itemView.findViewById(R.id.icons_parent);
                favouriteIcon = itemView.findViewById(R.id.favourite_icon);
                copyIcon = itemView.findViewById(R.id.copy_icon);
                shareIcon = itemView.findViewById(R.id.share_icon);
            }
        }
    }