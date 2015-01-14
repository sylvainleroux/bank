package com.sleroux.cyberplusclient.util.extractor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;

import com.sleroux.accountchecker.banquepopulaire.util.extractors.TokenExtractor;

public class TestTokenExtractor {

	class TestAppender extends AppenderSkeleton {
		private final List<LoggingEvent>	log	= new ArrayList<LoggingEvent>();

		@Override
		public boolean requiresLayout() {
			return false;
		}

		@Override
		protected void append(final LoggingEvent loggingEvent) {
			log.add(loggingEvent);
		}

		@Override
		public void close() {
		}

		public List<LoggingEvent> getLog() {
			return new ArrayList<LoggingEvent>(log);
		}
	}

	@Test
	public void testTokenExtractor() {

		TestAppender appender = new TestAppender();
		Logger logger = Logger.getRootLogger();
		logger.addAppender(appender);
		logger.setLevel(Level.DEBUG);

		TokenExtractor extractor = new TokenExtractor("test", "(abc)", logger);
		extractor.extract("abcvion");
		extractor.extract("avabcion");

		final List<LoggingEvent> log = appender.getLog();
		final LoggingEvent firstLogEntry = log.get(0);
		final LoggingEvent lastLogEntry = log.get(log.size() - 1);

		Assert.assertEquals("abcvion", firstLogEntry.getMessage());
		Assert.assertEquals("already found", lastLogEntry.getMessage());

		logger.removeAppender(appender);

	}

}
