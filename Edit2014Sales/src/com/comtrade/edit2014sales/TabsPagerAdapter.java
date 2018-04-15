package com.comtrade.edit2014sales;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

public class TabsPagerAdapter extends FragmentPagerAdapter  {
	final int PAGE_COUNT = 3;
	KasaFragment kasa;
	ListaArtikalaFragment lista;
	DodajArtikalFragment novi;
	
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		kasa = new KasaFragment();
		lista = new ListaArtikalaFragment();
		novi = new DodajArtikalFragment();
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
        case 0:
        	return kasa;
        case 1:
            return lista;
        case 2: 
            return novi;
        default:
            return null;
        }
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	
}
