package Model;

import Model.Customers.CustomerList;
import Model.Inventory.Inventory;
import Model.Inventory.SupplierList;

/**
 * Backend that will call methods and fields to execute user's demand 
 * @author Khoi
 * @version 1.0
 * @since October 10, 2020
 *
 */

public class Shop {
	
	/**
	 * Shop's inventory
	 */
	private Inventory inv;
	
	/**
	 * Shop's supplier list
	 */
	private SupplierList sup;
	
	private CustomerList cust;
	/**
	 * Constructs the shop given files to create it
	 * @param files FileMgr with shop's files
	 * @throws FileNotFoundException
	 */
	public Shop(Inventory inv, SupplierList sup, CustomerList cust) {
		setInv(inv);
		setSup(sup);
		setCust(cust);
	}
		
	/**
	 * Get shop inventory
	 * @return Inventory
	 */
	public Inventory getInv() {
		return inv;
	}
	
	/**
	 * Set shop inventory
	 * @param inv Inventory
	 */
	public void setInv(Inventory inv) {
		this.inv = inv;
	}
	
	/**
	 * Get shop supplier list
	 * @return SupplierList
	 */
	public SupplierList getSup() {
		return sup;
	}
	
	/**
	 * Set shop supplier list
	 * @param sup SupplierList
	 */
	public void setSup(SupplierList sup) {
		this.sup = sup;
	}


	public CustomerList getCust() {
		return cust;
	}


	public void setCust(CustomerList cust) {
		this.cust = cust;
	}	
}
