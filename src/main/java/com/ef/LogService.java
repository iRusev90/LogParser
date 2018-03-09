package com.ef;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogService {
	
	public void saveLogsFromFile(String pathToFile) throws IOException {
		
		try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
			String line;
			
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		}
		
	}
}
