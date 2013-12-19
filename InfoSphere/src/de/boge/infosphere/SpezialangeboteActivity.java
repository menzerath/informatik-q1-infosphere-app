package de.boge.infosphere;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.boge.infosphere.utils.NavDrawerHandler;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SpezialangeboteActivity extends SherlockActivity {
	private DrawerLayout navDrawerLayout;
	private ListView navDrawerList;
	private ActionBarDrawerToggle navDrawerToggle;
	private String[] navDrawerTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spezialangebote);

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
		
		// Alle (wichtigen) View-Elemente der Spezialangebote
		TextView tv1 = (TextView) findViewById(R.id.textView2);
		TextView tv2 = (TextView) findViewById(R.id.textView4);
		TextView tv3 = (TextView) findViewById(R.id.textView6);
		TextView tv4 = (TextView) findViewById(R.id.textView8);
		TextView tv5 = (TextView) findViewById(R.id.textView10);
		TextView tv6 = (TextView) findViewById(R.id.textView12);
		// Ende der View-Elemente
		
		// OnClickListener für die TextViews, welche dann eine Detail-Ansicht öffnen
		tv1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openUrl("http://schuelerlabor.informatik.rwth-aachen.de/infosphere-wettbewerb-2013-fuer-sus-der-oberstufe");
			}
		});
		tv2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openUrl("http://schuelerlabor.informatik.rwth-aachen.de/info-wettbewerb-2012-fuer-kids-der-klassen-7-bis-10-vom-3-bis-16-september-2012");
			}
		});
		tv3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openUrl("http://schuelerlabor.informatik.rwth-aachen.de/cs4hs-workshop");
			}
		});
		tv4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openUrl("http://www.informatik.rwth-aachen.de/Studium/Interessierte/Schueler/veranstaltungen.php");
			}
		});
		tv5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openUrl("http://www.rwth-aachen.de/aw/zentral/deutsch/Zielgruppen/~co/schueler/");
			}
		});
		tv6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openUrl("http://schuelerlabor.informatik.rwth-aachen.de/weitere-angebote");
			}
		});
		// Ende der OnClickListener
	}
	
	/**
	 * Ruft die URL des Spezialangebots in der WebViewActivity auf
	 * @param url
	 */
	private void openUrl(String url) {
		Intent i = new Intent(SpezialangeboteActivity.this, WebViewActivity.class);
		i.putExtra("abTitle", "Spezialangebote");
		i.putExtra("url", url);
		startActivity(i);
	}

	/**
	 * Behandelt einen Klick im NavigationDrawer entsprechend
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			NavDrawerHandler.entryClicked(position, getBaseContext(), SpezialangeboteActivity.this);
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
        	startActivity(new Intent(SpezialangeboteActivity.this, SettingsActivity.class));
        } else if (item.getItemId() == R.id.action_about) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setTitle(R.string.about_title);
        	builder.setMessage(R.string.about_text);
        	builder.setPositiveButton(R.string.about_close, new DialogInterface.OnClickListener() {
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