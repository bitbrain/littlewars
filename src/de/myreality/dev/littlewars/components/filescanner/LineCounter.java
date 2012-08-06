/** 
 * Class that counts all lines of *.java files in the src directory
 */
package de.myreality.dev.littlewars.components.filescanner;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.myreality.dev.littlewars.components.Updater;

public class LineCounter {

	public static void main(String[] args) {
		scan();
	}
	
	public static void scan() {
		System.out.println("Begin scanning files and counting lines...");
		// Initialize the comparison
		List<File> AllLocalFiles = new ArrayList<File>();
		
		String[] paths = {"src/"};
		
		// Get all local files
		for (String s : paths) {
			Updater.addFilesRecursively(new File(s), AllLocalFiles);
		}
		int totalLines = 0;
		for (File f : AllLocalFiles) {	
			if (f.isFile()) {
				int mid= f.getName().lastIndexOf(".");
				String ext= f.getName().substring(mid+1, f.getName().length());  
				if (ext.equals("java")) {
					int lines = getLinesOf(f);
					totalLines += lines;
					System.out.println(f.getName() + ", Lines: " + lines);
				}
			}
		}

		System.out.println("\nDone. Total lines: " + totalLines);
		
	}
	
	public static int getLinesOf(File file) {
		int lines = 0;
		try {
	       // Open the file that is the first 
	       // command line parameter
	       FileInputStream fstream = new FileInputStream(file.getPath());
	       // Get the object of DataInputStream
	       DataInputStream in = new DataInputStream(fstream);
	       BufferedReader br = new BufferedReader(new InputStreamReader(in));
	       String singleLine;
	  	   //Read File Line By Line
	  	   while ((singleLine = br.readLine()) != null)   {
	  		   if (!hasOnlyChars(singleLine, ' ')) {
	  			   ++lines;
	  		   }
	       }
		    //Close the input stream
		    in.close();
	    } catch (Exception e){//Catch exception if any
	    	System.err.println("Error: " + e.getMessage());
	    }
	     return lines;
	}
	
	public static boolean hasOnlyChars(String s, char c) {
		
		for (int i = 0; i < s.length(); ++i) {
			char sc = s.charAt(i);
			if (sc != c) {
				return false;
			}
		}
		return true;
	}
}
