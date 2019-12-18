package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Via WinSCP (oder ähnlichen) können Dateien auf den Brick geladen werden. I
 * have installed winscp on my windows PC(Windows 8) I try to connect via winscp
 * and use host 10.0.1.1, user root and no password
 */
public class FileAccess {

	public static void displayDirectoryContents(File dir) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					System.out.println("directory:" + file.getCanonicalPath());
					displayDirectoryContents(file);
				} else {
					System.out.println("     file:" + file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * File object
	 */
	private File file;

	/**
	 * Stores the filename, without path
	 */
	public final String filename;

	/**
	 * Path object
	 */
	private Path path;

	/**
	 * Constructor
	 * 
	 * @param filename
	 *            the file's name
	 */
	public FileAccess(String filename) {
		this.filename = filename;
		path = Paths.get(filename);
		file = new File(filename);
	}

	/**
	 * Creates a new empty file, unless it already exists
	 */
	public void create() {
		try {
			Files.createFile(path);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Deletes a file if it exists
	 */
	public void delete() {
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Check if the file exists
	 * 
	 * @return boolean true if exists, false if not
	 */
	public boolean fileExists() {
		return file.exists();
	}

	public File getFile() {
		return file;
	}

	public Path getPath() {
		return path;
	}

	/**
	 * Reads data from a file to a List
	 * 
	 * @return the retrieved data, with each List element representing a line in
	 *         the file
	 */
	public List<String> readData() {
		List<String> data = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;

			while ((line = reader.readLine()) != null) {
				data.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * Writes a String of data to a file
	 * 
	 * @param data
	 *            the data to write
	 */
	public void write(String data) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			if (!file.exists()) {
				file.createNewFile();
			}

			writer.write(data);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Writes data from a List of Strings to a file. Each List element forms a
	 * separate line
	 * 
	 * @param list
	 *            the data to write.
	 */
	public void writeData(List<String> list) {
		String[] data = new String[list.size()];
		list.toArray(data);
		writeData(data);
	}

	/**
	 * Writes an array of String data to a file. Each array element forms a
	 * separate line
	 * 
	 * @param data
	 *            the data to write
	 */
	public void writeData(String[] data) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			if (!file.exists()) {
				file.createNewFile();
			}

			for (String line : data) {
				writer.append(line);
				writer.newLine();
			}
			writer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
