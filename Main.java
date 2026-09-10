import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.StandardOpenOption;


public class Main {
	public static final String DATAFILENAME = "data.csv";

	public static void checkDatabaseIntegrity() {
		Path path = Paths.get(DATAFILENAME);

		// 1. Check if the path exists (can be a file OR a directory)
		if (Files.exists(path)) {

			// 2. To ensure it is specifically a regular file and not a directory
			if (Files.isRegularFile(path)) {
				System.out.println("Data file exists and is a regular file.");
			}
		} else {
			System.out.println("The file does not exist, creating.");
			try {
				// Creates the file; throws exception if it already exists
				Path newFile = Files.createFile(path);
				System.out.println("File created successfully at: " + newFile.toAbsolutePath());
			} catch (IOException e) {
				System.err.println("An error occurred or file already exists: " + e.getMessage());
			}
		}
	}

	public static void printMenu() {
		System.out.println("1. Log New Weather Data");
		System.out.println("2. Show All Weather Entries");
		System.out.println("3. Show Weather Averages");
		System.out.println("4. Exit");
		System.out.println("Enter Choice");
	}

	public static Entry getUserInput() {
		Scanner scanner = new Scanner(System.in);
		Entry e = new Entry();

		System.out.println("Enter Date (YYYY-MM-DD):");
		String date = scanner.nextLine();
		e.setDate(date);
		System.out.println("Enter Temperature (Celsius):");
		float choice = scanner.nextFloat();
		e.setTemperature(choice);
		System.out.println("Enter Humidity (%):");
		choice = scanner.nextFloat();
		e.setHumidity(choice);
		System.out.println("Enter Precipitation (mm):");
		choice = scanner.nextFloat();
		e.setPrecipitation(choice);
//		scanner.close();
		return e;
	}

	public static void readDataBase() {
		String csvFile = DATAFILENAME;
		String line = "";
		String csvSplitBy = ",";

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			while ((line = br.readLine()) != null) {
				// Split the row by comma
				String[] row = line.split(csvSplitBy);

				// Process data (Example: print the first two columns)
				if (row.length > 0) {
					System.out.println(row[0] + "," + row[1] + "," + row[2] + "," + row[3]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printAverages() {
		float averageTemperature = 0;
		float averageHumidity = 0;
		float averagePrecipitation = 0;
		String csvFile = DATAFILENAME;
		String line = "";
		String csvSplitBy = ",";

		Path path = Paths.get(csvFile);
		long numbLines = 0;
		try {
			numbLines = Files.lines(path).count();
			System.out.println("Size: " + numbLines + " Entries");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (numbLines > 0) {
			try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
				while ((line = br.readLine()) != null) {
					// Split the row by comma
					String[] row = line.split(csvSplitBy);

					// Process data (Example: print the first two columns)
					if (row.length > 0) {
						averageTemperature += Float.parseFloat(row[1]) / numbLines;
						averageHumidity += Float.parseFloat(row[2]) / numbLines;
						averagePrecipitation += Float.parseFloat(row[3]) / numbLines;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.printf("Avg. Temp: %f\n", averageTemperature);
			System.out.printf("Avg. Humidity: %f\n", averageHumidity);
			System.out.printf("Avg. Precipitation: %f\n", averagePrecipitation);
		}
	}

	public static void addEntrytoDataBase(Entry entry) {
		Path filePath = Paths.get(DATAFILENAME);
		String textToAppend = entry.getDate() + "," +entry.getTemperature() + "," + entry.getHumidity() + "," + entry.getPrecipitation() + "\n";
		try {
			Files.writeString(filePath, textToAppend, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Data logged successfully");
	}

	public static void main(String[] args) {
		System.out.println("WeatherLogger v1.1");
		checkDatabaseIntegrity();
		Scanner scanner = new Scanner(System.in);
		boolean running = true;
		while (running) {
			printMenu();
			int choice = scanner.nextInt();
			switch (choice) {
			case 1:
				addEntrytoDataBase(getUserInput());
				break;
			case 2:
				readDataBase();
				break;
			case 3:
				printAverages();
				break;
			case 4:
				running = false;
				System.out.println("Exiting");
				scanner.close();
				break;
			default:
				System.out.println("Invalid");
				break;
			}
		}
	}
}
