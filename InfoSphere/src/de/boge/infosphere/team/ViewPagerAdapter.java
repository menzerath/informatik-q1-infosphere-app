package de.boge.infosphere.team;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Der ViewPager für das Team: Lädt für jeden Tab das ensprechende Fragment und beinhaltet die Anzahl der angezeigten Tabs
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
 
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int arg0) {
        switch (arg0) {
        case 0:
            TabLeitung fragmenttab1 = new TabLeitung();
            return fragmenttab1;
        case 1:
        	TabMitarbeiter fragmenttab2 = new TabMitarbeiter();
            return fragmenttab2;
        case 2:
        	TabPartner fragmenttab3 = new TabPartner();
            return fragmenttab3;            
        }
        return null;
    }
 
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}