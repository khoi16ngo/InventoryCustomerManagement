package ServerController;

import java.util.ArrayList;

import Model.Shop;
import Model.Customers.Customer;
import ServerController.DatabaseController.ShopManager;

public class CustomerModelController {
	
	private ShopManager db;
	private Shop myShop;
	
	public CustomerModelController(ShopManager db, Shop myShop) {
		setDb(db);
		setMyShop(myShop);
	}
	
	public boolean addCustomer(Customer c) {
		myShop.getCust().addCustomer(c);
		return db.addCustomer(c);
	}
	
	public boolean modifyCustomer(Customer c) {
		myShop.getCust().updateCustomer(c);
		return db.updateCustomer(c);
	}
	
	public boolean deleteCustomer(int id) {
		myShop.getCust().deleteCustomer(id);
		return db.deleteCustomer(id);
	}
	
	public Customer searchCustomerById(int id) {
		return db.searchCustomerById(id);
	}
	
	public ArrayList<Customer> searchCustomerByName(String name) {
		return db.searchCustomerByName(name);
	}
	
	public ArrayList<Customer> searchCustomerByType(String type) {
		return db.searchCustomerByType(type);
	}
	
	public ShopManager getDb() {
		return db;
	}

	public void setDb(ShopManager db) {
		this.db = db;
	}
	
	public Shop getMyShop() {
		return myShop;
	}
	
	public void setMyShop(Shop myShop) {
		this.myShop = myShop;
	}
}
