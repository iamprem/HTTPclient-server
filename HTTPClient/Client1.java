import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client1 {

	@SuppressWarnings("resource")
	public static void getMethod(String host, int port, String path)
			throws IOException {

		// Opening Connection based on the port number 80(HTTP) and 443(HTTPS)
		Socket clientSocket = null;

		// Disabling port check to test the server
		// if (port == 80) {
		clientSocket = new Socket(host, port);
		// } else if (port == 443) {
		// SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory
		// .getDefault();
		// clientSocket = (SSLSocket) factory.createSocket(host, port);
		// } else {
		// System.out.println("Check your port Number!!");
		// return;
		// }

		System.out.println("======================================");
		System.out.println("Connected");
		System.out.println("======================================");

		// Declare a writer to this url
		PrintWriter request = new PrintWriter(clientSocket.getOutputStream(),
				true);

		// Declare a listener to this url
		BufferedReader response = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

		// Sending request to the server
		// Building HTTP request header
		request.print("GET " + path + " HTTP/1.1\r\n"); // "+path+"
		request.print("Host: " + host + "\r\n");
		request.print("Connection: close\r\n");
		request.print("User-Agent: Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10\r\n");
		request.print("\r\n");
		request.flush();
		System.out.println("Request Sent!");
		System.out.println("======================================");

		// Receiving response from server
		String responseLine;
		while ((responseLine = response.readLine()) != null) {
			System.out.println(responseLine);
		}
		System.out.println("======================================");
		System.out.println("Response Recieved!!");
		System.out.println("======================================");

		response.close();
		request.close();
		clientSocket.close();
	}

	public static void putMethod(String host, int port, String file)
			throws UnknownHostException, IOException {

		// Opening Connection based on the port number 80(HTTP) and 443(HTTPS)
		Socket clientSocket = null;

		// if (port == 80) {
		clientSocket = new Socket(host, port);
		// } else if (port == 443) {
		// SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory
		// .getDefault();
		// clientSocket = (SSLSocket) factory.createSocket(host, port);
		// } else {
		// System.out.println("Check your port Number!!");
		// return;
		// }

		System.out.println("======================================");
		System.out.println("Connected");
		System.out.println("======================================");

		PrintWriter request = new PrintWriter(clientSocket.getOutputStream(),
				true);

		// Declare a listener to this url
		BufferedReader response = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

		// Sending request to the server
		// Building HTTP request header
		request.println("PUT /" + file + "/ HTTP/1.1"); // "+path+"
		request.println("Host: " + host);
		request.println("Accept-Language: en-us");
		request.println("Connection: Keep-Alive");
		request.println("Mozilla/4.0 (compatible; MSIE5.01; Windows NT)");
		request.println("Content-type: text/html");
		request.println("Content-Length: 0");
		request.println("");

		System.out.println("PUT Request Header Sent!");
		System.out.println("======================================");

		// Send the Data to be PUT
		String htmlContent = readHtmlFile(file);
		request.println(htmlContent);
		request.flush();

		System.out.println("PUT Data Sent!");
		System.out.println("======================================");

		// Receiving response from server
		String responseLine;
		while ((responseLine = response.readLine()) != null) {
			System.out.println(responseLine);
		}
		System.out.println("======================================");
		System.out.println("Response Recieved!!");
		System.out.println("======================================");
		request.close();
		response.close();
		clientSocket.close();
	}

	private static String readHtmlFile(String file) throws IOException {
		File myFile = new File(file);
		// Print file path
		System.out.println(myFile.getPath());
		System.out.println(myFile.getAbsolutePath());
		try {
			System.out.println(myFile.getCanonicalPath());
		} catch (Exception e) {
		}

		BufferedReader reader = new BufferedReader(new FileReader(myFile));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		reader.close();
		return stringBuilder.toString();
	}
}
