package com.ef;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.ef.service.LogService;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {
	
	@Mock
	private LogService logService;
	
	@Mock
	private ProgramParametersParser parameterParser;
	
	@InjectMocks
	private Parser parser;
	
	@Test
	public void testRun__WhenAccessLog() throws IOException {
		ProgramParameters stubParameters = new ProgramParameters();
		stubParameters.setDuration("daily");
		stubParameters.setStartDate(LocalDateTime.now());
		stubParameters.setThreshhold(100);
		stubParameters.getPathsToAccessLogs().add("baz");
		
		Mockito.when(parameterParser.parseParameters(Mockito.any())).thenReturn(stubParameters);
		
		ReflectionTestUtils.invokeMethod(parser, "run", new Object[]{new String[]{"foo","bar"}});
		
		Mockito.verify(logService).saveLogsFromFiles(stubParameters.getPathsToAccessLogs());
		Mockito.verify(logService).banAndShowIps(stubParameters.getStartDate(), stubParameters.getDuration(),
																				stubParameters.getThreshhold());
	}
	
	@Test
	public void testRun__WhenNoAccessLog() throws IOException {		
		Mockito.when(parameterParser.parseParameters(Mockito.any())).thenReturn(new ProgramParameters());
		
		ReflectionTestUtils.invokeMethod(parser, "run", new Object[]{new String[]{"foo","bar"}});
		
		Mockito.verify(logService, Mockito.never()).saveLogsFromFiles(Mockito.any());
		Mockito.verify(logService).banAndShowIps(Mockito.any(), Mockito.any(), Mockito.any());
	}
	
}
