package cz.komix.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for managing input from console and file.
 */
public class UserInputService {
    private final Pattern pattern = Pattern.compile("([A-Z]{3}) ((\\-)?(0|([1-9][0-9]*)))");

    private RecordsAccess recordsAccess;

    public UserInputService(RecordsAccess recordsAccess) {
        this.recordsAccess = recordsAccess;
    }

    /**
     * Start reading input from user. First load file if user specified filename. Then reading input from console, until
     * user types quit.
     */
    public void startReadingInput() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Specify filename if you want load records from file, else leave blank");
        String line = sc.nextLine();
        if (!line.isEmpty()) {
            readFromFile(line);
        }
        System.out.println("Put payments in format: currency(3 letter code) amount. Exit by typing quit on new line.");
        while (true) {
            line = sc.nextLine();
            if (line.equals("quit")) {
                sc.close();
                return;
            }
            parseInput(line);
        }
    }


    /**
     * Parse line from input and put new record to payment records.
     * @param input
     */
    private void parseInput(String input) {
        String currency = "";
        long amount;
        try {
            if ((pattern.matcher(input).matches())) {
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()) {
                    currency = matcher.group(1);
                    amount = Integer.parseInt(matcher.group(2));
                    recordsAccess.putRecord(currency, amount);
                    System.out.println("Record added for currency: " + currency + " with amount: " + amount);
                }
            } else if (!input.equals("quit")) {
                System.out.println("Invalid input format.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("currency "+ currency +" does not exist");
        }
    }


    /**
     * Load from file
     * @param fileName
     */
    public void readFromFile(String fileName) {
        try {
            Path path = Paths.get(fileName);
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                parseInput(line);
            }

        } catch (NoSuchFileException e) {
            System.out.println("No such file or directory" + fileName);
        } catch (IOException e) {
            System.out.println("Exception when load from file: " + e.getMessage());
        }
    }
}
