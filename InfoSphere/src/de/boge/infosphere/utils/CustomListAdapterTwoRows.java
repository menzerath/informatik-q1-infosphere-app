package de.boge.infosphere.utils;

import de.boge.infosphere.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Der Adapter für eine Liste mit einer Überschrift und einer Unter-Überschrift.
 * Wird verwendet, damit die Liste einfacher angepasst werden kann (Schrift, Paddings, ...)
 * Siehe dazu auch: /layout/list_item_two_rows.xml
 */
public class CustomListAdapterTwoRows extends BaseAdapter {
	public String title[];
	public String description[];

	public Activity context;
	public LayoutInflater inflater;

	public CustomListAdapterTwoRows(Activity context, String[] title, String[] description) {
		super();

		this.context = context;
		this.title = title;
		this.description = description;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return title.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public static class ViewHolder {
		TextView txtViewTitle;
		TextView txtViewDescription;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_item_two_rows, null);
			holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
			holder.txtViewDescription = (TextView) convertView.findViewById(R.id.txtViewDescription);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtViewTitle.setText(title[position]);
		holder.txtViewDescription.setText(description[position]);
		return convertView;
	}
}