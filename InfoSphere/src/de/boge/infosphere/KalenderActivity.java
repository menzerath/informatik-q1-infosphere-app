package de.boge.infosphere;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.boge.infosphere.parser.CalendarItem;
import de.boge.infosphere.parser.Parser;
import de.boge.infosphere.utils.CalendarHelper;
import de.boge.infosphere.utils.CustomListAdapterOneRow;
import de.boge.infosphere.utils.CustomListAdapterTwoRows;
import de.boge.infosphere.utils.NavDrawerHandler;

public class KalenderActivity extends SherlockActivity {
	private DrawerLayout navDrawerLayout;
	private ListView navDrawerList;
	private ActionBarDrawerToggle navDrawerToggle;
	private String[] navDrawerTitles;
	
	private ArrayList<Object> parsedObjects = new ArrayList<Object>();
	
	private ListView listviewCalendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kalender);

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
		// Ende Navigation-Drwer

		// Beginn Kalender-Liste
		listviewCalendar = (ListView) findViewById(R.id.listView1);
		String[] dummyList = new String[] { "Lädt..." };

		CustomListAdapterOneRow adapter = new CustomListAdapterOneRow(this, dummyList);
		listviewCalendar.setAdapter(adapter);
		listviewCalendar.setEnabled(false);
		listviewCalendar.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final CalendarItem myItem = (CalendarItem) parsedObjects.get(position);
				
				// Dialog, der weitere Details zum Termin anzeigt
				AlertDialog.Builder builder = new AlertDialog.Builder(KalenderActivity.this);
				builder.setTitle(myItem.getTitle());
				if (myItem.getStartDate().equals(myItem.getEndDate())) {
					builder.setMessage("Zeitpunkt: " + myItem.getStartDate());
				} else {
					builder.setMessage("Zeitpunkt: " + myItem.getStartDate() + " bis " + myItem.getEndDate());
				}
				builder.setPositiveButton("In Kalender eintragen",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Starte ein Intent, welches die wichtigsten Daten an den Kalender weitergibt
								Intent intent = new Intent(Intent.ACTION_EDIT);
								intent.setType("vnd.android.cursor.item/event");
								intent.putExtra("title", myItem.getTitle());
								intent.putExtra("beginTime", CalendarHelper.parseFeedDate(myItem.getStartDate()));
								intent.putExtra("endTime", CalendarHelper.parseFeedDate(myItem.getEndDate()));
								intent.putExtra("allDay", true);
								intent.putExtra("eventLocation", "InfoSphere Aachen (Dennewartstraße 27)");
								startActivity(intent);
							}
						});
				builder.setNegativeButton("Weiterlesen",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Starte ein Intent, welches die Details in einem WebView anzeigt
								Intent i = new Intent(KalenderActivity.this, WebViewActivity.class);
								i.putExtra("abTitle", myItem.getTitle());
								if (myItem.getStartDate().equals(myItem.getEndDate())) {
									i.putExtra("abSubTitle", myItem.getStartDate());
								} else {
									i.putExtra("abSubTitle", myItem.getStartDate() + " bis " + myItem.getEndDate());
								}
								i.putExtra("abSubTitle", myItem.getStartDate());
								i.putExtra("url", myItem.getMore());
								startActivity(i);
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		new updateData().execute("");
	}

	/**
	 * Lädt die Daten asynchon aus dem Feed (mit dem Parser) und zeigt diese dann in den entsprechenden Views an.
	 */
	private class updateData extends AsyncTask<String, Void, String> {
		private String[] calendarTitles;
		private String[] calendarDates;

		/**
		 * Lädt die Daten in Arrays
		 */
		@Override
		protected String doInBackground(String... params) {
			parsedObjects = new Parser(KalenderActivity.this, 1).getFeedObjects(0);
			calendarTitles = new String[parsedObjects.size()];
			calendarDates = new String[parsedObjects.size()];
			for (int i = 0; i < parsedObjects.size(); i++) {
				calendarTitles[i] = (String) ((CalendarItem) parsedObjects.get(i)).getTitle();
				calendarDates[i] = (String) ((CalendarItem) parsedObjects.get(i)).getStartDate().split(" - ")[0].trim();
			}

			return "Executed";
		}

		/**
         * Lädt die geparsten Daten in die entsprechenden View-Elemente
         */
		@Override
		protected void onPostExecute(String result) {
			if (calendarTitles.length == 0 || calendarDates.length == 0) {
				// Keine Daten geladen
				CustomListAdapterOneRow adapterCalendar = new CustomListAdapterOneRow(KalenderActivity.this, new String[]{"Es konnten keine Daten geladen werden."});
				listviewCalendar.setAdapter(adapterCalendar);
				listviewCalendar.setEnabled(false);
			} else {
				// Daten erfolgreich geladen
				CustomListAdapterTwoRows adapterCalendar = new CustomListAdapterTwoRows(KalenderActivity.this, calendarTitles, calendarDates);
				listviewCalendar.setAdapter(adapterCalendar);
				listviewCalendar.setEnabled(true);
			}
		}
	}
	
	/**
	 * Behandelt einen Klick im NavigationDrawer entsprechend
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			NavDrawerHandler.entryClicked(position, getBaseContext(), KalenderActivity.this);
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
			startActivity(new Intent(KalenderActivity.this, SettingsActivity.class));
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