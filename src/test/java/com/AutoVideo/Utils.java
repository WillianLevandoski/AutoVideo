package com.AutoVideo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Utils {
	
	public static StringBuilder lerArquivo(File arquivo) {
		StringBuilder text = new StringBuilder();
		String NL = System.getProperty("line.separator");
		Scanner scanner = null;
		try {
			scanner = new Scanner(arquivo, "utf8");
			while (scanner.hasNextLine()) {
				text.append(scanner.nextLine() + NL);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
		      scanner.close();
	    }
		return text;
	}

}
