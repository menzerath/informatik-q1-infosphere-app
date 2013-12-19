package de.boge.infosphere.module;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Der ViewPager für die Module: Lädt für jeden Tab das ensprechende Fragment und beinhaltet die Anzahl der angezeigten Tabs
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
 
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int arg0) {
        switch (arg0) {
        case 0:
            TabGrundschule fragmenttab1 = new TabGrundschule();
            return fragmenttab1;
        case 1:
            TabUnterstufe fragmenttab2 = new TabUnterstufe();
            return fragmenttab2;
        case 2:
            TabMittelstufe fragmenttab3 = new TabMittelstufe();
            return fragmenttab3;
        case 3:
        	TabOberstufe fragmenttab4 = new TabOberstufe();
        	return fragmenttab4;
        }
        return null;
    }
 
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}