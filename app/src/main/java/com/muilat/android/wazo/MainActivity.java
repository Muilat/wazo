package com.muilat.android.wazo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jorgecastilloprz.pagedheadlistview.PagedHeadListView;
import com.jorgecastilloprz.pagedheadlistview.utils.PageTransformerTypes;
import com.muilat.android.wazo.adapter.CategoryListAdapter;
import com.muilat.android.wazo.data.Categories;
import com.muilat.android.wazo.data.CategoryContract;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    public static FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addWordIntent = new Intent(MainActivity.this, AddWordActiviity.class);
                startActivity(addWordIntent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ViewPager viewPager = findViewById(R.id.viewpager);
        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);


        FragmentPagerItems pages = new FragmentPagerItems(this);

        CategoryFragment categoryFragment = new CategoryFragment();
        CommonFragment commonFragment = new CommonFragment();
        pages.add(FragmentPagerItem.of("Categories", categoryFragment.getClass()));
        pages.add(FragmentPagerItem.of("All Words", commonFragment.getClass()));
        pages.add(FragmentPagerItem.of("Conversations", commonFragment.getClass()));


        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(getIntent().getIntExtra(DEFAULT_TAB,1));
        viewPagerTab.setViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            //pass current dat as extra
            startActivity(searchIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_favourite) {
            Intent favouriteIntent = new Intent(this, FavouriteActivity.class);
            startActivity(favouriteIntent);
        } else if (id == R.id.nav_dico) {
            Intent dictionaryIntent = new Intent(this, DictionaryActivity.class);
            startActivity(dictionaryIntent);
        } else if (id == R.id.nav_quiz) {
            Intent quizIntent = new Intent(this, QuizActivity.class);
            startActivity(quizIntent);
        } else if (id == R.id.nav_manage) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);

        } else if (id == R.id.nav_card) {
            Intent flashCardIntent = new Intent(this, FlashCardActivity.class);
            startActivity(flashCardIntent);

        } else if (id == R.id.nav_share) {
            String textToShare = "Checkout "+getString(R.string.app_name);
            textToShare += ": An awesome app to learn Yoruba language ";
            textToShare += "\nInstall from http://play.google.com/store/apps/details?id=" + getPackageName();

            Intent shareIntent = ShareCompat.IntentBuilder.from(MainActivity.this)
                    .setText(textToShare)
                    .setChooserTitle("Share "+getString(R.string.app_name)+" with")
                    .setSubject(getString(R.string.app_name))
                    .setType("text/plain")
                    .createChooserIntent();

            if(shareIntent.resolveActivity(getPackageManager()) != null){
                startActivity(shareIntent);
            }
        } else if (id == R.id.nav_exit) {
//            showInterstitialAd();
            finish();
        } else if (id == R.id.nav_rate_app) {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + getPackageName())));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        }else if(id==R.id.nav_about){
//            showInterstitialAd();
//            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
//            intent.putExtra(InfoActivity.EXTRA_INFO_TYPE, InfoActivity.EXTRA_INFO_TYPE_ABOUT);
//            startActivity(intent);
        }else if(id==R.id.nav_developer){
//            showInterstitialAd();
//            Intent intent = new Intent(MainActivity.this, DeveloperActivity.class);
//            startActivity(intent);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mCategoryAdapter.swapCursor(mCategories);
    }
}
