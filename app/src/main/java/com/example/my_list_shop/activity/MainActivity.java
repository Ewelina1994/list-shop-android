package com.example.my_list_shop.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.my_list_shop.fragments.CommunicateBetweenFragments;
import com.example.my_list_shop.entity.Item;
import com.example.my_list_shop.fragments.FragmentActivityList;
import com.example.my_list_shop.fragments.NoActivityListFragment;
import com.example.my_list_shop.R;
import com.example.my_list_shop.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements CommunicateBetweenFragments {

    private static final String CURRENT_FRAGMENT = "currentFrgm";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewPagerId);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        //set fragment
        viewPagerAdapter.addFragment(new FragmentActivityList(), String.valueOf(R.string.list));
        viewPagerAdapter.addFragment(new NoActivityListFragment(), String.valueOf(R.string.list_archive));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_list_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_archive_black_24dp);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

        int[] navIcons = {
                R.drawable.ic_list_black_24dp,
                R.drawable.ic_archive_black_24dp,
        };
        int[] navLabels = {
                R.string.list,
                R.string.list_archive,
        };

        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.nav_tab, null);

            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);

            tab_label.setText(getResources().getString(navLabels[i]));
            tab_icon.setImageResource(navIcons[i]);

            tabLayout.getTabAt(i).setCustomView(tab);
        }

        if(savedInstanceState!=null){

        }

    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void sendItemToArchive(Item itemToArchive) {
        Fragment f = (NoActivityListFragment) viewPagerAdapter.getItem(1);
        if(f!=null) {
            ((NoActivityListFragment) f).addItemToArchivedListFromAnotherFragment(itemToArchive);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.id_sort) {
            sortListInFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortListInFragment() {
        //sprawdzamy która zakładka jest wybrana i odpowiednia metoda sortująca we fragmencie jest wywoływana
         if(tabLayout.getSelectedTabPosition()==0){
             Fragment f = (FragmentActivityList) viewPagerAdapter.getItem(0);
             if(f!=null) {
                 ((FragmentActivityList) f).sortList();
             }
         }else if(tabLayout.getSelectedTabPosition()==1){
             Fragment f = (NoActivityListFragment) viewPagerAdapter.getItem(1);
             if(f!=null) {
                 ((NoActivityListFragment) f).sortListByDate();
             }

         }
    }
}
