package de.boge.infosphere;

import java.io.InputStream;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.boge.infosphere.utils.NavDrawerHandler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class FotoActivity extends SherlockActivity implements OnClickListener {
	private DrawerLayout navDrawerLayout;
	private ListView navDrawerList;
	private ActionBarDrawerToggle navDrawerToggle;
	private String[] navDrawerTitles;
	
	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;
	private ImageView iv4;
	private ImageView iv5;
	private ImageView iv6;
	private ImageView iv7;
	private ImageView iv8;
	private ImageView iv9;
	private ImageView iv10;
	private ImageView iv11;
	private ImageView iv12;
	private ImageView iv13;
	private ImageView iv14;
	private ImageView iv15;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fotos);

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
		
		// Alle ImageViews
		iv1 = (ImageView)findViewById(R.id.imageView1);
		iv2 = (ImageView)findViewById(R.id.imageView2);
		iv3 = (ImageView)findViewById(R.id.imageView3);
		iv4 = (ImageView)findViewById(R.id.imageView4);
		iv5 = (ImageView)findViewById(R.id.imageView5);
		iv6 = (ImageView)findViewById(R.id.imageView6);
		iv7 = (ImageView)findViewById(R.id.imageView7);
		iv8 = (ImageView)findViewById(R.id.imageView8);
		iv9 = (ImageView)findViewById(R.id.imageView9);
		iv10 = (ImageView)findViewById(R.id.imageView10);
		iv11 = (ImageView)findViewById(R.id.imageView11);
		iv12 = (ImageView)findViewById(R.id.imageView12);
		iv13 = (ImageView)findViewById(R.id.imageView13);
		iv14 = (ImageView)findViewById(R.id.imageView14);
		iv15 = (ImageView)findViewById(R.id.imageView15);
		// Ende der ImageViews
		
		// ActionListener für jedes ImageView
		iv1.setOnClickListener(this);
		iv2.setOnClickListener(this);
		iv3.setOnClickListener(this);
		iv4.setOnClickListener(this);
		iv5.setOnClickListener(this);
		iv6.setOnClickListener(this);
		iv7.setOnClickListener(this);
		iv8.setOnClickListener(this);
		iv9.setOnClickListener(this);
		iv10.setOnClickListener(this);
		iv11.setOnClickListener(this);
		iv12.setOnClickListener(this);
		iv13.setOnClickListener(this);
		iv14.setOnClickListener(this);
		iv15.setOnClickListener(this);
		// Ende der ActionListener
		
		// Lade die Bilder
		new DownloadImage(iv1).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/IMAG1871.jpg");
		new DownloadImage(iv2).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/IMAG1879.jpg");
		new DownloadImage(iv3).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/IMAG1877.jpg");
		new DownloadImage(iv4).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/DSCF0252_0.JPG");
		new DownloadImage(iv5).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/DSCF0253_0.JPG");
		new DownloadImage(iv6).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/DSCF0262_0.JPG");
		new DownloadImage(iv7).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/2013-03-27%2009.50.18.jpg");
		new DownloadImage(iv8).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/2013-03-27%2009.50.50_0.jpg");
		new DownloadImage(iv9).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/2013-03-27%2010.05.48.jpg");
		new DownloadImage(iv10).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/IMAG0375.jpg");
		new DownloadImage(iv11).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/IMAG0376.jpg");
		new DownloadImage(iv12).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/IMAG0377.jpg");
		new DownloadImage(iv13).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/IMAG0240.jpg");
		new DownloadImage(iv14).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/IMAG0241.jpg");
		new DownloadImage(iv15).execute("http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/slideshow/IMAG0245.jpg");
	}
	
	/**
	 * Lädt die Bilder asynchon von der InfoSphere Seite und zeigt diese dann in den entsprechenden Views an.
	 */
	private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

	    public DownloadImage(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    /**
	     * Lädt das Bild von der Website herunter
	     */
	    protected Bitmap doInBackground(String... urls) {
	    	String urldisplay = urls[0];
	    	Bitmap mIcon11 = null;
	    	try {
	    		InputStream in = new java.net.URL(urldisplay).openStream();
	    		mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception ignored) {}
	        return mIcon11;
	    }

	    /**
	     * Zeigt das heruntergeladene Bild an
	     */
	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
    }
	
	@Override
	public void onClick(View v) {
		ImageView source = (ImageView)v;
		Intent i = new Intent(FotoActivity.this, WebViewActivity.class);
		
		if (source == iv1 || source == iv2 || source == iv3) {
			i.putExtra("abTitle", "InfoSphere-Team");
			i.putExtra("url", "http://schuelerlabor.informatik.rwth-aachen.de/content/team");
		} else if (source == iv4 || source == iv5 || source == iv6) {
			i.putExtra("abTitle", "MINT-L^4 Auftaktveranstaltung");
			i.putExtra("url", "http://schuelerlabor.informatik.rwth-aachen.de/content/mint-l4-auftaktveranstaltung");
		} else if (source == iv7 || source == iv8 || source == iv9) {
			i.putExtra("abTitle", "go4IT!-Aufbauworkshop Ostern 2013");
			i.putExtra("url", "http://schuelerlabor.informatik.rwth-aachen.de/content/go4it-aufbauworkshop-ostern-2013");
		} else if (source == iv10 || source == iv11 || source == iv12) {
			i.putExtra("abTitle", "MINT-Zukunftsforum");
			i.putExtra("url", "http://schuelerlabor.informatik.rwth-aachen.de/content/mint-zukunftsforum");
		} else if (source == iv13 || source == iv14 || source == iv15) {
			i.putExtra("abTitle", "LeLa-Jahrestagung");
			i.putExtra("url", "http://schuelerlabor.informatik.rwth-aachen.de/content/lela-jahrestagung");
		}
		
		startActivity(i);
	}

	/**
	 * Behandelt einen Klick im NavigationDrawer entsprechend
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			NavDrawerHandler.entryClicked(position, getBaseContext(), FotoActivity.this);
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
        	startActivity(new Intent(FotoActivity.this, SettingsActivity.class));
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