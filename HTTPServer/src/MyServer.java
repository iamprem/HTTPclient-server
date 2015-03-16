import java.io.IOException;

class MyServer {

	/**
	 * Creates a SocketServer object and starts the server.
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	
	public static void main(String[] args) throws InterruptedException {
		int portNumber = Integer.parseInt(args[0]);

//		int portNumber = 5554;
		try {
			// initializing the Socket Server
			MultiThreadedServer socketServer = new MultiThreadedServer(
					portNumber);
			socketServer.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}