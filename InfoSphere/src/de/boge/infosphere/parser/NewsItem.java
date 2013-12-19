package de.boge.infosphere.parser;

/**
 * Ein News-Item, welches alle Daten einer News beinhaltet (Titel, Inhalt und Datum)
 */
public class NewsItem {
    private String title;
    private String content;
    private String date;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public void setContent(String content) {
        this.content = content.trim();
    }

    public void setDate(String date) {
        this.date = date.trim();
    }
}