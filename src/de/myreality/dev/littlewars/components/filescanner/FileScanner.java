package de.myreality.dev.littlewars.components.filescanner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import de.myreality.dev.littlewars.components.Updater;

public class FileScanner {
	
	public static final String VERSION = "1.0";
	
	public void scan(String path) {
		System.out.println("Begin scanning files and write to path '" + path + "'...");
		// Initialize the comparison
		List<File> AllLocalFiles = new ArrayList<File>();
		
		String[] paths = {"res/", "config/"};
		
		// Get all local files
		for (String s : paths) {
			Updater.addFilesRecursively(new File(s), AllLocalFiles);
		}
		
		FileWriter fstream;
		try {
			
			StringWriter writer = new StringWriter();	
			// Write header
			writer.write("<?xml version=" + (char)34 + "1.0" + (char)34 + " encoding=" + (char)34 + "UTF-8" + (char)34 + " ?>");
			writer.write("\n");
			writer.write("<files>");
			writer.write("\n");
			for (File f : AllLocalFiles) {		
				System.out.println("Scan and write '" + f.getPath() + "'.. (Size: " + f.length() + "KB)");
				if (f.isFile() && !f.getPath().equals("config\\files.xml")) {
					writer.write("    <file src=" + 
								(char)34 + f.getPath() + (char)34 + " sum=" + 
							    (char)34 + Updater.getFileCharSize(f.getPath()) + (char)34 + " size=" + 
								(char)34 + f.length() + (char)34 + "></file>");
					writer.write("\n");					
				}
			}
			//Close the output stream
			writer.write("</files>");
			
			fstream = new FileWriter(path + "files.xml");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(writer.toString());
			out.close();	
			fstream.close();
			System.out.println("Done!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("FileScanner Version " + VERSION);
		FileScanner scanner = new FileScanner();
		scanner.scan("config/");
	}
}
