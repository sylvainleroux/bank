package com.sleroux.bank.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;

import com.sleroux.bank.web.service.BudgetHandler;
import com.sleroux.bank.web.service.CalcHandler;
import com.sleroux.bank.web.service.CategorizerHandler;
import com.sleroux.bank.web.service.CheckoutHandler;
import com.sleroux.bank.web.service.KeylistHandler;
import com.sleroux.bank.web.service.TimelineHandler;

//-Dcom.sun.enterprise.web.connector.grizzly.fileCache.isEnabled=false
public class Server {

	public static Server	instance;
	private ExecutorService	executorService	= Executors.newFixedThreadPool(2);

	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	public Server() {
		HttpServer server = HttpServer.createSimpleServer("/Users/sleroux/ideo/bank-client/build/", 8181);
		for (NetworkListener n : server.getListeners()) {
			System.out.println(n.getName());
		}
		server.getListener("grizzly").getFileCache().setEnabled(false);
		server.getServerConfiguration().addHttpHandler(new CheckoutHandler(executorService), "/checkout");
		server.getServerConfiguration().addHttpHandler(new CategorizerHandler(executorService), "/catego");
		server.getServerConfiguration().addHttpHandler(new BudgetHandler(executorService), "/budget");
		server.getServerConfiguration().addHttpHandler(new TimelineHandler(), "/timeline");
		server.getServerConfiguration().addHttpHandler(new CalcHandler(), "/calc");
		server.getServerConfiguration().addHttpHandler(new KeylistHandler(), "/keylist");
		try {
			server.start();
			System.out.println("Press any key to stop the server...");
			System.in.read();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public static void main(String[] args) {
		getInstance();
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

}
