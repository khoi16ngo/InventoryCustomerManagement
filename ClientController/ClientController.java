package ClientController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Model.Customers.Customer;
import Model.Inventory.Item;
import Model.Inventory.Order;

public class ClientController {
	/**
	 * Server socket
	 */
	private Socket serverSocket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	
	public ClientController(String serverName, int portNumber) {
		try {
			//Connect to server socket  
			serverSocket = new Socket(serverName, portNumber);

			//Open streams on scoket, pass socket I/O handles
			socketOut = new ObjectOutputStream(serverSocket.getOutputStream());
			socketIn = new ObjectInputStream(serverSocket.getInputStream());
						
		} catch (IOException e) {
			System.err.println(e.getStackTrace());
		}
	}
	
	//---------------------------------------------------------------------------------------
    // CASE 1, ADD TOOL
    //---------------------------------------------------------------------------------------
	public boolean addTool(Item add) {
		boolean status = false;
		
		try {
			socketOut.writeObject(1);
			socketOut.writeObject(add);
			status = (boolean)socketIn.readObject();
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
	//---------------------------------------------------------------------------------------
    // CASE 2, DELETE TOOL
    //---------------------------------------------------------------------------------------
	public boolean deleteTool(int id) {
		boolean status = false;
		
		try {
			socketOut.writeObject(2);
			socketOut.writeObject(id);
			status = (boolean)socketIn.readObject();
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
	//---------------------------------------------------------------------------------------
    // CASE 3, SEARCH TOOL BY ID
    //---------------------------------------------------------------------------------------
	public Item searchToolById(int id) {
		try {
			socketOut.writeObject(3);
			socketOut.writeObject(id);
			Item foundTool = (Item)socketIn.readObject();
			return foundTool;
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//---------------------------------------------------------------------------------------
    // CASE 4, SEARCH TOOL BY NAME
    //---------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public ArrayList<Item> searchToolByName(String name) {
		try {
			socketOut.writeObject(4);
			socketOut.writeObject(name);
			ArrayList<Item> foundTools = (ArrayList<Item>)socketIn.readObject();
			return foundTools;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//---------------------------------------------------------------------------------------
    // CASE 5, CHECK INVENTORY AND DISPLAY ALL INVENTORY
    //---------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public ArrayList<Item> checkInventory() {
		try {
			socketOut.writeObject(5);
			ArrayList<Item> toolList = (ArrayList<Item>)socketIn.readObject();
			return toolList;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//---------------------------------------------------------------------------------------
    // CASE 6, SEARCH ORDER BY DATE
    //---------------------------------------------------------------------------------------
	public Order searchOrderByDate(String date) {
		
		try {
			socketOut.writeObject(6);
			socketOut.writeObject(date);
			Order foundOrder = (Order)socketIn.readObject();
			return foundOrder;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//---------------------------------------------------------------------------------------
    // CASE 7, DECREASE TOOL BY QUANTITY
    //---------------------------------------------------------------------------------------
	public boolean decreaseTool(int id, int quantity) {
		try {
			socketOut.writeObject(7);
			socketOut.writeObject(id);
			socketOut.writeObject(quantity);
			boolean status13 = (boolean)socketIn.readObject();
			return status13;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//---------------------------------------------------------------------------------------
    // CASE 8, ADD CUSTOMER
    //---------------------------------------------------------------------------------------
	public boolean addCustomer(Customer add) {
		boolean status = false;
		
		try {
			socketOut.writeObject(8);
			socketOut.writeObject(add);					
			status = (boolean)socketIn.readObject();
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return status;
	}

	
	//---------------------------------------------------------------------------------------
    // CASE 9, MODIFY CUSTOMER
    //---------------------------------------------------------------------------------------
	public boolean modifyCustomer(Customer mod) {
		boolean status = false;
		
		try {
			socketOut.writeObject(9);
			socketOut.writeObject(mod);
			status = (boolean)socketIn.readObject();
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
	//---------------------------------------------------------------------------------------
    // CASE 10, DELETE CUSTOMER BY ID
    //---------------------------------------------------------------------------------------
	public boolean deleteCustomer(int id) {
		boolean status = false;
		
		try {
			socketOut.writeObject(10);
			socketOut.writeObject(id);
			status = (boolean)socketIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
	//---------------------------------------------------------------------------------------
    // CASE 11, SEARCH CUSTOMER BY ID
    //---------------------------------------------------------------------------------------
	public Customer searchCustomerById(int id) {
		try {
			socketOut.writeObject(11);
			socketOut.writeObject(id);
			Customer foundCustomer = (Customer)socketIn.readObject();
			return foundCustomer;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//---------------------------------------------------------------------------------------
    // CASE 12, SEARCH CUSTOMER BY NAME
    //---------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public ArrayList<Customer> searchCustomerByName(String name) {
		try {
			socketOut.writeObject(12);
			socketOut.writeObject(name);
			ArrayList<Customer> foundCustomers = (ArrayList<Customer>)socketIn.readObject();
			return foundCustomers;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//---------------------------------------------------------------------------------------
    // CASE 13, SEARCH CUSTOMER BY TYPE
    //---------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public ArrayList<Customer> searchCustomerByType(String type) {
		try {
			socketOut.writeObject(13);
			socketOut.writeObject(type);
			ArrayList<Customer> foundCustomers = (ArrayList<Customer>)socketIn.readObject();
			return foundCustomers;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//---------------------------------------------------------------------------------------
    // DEFAULT CASE, CLOSE CONNECTION 
    //---------------------------------------------------------------------------------------
	public void closeClient() {
		try {
			socketOut.writeObject(Integer.MAX_VALUE);
			
			serverSocket.close();
			socketIn.close();
			socketOut.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}
