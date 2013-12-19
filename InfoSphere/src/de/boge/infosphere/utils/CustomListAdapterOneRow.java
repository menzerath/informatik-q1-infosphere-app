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
 * Der Adapter für eine Liste mit einer Überschrift.
 * Wird verwendet, damit die Liste einfacher angepasst werden kann (Schrift, Paddings, ...)
 * Siehe dazu auch: /layout/list_item_one_row.xml
 */
public class CustomListAdapterOneRow extends BaseAdapter {
	public String title[];
	public String description[];

	public Activity context;
	public LayoutInflater inflater;

	public CustomListAdapterOneRow(Activity context, String[] title) {
		super();

		this.context = context;
		this.title = title;
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
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_item_one_row, null);
			holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtViewTitle.setText(title[position]);
		return convertView;
	}
}