package ServerController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Model.Shop;
import Model.Customers.CustomerList;
import Model.Inventory.Inventory;
import Model.Inventory.SupplierList;
import ServerController.DatabaseController.ShopManager;

public class Server {

	/**
	 * The server socket
	 */
	private static ServerSocket serverSocket;
	
	/**
	 * Constructor to build a server
	 * @param port Port number of server socket
	 */
	public Server (int port) {
		try {
			setServerSocket(new ServerSocket (port));
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Run the server, to start a game two clients must be accepted, throw two clients to a game thread
	 * @param args
	 * @throws IOException
	 */
	public static void main (String [] args) throws IOException{
		
		new Server(8989);
		
		ShopManager db = new ShopManager();
		
		Inventory inv = new Inventory(db.getItems());
		SupplierList supp = new SupplierList(db.getSuppliers());
		CustomerList cust = new CustomerList(db.getCustomers());
		Shop myShop = new Shop(inv, supp, cust);
		
		System.out.println(">My Server: Server started.");
		
		while(true) {
			Socket client = null;
			
			try {
				//wait for a customer to join
				client = Server.serverSocket.accept();
				System.out.println(">My Server: New client is connected.");
				
				ObjectInputStream socketIn = new ObjectInputStream (client.getInputStream());
				ObjectOutputStream socketOut = new ObjectOutputStream (client.getOutputStream());
									
				System.out.println(">My Server: New client sending to a server controller.");
				
				//start game when two clients connected
				Runnable servControl = new ServerController(client, socketIn, socketOut, myShop, db);
				Thread t = new Thread(servControl);
				t.start();
								
			} catch (IOException e) {
				client.close();
				serverSocket.close();
				db.closeConnection();
				e.printStackTrace();
			} 		
		}
		
		
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
}