package de.boge.infosphere.parser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;

/**
 * Der Parser lädt die gewünschten Daten aus dem RSS-Feed der Website und formatiert sie, sodass die Daten in den entpsprechenden Activitys angezeigt werden können.
 */
public class Parser {
    private final String[] FEEDS = new String[]{"http://schuelerlabor.informatik.rwth-aachen.de/rss.xml",
            "http://schuelerlabor.informatik.rwth-aachen.de/feeds/termine?xml", "http://schuelerlabor.informatik.rwth-aachen.de/module_rss"};
    private final Context context;
    private final int chosenFeed;
    private ArrayList<Object> feedObjects = new ArrayList<Object>();

    public Parser(Context context, int feed) {
    	this.context = context;
        this.chosenFeed = feed;
    }

    /**
     * Gibt die Feed-Objekte als Liste zurück.
     * Gegebenenfalls wird vorher der Feed geparst oder die Daten werden aus dem temporären Cache genommen.
     *
     * @param max Maximale Anzahl an Einträgen, die zurückgegeben werden sollen. Standard: 0 --> Alle
     * @return Liste mit allen (gewählten) Feed-Objekten
     */
    public ArrayList<Object> getFeedObjects(int max) {
    	parseFeed();
    	
        if (max == 0) return feedObjects;
        if (feedObjects.size() < max) max = feedObjects.size();

        // Erstellt eine tempöräre Liste mit der gewählten maximalen Anzahl an Einträgen
        ArrayList<Object> chosenFeedObjects = new ArrayList<Object>();       
        for (int i = 0; i < max; i++) {
        	chosenFeedObjects.add(feedObjects.get(i));
        }

        return chosenFeedObjects;
    }

    /**
     * Parst den Feed und trägt den Inhalt jeweils als Objekt in die Liste ein.
     */
    private void parseFeed() {
    	// Falls keine Internetverbindung besteht, abbrechen;
    	// (leere Liste wird zurückgegeben; wird von der Activity entsprechend behandelt)
    	if (!isOnline()) {
    		return;
    	}
    	
    	// Initiere den XML-Parser
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new URL(FEEDS[chosenFeed]).openStream(), null);
            
