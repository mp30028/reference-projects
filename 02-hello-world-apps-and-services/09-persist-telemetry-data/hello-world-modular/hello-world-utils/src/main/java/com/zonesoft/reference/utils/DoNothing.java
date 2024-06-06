package com.zonesoft.reference.utils;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.incubator.trace.ExtendedTracer;
import io.opentelemetry.api.trace.Span;


public class DoNothing {
	private static final Logger LOGGER = LoggerFactory.getLogger(DoNothing.class);
	private static final String INSTRUMENTATION_NAME = DoNothing.class.getName();
	private static final int MINIMUM_WAIT_MS =30;
	private static final int MAXIMUM_WAIT_MS = 1200;
	private final OpenTelemetry openTelemetry;
	
	public DoNothing(OpenTelemetry openTelemetry) {
		super();
		this.openTelemetry = openTelemetry;
	}
	
	public void pretendDoingSomething() {
		ExtendedTracer tracer = ExtendedTracer.create(openTelemetry.getTracer(INSTRUMENTATION_NAME));
		tracer.spanBuilder("pretendDoingSomething").startAndRun(this::doPretendDoingSomething);
	}
	
	private void doPretendDoingSomething() {
		Span.current().setAttribute("MINIMUM_WAIT_MS", MINIMUM_WAIT_MS);
		Span.current().setAttribute("MAXIMUM_WAIT_MS", MAXIMUM_WAIT_MS);
        try {
        	int waitMilliseconds = new Random().nextInt(MAXIMUM_WAIT_MS - MINIMUM_WAIT_MS + 1) + MINIMUM_WAIT_MS;
        	Span.current().setAttribute("waitMilliseconds", waitMilliseconds);
        	LOGGER.debug("Starting waitMilliseconds={}", waitMilliseconds);
            Thread.sleep(waitMilliseconds);
            LOGGER.debug("Finished waitMilliseconds={}", waitMilliseconds);
        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
	}
	
}
