package de.boge.infosphere.parser;

/**
 * Ein Kalender-Item, welches alle Daten eines Termins beinhaltet (Titel, Beschreibung, Datum und den Link zu weiteren Infos)
 */
public class CalendarItem {
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String more;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public String getMore() {
    	return more;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }

    public void setStartDate(String date) {
        this.startDate = date.trim();
    }
    
    public void setEndDate(String date) {
        this.endDate = date.trim();
    }
    
    public void setMore(String more) {
        this.more = more.trim();
    }
}