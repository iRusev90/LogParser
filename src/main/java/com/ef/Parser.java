package com.ef;

import java.io.IOException;

public class Parser {
	public static void main(String[] args) throws IOException {
		System.out.println("Hello World!");
		System.out.println(args.length);
		
		ProgramParameters programParameters = new ProgramParameters(args);
		System.out.println(programParameters.getPathToAccessLog());
		System.out.println(programParameters.getStartDate());
		System.out.println(programParameters.getEndDate());
		System.out.println(programParameters.getThreshhold());
		
		LogService logService = new LogService();
		
		if (programParameters.getPathToAccessLog() != null) {
			logService.saveLogsFromFile(programParameters.getPathToAccessLog());
		}
	}
}
