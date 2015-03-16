import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketClientHandler implements Runnable {

	private Socket client;

	public SocketClientHandler(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			System.out.println("Thread started with name:"
					+ Thread.currentThread().getName());
			readResponse();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void readResponse() throws IOException, InterruptedException {

		try {

			BufferedReader request = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			BufferedWriter response = new BufferedWriter(
					new OutputStreamWriter(client.getOutputStream()));

			String putDataFromClient = "";
			String requestHeader = "";
			String temp = ".";
			while (!temp.equals("")) {
				temp = request.readLine();
				System.out.println(temp);
				requestHeader += temp + "\n";
			}

			// Get the method from HTTP header
			StringBuilder sb = new StringBuilder();
			String file = requestHeader.split("\n")[0].split(" ")[1]
					.split("/")[1];
			if (requestHeader.split("\n")[0].contains("GET")
					&& checkURL(file)) {

				// Get the correct page
				constructResponseHeader(200, sb);
				response.write(sb.toString());
				response.write(getData(file));
				sb.setLength(0);
				response.flush();

			} else if (requestHeader.split("\n")[0].contains("PUT")
					&& checkURL(file)) {

				// Get the data from the inputStream
				temp = ".";
				temp = request.readLine();
				while (temp.contains("<html>")||temp.contains("<!doctype html>")||temp.contains("<!DOCTYPE html>")) {
					while (!temp.equals("</html>")) {
						temp = request.readLine();
						System.out.println(temp);
						putDataFromClient += temp + "\n";
					}
					putDataFromClient += "</html>";
				}

				// PUT the data to file serverIndex.html
				if (putDataFromClient != "") {
					int responseCode = putData(putDataFromClient, file);
					constructResponseHeader(responseCode, sb);
					response.write(sb.toString());
					sb.setLength(0);
					response.flush();
				} else {
					constructResponseHeader(304, sb);
					response.write(sb.toString());
					sb.setLength(0);
					response.flush();
				}

			} else {
				// Enter the error code
				// 404 page not found
				constructResponseHeader(404, sb);
				response.write(sb.toString());
				sb.setLength(0);
				response.flush();
			}

			request.close();
			response.close();

			client.close();
			return;
		} catch (Exception e) {
		}

	}

	// Check the URL from the Request header to the server's database
	private static boolean checkURL(String file) {

		File myFile = new File(file);
//		System.out.println(file);
//		System.out.println("IT IS CHEKCING");
//		System.out.println(myFile.exists() && !myFile.isDirectory());
		return myFile.exists() && !myFile.isDirectory();

	}

	// Construct Response Header
	private static void constructResponseHeader(int responseCode,
			StringBuilder sb) {

		if (responseCode == 200) {

			sb.append("HTTP/1.1 200 OK\r\n");
			sb.append("Date:" + getTimeStamp() + "\r\n");
			sb.append("Server:localhost\r\n");
			sb.append("Content-Type: text/html\r\n");
			sb.append("Connection: Closed\r\n\r\n");

		} else if (responseCode == 404) {

			sb.append("HTTP/1.1 404 Not Found\r\n");
			sb.append("Date:" + getTimeStamp() + "\r\n");
			sb.append("Server:localhost\r\n");
			sb.append("\r\n");
		} else if (responseCode == 304) {
			sb.append("HTTP/1.1 304 Not Modified\r\n");
			sb.append("Date:" + getTimeStamp() + "\r\n");
			sb.append("Server:localhost\r\n");
			sb.append("\r\n");
		}
	}

	// PUT data to file ServerIndex.htm
	private static int putData(String putDataFromClient, String file)
			throws IOException {

		return writeHtmlFile(putDataFromClient, file);
	}

	private static String getData(String file) {

		File myFile = new File(file);
		String responseToClient = "";
		BufferedReader reader;

		// System.out.println(myFile.getAbsolutePath());

		try {
			reader = new BufferedReader(new FileReader(myFile));
			String line = null;
			while (!(line = reader.readLine()).contains("</html>")) {
				responseToClient += line;
			}
			responseToClient += line;
			// System.out.println(responseToClient);
			reader.close();

		} catch (Exception e) {

		}
		return responseToClient;
	}

	// Write the data to server - Helper method for putData method
	private static int writeHtmlFile(String putDataFromClient, String file) {

		File myFile = new File(file);
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(myFile));
			writer.write(putDataFromClient);
			writer.close();
			return 200;
		} catch (IOException e) {
			return 304;
		}
	}

	// TimeStamp
	private static String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		String formattedDate = sdf.format(date);
		return formattedDate;
	}

}
