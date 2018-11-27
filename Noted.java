import java.util.ArrayList;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.lang.SecurityException;
import java.io.IOException;
import java.util.Formatter;
import java.util.FormatterClosedException;

public class Noted {

    public static void main(String[] args) {
        
        ArrayList<Note> data = new ArrayList<Note>();
        data = readDataCSV();

        for (Note n : data) {
            System.out.println (n.getTextData());
        }

        Note newTestNote = new Note("1", "0", "9/2/1998", "0", "More Test Data");
        data.add(newTestNote);

        writeDataCSV(data);

        data = readDataCSV();

        for (Note n : data) {
            System.out.println (n.getTextData());
        }

    }

    public static ArrayList<Note> readDataCSV() {

        String filename = "data.csv";
        String workingDirectory = System.getProperty("user.dir");

        ArrayList<Note> data = new ArrayList<Note>();

        String id, active, date, priority, textData;

        Scanner input = null;
        input = initScanner(filename, workingDirectory, input);

        int count = 0;
        while (input.hasNext()) {
            count++;
            String currentLine = input.nextLine();
            String[] theLine = currentLine.split(",");
            if (count == 1) {
                continue;
            }

            id = theLine[0];
            active = theLine[1];
            date = theLine[2];
            priority = theLine[3];
            textData = theLine[4];

            Note d = new Note(id, active, date, priority, textData);
            data.add(d);
        }
        input.close();
        return data;
    }

    public static Scanner initScanner(String filename, String workingDirectory, Scanner input) {

        try {
            input = new Scanner(Paths.get(workingDirectory + "/" + filename));
        } catch (IOException ioExc) {
            System.out.println("Scanner was not initialized");
        }

        return input;
    }

    static void writeDataCSV(ArrayList<Note> data) {

        String filename = "data.csv";
        String workingDirectory = System.getProperty("user.dir");
        
        Formatter output = null;
        output = initFormatter(filename, workingDirectory, output);

        output.format("id, active, date, priority, textData,\n");
        for (int i = 0; i < data.size(); i++) {
            String id = data.get(i).getId();
            String active = data.get(i).getActive();
            String date = data.get(i).getDate();
            String priority = data.get(i).getPriority();
            String textData = data.get(i).getTextData();

            output.format(id + "," + active + "," + date + "," + priority + "," + textData + ",\n");
        }

        output.close();
    }


    public static Formatter initFormatter(String filename, String workingDirectory, Formatter output) {

        try {
            output = new Formatter(Paths.get(workingDirectory) + "/" + filename);
        } catch (SecurityException secExc) {
            System.err.println("Can't write to csv!");
            System.exit(1);
        } catch (FileNotFoundException fnfExc) {
            System.err.println("Can't find path to csv!");
            System.exit(1);
        }

        return output;
    }

        
}
