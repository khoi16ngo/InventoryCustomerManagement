package Model.Inventory;

import java.io.Serializable;

/**
 * Information about an order line within the fields 
 * @author Khoi
 * @version 1.0
 * @since October 10, 2020
 *
 */

public class OrderLine implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Item to order
	 */
	private Item i;
	
	/**
	 * Number to restock item's quantity
	 */
	private int restock;
	
	/**
	 * Constructs order line
	 * @param i Item to order
	 * @param restock Number to restock
	 */
	public OrderLine(Item i, int restock) {
		setItem(i);
		setRestock(restock);
	}
	
	/**
	 * Get item that's ordered
	 * @return Item ordered
	 */
	public Item getItem() {
		return i;
	}
	
	/**
	 * Set item to order
	 * @param i Item to order
	 */
	public void setItem(Item i) {
		this.i = i;
	}
	
	/**
	 * Get restock number
	 * @return Restock number
	 */
	public int getRestock() {
		return restock;
	}
	
	/**
	 * Set restock number
	 * @param restock Restock number
	 */
	public void setRestock(int restock) {
		this.restock = restock;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return i + " \tRestock: " + restock;
	}
}
