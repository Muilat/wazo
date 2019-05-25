package com.muilat.android.wazo.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.muilat.android.wazo.R;
import com.muilat.android.wazo.data.Categories;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends ArrayAdapter<Categories> {

    Context mContext;

    public CategoryListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public CategoryListAdapter(@NonNull Context context, @NonNull ArrayList<Categories> categories) {
        super(context, 0, categories);

        mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Categories object from the ArrayAdapter at the appropriate position
        Categories category = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_item, parent, false);
            convertView.setMinimumWidth(parent.getWidth());
        }



        CardView cardView = convertView.findViewById(R.id.category_card);
//        cardView.setMinimumWidth(convertView.getWidth());
//        ImageView iconView = (ImageView) convertView.findViewById(R.id.image_resource);




        TextView name = convertView.findViewById(R.id.category_name);
        name.setText(category.getDisplayName(mContext));


        return convertView;
    }
}
