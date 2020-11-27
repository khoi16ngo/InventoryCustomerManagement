package ServerController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Model.Shop;
import Model.Customers.Customer;
import Model.Inventory.*;
import ServerController.DatabaseController.ShopManager;

public class ServerController implements Runnable {
	
	private Socket client;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	private CustomerModelController custControl;
	private InventoryModelController invControl;
	private ShopManager db;
	
	public ServerController(Socket client, ObjectInputStream socketIn, ObjectOutputStream socketOut, Shop myShop, ShopManager db) {
		setClient(client);
		setSocketIn(socketIn);
		setSocketOut(socketOut);
		setDb(db);
		setCustControl(new CustomerModelController(db, myShop));
		setInvControl(new InventoryModelController(db, myShop));
	}

	@Override
	public void run() {
		boolean running = true;
		
		while(running) {
			try {
				int userInput = (int)socketIn.readObject();
				
				switch(userInput) {
					//add new tool
					case 1:
						Item newTool1 = (Item)socketIn.readObject();
						boolean status1 = invControl.addItem(newTool1);
						socketOut.writeObject(status1);
						break;
			
					//delete tool
					case 2:
						int delToolId = (int)socketIn.readObject();
						boolean status3 = invControl.deleteItem(delToolId);
						socketOut.writeObject(status3);
						break;
						
					//search tool by id
					case 3:
						int toolId = (int)socketIn.readObject();
						Item foundTool = invControl.searchItemById(toolId);
						socketOut.writeObject(foundTool);
						break;
					
					//search tool by name
					case 4:
						String toolName = (String)socketIn.readObject();
						ArrayList<Item> foundTools = invControl.searchItemByName(toolName);
						socketOut.writeObject(foundTools);
						break;
						
					//check tool stock
					case 5:
						ArrayList<Item> toolList = invControl.checkInventory();
						socketOut.writeObject(toolList);
						break;
						
					//get order
					case 6:
						String date = (String)socketIn.readObject();
						Order foundOrder = invControl.getOrder(date);
						socketOut.writeObject(foundOrder);
						break;
						
					case 7:
						int decreaseId = (int)socketIn.readObject();
						int decreaseQuant = (int)socketIn.readObject();
						boolean ret7 = invControl.decreaseTool(decreaseId, decreaseQuant);
						socketOut.writeObject(ret7);
						break;
						
					//--------------------------------------------------------------------------
						
					//add new customer
					case 8:
						Customer newCustomer = (Customer)socketIn.readObject();
						boolean status8 = custControl.addCustomer(newCustomer);
						socketOut.writeObject(status8);
						break;
						
					//modify customer
					case 9:
						Customer modifyCustomer = (Customer)socketIn.readObject();
						boolean status9 = custControl.modifyCustomer(modifyCustomer);
						socketOut.writeObject(status9);
						break;
						
					//delete customer
					case 10:
						int deleteCustomerId = (int)socketIn.readObject();
						boolean status10 = custControl.deleteCustomer(deleteCustomerId);
						socketOut.writeObject(status10);
						break;
						
					//search customer by id
					case 11:
						int id = (int)socketIn.readObject();
						Customer foundCustomer = custControl.searchCustomerById(id);
						socketOut.writeObject(foundCustomer);
						break;
						
					//search customer by name
					case 12:
						String customerName = (String)socketIn.readObject();
						ArrayList<Customer> foundCustomers1 = custControl.searchCustomerByName(customerName);
						socketOut.writeObject(foundCustomers1);
						break;
						
					//search customer by type
					case 13:
						String customerType = (String)socketIn.readObject();
						ArrayList<Customer> foundCustomers2 = custControl.searchCustomerByType(customerType);
						socketOut.writeObject(foundCustomers2);
						break;
						
					default:
						running = false;
						break;
				}
			} catch (ClassNotFoundException | IOException e) {
				System.err.println(e);
			}
		}
		
		try {
			client.close();
			socketIn.close();
			socketOut.close();
			
		} catch (IOException e) {
			System.err.println(e);
		}

	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public ObjectInputStream getSocketIn() {
		return socketIn;
	}

	public void setSocketIn(ObjectInputStream socketIn2) {
		this.socketIn = socketIn2;
	}

	public ObjectOutputStream getSocketOut() {
		return socketOut;
	}

	public void setSocketOut(ObjectOutputStream socketOut2) {
		this.socketOut = socketOut2;
	}

	public CustomerModelController getCustControl() {
		return custControl;
	}

	public void setCustControl(CustomerModelController custControl) {
		this.custControl = custControl;
	}

	public InventoryModelController getInvControl() {
		return invControl;
	}

	public void setInvControl(InventoryModelController invControl) {
		this.invControl = invControl;
	}

	public ShopManager getDb() {
		return db;
	}

	public void setDb(ShopManager db) {
		this.db = db;
	}

}
