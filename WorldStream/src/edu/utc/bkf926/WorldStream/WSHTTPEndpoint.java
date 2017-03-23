package edu.utc.bkf926.WorldStream;

import com.google.common.io.Files;
import com.sun.net.httpserver.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WSHTTPEndpoint {
	
	static HttpServer server;

	public static void startServer() throws IOException{
		server = HttpServer.create(new InetSocketAddress(420), 0); //blaze it

		server.createContext("/get", new DownloadHandler());
		
		server.setExecutor(null);
		server.start();
	}
	
	/**
	 * Handles world download requests.
	 * REQUEST FORMAT: [host]/get/[world]/x/z/
	 * @author Derek Elam
	 * @throws IOException. This should only occur if the server is unable to send a response due to some configuration problem.
	 */
	static class DownloadHandler implements HttpHandler{
		public void handle(HttpExchange exchange) throws IOException{
			//InputStream in = exchange.getRequestBody();
			OutputStream out = exchange.getResponseBody();
			
			String[] reqTokens = exchange.getRequestURI().toString().split("/");
			int s = reqTokens.length;
			int x = 0; int z = 0; String worldName = null;
			
			try {
				z = Integer.parseInt(reqTokens[s-1]);
				x = Integer.parseInt(reqTokens[s-2]);
				worldName = reqTokens[s-3];
				
				File file = WSServerPlugin.getJSONFile(worldName, x, z);
				if (file==null){
					String err = "Chunk not found! Check the spelling of the world name and try again.";
					exchange.sendResponseHeaders(404, err.length());
					out.write(err.getBytes());
					out.close();
					return;
				}
				
				//Prepare JSON file for transit
				byte[] fileBytes = new byte[(int)file.length()];
				FileInputStream in = new FileInputStream(file);
				BufferedInputStream buffer = new BufferedInputStream(in);
				buffer.read(fileBytes, 0, fileBytes.length);
				buffer.close();
				
				//Transmit file as HTTP response
				exchange.sendResponseHeaders(200, file.length());
				out.write(fileBytes, 0, fileBytes.length);
				out.close();
			}
			catch (NumberFormatException | IndexOutOfBoundsException e1){
				String err = "WorldStream could not understand that request. Chunk URI should be /get/[worldName]/[x]/[z] . "
						+ "Please check the format of your URI.";
				exchange.sendResponseHeaders(400, err.length());
				out.write(err.getBytes());
				out.close();
			}
			catch (FileNotFoundException e3){
				
			}
			
			//Request body should contain world name, chunk X and Z
			//Call getJSONFile(world) to return the file or check timestamps to see if it needs to be re-exported
			//Write resultant file to outputStream
			//Close
		}
	}
	
}
