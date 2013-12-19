package de.boge.infosphere;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.boge.infosphere.parser.NewsItem;
import de.boge.infosphere.parser.Parser;
import de.boge.infosphere.utils.CustomListAdapterOneRow;
import de.boge.infosphere.utils.CustomListAdapterTwoRows;
import de.boge.infosphere.utils.NavDrawerHandler;

import android.os.AsyncTask;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NewsActivity extends SherlockActivity {
	private DrawerLayout navDrawerLayout;
	private ListView navDrawerList;
	private ActionBarDrawerToggle navDrawerToggle;
	private String[] navDrawerTitles;
	
	private ArrayList<Object> parsedObjects = new ArrayList<Object>();
	
	private ListView listviewNews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

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
		
		// Beginn News-Liste
		listviewNews = (ListView) findViewById(R.id.listView1);
		String[] dummyList = new String[]{"Lädt..."};

		CustomListAdapterOneRow adapter = new CustomListAdapterOneRow(this, dummyList);
		listviewNews.setAdapter(adapter);
		listviewNews.setEnabled(false);
		listviewNews.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(NewsActivity.this, WebViewActivity.class);
				i.putExtra("abTitle", ((NewsItem) parsedObjects.get(position)).getTitle());
				i.putExtra("abSubTitle", ((NewsItem) parsedObjects.get(position)).getDate());
				i.putExtra("data", ((NewsItem) parsedObjects.get(position)).getContent());
				startActivity(i);
			}
		});
		// Ende News-Liste
		
		// Lade die Daten
		new updateData().execute("");
	}
	
	/**
	 * Lädt die Daten asynchon aus dem Feed (mit dem Parser) und zeigt diese dann in den entsprechenden Views an.
	 */
	private class updateData extends AsyncTask<String, Void, String> {
		private String[] newsTitles;
		private String[] newsDates;

		/**
		 * Lädt die Daten in Arrays
		 */
        @Override
        protected String doInBackground(String... params) {	
        	parsedObjects = new Parser(NewsActivity.this, 0).getFeedObjects(0);
    		newsTitles = new String[parsedObjects.size()];
    		newsDates = new String[parsedObjects.size()];
    		for (int i = 0; i < parsedObjects.size(); i++) {
    			newsTitles[i] = (String) ((NewsItem) parsedObjects.get(i)).getTitle();
    			newsDates[i] = (String) ((NewsItem) parsedObjects.get(i)).getDate();
    		}

            return "Executed";
        }

        /**
         * Lädt die geparsten Daten in die entsprechenden View-Elemente
         */
        @Override
        protected void onPostExecute(String result) {
        	if (newsTitles.length == 0 || newsDates.length == 0) {
    			CustomListAdapterOneRow adapter = new CustomListAdapterOneRow(NewsActivity.this, new String[]{"Es konnten keine Daten geladen werden."});
        		listviewNews.setAdapter(adapter);
        		listviewNews.setEnabled(false);
    		} else {
    			CustomListAdapterTwoRows adapter = new CustomListAdapterTwoRows(NewsActivity.this, newsTitles, newsDates);
    			listviewNews.setAdapter(adapter);
    			listviewNews.setEnabled(true);
    		}
        }
    }
	
	/**
	 * Behandelt einen Klick im NavigationDrawer entsprechend
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			NavDrawerHandler.entryClicked(position, getBaseContext(), NewsActivity.this);
			navDrawerLayout.closeDrawer(navDrawerList);
		}
	}

	/**
	 * Lädt die Menü-Items
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Blendet das Refresh-Icon aus, sobald der Navigation-Drawer geöffnet ist
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = navDrawerLayout.isDrawerOpen(navDrawerList);
		menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
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
        } else if (item.getItemId() == R.id.action_refresh) {
        	Toast.makeText(this, "Aktualisiere...", Toast.LENGTH_SHORT).show();
        	new updateData().execute("");
        } else if (item.getItemId() == R.id.action_settings) {
        	startActivity(new Intent(NewsActivity.this, SettingsActivity.class));
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
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		navDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		navDrawerToggle.onConfigurationChanged(newConfig);
	}
}