package Model.Inventory;

import java.io.Serializable;

/**
 * Stores information about item in inventory
 * @author Khoi
 * @version 1.0
 * @since October 10, 2020
 *
 */

public class Item implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Item's id
	 */
	private int id;
	
	/**
	 * Item's name
	 */
	private String name;
	
	private String type;
	/**
	 * Item's stock/quantity
	 */
	private int stock;
	
	/**
	 * Item's price
	 */
	private float price;
	
	/**
	 * Item's supplier's id
	 */
	private int supplierId;
	
	/**
	 * Constructs the item 
	 * @param id Item id
	 * @param name Item name
	 * @param stock Item stock/quantity
	 * @param price Item price
	 * @param supplierId Item supplier id
	 */
	public Item(int id, String name, String type, int stock, float price, int supplierId) {
		setId(id);
		setName(name);
		setType(type);
		setStock(stock);
		setPrice(price);
		setSupplierId(supplierId);
	}
	
	/**
	 * Decrease item stock by one
	 */
	public void decreaseItem() {
		stock--;
	}
	
	/**
	 * Get the item id 
	 * @return Item id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set the item id
	 * @param id Item id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Set the item name
	 * @return Item name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Get the item name
	 * @param name Item name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the item stock/quantity
	 * @return Item stock 
	 */
	public int getStock() {
		return stock;
	}
	
	/**
	 * Set the item stock/quantity
	 * @param stock Item stock
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	/**
	 * Get the item price
	 * @return Item price
	 */
	public float getPrice() {
		return price;
	}
	
	/**
	 * Set the item price
	 * @param price Item price
	 */
	public void setPrice(float price) {
		this.price = price;
	}
	
	/**
	 * Get the item supplier id
	 * @return Supplier id
	 */
	public int getSupplierId() {
		return supplierId;
	}
	
	/**
	 * Set the item supplier id
	 * @param supplierId Supplier id
	 */
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "[" + id + "] " + name + "  \tPrice: " + price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
