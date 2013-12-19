package de.boge.infosphere;

import java.util.ArrayList;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.boge.infosphere.parser.CalendarItem;
import de.boge.infosphere.parser.NewsItem;
import de.boge.infosphere.parser.Parser;
import de.boge.infosphere.utils.CalendarHelper;
import de.boge.infosphere.utils.NavDrawerHandler;

import android.net.Uri;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SherlockActivity {
	private DrawerLayout navDrawerLayout;
	private ListView navDrawerList;
	private ActionBarDrawerToggle navDrawerToggle;
	private String[] navDrawerTitles;
	
	private TextView news1;
	private TextView news2;
	private TextView news3;
	private TextView termin1;
	private TextView termin2;
	private TextView termin3;
	private ImageView iv1;
	
	private ArrayList<Object> parsedObjectsNews = new ArrayList<Object>();
	private ArrayList<Object> parsedObjectsCalendar = new ArrayList<Object>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
				
		// Alle View-Elemente dieser Activity
		news1 = (TextView) findViewById(R.id.textViewN1);
		news2 = (TextView) findViewById(R.id.textViewN2);
		news3 = (TextView) findViewById(R.id.textViewN3);
		termin1 = (TextView) findViewById(R.id.textViewT1);
		termin2 = (TextView) findViewById(R.id.textViewT2);
		termin3 = (TextView) findViewById(R.id.textViewT3);
		iv1 = (ImageView) findViewById(R.id.imageView1);
		
		setItems(false);
		
		// OnClickListener für die Pseudo-Liste
		news1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				newsClicked(0);
			}
		});
		news2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				newsClicked(1);
			}
		});
		news3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				newsClicked(2);
			}
		});
		termin1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				terminClicked(0);
			}
		});
		termin2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				terminClicked(1);
			}
		});
		termin3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				terminClicked(2);
			}
		});
		
		// OnClickListener für die Sponsoren
		iv1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Zeige einen Auswahl-Dialog mit den drei Sponsoren. Nach Auswahl wird der Browser mit der entsprechenden Seite geöffnet.
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("Weitere Infos über...");
				builder.setSingleChoiceItems(new String[]{"LuFG i9", "zdi-Schülerlabor", "RWTH Aachen"}, 0, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://learntech.rwth-aachen.de/tiki-index.php")));
						} else if (which == 1) {
							startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.wissenschaft.nrw.de/studium/informieren/mint-entdecken/standorte-von-zdi/")));
						} else if (which == 2) {
							startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.rwth-aachen.de/")));
						}
						dialog.dismiss();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		// Daten laden
		new updateData().execute("");
		
	}
	
	/**
	 * Lädt die Daten asynchon aus dem Feed (mit dem Parser) und zeigt diese dann in den entsprechenden Views an.
	 */
	private class updateData extends AsyncTask<String, Void, String> {
		private String[] newsTitles;
		private String[] calendarTitles;

		/**
		 * Lädt die Daten in Arrays
		 */
        @Override
        protected String doInBackground(String... params) {	
        	parsedObjectsNews = new Parser(MainActivity.this, 0).getFeedObjects(3);
    		newsTitles = new String[parsedObjectsNews.size()];
    		for (int i = 0; i < parsedObjectsNews.size(); i++) {
    			newsTitles[i] = (String) ((NewsItem) parsedObjectsNews.get(i)).getTitle();
    		}

    		parsedObjectsCalendar = new Parser(MainActivity.this, 1).getFeedObjects(3);
    		calendarTitles = new String[parsedObjectsCalendar.size()];
    		for (int i = 0; i < parsedObjectsCalendar.size(); i++) {
    			calendarTitles[i] = (String) ((CalendarItem) parsedObjectsCalendar.get(i)).getTitle();
    		}
    		
            return "Executed";
        }

        /**
         * Lädt die geparsten Daten in die entsprechenden View-Elemente
         */
        @Override
        protected void onPostExecute(String result) {
        	if (newsTitles.length == 0 || calendarTitles.length == 0) {
        		// Keine Daten geladen
    			news1.setText("Es konnten keine Daten geladen werden.");
    			news2.setVisibility(View.GONE);
    			news3.setVisibility(View.GONE);
    			termin1.setText("Es konnten keine Daten geladen werden.");
    			termin2.setVisibility(View.GONE);
    			termin3.setVisibility(View.GONE);
    			
    			setItems(false);
    			
    			// Margin nach unten erstellen
    			ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) news1.getLayoutParams();
    			mlp.setMargins(0, 0, 0, 10);
    		} else {
    			// Daten erfolgreich geladen
    			news1.setText(newsTitles[0]);
    			news2.setText(newsTitles[1]);
    			news3.setText(newsTitles[2]);
    			termin1.setText(calendarTitles[0]);
    			termin2.setText(calendarTitles[1]);
    			termin3.setText(calendarTitles[2]);
    			
    			setItems(true);
        		
        		news2.setVisibility(View.VISIBLE);
    			news3.setVisibility(View.VISIBLE);
        		termin2.setVisibility(View.VISIBLE);
    			termin3.setVisibility(View.VISIBLE);
    			
    			// Margin nach unten entfernen (falls vorhanden)
    			ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) news1.getLayoutParams();
    			mlp.setMargins(0, 0, 0, 0);
    		}
        }
    }
	
	/**
	 * Ein News-Item wurde gedrückt: Öffne die Detail-Ansicht mit den jeweiligen Daten
	 * @param pos News-Item-Id
	 */
	private void newsClicked(int pos) {
		Intent i = new Intent(MainActivity.this, WebViewActivity.class);
		i.putExtra("abTitle", ((NewsItem) parsedObjectsNews.get(pos)).getTitle());
		i.putExtra("abSubTitle", ((NewsItem) parsedObjectsNews.get(pos)).getDate());
		i.putExtra("data", ((NewsItem) parsedObjectsNews.get(pos)).getContent());
		startActivity(i);
	}
	
	/**
	 * Ein Kalender-Item wurde gedrückt: Zeige den Dialog mit dem Zeitpunkt und biete die Eintragung in den Kalender sowie das 
	 * Öffnen der Detail-Ansicht mit den jeweiligen Daten an.
	 * @param pos Kalender-Item-Id
	 */
	private void terminClicked(int pos) {
		final CalendarItem myItem = (CalendarItem) parsedObjectsCalendar.get(pos);
		
		// Dialog, der weitere Details zum Termin anzeigt
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
		builder.setNegativeButton("Weitere Informationen",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Starte ein Intent, welches die Details in einem WebView anzeigt
						Intent i = new Intent(MainActivity.this, WebViewActivity.class);
						i.putExtra("abTitle", myItem.getTitle());
						if (myItem.getStartDate().equals(myItem.getEndDate())) {
							i.putExtra("abSubTitle", myItem.getStartDate());
						} else {
							i.putExtra("abSubTitle", myItem.getStartDate() + " bis " + myItem.getEndDate());
						}
						i.putExtra("url", myItem.getMore());
						startActivity(i);
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * Da wir eine Pseudo-Liste nutzen (TextViews untereinander mit Border), müssen wir die nicht genutzten Elemente auch aktivieren/deaktivieren können.
	 * @param enabled Items aktivieren oder deaktivieren
	 */
	private void setItems(boolean enabled) {
		news1.setEnabled(enabled);
		news2.setEnabled(enabled);
		news3.setEnabled(enabled);
		termin1.setEnabled(enabled);
		termin2.setEnabled(enabled);
		termin3.setEnabled(enabled);
	}
	
	/**
	 * Behandelt einen Klick im NavigationDrawer entsprechend
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			NavDrawerHandler.entryClicked(position, getBaseContext(), MainActivity.this);
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
			startActivity(new Intent(MainActivity.this, SettingsActivity.class));
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