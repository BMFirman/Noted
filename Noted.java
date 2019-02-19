import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.toIntExact;

public class Noted {

    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        Scanner mainInput = new Scanner(System.in);
        ArrayList < Note > data = new ArrayList < > ();
        try {
            data = readDataCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initPrintout(data);
        boolean flag = true;

        Date currentDate = new Date();
        calculateDaysLeft(currentDate, data);

        try {
            while (flag) {
                writeDataCSV(data); // Ensure due date flag is accurate
                switch (args[0].substring(0, 1)) {
                    case "a":
                        flag = false;
                        data.add(addNewNote(mainInput, data));
                        calculateDaysLeft(currentDate, data);
                        //writeDataCSV(data);
                        break;
                    case "d":
                        flag = false;
                        data = deleteOldNote(data, args);
                        break;
                    case "q":
                        System.exit(0);
                    case "h":
                        helpPrintout();
                        System.exit(0);
                    default:
                        System.exit(0);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.exit(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void initPrintout(ArrayList < Note > data) {
        int numberOfNotes = data.size();

        System.out.print("You have " + numberOfNotes);

        if (numberOfNotes == 1) {
            System.out.println(" item left on the reminder!");
        } else {
            System.out.println(" items left on the reminder!");
        }

        for (Note n: data) {
            System.out.print(ANSI_BLUE + n.getId() + ") " + ANSI_RESET);
            System.out.print(n.getTextData());
            initPrintoutTimeWarning(n.getPriority());
        }
    }

    private static void initPrintoutTimeWarning(int days) {
        int days2 = days / 24;
        if (days > 168) {
            System.out.println(ANSI_GREEN + " (" + days2 + " days left)" + ANSI_RESET);
        } else if (days < 168 && days > 72) {
            System.out.println(ANSI_YELLOW + " (" + days2 + " days left)" + ANSI_RESET);
        } else if (days <= 72 && days > 47) {
            System.out.println(ANSI_RED + " (" + days2 + " days left)" + ANSI_RESET);
        } else if (days <= 47 && days >= 24) {
            System.out.println(ANSI_RED + " (" + days2 + " day left)" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + " (DUE!)" + ANSI_RESET);
        }
    }

    private static void calculateDaysLeft(Date currentDate, ArrayList < Note > data) {
        for (Note n: data) {
            try {
                String dueDateString = n.getDate();
                Date dueDate = new SimpleDateFormat("dd/MM/yyyy").parse(dueDateString);
                long remainingDays = getDifferenceDays(dueDate, currentDate);
                int remainingDaysInteger = toIntExact(remainingDays);
                n.setPriority(remainingDaysInteger);
                writeDataCSV(data);
            } catch (ParseException e) {
                System.out.println("Could not parse");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private static long getDifferenceDays(Date d1, Date d2) {
        long diff = d1.getTime() - d2.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private static Note addNewNote(Scanner mainInput, ArrayList < Note > data) {
        int newIndex = data.size();

        System.out.print("Title: ");
        String textData = mainInput.nextLine();
        System.out.println();
        System.out.print("Expiry (DD/MM/YYYY): ");
        String date = mainInput.next();

        return new Note(String.valueOf(newIndex), "0", date, 0, textData);
    }

    private static ArrayList < Note > deleteOldNote(ArrayList < Note > data, String[] args) {
        System.out.println(args[1]);

        try {
            int key = Integer.parseInt(args[1]);
            data.remove(key);
            writeDataCSV(data);
        } catch (NumberFormatException ex) {
            System.out.println("Input is not a valid integer");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }

    private static void helpPrintout() {
        System.out.println("Usage: Noted arg <index>");
        System.out.println("a -add               - Add a new item.");
        System.out.println("d -delete <index>    - Remove an item by index.");
        System.out.println("l -list              - List all items");
        System.out.println("h -help              - Display help message");
    }

    // There's no need to change anything from this point,
    // Unless you wished to add or change functionality to the note objects.
    private static ArrayList < Note > readDataCSV() throws IOException {

        String filename = "data.csv";
        String workingDirectory = System.getProperty("user.dir");

        ArrayList < Note > data = new ArrayList < > ();

        String id, active, date, textData;
        int priority;
        Scanner input = initScanner(filename, workingDirectory);

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

    private static Scanner initScanner(String filename, String workingDirectory) throws IOException {
        return new Scanner(Paths.get(workingDirectory + "/" + filename));
    }

    private static void writeDataCSV(ArrayList < Note > data) throws FileNotFoundException {

        String filename = "data.csv";
        String workingDirectory = System.getProperty("user.dir");

        Formatter output = initFormatter(filename, workingDirectory);

        output.format("id, active, date, priority, textData,\n");
        for (int i = 0; i < data.size(); i++) {
            String id = String.valueOf(i);
            String active = data.get(i).getActive();
            String date = data.get(i).getDate();
            int priority = data.get(i).getPriority();
            String textData = data.get(i).getTextData();

            output.format(id + "," + active + "," + date + "," + priority + "," + textData + ",\n");
        }

        output.close();
    }


    private static Formatter initFormatter(String filename, String workingDirectory) throws FileNotFoundException {

        return new Formatter(Paths.get(workingDirectory) + "/" + filename);

    }
}