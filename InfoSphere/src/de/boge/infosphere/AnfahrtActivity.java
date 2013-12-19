package de.boge.infosphere;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.boge.infosphere.utils.NavDrawerHandler;

public class AnfahrtActivity extends SherlockFragmentActivity {
	private DrawerLayout navDrawerLayout;
	private ListView navDrawerList;
	private ActionBarDrawerToggle navDrawerToggle;
	private String[] navDrawerTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anfahrt);

		// Beginn Navigation-Drawer
		navDrawerTitles = getResources().getStringArray(R.array.hauptmenu_eintrage);
		navDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		navDrawerList = (ListView) findViewById(R.id.left_drawer);

		navDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		navDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, navDrawerTitles));
		navDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		navDrawerToggle = new ActionBarDrawerToggle(this, navDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
			}
		};
		navDrawerLayout.setDrawerListener(navDrawerToggle);
		// Ende Navigation-Drawer
		
		// Setzt einen Marker bei der Position des InfoSpheres und zoomt dorthin
        GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng infosphere = new LatLng(50.7802596, 6.1038168);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(infosphere, 12));
        map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// Starte die Navigation mit Google Maps, sobald der Marker gedrückt wird
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + 50.7802596 + "," + 6.1038168));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				return false;
			}
		});
        map.addMarker(new MarkerOptions().title("InfoSphere").position(infosphere));
	}

	/**
	 * Behandelt einen Klick im NavigationDrawer entsprechend
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			NavDrawerHandler.entryClicked(position, getBaseContext(), AnfahrtActivity.this);
			navDrawerLayout.closeDrawer(navDrawerList);
		}
	}
	
	/**
	 * Lädt die Menü-Items
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main_light, menu);
		return true;
	}

	/**
	 * Behandelt einen Klick auf ein Menü-Item entsprechend
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (navDrawerLayout.isDrawerOpen(navDrawerList)) {
				navDrawerLayout.closeDrawer(navDrawerList);
			} else {
				navDrawerLayout.openDrawer(navDrawerList);
			}
		} else if (item.getItemId() == R.id.action_settings) {
			startActivity(new Intent(AnfahrtActivity.this, SettingsActivity.class));
		} else if (item.getItemId() == R.id.action_about) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.about_title);
			builder.setMessage(R.string.about_text);
			builder.setPositiveButton(R.string.about_close,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		navDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		navDrawerToggle.syncState();
	}
}