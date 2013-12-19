package de.boge.infosphere.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import de.boge.infosphere.AnfahrtActivity;
import de.boge.infosphere.FotoActivity;
import de.boge.infosphere.KalenderActivity;
import de.boge.infosphere.MainActivity;
import de.boge.infosphere.ModuleActivity;
import de.boge.infosphere.NewsActivity;
import de.boge.infosphere.SpezialangeboteActivity;
import de.boge.infosphere.TeamActivity;

public class NavDrawerHandler {

	/**
	 * Wird aus jeder Activity mit Navigation-Drawer heraus aufgerufen und startet die entsprechende Activity
	 * @param position	Welcher Eintrag wurde geklickt?
	 * @param context	Context der aufrufenden Activity
	 * @param activity	Aufrufende Activity
	 */
	public static void entryClicked(int position, Context context, Activity activity) {
		if (position == 0) {
			Intent i = new Intent(activity, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (position == 1) {
			Intent i = new Intent(activity, NewsActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (position == 2) {
			Intent i = new Intent(activity, ModuleActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (position == 3) {
			Intent i = new Intent(activity, SpezialangeboteActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (position == 4) {
			Intent i = new Intent(activity, FotoActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (position == 5) {
			Intent i = new Intent(activity, KalenderActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (position == 6) {
			Intent i = new Intent(activity, TeamActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (position == 7) {
			Intent i = new Intent(activity, AnfahrtActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}
}