package com.gifisan.nio.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gifisan.nio.server.InnerResponse;

public abstract class AsynchServiceAcceptor implements Runnable, ServiceAcceptor {

	private Logger			logger	= LoggerFactory.getLogger(AsynchServiceAcceptor.class);
	private Request		request	= null;
	private InnerResponse	response	= null;

	public AsynchServiceAcceptor(Request request, Response response) {
		this.request = request;
		this.response = (InnerResponse) response;

	}

	public void accept(Throwable exception) {
		try {
			// error connection , should not flush
			response.flush();
		} catch (IOException e) {
			// ignore
			logger.error(e.getMessage(), e);
		}
	}

	public void run() {
		try {
			this.accept(request, response);
		} catch (Throwable throwable) {
			logger.error(throwable.getMessage(), throwable);
			this.accept(throwable);
		}
	}

}