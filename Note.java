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

    public void printNote() {
        System.out.println(this.id + " " + this.active + " " + this.date + " " + this.priority + " " + this.textData);
    }

    public String getId() {
        return id;
    }

    public String getActive() {
        return active;
    }

    public String getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTextData() {
        return textData;
    }
}