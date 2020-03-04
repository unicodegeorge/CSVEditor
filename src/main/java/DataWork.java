
import com.opencsv.CSVReader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.sun.deploy.ref.Helpers;

import static java.lang.System.exit;

public class DataWork {
    Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        DataWork worker = new DataWork();
        worker.menu();
    }
    public void menu() throws IOException {
        System.out.println("CSV WRITER MENU");
        System.out.println("1) CSVWriter");
        System.out.println("2) CSReader");
        int vyber = sc.nextInt();
        switch (vyber) {
            case 1:
                csvWriterMenu();
                break;
            case 2:
                returnLastData();
                break;
            default:
                System.out.println("Choose between 1 - 2");
                menu();
        }
    }
    public void editMenu() throws IOException {
        System.out.println("1) Pridat data do existujiciho .csv souboru");
        System.out.println("2) Zmenit hodnotu dat");
        System.out.print("--> ");
        int vyber = sc.nextInt();
        switch (vyber) {
            case 1:
                editOldFile();
                break;
            case 2:
                editData();
                break;
            default:
                System.out.println("Vybirejte ze dvou moznosti!");
                System.out.println("1) Pridat data do existujiciho .csv souboru");
                System.out.println("2) Zmenit hodnotu dat");
                editMenu();
                break;
        }
    }
    public void csvWriterMenu() throws IOException {
        System.out.println("1) Vytvorit novy soubor");
        System.out.println("2) Upravit existujici soubor");

        int vyber = sc.nextInt();

        switch (vyber) {
            case 1:
                writeData();
                break;
            case 2:
                editMenu();
                break;
            default:
                System.out.println("Choose from 1 or 2");
                csvWriterMenu();
                break;
        }
    }
    public void returnLastData() throws IOException {
        try (Stream<Path> walk = Files.walk(Paths.get(""))) {
            System.out.println("[LIST OF CSV FILES IN THIS FOLDER]");
            List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".csv")).collect(Collectors.toList());
            result.forEach(System.out::println);

            System.out.println("Enter name of the file : ");
            String nameOfFile = sc.next();

            try (
                    Reader reader = Files.newBufferedReader(Paths.get(nameOfFile + ".csv"));
                    CSVReader csvReader = new CSVReader(reader);

            ) {
                // Reading Records One by One in a String array
                System.out.println("Working");
                String[] nextRecord;

                List<String[]> readData = csvReader.readAll();

                if (readData.size() <= 1) {
                    System.out.println("There is " + readData.size() + " records in file named " + nameOfFile + ".csv");
                } else if (readData.size() > 1) {
                    System.out.println("There are " + readData.size() + " records in file named " + nameOfFile + ".csv");
                }

                for (int i = 0; i < readData.size(); i++) {
                    System.out.println("Name : " + readData.get(i)[0]);
                    System.out.println("Email : " + readData.get(i)[1]);
                    System.out.println("Phone : " + readData.get(i)[2]);
                    System.out.println("Country : " + readData.get(i)[3]);
                    System.out.println("==========================");
                }
            }
        }
        menu();
    }
    public void editOldFile() {
        try (Stream<Path> walk = Files.walk(Paths.get(""))) {
            System.out.println("[LIST OF CSV FILES IN THIS FOLDER]");
            List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".csv")).collect(Collectors.toList());

            result.forEach(System.out::println);

            System.out.println("Which file do you wan't to edit?");
            String fileName = sc.next();
            boolean exit = false;
            
            if (fileName.contains(".csv")) {
                fileName = fileName.replace(".csv", "");
            }
            
            try (Reader reader = Files.newBufferedReader(Paths.get(fileName + ".csv"));
                 CSVReader csvReader1 = new CSVReader(reader);
            ) {
                List<String[]> readData = csvReader1.readAll();
                ArrayList<String[]> data = new ArrayList<String[]>();
                System.out.println("Readdata size : " + readData.size());
                for (int i = 0; i < readData.size(); i++) {

                    System.out.println("Current i = " + i);
                    System.out.println("Name : " + readData.get(i)[0] + "\n");
                    System.out.println("Email : " + readData.get(i)[1] + "\n");
                    System.out.println("Tel : " + readData.get(i)[2] + "\n");
                    System.out.println("Country : " + readData.get(i)[3] + "\n");

                    data.add(new String[]{readData.get(i)[0], readData.get(i)[1], readData.get(i)[2], readData.get(i)[3]});
                }
                String name = "";
                String email = "";
                String phone = "";
                String country = "";
                try (
                        Writer writer = Files.newBufferedWriter(Paths.get(fileName + ".csv"));
                        CSVWriter csvWriter = new CSVWriter(writer,
                                CSVWriter.DEFAULT_SEPARATOR,
                                CSVWriter.NO_QUOTE_CHARACTER,
                                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                                CSVWriter.DEFAULT_LINE_END);
                ) {
                    while (exit != true) {
                        System.out.println("Enter name : ");
                        name = sc.next();
                        System.out.println("Enter email : ");
                        email = sc.next();
                        System.out.println("Enter phone : ");
                        phone = sc.next();
                        System.out.println("Enter country : ");
                        country = sc.next();
                        data.add(new String[]{name, email, phone, country});
                        System.out.println("Do you wan't to add more? (Y,N)");
                        String odpoved = sc.next();
                        switch (odpoved.charAt(0)) {
                            case 'Y':
                                System.out.println("Reloading Writer...");
                            case 'y':
                                System.out.println("Reloading writer...");
                                break;
                            case 'N':
                                System.out.println("Writing data");
                                csvWriter.writeAll(data);

                                exit = true;
                            case 'n':
                                csvWriter.writeAll(data);
                                exit = true;
                                break;
                            default:
                                System.out.println("wrong answer");
                                break;
                        }
                    }
                    csvWriter.flush();
                    csvWriter.close();
                    menu();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editData() throws IOException {
        ArrayList<String[]> writingList = new ArrayList<>();
        ArrayList<String[]> temp = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(""))) {
            System.out.println("[LIST OF CSV FILES IN THIS FOLDER]");
            List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".csv")).collect(Collectors.toList());

            result.forEach(System.out::println);
        }
        
        System.out.print("Select file > ");
        String slctFile = sc.next();
        if (slctFile.contains(".cvs")) {
            slctFile = slctFile.replace(".cvs", "");
        }

        try (Reader reader = Files.newBufferedReader(Paths.get(slctFile + ".csv"));
             CSVReader csvReader1 = new CSVReader(reader);
        ) {
            List<String[]> readData = csvReader1.readAll();
            
            for (int x = 0; x < readData.size(); x++) {
                int xplus = x + 1;
                temp.add(new String[]{readData.get(x)[0], readData.get(x)[1], readData.get(x)[2], readData.get(x)[3]});
                System.out.println("\n****************************");
                System.out.println("DATABLOCK" + xplus);
                System.out.println("****************************");
                System.out.println("Name : " + readData.get(x)[0]);
                System.out.println("Email : " + readData.get(x)[1]);
                System.out.println("Phone : " + readData.get(x)[2]);
                System.out.println("Country : " + readData.get(x)[3]);
                System.out.println("****************************");
            }
            
            System.out.println("Jaky datablock chcete upravovat (1-" + temp.size() + ")");
            System.out.print("--> ");
            int editingDataBlock = sc.nextInt();
            editingDataBlock--;
            System.out.println("****************************");
            System.out.println("1) Name : " + readData.get(editingDataBlock)[0]);
            System.out.println("2) Email : " + readData.get(editingDataBlock)[1]);
            System.out.println("3) Phone : " + readData.get(editingDataBlock)[2]);
            System.out.println("4) Country : " + readData.get(editingDataBlock)[3]);
            System.out.println("****************************");
            System.out.println("Which value you want to choose ?");
            System.out.print("--> ");
            int editingValue = sc.nextInt();
            editingValue--;
            switch (editingValue) {
                case 0:
                    System.out.print("Enter new name : ");
                    String newName = sc.next();
                    temp.remove(editingDataBlock);
                    temp.add(editingDataBlock, new String[]{newName, readData.get(editingDataBlock)[1], readData.get(editingDataBlock)[2], readData.get(editingDataBlock)[3]});
                    break;
                case 1:
                    System.out.print("Enter new email : ");
                    String newEmail = sc.next();
                    temp.remove(editingDataBlock);
                    temp.add(editingDataBlock, new String[]{readData.get(editingDataBlock)[0], newEmail, readData.get(editingDataBlock)[2], readData.get(editingDataBlock)[3]});
                    break;
                case 2:
                    System.out.print("Enter new number : ");
                    String newNumber = sc.next();
                    temp.remove(editingDataBlock);
                    temp.add(editingDataBlock, new String[]{readData.get(editingDataBlock)[0], readData.get(editingDataBlock)[1], newNumber, readData.get(editingDataBlock)[3]});
                    break;
                case 3:
                    System.out.print("Enter new country : ");
                    String newCountry = sc.next();
                    temp.remove(editingDataBlock);
                    temp.add(editingDataBlock, new String[]{readData.get(editingDataBlock)[0], readData.get(editingDataBlock)[1], readData.get(editingDataBlock)[2], newCountry});
                    break;
            }
        }
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(slctFile + ".csv"));
                CSVWriter csvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
        ) {
            csvWriter.writeAll(temp);
            csvWriter.flush();
            menu();
        }
    }
    public void writeData() throws IOException {
        ArrayList<String[]> data = new ArrayList<String[]>();
        System.out.println("Enter name of the file : ");
        String nameOfFile = sc.next();
        if (nameOfFile.contains(".csv")) {
            nameOfFile = nameOfFile.replace(".csv", "");
        }
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(nameOfFile + ".csv"));
                CSVWriter csvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
        ) {
            String[] headerRecord = {"Name", "Email", "Phone", "Country"};
            System.out.println("File : " + nameOfFile + ".csv" + " created!");
            System.out.println("Writing to file : " + nameOfFile + ".csv");
            boolean exit = false;
            String name = "";
            String email = "";
            String phone = "";
            String country = "";
            while (exit != true) {
                System.out.println("Enter name : ");
                name = sc.next();
                System.out.println("Enter email : ");
                email = sc.next();
                System.out.println("Enter phone : ");
                phone = sc.next();
                System.out.println("Enter country : ");
                country = sc.next();
                data.add(new String[]{name, email, phone, country});
                System.out.println("Do you wan't to add more? (Y,N)");
                String odpoved = sc.next();
                switch (odpoved.charAt(0)) {
                    case 'Y':
                        System.out.println("Reloading Writer...");
                    case 'y':
                        System.out.println("Reloading writer...");
                        break;
                    case 'N':
                        exit = true;
                    case 'n':
                        csvWriter.writeAll(data);
                        exit = true;
                        break;
                    default:
                        System.out.println("wrong answer");
                        break;
                }
            }
            csvWriter.flush();
            csvWriter.close();
            menu();
        }
    }
}



