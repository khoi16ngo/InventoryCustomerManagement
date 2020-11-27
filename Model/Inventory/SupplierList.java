package Model.Inventory;

import java.util.ArrayList;

/**
 * Supplier list that stores inventory items' supplier
 * @author Khoi
 * @version 1.0
 * @since October 10, 2020
 *
 */
public class SupplierList {

	/**
	 * List fo supplier of store
	 */
	private ArrayList<Supplier> list;
	
	/**
	 * Constructs the supplier list of inventory
	 * @param list Supplier List
	 */
	public SupplierList(ArrayList<Supplier> list) {
		setList(list);
	}
	
	/**
	 * Adds a supplier to list
	 * @param s Supplier to add
	 */
	public void addSupplier(Supplier s) {
		list.add(s);
	}
	
	/**
	 * Deletes a supplier in list; supplier must be found in list to be deleted
	 * @param s Supplier to delete
	 */
	public void deleteSupplier(Supplier s) {
		//remove supplier from list
		if(list.remove(s))
			//notify if deleted
			System.out.println(s + " deleted from supplier list.");
		else
			//notify if not deleted
			System.out.println(s + " not in supplier list and wasn't deleted.");
	}
	
	/**
	 * Gets the supplier list of the store
	 * @return List of suppliers
	 */
	public ArrayList<Supplier> getList() {
		return list;
	}
	
	/**
	 * Set the supplier list of the store
	 * @param list List of suppliers
	 */
	public void setList(ArrayList<Supplier> list) {
		this.list = list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String str = "Suppliers List:\n";
		for(Supplier s: list) {
			str += s + "\n";
		}
		return str;
	}
}
