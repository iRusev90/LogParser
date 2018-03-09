package com.ef;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ef.service.LogService;

public class Parser {
	@Autowired
	private LogService logService;
	
	public static void main(String[] args) throws IOException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		Parser parser = ctx.getBean(Parser.class);
	
		parser.run(args);
		((ConfigurableApplicationContext) ctx).close();		
	}
	
	private void run(String[] args) throws IOException {
		System.out.println("Hello World!");
		System.out.println(args.length);
		
		ProgramParameters programParameters = new ProgramParameters(args);
		System.out.println(programParameters.getPathToAccessLog());
		System.out.println(programParameters.getStartDate());
		System.out.println(programParameters.getEndDate());
		System.out.println(programParameters.getThreshhold());
		
		if (programParameters.getPathToAccessLog() != null) {
			logService.saveLogsFromFile(programParameters.getPathToAccessLog());
		}
	}
}
