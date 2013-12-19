package de.boge.infosphere.module;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;

import de.boge.infosphere.R;
import de.boge.infosphere.WebViewActivity;
import de.boge.infosphere.parser.ModulItem;
import de.boge.infosphere.parser.Parser;
import de.boge.infosphere.utils.CustomListAdapterOneRow;
import de.boge.infosphere.utils.CustomListAdapterTwoRows;

public class TabGrundschule extends SherlockFragment {
	private ListView listView;
	private ArrayList<Object> parsedObjects = new ArrayList<Object>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.module_unterstufe, container, false);

		// Beginn Liste
		listView = (ListView) view.findViewById(R.id.listView1);
		String[] dummyList = new String[] { "Lädt..." };

		CustomListAdapterOneRow adapter = new CustomListAdapterOneRow(getActivity(), dummyList);
		listView.setAdapter(adapter);
		listView.setEnabled(false);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Öffne die Seite mit dem Anmelde-Formular
				Intent i = new Intent(getActivity(), WebViewActivity.class);
				i.putExtra("abTitle", "Anmeldung");
				i.putExtra("url", "http://schuelerlabor.informatik.rwth-aachen.de/anmeldung_lehrkraefte");
				startActivity(i);
			}
		});
		// Ende der Liste

		// Lade die Daten
		new updateData().execute("");

		return view;
	}

	/**
	 * Lädt die Daten asynchon aus dem Feed (mit dem Parser) und zeigt diese dann in den entsprechenden Views an.
	 */
	private class updateData extends AsyncTask<String, Void, String> {
		private String[] modulTitle;
		private String[] modulData;

		/**
		 * Lädt die Daten in Arrays
		 */
		@Override
		protected String doInBackground(String... params) {
			parsedObjects = new Parser(getActivity(), 2).getFeedObjects(0);
			
			// Nur Elemente mit der ensprechenden Kategorie werden übernommen
			ArrayList<Object> tempList = new ArrayList<Object>(parsedObjects);
			for (Object entry : parsedObjects) {
				String cat = ((ModulItem) entry).getCategory();
				if (!cat.equalsIgnoreCase("grundschule")) {
					tempList.remove(entry);
				}
			}
			parsedObjects = tempList;
			
			modulTitle = new String[parsedObjects.size()];
			modulData = new String[parsedObjects.size()];
			for (int i = 0; i < parsedObjects.size(); i++) {
				modulTitle[i] = (String) ((ModulItem) parsedObjects.get(i)).getTitle();
				modulData[i] = (String) ((ModulItem) parsedObjects.get(i)).getDescription() + "\n\nDauer: " + ((ModulItem) parsedObjects.get(i)).getDuration() + "\nVorwissen: " + ((ModulItem) parsedObjects.get(i)).getKnowledge();
			}

			return "Executed";
		}

		/**
         * Lädt die geparsten Daten in die entsprechenden View-Elemente
         */
		@Override
		protected void onPostExecute(String result) {
			if (modulTitle.length == 0 || modulData.length == 0) {
				CustomListAdapterOneRow adapterCalendar = new CustomListAdapterOneRow(getActivity(), new String[]{"Es konnten keine Daten geladen werden."});
				listView.setAdapter(adapterCalendar);
				listView.setEnabled(false);
			} else {
				CustomListAdapterTwoRows adapterCalendar = new CustomListAdapterTwoRows(getActivity(), modulTitle, modulData);
				listView.setAdapter(adapterCalendar);
				listView.setEnabled(true);
			}
		}
	}
}