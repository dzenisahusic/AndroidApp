package com.comtrade.edit2014sales;

import java.util.Random;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import com.comtrade.edit2014salesDzenisa.R;

public class MainActivity extends FragmentActivity {
	static ViewPager mViewPager;
	private ArtikliDAO artikliDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        artikliDAO = new ArtikliDAO(this);
        artikliDAO.otvoriBazu();
        
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });
        
        FragmentManager fm = getSupportFragmentManager();
        TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(fm);
        mViewPager.setAdapter(pagerAdapter);
        
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getActionBar().setDisplayShowTitleEnabled(false);
        
        
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				mViewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {	
			}
        };
        
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Kasa")
                        .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Lista artikala")
                        .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Dodaj artikal")
                        .setTabListener(tabListener));  
       
    }  
    
    public static void promijeniFragmentIUredi (int frag, Artikal artikal) {
    	mViewPager.setCurrentItem(frag);
    	if(artikal != null) DodajArtikalFragment.izmijeniArtikal(artikal);
    }

	
}
