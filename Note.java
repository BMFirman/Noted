public class Note {
    private String id;
    private String active;
    private String date;
    private int priority;
    private String textData;

    Note(String id, String active, String date, int priority, String textData) {
        this.id = id;
        this.active = active;
        this.date = date;
        this.priority = priority;
        this.textData = textData;
    }

    String getId() {
        return id;
    }

    String getActive() {
        return active;
    }

    String getDate() {
        return date;
    }

    int getPriority() {
        return priority;
    }

    void setPriority(int priority) {
        this.priority = priority;
    }

    String getTextData() {
        return textData;
    }
}