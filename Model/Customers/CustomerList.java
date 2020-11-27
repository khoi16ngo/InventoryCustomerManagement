package Model.Customers;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * List fo supplier of store
	 */
	private ArrayList<Customer> list;
	
	/**
	 * Constructs the supplier list of inventory
	 * @param list Supplier List
	 */
	public CustomerList(ArrayList<Customer> list) {
		setList(list);
	}
	
	/**
	 * Adds a supplier to list
	 * @param s Supplier to add
	 */
	public void addCustomer(Customer cust) {
		list.add(cust);
	}
	
	public void updateCustomer(Customer c) {
		int index = getCustomerIndex(c.getCustId());
		
		if(index != -1){
			list.remove(index);
			list.add(c);
		}
	}
	
	public int getCustomerIndex(int id) {
		int index = -1;
		for(Customer c: list) {
			index++;
			if(c.getCustId() == id)
				return index;
		}
		return index;
	}

	/**
	 * Deletes a supplier in list; supplier must be found in list to be deleted
	 * @param s Supplier to delete
	 */
	public void deleteCustomer(int id) {
		//remove Customer from list
		if(list.remove(getCustomerIndex(id)) != null)
			//notify if deleted
			System.out.println(id + " deleted from Customer list.");
		else
			//notify if not deleted
			System.out.println(id + " not in Customer list and wasn't deleted.");
	}
	
	/**
	 * Gets the supplier list of the store
	 * @return List of suppliers
	 */
	public ArrayList<Customer> getList() {
		return list;
	}
	
	/**
	 * Set the supplier list of the store
	 * @param list List of suppliers
	 */
	public void setList(ArrayList<Customer> list) {
		this.list = list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String str = "Customers List:\n";
		for(Customer cust: list) {
			str += cust + "\n";
		}
		return str;
	}

}
