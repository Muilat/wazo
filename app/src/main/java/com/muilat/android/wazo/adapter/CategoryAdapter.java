package com.muilat.android.wazo.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.muilat.android.wazo.CommonFragment;
import com.muilat.android.wazo.R;
import com.muilat.android.wazo.data.Categories;


public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Cursor mCursor;
    private final String TAG = CategoryAdapter.class.getSimpleName();

    Context mContext;

    String user_lang_pref;



    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         mContext = parent.getContext();
        final  ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.category_item, parent, false));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = viewHolder.getAdapterPosition();
                Categories category = getItem(position);


                //use CommonFragment to list word acc to category
                CommonFragment commonFragment = new CommonFragment();
                Bundle args = new Bundle();
                args.putInt(CommonFragment.ARG_CATEGORY_ID,category.getId());
                commonFragment.setArguments(args);

//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.container,commonFragment );
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();

            }
        });


        return viewHolder;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {

        mCursor.moveToPosition(position); // get to the right location in the cursor

        Categories category = new Categories(mCursor);


        holder.name.setText(category.getDisplayName(mContext));

//        holder.name.setBackgroundColor(category.getColorInt());


    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * Return a {@link Categories} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position Cursor item position
     * @return A new {@link Categories}
     */
    public Categories getItem(int position) {
        if (mCursor.moveToPosition(position)) {
            return new Categories(mCursor);
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
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.category_name);
        }
    }
}