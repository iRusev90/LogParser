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
	
	@Autowired
	private ProgramParametersParser parametersParser;
	
	public static void main(String[] args) throws IOException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml", "classpath:/database.xml");
		Parser parser = ctx.getBean(Parser.class);
	
		parser.run(args);
		((ConfigurableApplicationContext) ctx).close();		
	}
	
	private void run(String[] args) throws IOException {		
		ProgramParameters programParameters = parametersParser.parseParameters(args);
		
		if (!programParameters.getPathsToAccessLogs().isEmpty()) {
			logService.saveLogsFromFiles(programParameters.getPathsToAccessLogs());
		}
		
		logService.banAndShowIps(programParameters.getStartDate(), programParameters.getDuration(), programParameters.getThreshhold());
	}
}
