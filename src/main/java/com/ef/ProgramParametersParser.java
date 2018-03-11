package com.ef;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ef.exception.InvalidParserParamterException;

public class ProgramParametersParser {
	private static final String ARGUMENT_REGEX = "^--(.+?)=(.+?)$";
	private static final String START_DATE_FORMAT = "yyyy-MM-dd.HH:mm:ss";
	
	public ProgramParameters parseParameters(String[] args) {
		ProgramParameters programParameters = new ProgramParameters();
		
		Pattern argPattern = Pattern.compile(ARGUMENT_REGEX);
		for (String argument : args) {
			Matcher argMatcher = argPattern.matcher(argument);
			if (!argMatcher.matches()) {
				throw new InvalidParserParamterException(
						String.format("Expected string in format \"%s\" but was \"%s\"", ARGUMENT_REGEX, argument));
			}

			String parameterName = argMatcher.group(1);
			String parameterValue = argMatcher.group(2);

			switch (parameterName) {
				case "accesslog":
					programParameters.getPathsToAccessLogs().add(parameterValue);
					break;
				case "startDate":
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(START_DATE_FORMAT);
					programParameters.setStartDate(LocalDateTime.parse(parameterValue, dateTimeFormatter));
					break;
				case "duration":
					programParameters.setDuration(parameterValue);
					break;
				case "threshold":
					programParameters.setThreshhold(Integer.parseInt(parameterValue));
					break;
				default:
					throw new InvalidParserParamterException(
						String.format("Parameter with name \"%s\" not recognized.", parameterName));
			}

		}
		
		return programParameters;
	}
}
