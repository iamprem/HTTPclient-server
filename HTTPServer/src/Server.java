//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class Server {
//
//	private ServerSocket serverSocket;
//	private int port;
//
//	public Server(int port) {
//		this.port = port;
//	}
//
//	public void serverStart() throws IOException {
//
//		serverSocket = new ServerSocket(port);
//		Socket client = null;
//		
//		while(true){
//        	System.out.println("Waiting for clients...");
//        	client = serverSocket.accept();
//        	System.out.println("The following client has connected:"+client.getInetAddress().getCanonicalHostName());
//        	//A client has connected to this server. Send welcome message
//            Thread thread = new Thread(new SocketClientHandler(client));
//            thread.start();
//        }    
//		
//		try {
//			System.out.println("Waiting for Clients...");
//			System.out.println("==============================");
//
//			Socket client = serverSocket.accept();
//
//			BufferedReader request = new BufferedReader(new InputStreamReader(
//					client.getInputStream()));
//			BufferedWriter response = new BufferedWriter(
//					new OutputStreamWriter(client.getOutputStream()));
//
//			String putDataFromClient = "";
//			String requestHeader = "";
//			String temp = ".";
//			while (!temp.equals("")) {
//				temp = request.readLine();
//				System.out.println(temp);
//				requestHeader += temp + "\n";
//			}
//
//			// Get the method from HTTP header
//			StringBuilder sb = new StringBuilder();
//			if (requestHeader.split("\n")[0].contains("GET")
//					&& checkURL(requestHeader)) {
//
//				// Get the correct page
//				String file = requestHeader.split("\n")[0].split(" ")[1]
//						.split("/")[1];
//				constructResponseHeader(200, sb);
//				response.write(sb.toString());
//				response.write(getData(file));
//				sb.setLength(0);
//				response.flush();
//
//			} else if (requestHeader.split("\n")[0].contains("PUT")
//					&& checkURL(requestHeader)) {
//
//				// Get the data from the inputStream
//				temp = ".";
//				temp = request.readLine();
//				while (temp.contains("<html>")) {
//					while (!temp.equals("</html>")) {
//						temp = request.readLine();
//						System.out.println(temp);
//						putDataFromClient += temp + "\n";
//					}
//					putDataFromClient += "</html>";
//				}
//
//				// PUT the data to file serverIndex.html
//				if (putDataFromClient != "") {
//					String file = requestHeader.split("\n")[0].split(" ")[1]
//							.split("/")[1];
//					int responseCode = putData(putDataFromClient, file);
//					constructResponseHeader(responseCode, sb);
//					response.write(sb.toString());
//					sb.setLength(0);
//					response.flush();
//				} else {
//					constructResponseHeader(304, sb);
//					response.write(sb.toString());
//					sb.setLength(0);
//					response.flush();
//				}
//
//			} else {
//				// Enter the error code
//				// 404 page not found
//				constructResponseHeader(404, sb);
//				response.write(sb.toString());
//				sb.setLength(0);
//				response.flush();
//			}
//
//			request.close();
//			response.close();
//			client.close();
//			serverSocket.close();
//			serverStart(port);
//			return;
//		} catch (Exception e) {
//			serverSocket.close();
//			serverStart(port);
//		}
//
//	}
//
//	// Check the URL from the Request header to the server's database
//	private static boolean checkURL(String requestHeader) {
//
//		String url;
//
//		url = requestHeader.split("\n")[1].substring(6)
//				+ requestHeader.split("\n")[0].split(" ")[1];
//		// System.out.println(url);
//
//		return url.equals("localhost/") || url.equals("localhost/index.htm/")
//				|| url.equals("localhost/home.html/")
//				|| url.equals("localhost/content.html/");
//
//	}
//
//	// Construct Response Header
//	private static void constructResponseHeader(int responseCode,
//			StringBuilder sb) {
//
//		if (responseCode == 200) {
//
//			sb.append("HTTP/1.1 200 OK\r\n");
//			sb.append("Date:" + getTimeStamp() + "\r\n");
//			sb.append("Server:localhost\r\n");
//			sb.append("Content-Type: text/html\r\n");
//			sb.append("Connection: Closed\r\n\r\n");
//
//		} else if (responseCode == 404) {
//
//			sb.append("HTTP/1.1 404 Not Found\r\n");
//			sb.append("Date:" + getTimeStamp() + "\r\n");
//			sb.append("Server:localhost\r\n");
//			sb.append("\r\n");
//		} else if (responseCode == 304) {
//			sb.append("HTTP/1.1 304 Not Modified\r\n");
//			sb.append("Date:" + getTimeStamp() + "\r\n");
//			sb.append("Server:localhost\r\n");
//			sb.append("\r\n");
//		}
//	}
//
//	// PUT data to file ServerIndex.htm
//	private static int putData(String putDataFromClient, String file)
//			throws IOException {
//
//		return writeHtmlFile(putDataFromClient, file);
//	}
//
//	private static String getData(String file) {
//
//		File myFile = new File(file);
//		String responseToClient = "";
//		BufferedReader reader;
//
//		System.out.println(myFile.getAbsolutePath());
//
//		try {
//			reader = new BufferedReader(new FileReader(myFile));
//			String line = null;
//			while (!(line = reader.readLine()).contains("</html>")) {
//				responseToClient += line;
//			}
//			responseToClient += line;
//			System.out.println(responseToClient);
//			reader.close();
//
//		} catch (Exception e) {
//
//		}
//		return responseToClient;
//	}
//
//	// Write the data to server - Helper method for putData method
//	private static int writeHtmlFile(String putDataFromClient, String file) {
//
//		File myFile = new File(file);
//		BufferedWriter writer;
//		try {
//			writer = new BufferedWriter(new FileWriter(myFile));
//			writer.write(putDataFromClient);
//			writer.close();
//			return 200;
//		} catch (IOException e) {
//			return 304;
//		}
//
//	}
//
//	// TimeStamp
//	private static String getTimeStamp() {
//		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
//		String formattedDate = sdf.format(date);
//		return formattedDate;
//	}
//
//}
