package net.tnemc.core.configuration.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class FileMgmt {

	public static void checkFolders(String[] folders) {

		for (String folder : folders) {
			File f = new File(folder);
			if (!(f.exists() && f.isDirectory())) {
				f.getParentFile().mkdirs();
				f.mkdir();

			}
		}
	}

	public static String fileSeparator() {

		return System.getProperty("file.separator");
	}

	public static File CheckYMLExists(File file) {

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * Pass a file and it will return it's contents as a string.
	 *
	 * @param file File to read.
	 * @return Contents of file. String will be empty in case of any errors.
	 */
	public static String convertFileToString(File file) {

		if (file != null && file.exists() && file.canRead() && !file.isDirectory()) {
			Writer writer = new StringWriter();
			InputStream is = null;

			char[] buffer = new char[1024];
			try {
				is = new FileInputStream(file);
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
				reader.close();
			} catch (IOException e) {
				System.out.println("Exception ");
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException ignore) {
					}
				}
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	/**
	 * Writes the contents of a string to a file.
	 *
	 * @param source String to write.
	 * @param file   File to write to.
	 * @return True on success.
	 * @throws IOException
	 */
	public static void stringToFile(String source, File file) {

		try {

			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

			//BufferedWriter out = new BufferedWriter(new FileWriter(FileName));

			source = source.replaceAll("\n", System.getProperty("line.separator"));

			out.write(source);
			out.close();

		} catch (IOException e) {
			System.out.println("Exception ");
		}
	}
}
