package com.sleroux.bank.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {

	private Logger			logger	= Logger.getLogger(AuthTokenService.class);

	private ServerSocket	serverSocket;

	public String createAuthencationToken(String _login, String _password) {
		final String token = UUID.randomUUID().toString();
		new Thread(() -> {
			try {

				logger.info("Start auth server on port 8384");
				if (serverSocket == null) {
					serverSocket = new ServerSocket(8384);
				}

				Socket clientSocket = serverSocket.accept();

				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

				String s;
				String url = null;
				while ((s = in.readLine()) != null) {
					if (s.startsWith("GET")) {
						url = s.split(" ")[1].substring(1);
					}
					break;

				}

				out.write("HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\n");
				out.write("");
				// out.write("Content-Length: 59\r\n");

				out.write("");
				if (token.equals(url)) {
					out.write("{\"u\":\"" + _login + "\",\"p\":\"" + _password + "\"}\n");
				} else {
					out.write("{}");
				}

				out.close();
				in.close();
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}).start();
		return token;
	}
}
