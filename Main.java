import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {
	public static final String DATAFILENAME = "data.csv";

	/**
	 * Checks of the data file exists. If not, one is created.
	 * 
	 * @throws IOException If file exists and tries to create it.
	 */
	public static void checkDatabaseIntegrity() {
		Path path = Paths.get(DATAFILENAME);
		if (Files.exists(path)) {
			if (Files.isRegularFile(path)) {
//				System.out.println("Data file exists and is a regular file.");
			}
		} else {
			System.out.println("The file does not exist, creating.");
			try {
				// Creates the file; throws exception if it already exists
				// Shouldn't get here if the file exists.
				Path newFile = Files.createFile(path);
				System.out.println("File created successfully at: " + newFile.toAbsolutePath());
			} catch (IOException e) {
				System.err.println("An error occurred or file already exists: " + e.getMessage());
			}
		}
	}

	/**
	 * Description
	 *
	 * @param
	 * @retrun
	 * @throws
	 */
	public static void printMenu() {
		System.out.println("1. Log New Weather Data");
		System.out.println("2. Show All Weather Entries");
		System.out.println("3. Show Weather Averages");
		System.out.println("4. Exit");
		System.out.print("Enter Choice: ");
	}

	/**
	 * Description
	 *
	 * @param
	 * @return Entry An Entry object
	 * @throws
	 */
	public static Entry getUserInput() {
		Scanner scanner = new Scanner(System.in);
		Entry entry = new Entry();
		System.out.print("Enter Date (YYYY-MM-DD): ");
		String date = scanner.nextLine();
		entry.setDate(date);
		System.out.print("Enter Temperature (Celsius): ");
		float choice = scanner.nextFloat();
		entry.setTemperature(choice);
		System.out.print("Enter Humidity (%): ");
		choice = scanner.nextFloat();
		entry.setHumidity(choice);
		System.out.print("Enter Precipitation (mm): ");
		choice = scanner.nextFloat();
		entry.setPrecipitation(choice);
		//this causes exception not sure why
//		scanner.close();
		return entry;
	}

	/**
	 * Description Prints the entire database
	 *
	 * @param
	 * @retrun
	 * @throws
	 */
	public static void printDataBase() {
		String csvFile = DATAFILENAME;
		String line = "";
		String csvSplitBy = ",";
		System.out.println("");

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			while ((line = br.readLine()) != null) {
				// Split the row by comma
				String[] row = line.split(csvSplitBy);
				if (row.length > 0) {
					System.out.println(row[0] + "," + row[1] + "," + row[2] + "," + row[3]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("");
	}

	/**
	 * Description
	 *
	 * @param
	 * @retrun
	 * @throws
	 */
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
//			System.out.println("Size: " + numbLines + " Entries");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (numbLines > 0) {
			try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
				while ((line = br.readLine()) != null) {
					// Split the row by comma
					String[] row = line.split(csvSplitBy);
					if (row.length > 0) {
						averageTemperature += Float.parseFloat(row[1]) / numbLines;
						averageHumidity += Float.parseFloat(row[2]) / numbLines;
						averagePrecipitation += Float.parseFloat(row[3]) / numbLines;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("");
			System.out.printf("Average Temperature: %.1f", averageTemperature);
			System.out.println("\u00B0C");
			System.out.printf("Average Humidity: %.1f", averageHumidity);
			System.out.println("%");
			System.out.printf("Average Precipitation: %.1f mm\n", averagePrecipitation);
			System.out.println("");
		}
	}

	/**
	 * Adds an entry to the database by writing to CSV data file.
	 *
	 * @param entry The Entry object to write.
	 * @throws IOException If subtotal is negative.
	 */
	public static void addEntrytoDataBase(Entry entry) {
		Path filePath = Paths.get(DATAFILENAME);
		String textToAppend = entry.getDate() + "," + entry.getTemperature() + "," + entry.getHumidity() + ","
				+ entry.getPrecipitation() + "\n";
		try {
			Files.writeString(filePath, textToAppend, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Data logged successfully");
	}

	/**
	 * Description
	 *
	 * @param
	 * @retrun
	 * @throws
	 */
	public static void main(String[] args) {
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
				printDataBase();
				break;
			case 3:
				printAverages();
				break;
			case 4:
				running = false;
				System.out.println("Exiting");
				break;
			default:
				System.out.println("Invalid Entry");
				break;
			}
		}
		scanner.close();
	}
}
