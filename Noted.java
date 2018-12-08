import java.util.ArrayList;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.lang.SecurityException;
import java.io.IOException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import static java.lang.Math.toIntExact;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;

public class Noted {

    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        Scanner mainInput = new Scanner(System.in);
        ArrayList<Note> data = new ArrayList<Note>();
        data = readDataCSV();
        initPrintout(data);
        boolean flag = true;
        

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
        Date currentDate = new Date();  
        System.out.println(formatter.format(currentDate));
        calculateDaysLeft(currentDate, data);

        try {
            while(flag) {
                if (args[0].substring(0,1).equals("a")) {
                    flag = false;
                    data.add(addNewNote(mainInput, data));
                    writeDataCSV(data);
                } else if (args[0].substring(0,1).equals("d")) {
                    flag = false;
                    data = deleteOldNote(mainInput, data, args);
                } else if (args[0].substring(0,1).equals("q")) {
                    System.exit(0);
                } else if (args[0].substring(0,1).equals("h")) {
                    helpPrintout();
                    System.exit(0);
                } else {
                    System.exit(0);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.exit(0);
        }
    }

    static void initPrintout(ArrayList<Note> data) {
        int numberOfNotes = data.size();

        System.out.print("You have " + numberOfNotes);

        if(numberOfNotes == 1) {
            System.out.println(" item left on the reminder!");
        } else {
            System.out.println(" items left on the reminder!");
        }

        for (Note n : data) {
            System.out.print(ANSI_BLUE + n.getId() + ") " + ANSI_RESET);
            System.out.println(n.getTextData());
        }
    }

    static void calculateDaysLeft (Date currentDate, ArrayList<Note> data) {
        for (Note n : data) {
            try {
                String dueDateString = n.getDate();
                System.out.println(dueDateString);
                Date dueDate = new SimpleDateFormat("dd/MM/yyyy").parse(dueDateString);  
                long remainingDays = getDifferenceDays(dueDate, currentDate);
                int remainingDaysInteger = toIntExact(remainingDays); 
                System.out.println(remainingDaysInteger);
                n.setPriority(remainingDaysInteger);
            } catch (ParseException e){
                System.out.println("Could not parse");
            }
        }
    }

    static long getDifferenceDays(Date d1, Date d2) {
        long diff = d1.getTime() - d2.getTime();
        long days = (diff / (1000*60*60*24));
        //return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return days;
    }

    static Note addNewNote(Scanner mainInput, ArrayList<Note> data) {
        int newIndex = data.size();

        System.out.print("Title: ");
        String textData = mainInput.nextLine();
        System.out.println();
        System.out.print("Expiry (DD/MM/YY): ");
        String date = mainInput.next();

        Note newNote = new Note(String.valueOf(newIndex), "0", date, 0, textData);

        return newNote;
    }

    static ArrayList<Note> deleteOldNote(Scanner mainInput, ArrayList<Note> data, String[] args) {
        System.out.println(args[1]);

        try{
            int key = Integer.parseInt(args[1]);
            data.remove(key);
            writeDataCSV(data);
        }catch (NumberFormatException ex) {
            System.out.println("Input is not a valid integer");
        }

        return data;
    }

    static void helpPrintout() {
        System.out.println("Usage: Noted arg <index>");
        System.out.println("a -add               - Add a new item.");
        System.out.println("r -remove <index>    - Remove an item by index.");
        System.out.println("l -list              - List all items");
        System.out.println("h -help              - Display help message");
    }


    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static ArrayList<Note> readDataCSV() {

        String filename = "data.csv";
        String workingDirectory = System.getProperty("user.dir");

        ArrayList<Note> data = new ArrayList<Note>();

        String id, active, date, textData;
        int priority;

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
            priority = Integer.parseInt(theLine[3]);
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
            int priority = data.get(i).getPriority();
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
