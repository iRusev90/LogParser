package com.ef;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import com.ef.exception.InvalidParserParamterException;

public class ProgramParametersParserTest {
	private static final String START_DATE_FORMAT = "yyyy-MM-dd.HH:mm:ss";
	ProgramParametersParser parameterParser = new ProgramParametersParser();

	@Test
	public void testParseProgramParameters__whenArgsAreValid() {
		String filePath = "/foo/bar.txt";
		String startDate = "2017-01-01.15:00:00";
		String duration = "daily";
		String threshold = "50";

		String[] args = new String[] { "--accesslog=" + filePath, "--startDate=" + startDate, "--duration=" + duration,
				"--threshold=" + threshold };

		ProgramParameters params = parameterParser.parseParameters(args);

		assertEquals(filePath, params.getPathsToAccessLogs().get(0));
		assertEquals(duration, params.getDuration());
		assertTrue(Integer.parseInt(threshold) == params.getThreshhold());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(START_DATE_FORMAT);
		assertEquals(LocalDateTime.parse(startDate, dtf), params.getStartDate());
	}

	@Test
	public void testParseProgramParameters__WhenArgsAreInInvalidFormat__ShouldThrow() {
		String[] args = new String[] { "-foo=bar" };

		assertThatThrownBy(() -> parameterParser.parseParameters(args))
				.isInstanceOf(InvalidParserParamterException.class)
				.hasMessage("Expected string in format \"^--(.+?)=(.+?)$\" but was \"-foo=bar\"");
	}
	
	@Test
	public void testParseProgramParameters__WhenParamKeyIsUnrecognized__ShouldThrow() {
		String[] args = new String[] { "--foo=bar" };

		assertThatThrownBy(() -> parameterParser.parseParameters(args))
				.isInstanceOf(InvalidParserParamterException.class)
				.hasMessage("Parameter with name \"foo\" not recognized.");
	}
}
