package de.boge.infosphere.team;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import de.boge.infosphere.R;

/**
 * Hier passiert nichts außer dem Laden des richtigen Layouts sowie dem Senden einer E-Mail (falls die Adresse geklickt wird)
 */
public class TabMitarbeiter extends SherlockFragment implements OnClickListener {
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_mitarbeiter, container, false);
        
        // Setze den onClick-Listener für die E-Mail TextViews
        view.findViewById(R.id.textViewMail1).setOnClickListener(this);
        view.findViewById(R.id.textViewMail2).setOnClickListener(this);
        view.findViewById(R.id.textViewMail3).setOnClickListener(this);
        view.findViewById(R.id.textViewMail4).setOnClickListener(this);
        view.findViewById(R.id.textViewMail5).setOnClickListener(this);
        return view;
    }

    /**
     * Sende eine E-Mail an die Adresse, die gerade geklickt wurde.
     * An, Betreff und Inhalt sind dabei bereits (teilweise) ausgefüllt
     */
	@Override
	public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/html");
        i.putExtra(Intent.EXTRA_EMAIL, new String[] {((TextView)v).getText().toString()});
        i.putExtra(Intent.EXTRA_SUBJECT, "InfoSphere - Kontaktanfrage");
        i.putExtra(Intent.EXTRA_TEXT, "\n\n----------\nDiese E-Mail wurde mit der InfoSphere-App für Android versendet.");
        startActivity(Intent.createChooser(i, "E-Mail senden"));
	}
}