package ServerController.DatabaseController;

import Model.Customers.Commercial;
import Model.Customers.Customer;
import Model.Customers.Residential;
import Model.Inventory.Electrical;
import Model.Inventory.International;
import Model.Inventory.Item;
import Model.Inventory.Local;
import Model.Inventory.NonElectrical;
import Model.Inventory.Order;
import Model.Inventory.OrderLine;
import Model.Inventory.Supplier;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

// Pre-Project Exercise 

// This program allows you to create and manage a store inventory database.
// It creates a database and datatable, then populates that table with tools from
// items.txt.
public class ShopManager {
	
	public Connection con;
	public PreparedStatement preStmnt;
	public String itemFile = "items.txt", suppFile = "suppliers.txt";
	
	// Students should configure these variables for their own MySQL environment
	// If you have not created your first database in mySQL yet, you can leave the 
	// "[DATABASE NAME]" blank to get a connection and create one with the createDB() method.
	public String connectionInfo = "jdbc:mysql://localhost:3306/ensf608",  
				  login          = "root",
				  password       = "password";

	public ShopManager() {
		try {
			// If this throws an error, make sure you have added the mySQL connector JAR to the project
			Class.forName("com.mysql.jdbc.Driver");
			
			// If this fails make sure your connectionInfo and login/password are correct
			con = DriverManager.getConnection(connectionInfo, login, password);
			System.out.println("Connected to: " + connectionInfo + "\n");
		}
		catch(SQLException e) {
			e.printStackTrace(); 
		}
		catch(Exception e) { 
			e.printStackTrace(); 
		}
	}
	