            // Parse den gewählten Feed (siehe Konstruktor)
            if (chosenFeed == 0) {
                parseNewsXML(parser);
            } else if (chosenFeed == 1) {
                parseCalendarXML(parser);
            } else if (chosenFeed == 2) {
                parseModulXML(parser);
            }
        } catch (XmlPullParserException ignored) {} catch (IOException ignored) {}
    }

    /**
     * Parst den News-Feed und speichert die News als NewsItem in der parsedObjects-Liste
     * @param parser Welcher Parser die Aufgabe übernehmen soll.
     */
    private void parseNewsXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        String name;
        NewsItem currentItem = null;

        // Solange nicht das Ende des Feeds erreicht ist, wird dieser durchlaufen
        while (eventType != XmlPullParser.END_DOCUMENT) {
            name = "";
            if (eventType == XmlPullParser.START_TAG) {
            	name = parser.getName();       	
                if (name.equalsIgnoreCase("item")) {
                	// Anfang einer News --> Erzeuge neues NewsItem
                	currentItem = new NewsItem();
                } else if (name.equalsIgnoreCase("title") && currentItem != null) {
                	// Titel der News
                	currentItem.setTitle(parser.nextText());
                } else if (name.equalsIgnoreCase("description") && currentItem != null) {
                	// Inhalt der News
                	String tempDesc = parser.nextText();
                	// Nur den ersten Teil (Beschreibung) des Tags speichern
                	tempDesc = tempDesc.split("<div class=\"field ")[0];
                	// Bilder sollen (falls vorhanden) angezeigt werden
                	tempDesc = tempDesc.replace("/sites/default/files/content/Bilder/", "http://schuelerlabor.informatik.rwth-aachen.de/sites/default/files/content/Bilder/");
                	currentItem.setContent(tempDesc);
                } else if (name.equalsIgnoreCase("pubDate") && currentItem != null) {
                	// Veröffentlichungszeitpunkt der News
                	String tempDate = parser.nextText();
                	
                	// Entferne den Wochentag und schreibe den Monat aus
                	tempDate = tempDate.replace("+0000", "");
                	tempDate = tempDate.replace("Mon, ", "");
                	tempDate = tempDate.replace("Tue, ", "");
                	tempDate = tempDate.replace("Wed, ", "");
                	tempDate = tempDate.replace("Thu, ", "");
                	tempDate = tempDate.replace("Fri, ", "");
                	tempDate = tempDate.replace("Sat, ", "");
                	tempDate = tempDate.replace("Sun, ", "");
                	tempDate = tempDate.replace(" Jan", ". Januar");
                	tempDate = tempDate.replace(" Feb", ". Februar");
                	tempDate = tempDate.replace(" Mar", ". März");
                	tempDate = tempDate.replace(" Apr", ". April");
                	tempDate = tempDate.replace(" May", ". Mai");
                	tempDate = tempDate.replace(" Jun", ". Juni");
                	tempDate = tempDate.replace(" Jul", ". Juli");
                	tempDate = tempDate.replace(" Aug", ". August");
                	tempDate = tempDate.replace(" Sep", ". September");
                	tempDate = tempDate.replace(" Oct", ". Oktober");
                	tempDate = tempDate.replace(" Nov", ". November");
                	tempDate = tempDate.replace(" Dec", ". Dezember");
                	tempDate = tempDate.trim();

                	currentItem.setDate(tempDate);
                }
            } else if (eventType == XmlPullParser.END_TAG) {
            	// Ende des News-Items --> Speichere das Objekt in der Liste und entferne das aktuelle Objekt
            	name = parser.getName();
            	if (name.equalsIgnoreCase("item") && currentItem != null) {
            		feedObjects.add(currentItem);
                    currentItem = null;
            	}
            }

            eventType = parser.next();
        }
    }

    /**
     * Parst den Kalender-Feed und speichert die Termine als CalendarItem in der parsedObjects-Liste
     * @param parser Welcher Parser die Aufgabe übernehmen soll.
     */
    private void parseCalendarXML(XmlPullParser parser) throws XmlPullParserException, IOException {
    	int eventType = parser.getEventType();
        String name;
        CalendarItem currentItem = null;

        // Solange nicht das Ende des Feeds erreicht ist, wird dieser durchlaufen
        while (eventType != XmlPullParser.END_DOCUMENT) {
            name = "";
            if (eventType == XmlPullParser.START_TAG) {
            	name = parser.getName();           	
                if (name.equalsIgnoreCase("item")) {
                	// Anfang eines Termins --> Erzeuge neues CalendarItem
                	currentItem = new CalendarItem();
                } else if (name.equalsIgnoreCase("title") && currentItem != null) {
                	// Titel des Termins
                	currentItem.setTitle(parser.nextText());
                } else if (name.equalsIgnoreCase("link") && currentItem != null) {
                	// Link mit weiteren Infos
                	currentItem.setMore(parser.nextText());
                } else if (name.equalsIgnoreCase("description") && currentItem != null) {
                	// Beschreibung des Termins (Datum, ...)
                	String tempDesc = parser.nextText();
                	
                	// HTML-Tags entfernen
                	tempDesc = tempDesc.replace("&lt;", "<");
                	tempDesc = tempDesc.replace("&gt;", ">");
                	tempDesc = tempDesc.replace("&quot;", "\"");
                	
                	// Anfangs und Endzeitpunkt auslesen (RegEx)
                	Pattern p = Pattern.compile("<span class=\"date-display-single\">(.*?)</span>");
                	Pattern p2 = Pattern.compile("<span class=\"date-display-start\">(.*?)</span>");
                	Pattern p3 = Pattern.compile("<span class=\"date-display-end\">(.*?)</span>");
                	Matcher m = p.matcher(tempDesc);
                	Matcher m2 = p2.matcher(tempDesc);
                	Matcher m3 = p3.matcher(tempDesc);
                	if (m.find()) {
                		currentItem.setStartDate(m.group(1));
                		currentItem.setEndDate(m.group(1));
                	} else if (m2.find() && m3.find()){
                		currentItem.setStartDate(m2.group(1));
                		currentItem.setEndDate(m3.group(1));
                	} else {
                		currentItem.setStartDate("Termin unbekannt");
                		currentItem.setEndDate("Termin unbekannt");
                	}
                	
                	currentItem.setDescription(tempDesc);
                }
            } else if (eventType == XmlPullParser.END_TAG) {
            	// Ende des Termins --> Speichere das Objekt in der Liste und entferne das aktuelle Objekt
            	name = parser.getName();
            	if (name.equalsIgnoreCase("item") && currentItem != null) {
            		feedObjects.add(currentItem);
                    currentItem = null;
            	}
            }

            eventType = parser.next();
        }
        Collections.reverse(feedObjects);
    }

    /**
     * Parst den Modul-Feed und speichert die Module als ModulItem in der parsedObjects-Liste
     * @param parser Welcher Parser die Aufgabe übernehmen soll.
     */
    private void parseModulXML(XmlPullParser parser) throws XmlPullParserException, IOException {
    	int eventType = parser.getEventType();
        String name;
        ModulItem currentItem = null;

     	// Solange nicht das Ende des Feeds erreicht ist, wird dieser durchlaufen
        while (eventType != XmlPullParser.END_DOCUMENT) {
            name = "";
            if (eventType == XmlPullParser.START_TAG) {
            	name = parser.getName();           	
                if (name.equalsIgnoreCase("item")) {
                	// Anfang eines Moduls --> Erzeuge neues ModulItem
                	currentItem = new ModulItem();
                } else if (name.equalsIgnoreCase("title") && currentItem != null) {
                	// Titel des Moduls
                	currentItem.setTitle(parser.nextText());
                } else if (name.equalsIgnoreCase("description") && currentItem != null) {
                	// Beschreibung des Moduls
                	String tempDesc = parser.nextText();
                	
                	// HTML-Tags entfernen
                	tempDesc = tempDesc.replace("&lt;", "<");
                	tempDesc = tempDesc.replace("&gt;", ">");
                	tempDesc = tempDesc.replace("&quot;", "\"");
                	
                	// Beschreibung des Moduls: Erster Teil des Feeds, entferne vorher HTML-Tags / -Elemente
                	String newDesc = Html.fromHtml(tempDesc.split("<div class=\"field ")[0]).toString();
                	// Beschreibung kürzen, damit der Nutzer nicht "erschlagen" wird
                	if (newDesc.length() > 200) {
                		newDesc = newDesc.substring(0, 200) + "...";
                	}
                	currentItem.setDescription(newDesc);
                	
                	// Ganze Description in Zeilen zerlegen
                	String[] lines = tempDesc.split("\\r?\\n");
                	
                	// Nach Zeilen für Vorwissen und Dauer suchen, diese dann von HTML-Tags befreien
                	// und im Eintrag speichern
                	for (int i = 0; i < lines.length; i++) {
                		if (lines[i].trim().startsWith("<div class=\"field field-type-text field-field-modul-vorwissen\">")) {
                			currentItem.setKnowledge(Html.fromHtml(lines[i+4]).toString().replace("&auml;", "ä").replace("&ouml;", "ö").replace("&uuml;", "ü"));
                		} else if (lines[i].trim().startsWith("<div class=\"field field-type-number-float field-field-modul-dauer\">")) {
                			currentItem.setDuration(Html.fromHtml(lines[i+5]).toString().replace("Zeitstunden", " Zeitstunden"));
                		}
                	}
                	
                	// Zielgruppe heraussuchen (aktuell nur _eine_ möglich!)
                	Pattern p = Pattern.compile("<a href=\"/category/jahrgangsstufe/(.*?)\" rel=\"tag\" ");
                	Matcher m = p.matcher(tempDesc);
                	if (m.find()) {
                		currentItem.setCategory(m.group(1));
                	}
                }
            } else if (eventType == XmlPullParser.END_TAG) {
            	// Ende des Moduls --> Speichere das Objekt in der Liste und entferne das aktuelle Objekt
            	name = parser.getName();
            	if (name.equalsIgnoreCase("item") && currentItem != null) {
            		feedObjects.add(currentItem);
                    currentItem = null;
            	}
            }

            eventType = parser.next();
        }
    }

    /**
     * Prüft, ob eine Internetverbindung besteht.
     * @return Ob das Smartphone eine aktive Internetverbindung hat
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}