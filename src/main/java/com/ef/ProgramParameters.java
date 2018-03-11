package com.ef;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ef.exception.InvalidParserParamterException;

public class ProgramParameters {
	private static final String ARGUMENT_REGEX = "^--(.+?)=(.+?)$";
	private static final String START_DATE_FORMAT = "yyyy-MM-dd.HH:mm:ss";

	private List<String> pathsToAccessLogs = new ArrayList<String>();
	private LocalDateTime startDate = null;
	private String duration = null;
	private Integer threshhold = null;

	public ProgramParameters(String[] args) {
		parseAndStoreArguments(args);
	}

	private void parseAndStoreArguments(String[] args) {
		Pattern argPattern = Pattern.compile(ARGUMENT_REGEX);
		for (String argument : args) {
			Matcher argMatcher = argPattern.matcher(argument);
			if (!argMatcher.matches()) {
				throw new InvalidParserParamterException(
						String.format("Expected string in format \"%s\" but was \"s%\"", ARGUMENT_REGEX, argument));
			}

			String parameterName = argMatcher.group(1);
			String parameterValue = argMatcher.group(2);

			switch (parameterName) {
				case "accesslog":
					this.pathsToAccessLogs.add(parameterValue);
					break;
				case "startDate":
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(START_DATE_FORMAT);
					startDate = LocalDateTime.parse(parameterValue, dateTimeFormatter);
					break;
				case "duration":
					this.duration = parameterValue;
					break;
				case "threshold":
					this.threshhold = Integer.parseInt(parameterValue);
					break;
				default:
					throw new InvalidParserParamterException(
						String.format("Parameter with name \"%s\" not recognized.", parameterName));
			}

		}
	}
	
	public String getDuration() {
		return this.duration;
	}

	public List<String> getPathsToAccessLogs() {
		return this.pathsToAccessLogs;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public Integer getThreshhold() {
		return threshhold;
	}
}