	//-------------------------------------------------------------------------------------------
	//	INSERT ITEMS INTO TOOL TABLE WITH FILE
	//-------------------------------------------------------------------------------------------
	// Fills the data table with all the tools from the text file 'items.txt' if found
	public void fillItemTable() {
		try{
			Scanner sc = new Scanner(new FileReader(itemFile));
			while(sc.hasNext()) {
				String toolInfo[] = sc.nextLine().split(";");
				
				addItem( new Item( Integer.parseInt(toolInfo[0]),
						                            toolInfo[1],
						                            toolInfo[2],
						           Integer.parseInt(toolInfo[3]),
						         Float.parseFloat(toolInfo[4]),
						           Integer.parseInt(toolInfo[5])) );
			}
			sc.close();
		}
		catch(FileNotFoundException e) {
			System.err.println("File " + itemFile + " Not Found!");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//-------------------------------------------------------------------------------------------
	//	INSERT ITEM INTO TOOL TABLE
	//-------------------------------------------------------------------------------------------
	// Add a tool to the tool database table
	public boolean addItem(Item tool) {
		boolean stat = true;
		
		String sql = "INSERT INTO TOOL VALUES (?, ?, ?, ?, ?, ?);";
		try {
			preStmnt = con.prepareStatement(sql);
			preStmnt.setInt(1, tool.getId());
			preStmnt.setString(2, tool.getName());
			preStmnt.setString(3, tool.getType());
			preStmnt.setInt(4, tool.getStock());
			preStmnt.setFloat(5, tool.getPrice());
			preStmnt.setInt(6, tool.getSupplierId());
			
			int i = preStmnt.executeUpdate();  
			System.out.println(i+" records inserted");  
			
		} catch (SQLException e1) {
			stat = false;
		}
		
		if(stat==true && tool instanceof Electrical) {
			stat = addElectricalItem((Electrical) tool);
		}
		
		return stat;
	}
	
	// Add electrical tool to Electrical database table
	public boolean addElectricalItem(Electrical tool) {
		String sql = "INSERT INTO ELECTRICAL VALUES (?, ?);";
		try {
			preStmnt = con.prepareStatement(sql);
			preStmnt.setInt(1, tool.getId());
			preStmnt.setString(2, tool.getPowerType());
			
			int i = preStmnt.executeUpdate();  
			System.out.println(i+" records inserted");  
			
		} catch (SQLException e1) {
			return false;
		}  
		
		return true;
	}
	
	//-------------------------------------------------------------------------------------------
	//	DELETE ITEM IN TOOL TABLE
	//-------------------------------------------------------------------------------------------
	// Delete item from the Tool database table
	public boolean deleteItem(int i) {
		boolean stat = true;
		
		String sql = "DELETE FROM TOOL WHERE TOOLID = ?";
		
		try {
		preStmnt = con.prepareStatement(sql);
		
		preStmnt.setInt(1, i);
		
		int num = preStmnt.executeUpdate();  
		System.out.println(num+" records deleted");  
		
		} catch (SQLException e1) {
			stat = false;
		}
		
		return stat;
	}
	
	//-------------------------------------------------------------------------------------------
	//	GET POWER TYPE FOR ELECTRICAL TOOLS FROM ELECTRICAL TABLE
	//-------------------------------------------------------------------------------------------
	// Returns the string, Power Type, of an electrical item given the id of an electrical item
	// in the Electrical database table
	private String getPowerType(int toolID) {
		String restr = "";
		ResultSet power;
		
		String sql = "SELECT * FROM ELECTRICAL WHERE TOOLID = ?";
		
		try {
			preStmnt = con.prepareStatement(sql);
			preStmnt.setInt(1, toolID);
			power = preStmnt.executeQuery();
			
			if(power.next())	{
				restr = power.getString("POWERTYPE");
			}
		
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		
		return restr;
	}
	
	//-------------------------------------------------------------------------------------------
	//	SELECT ALL ITEMS IN TOOL TABLE
	//-------------------------------------------------------------------------------------------
	// Returns an arraylist of items in the Tool database table
	public ArrayList<Item> getItems(){
		ArrayList<Item> reArr = new ArrayList<Item>();
		ResultSet tool;
		
		String sql = "SELECT * FROM TOOL;";
		
		try {
			preStmnt = con.prepareStatement(sql);
			tool = preStmnt.executeQuery();
			
			while(tool.next()) {
				if(tool.getString("TYPE").equalsIgnoreCase("Electrical"))
					reArr.add(new Electrical(tool.getInt("TOOLID"),
						tool.getString("NAME"), 
						tool.getString("TYPE"), 
						tool.getInt("QUANTITY"), 
						tool.getFloat("PRICE"), 
						tool.getInt("SUPPLIERID"),
						getPowerType(tool.getInt("TOOLID"))));
				else
					reArr.add(new NonElectrical(tool.getInt("TOOLID"),
							tool.getString("NAME"), 
							tool.getString("TYPE"), 
							tool.getInt("QUANTITY"), 
							tool.getFloat("PRICE"), 
							tool.getInt("SUPPLIERID")));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return reArr;
	}
	
	//-------------------------------------------------------------------------------------------
	//	SELECT ALL ITEMS IN TOOL TABLE WHERE NAME CONTAINS GIVEN SEARCHED NAME
	//-------------------------------------------------------------------------------------------
	// Returns an arraylist of items that match the given searched name in the Tool database table
	public ArrayList<Item> searchTool(String toolName) {
		ArrayList<Item> reArr = new ArrayList<Item>();
		ResultSet tool;
		
		String sql = "SELECT * FROM TOOL WHERE NAME LIKE ?;";
		
		try {
			preStmnt = con.prepareStatement(sql);
			preStmnt.setString(1, '%'+toolName+'%');
			tool = preStmnt.executeQuery();
			
			while(tool.next())	{
				if(tool.getString("TYPE").equalsIgnoreCase("Electrical"))
					reArr.add(new Electrical(tool.getInt("TOOLID"),
						tool.getString("NAME"), 
						tool.getString("TYPE"), 
						tool.getInt("QUANTITY"), 
						tool.getFloat("PRICE"), 
						tool.getInt("SUPPLIERID"),
						getPowerType(tool.getInt("TOOLID"))));
				else
					reArr.add(new NonElectrical(tool.getInt("TOOLID"),
							tool.getString("NAME"), 
							tool.getString("TYPE"), 
							tool.getInt("QUANTITY"), 
							tool.getFloat("PRICE"), 
							tool.getInt("SUPPLIERID")));
			}
		
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		
		return reArr;
	}
	
	//-------------------------------------------------------------------------------------------
	//	SELECT ALL ITEMS IN TOOL TABLE WHERE TOOLID EQUALS GIVEN SEARCHED ID
	//-------------------------------------------------------------------------------------------
	// Returns an item that equals the given searched id in the Tool database table
	public Item searchTool(int toolID) {
		ResultSet tool;
		
		String sql = "SELECT * FROM TOOL WHERE TOOLID = ?";
		
		try {
			preStmnt = con.prepareStatement(sql);
			preStmnt.setInt(1, toolID);
			tool = preStmnt.executeQuery();
			
			if(tool.next())	{
				if(tool.getString("TYPE").equalsIgnoreCase("Non-Electrical"))
					return new NonElectrical(tool.getInt("TOOLID"),
						tool.getString("NAME"), 
						tool.getString("TYPE"), 
						tool.getInt("QUANTITY"), 
						tool.getFloat("PRICE"), 
						tool.getInt("SUPPLIERID"));
				else
					return new Electrical(tool.getInt("TOOLID"),
							tool.getString("NAME"), 
							tool.getString("TYPE"), 
							tool.getInt("QUANTITY"), 
							tool.getFloat("PRICE"), 
							tool.getInt("SUPPLIERID"),
							getPowerType(toolID));
			}
		
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		
		return null;
	}
	
	//-------------------------------------------------------------------------------------------
	//	DECREASE ITEM BY QUANTITY
	//-------------------------------------------------------------------------------------------
	// Returns an item that equals the given searched id in the Tool database table
	public boolean decreaseTool(int toolID, int quantity) {
		String sql = "UPDATE TOOL SET QUANTITY = QUANTITY - ? WHERE TOOLID = ?";
		try {
			preStmnt = con.prepareStatement(sql);
			
			preStmnt.setInt(1, quantity);
			preStmnt.setInt(2, toolID);
			
			int i = preStmnt.executeUpdate();  
			System.out.println(i+" records updated");  
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//-------------------------------------------------------------------------------------------
	//	INSERT SUPPLIERS INTO SUPPLIER TABLE WITH FILE
	//-------------------------------------------------------------------------------------------
	// Fills supplier table with info from text file, suppliers.txt
	public void fillSupplierTable()	{
		try {
			Scanner sc = new Scanner(new FileReader(suppFile));
			while(sc.hasNext())
			{
				String suppInfo[] = sc.nextLine().split(";");

				addSupplier( new Supplier( Integer.parseInt(suppInfo[0]),
														suppInfo[1],
														suppInfo[2],
														suppInfo[3],
														suppInfo[4],
														suppInfo[5]) );
			}
			sc.close();
		}
		catch(FileNotFoundException e) {
			System.err.println("File " + suppFile + " Not Found!");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	//-------------------------------------------------------------------------------------------
	//	INSERT SUPPLIER INTO SUPPLIER TABLE
	//-------------------------------------------------------------------------------------------
	// Adds supplier to the Supplier database table
	public void addSupplier(Supplier supp) {
		String sql = "INSERT INTO SUPPLIER VALUES(?, ?, ?, ?, ?, ?);";
		
		try {
			preStmnt = con.prepareStatement(sql);
			
			preStmnt.setInt(1,  supp.getId());
			preStmnt.setString(2,  supp.getCompany());
			preStmnt.setString(3,  supp.getType());
			preStmnt.setString(4,  supp.getAddress());
			preStmnt.setString(5,  supp.getContact());
			preStmnt.setString(6,  supp.getPhone());
			
			int i = preStmnt.executeUpdate();  
			System.out.println(i+" records inserted");  
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	//-------------------------------------------------------------------------------------------
	//	SELECT ALL SUPPLIERS IN SUPPLIER TABLE
	//-------------------------------------------------------------------------------------------
	// Returns an arraylist of items in the Tool database table
	public ArrayList<Supplier> getSuppliers(){
		ArrayList<Supplier> reArr = new ArrayList<Supplier>();
		ResultSet tool;
		
		String sql = "SELECT * FROM SUPPLIER;";
		
		try {
			preStmnt = con.prepareStatement(sql);
			tool = preStmnt.executeQuery();
			
			while(tool.next()) {
				if(tool.getString("TYPE").equalsIgnoreCase("Electrical"))
					reArr.add(new Local(tool.getInt("SUPPLIERID"),
						tool.getString("NAME"), 
						tool.getString("TYPE"), 
						tool.getString("ADDRESS"), 
						tool.getString("CNAME"), 
						tool.getString("PHONE")));
				else
					reArr.add(new International(tool.getInt("SUPPLIERID"),
							tool.getString("NAME"), 
							tool.getString("TYPE"), 
							tool.getString("ADDRESS"), 
							tool.getString("CNAME"), 
							tool.getString("PHONE"),
							getImportTax(tool.getInt("SUPPLIERID"))));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return reArr;
	}
	
	private float getImportTax(int id) {
		float ret = 0;
		
		ResultSet tool;
		
		String sql = "SELECT * FROM INTERNATIONAL WHERE SUPPLIERID = ?;";
		
		try {
			preStmnt = con.prepareStatement(sql);
			preStmnt.setInt(1, id);
			tool = preStmnt.executeQuery();
			
			while(tool.next()) {
				ret = tool.getFloat("IMPORTTAX");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	//-------------------------------------------------------------------------------------------
	//	ADD ORDER TO SUPPLIER_ORDER TABLE AND ORDERLINES FROM ORDER TO ORDERLINE TABLE
	//-------------------------------------------------------------------------------------------
	// Adds order to the Supplier_Order database table
	public void addOrder(Order ord) {
		String sql = "INSERT INTO SUPPLIER_ORDER VALUES(?, ?);";
		
		try {
			preStmnt = con.prepareStatement(sql);
			
			preStmnt.setInt(1, ord.getId());
			preStmnt.setString(2, ord.getDate());
			
			int i = preStmnt.executeUpdate();
			System.out.println(i+" records inserted");  
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		addOrderLine(ord.getId(), ord.getOrders());
	}
	
	// Adds orderlines from order to the OrderLine database table
	private void addOrderLine(int id, ArrayList<OrderLine> orders) {
		int i = 0;
		for(OrderLine ord: orders) {
			String sql = "INSERT INTO ORDERLINE VALUES(?, ?, ?, ?);";
			
			try {
				preStmnt = con.prepareStatement(sql);
				
				preStmnt.setInt(1, id);
				preStmnt.setInt(2, ord.getItem().getId());
				preStmnt.setInt(3, ord.getItem().getSupplierId());
				preStmnt.setInt(4, ord.getRestock());
				
				i += preStmnt.executeUpdate();
				
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println(i+" records inserted");  
	}
		
	//-------------------------------------------------------------------------------------------
	//	GET ORDER GIVE A DATE FROM SUPPLIER_ORDER TABLE AND ORDERLINES FROM ORDERLINE TABLE
	//-------------------------------------------------------------------------------------------
	// Get order given a date from the Supplier_Order database table
	public Order getOrder(String date) {
		ArrayList<OrderLine> lines = new ArrayList<OrderLine>();
		ResultSet ord;
		int id = 0;
		String sql = "SELECT * FROM SUPPLIER_ORDER WHERE DATE = ?;";
		
		try {
			preStmnt = con.prepareStatement(sql);
			preStmnt.setString(1, date);
			ord = preStmnt.executeQuery();
			
			if(ord.next()) {
				id = ord.getInt("ORDERID");
				lines.addAll(getOrderLine(id));
				
			}
			
			if(!lines.isEmpty())
				return new Order(id, date, lines);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//Gets orderlines give order id from the OrderLine database table
	private ArrayList<OrderLine> getOrderLine(int id) {
		ArrayList<OrderLine> lines = new ArrayList<OrderLine>();
		String sql = "SELECT * FROM ORDERLINE WHERE ORDERID =  ?";
		ResultSet line;
		try {
			preStmnt = con.prepareStatement(sql);
			preStmnt.setInt(1, id);
			
			line = preStmnt.executeQuery();
			
			while(line.next()) {
				Item i = searchTool(line.getInt("TOOLID"));
				int restock = line.getInt("QUANTITY");
				lines.add(new OrderLine(i, restock));
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return lines;
	}
	
	//-------------------------------------------------------------------------------------------
	//	INSERT CUSTOMER INTO CUSTOMER TABLE
	//-------------------------------------------------------------------------------------------
	// Adds customer into the Customer database table
	public boolean addCustomer(Customer cust) {
		
		String sql = "INSERT INTO CUSTOMER VALUES(?, ?, ?, ?, ?, ?, ?);";
		
		try {
			preStmnt = con.prepareStatement(sql);
			
			preStmnt.setInt(1, cust.getCustId());
			preStmnt.setString(2, cust.getLastName());
			preStmnt.setString(3, cust.getFirstName());
			preStmnt.setString(4, cust.getType());
			preStmnt.setString(5, cust.getPhone());
			preStmnt.setString(6, cust.getAdr());
			preStmnt.setString(7, cust.getPostal());
			
			int i = preStmnt.executeUpdate();
			System.out.println(i+" records inserted");  
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//-------------------------------------------------------------------------------------------
	//	UPDATE CUSTOMER IN CUSTOMER TABLE
	//-------------------------------------------------------------------------------------------
	// Updates customer information in the Customer database table
	public boolean updateCustomer(Customer cust) {
		
		String sql = "UPDATE CUSTOMER SET LNAME=?, FNAME=?,TYPE=?, PHONE=?, ADDRESS=?, POSTALCODE=? WHERE CUSTOMERID=?;";
		try {
			preStmnt = con.prepareStatement(sql);
			
			preStmnt.setString(1, cust.getLastName());
			preStmnt.setString(2, cust.getFirstName());
			preStmnt.setString(3, cust.getType());
			preStmnt.setString(4, cust.getPhone());
			preStmnt.setString(5, cust.getAdr());
			preStmnt.setString(6, cust.getPostal());
			preStmnt.setInt(7, cust.getCustId());
			
			int i = preStmnt.executeUpdate();  
			System.out.println(i+" records updated");  
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;

	}
	
	//-------------------------------------------------------------------------------------------
	//	DELETE CUSTOMER IN CUSTOMER TABLE
	//-------------------------------------------------------------------------------------------
	// Deletes a customer in the Customer database table
	public boolean deleteCustomer(int id) {
		
		String sql = "DELETE FROM CUSTOMER WHERE CUSTOMERID = ?;";
		
		try {
		preStmnt = con.prepareStatement(sql);
		
		preStmnt.setInt(1, id);
		
		int i = preStmnt.executeUpdate();  
		System.out.println(i+" records deleted");  
		
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//-------------------------------------------------------------------------------------------
	//	INSERT CUSTOMER INTO CUSTOMER TABLE
	//-------------------------------------------------------------------------------------------
	// Adds customer into the Customer database table
	public ArrayList<Customer> getCustomers() {
		ArrayList<Customer> reArr = new ArrayList<Customer>();
		
		ResultSet cust;
		
		String sql = "SELECT * FROM CUSTOMER;";
		
		try {
			preStmnt = con.prepareStatement(sql);
			cust = preStmnt.executeQuery();
			
			while(cust.next())	{
				if(cust.getString("TYPE").equalsIgnoreCase("Commercial"))
					reArr.add(new Commercial(cust.getInt("CUSTOMERID"),
						 cust.getString("LNAME"),
						 cust.getString("FNAME"), 
						 cust.getString("TYPE"), 
						 cust.getString("PHONE"), 
						 cust.getString("ADDRESS"), 
						 cust.getString("POSTALCODE")));
				else
					reArr.add(new Residential(cust.getInt("CUSTOMERID"),
							 cust.getString("LNAME"),
							 cust.getString("FNAME"), 
							 cust.getString("TYPE"), 
							 cust.getString("PHONE"), 
							 cust.getString("ADDRESS"), 
							 cust.getString("POSTALCODE")));
			}
		
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		
		return reArr;

	}
	//-------------------------------------------------------------------------------------------
	//	SEARCH FOR CUSTOMER INFORMATION GIVEN CUSTOMER ID IN CUSTOMER TABLE
	//-------------------------------------------------------------------------------------------
	// Returns a customer if the given searched id equals in the Customer database table
	public Customer searchCustomerById(int id) {
		
		ResultSet cust;
		
		String sql = "SELECT * FROM CUSTOMER WHERE CUSTOMERID = ?;";
		
		try {
			preStmnt = con.prepareStatement(sql);
			preStmnt.setInt(1, id);
			cust = preStmnt.executeQuery();
			
			if(cust.next())	{
				if(cust.getString("TYPE").equalsIgnoreCase("Commercial"))
					return new Commercial(cust.getInt("CUSTOMERID"),
						 cust.getString("LNAME"),
						 cust.getString("FNAME"), 
						 cust.getString("TYPE"), 
						 cust.getString("PHONE"), 
						 cust.getString("ADDRESS"), 
						 cust.getString("POSTALCODE"));
				else
					return new Residential(cust.getInt("CUSTOMERID"),
							 cust.getString("LNAME"),
							 cust.getString("FNAME"), 
							 cust.getString("TYPE"), 
							 cust.getString("PHONE"), 
							 cust.getString("ADDRESS"), 
							 cust.getString("POSTALCODE"));
			}
		
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		
		return null;
		
	}
	
	//-------------------------------------------------------------------------------------------
	//	SEARCH FOR CUSTOMER INFORMATION GIVEN CUSTOMER NAME IN CUSTOMER TABLE
	//-------------------------------------------------------------------------------------------
	// Returns an arraylist of customers if the given searched name contains in the first name 
	// or last name in the Customer database table
	public ArrayList<Customer> searchCustomerByName(String name) {
		ArrayList<Customer> reArr = new ArrayList<Customer>();
		
		ResultSet cust;
		
		String sql = "SELECT * FROM CUSTOMER WHERE LNAME LIKE ? OR FNAME LIKE ?;";
		
		try {
			preStmnt = con.prepareStatement(sql);
			preStmnt.setString(1, "%"+name+"%");
			preStmnt.setString(2, "%"+name+"%");
			cust = preStmnt.executeQuery();
			
			while(cust.next())	{
				if(cust.getString("TYPE").equalsIgnoreCase("Commercial"))
					reArr.add(new Commercial(cust.getInt("CUSTOMERID"),
						 cust.getString("LNAME"),
						 cust.getString("FNAME"), 
						 cust.getString("TYPE"), 
						 cust.getString("PHONE"), 
						 cust.getString("ADDRESS"), 
						 cust.getString("POSTALCODE")));
				else
					reArr.add(new Residential(cust.getInt("CUSTOMERID"),
							 cust.getString("LNAME"),
							 cust.getString("FNAME"), 
							 cust.getString("TYPE"), 
							 cust.getString("PHONE"), 
							 cust.getString("ADDRESS"), 
							 cust.getString("POSTALCODE")));
			}
		
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		
		return reArr;

	}
	
	//-------------------------------------------------------------------------------------------
	//	SEARCH FOR CUSTOMER INFORMATION GIVEN CUSTOMER TYPE IN CUSTOMER TABLE
	//-------------------------------------------------------------------------------------------
	// Returns an arraylist of customers given type in the Customer database table
	public ArrayList<Customer> searchCustomerByType(String type) {
		ArrayList<Customer> reArr = new ArrayList<Customer>();
		
		ResultSet cust;
		
		String sql = "SELECT * FROM CUSTOMER WHERE TYPE = ?;";
		
		try {
			preStmnt = con.prepareStatement(sql);
			preStmnt.setString(1, type);
			cust = preStmnt.executeQuery();
			
			while(cust.next())	{
				if(type.equalsIgnoreCase("Commercial"))
					reArr.add(new Commercial(cust.getInt("CUSTOMERID"),
						 cust.getString("LNAME"),
						 cust.getString("FNAME"), 
						 cust.getString("TYPE"), 
						 cust.getString("PHONE"), 
						 cust.getString("ADDRESS"), 
						 cust.getString("POSTALCODE")));
				else
					reArr.add(new Residential(cust.getInt("CUSTOMERID"),
							 cust.getString("LNAME"),
							 cust.getString("FNAME"), 
							 cust.getString("TYPE"), 
							 cust.getString("PHONE"), 
							 cust.getString("ADDRESS"), 
							 cust.getString("POSTALCODE")));
			}
		
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		
		return reArr;

	}
	
	//-------------------------------------------------------------------------------------------
	//	CLOSE PREPARED STATEMENT AND JDBC CONNECTION
	//-------------------------------------------------------------------------------------------
	public void closeConnection() {
		try {
			preStmnt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//-------------------------------------------------------------------------------------------
	//	PRINT ARRAY LIST CONTENTS
	//-------------------------------------------------------------------------------------------
	public void printItemList(ArrayList<Item> list) {
		for(Item i: list) {
			System.out.println(i);
			
			if(i instanceof Electrical)
				System.out.println("Electrical => " + ((Electrical)i).getPowerType());
		}
	}
	
	public void printSupplierList(ArrayList<Supplier> list) {
		for(Supplier i: list) {
			System.out.println(i);
		}
	}
	
	public void printCustomerList(ArrayList<Customer> list) {
		for(Customer i: list) {
			System.out.println(i);
		}
	}
	
	
	public static void main(String args[]) {
		ShopManager inventory = new ShopManager();
		
		System.out.println(inventory.deleteItem(1050));
//		System.out.println(inventory.decreaseTool(1,5));
//		System.out.println("\nTest Order");
//		System.out.println(inventory.getOrder("2020-03-30"));
//		System.out.println("\nTest Add Tool by");
//		Electrical tool = new Electrical(285, "sdsfsdfsd", "Electrical", 28, (float) 55.55, 6, "AC POwer");
//		System.out.println(inventory.addItem(tool));
//		System.out.println("\nTest Search Tool by ID");
//		System.out.println(inventory.searchTool(14));
//		System.out.println("\nTest Search Tool by Name");
//		inventory.printItemList(inventory.searchTool("a"));
//		System.out.println("\nTest Search Customer by ID");
//		System.out.println(inventory.searchCustomerById(1));
//		System.out.println("\nTest Search Customer by Name");
//		inventory.printCustomerList(inventory.searchCustomerByName("lebron"));
//		System.out.println("\nTest Search Customer by Type");
//		inventory.printCustomerList(inventory.searchCustomerByType("C"));
//		
		//System.out.println("\nFilling the Supplier table with suppliers");
		//inventory.fillSupplierTable();
	
		//System.out.println("\nFilling the Tool table with tools");
		//inventory.fillItemTable();
		
//		System.out.println("Reading all tools from the table:");
//		inventory.printItemList(inventory.getItems());
//
//		System.out.println("\nSearching table for tool 1002: should return 'Grommets'");
//		int toolID = 1002;
//		Item searchResult = inventory.searchTool(toolID);
//		if(searchResult == null)
//			System.out.println("Search Failed to find a tool matching ID: " + toolID);
//		else
//			System.out.println("Search Result: " + searchResult.toString());
//
//		System.out.println("\nSearching table for tool 9000: should fail to find a tool");
//		toolID = 9000;
//		searchResult = inventory.searchTool(toolID);
//		if(searchResult == null)
//			System.out.println("Search Failed to find a tool matching ID: " + toolID);
//		else
//			System.out.println("Search Result: " + searchResult.toString());
		
		inventory.closeConnection();
	}
	
}