package test.template.utils;

import ch.qos.logback.core.OutputStreamAppender;

public class MTLogAppender<E> extends OutputStreamAppender<E> {
	@Override
	public void start() {
		setOutputStream(MTLogBuffer.getInstance());
		super.start();
	}
}
