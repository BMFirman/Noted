public class Note {
    private String id;
    private String active;
    private String date;
    private String priority;
    private String textData;

    Note(String id, String active, String date, String priority, String textData) {
        this.id = id;
        this.active = active;
        this.date = date;
        this.priority = priority;
        this.textData = textData;
    }

    public void printNote() {
        System.out.println(this.id + " " + this.active + " " + this.date + " " + this.priority + " " + this.textData);
    }

    public String getTextData() {
        return textData;
    }
}