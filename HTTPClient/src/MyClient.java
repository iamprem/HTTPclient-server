import java.io.IOException;


public class MyClient {

	public static void main(String[] args) throws IOException {

		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String command = args[2];
		String path = args[3];

//		String host = "localhost";
//		int port = 5555;
//		String command = "PUT";
//		String path = "index.htm";
		
		// Method Check GET or PUT
		if ("GET".equals(command)) {
			Client.getMethod(host, port, path);
		} else if ("PUT".equals(command)) {
			Client.putMethod(host, port, path);
		}else{
			System.out.println("Check the HTTP command! It should be either GET or PUT");
			return;
		}
		
	}

}
