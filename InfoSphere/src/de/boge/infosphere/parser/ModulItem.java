package de.boge.infosphere.parser;

/**
 * Ein Modul-Item, welches alle Daten eines Moduls beinhaltet (Titel, Beschreibung, Kategorie, Dauer und Vorwissen)
 */
public class ModulItem {
    private String title;
    private String description;
    private String category;
    private String duration;
    private String knowledge;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getDuration() {
    	if (duration == null) return "-";
        return duration;
    }

    public String getKnowledge() {
    	if (knowledge == null) return "-";
        return knowledge;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }

    public void setCategory(String category) {
        this.category = category.trim();
    }

    public void setDuration(String duration) {
        this.duration = duration.trim();
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge.trim();
    }
}