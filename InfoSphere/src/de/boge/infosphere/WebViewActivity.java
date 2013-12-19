package de.boge.infosphere;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_webview);
		
		Intent i = getIntent();

		// Bereite die ActionBar vor und setze (Unter)Titel entsprechend
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(i.getStringExtra("abTitle"));
		getSupportActionBar().setSubtitle(i.getStringExtra("abSubTitle"));
		
		// Zeige ein Lade-Symbol
		setSupportProgressBarIndeterminateVisibility(true);
		
		// WebView: Zeigt die entsprechende Seite in der App an
		WebView wv1 = (WebView) findViewById(R.id.webView1);
		wv1.getSettings().setJavaScriptEnabled(true);
		
		// HTML übergeben, lade keine URL!
		if(i.hasExtra("data")) {
			wv1.loadDataWithBaseURL(null, i.getStringExtra("data"), "text/html", "utf-8", null);
		} else {
			wv1.loadUrl(i.getStringExtra("url"));
		}

		wv1.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	// Lade die URL und zeige das Lade-Symbol
                view.loadUrl(url);
                setSupportProgressBarIndeterminateVisibility(true);
                return true;
            }
            
            public void onPageFinished(WebView view, String url) {
            	// Entferne das Lade-Symbol
            	setSupportProgressBarIndeterminateVisibility(false);
            }
        });
	}

	/**
	 * Schließe diese Activity, sobald auf das Home-Icon geklickt wird (--> Zurück)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
            finish();
        }
		return super.onOptionsItemSelected(item);
	}
}