package Model.Inventory;

import java.util.ArrayList;

/**
 * Takes care of items and orders with methods and fields
 * @author Khoi
 * @version 1.0
 * @since October 10, 2020
 *
 */
public class Inventory {
	
	/**
	 * Number when items go below will be restocked
	 */
	private static final int ITEM_COUNT = 40;
	
	/**
	 * List of items in inventory
	 */
	private ArrayList<Item> list;
	
	/**
	 * List of orders in inventory
	 */
	private ArrayList<Order> orders;
	
	/**
	 * Constructs new inventory with items given
	 * @param list ArrayList of items to stock
	 */
	public Inventory (ArrayList<Item> list) {
		setList(list);
		orders = new ArrayList<Order>();
	}
	
	/**
	 * Adds an item into the item list
	 * @param i Item to add
	 */
	public void addItem(Item i) {
		list.add(i);
		System.out.println(i + " added to inventory.");
	}
	
	/**
	 * Deletes an item from the item list, must be in inventory to delete
	 * @param i Item to delete
	 */
	public void deleteItem(int id) {
		int index = 0;
		for(Item i: list) {
			if(i.getId() == id)
				break;
			index++;
		}
		//check to see if item deleted
		if(index != list.size()) {
			list.remove(index);
			System.out.println(index + " deleted from inventory.");
		}
		else
			System.out.println("Not in inventory and wasn't deleted.");
	}
	
	/**
	 * User buys an item, decreases item's stock by one
	 * @param i Item to buy
	 */
	public void decreaseItem(Item i) {
		i.decreaseItem();
	}
	
	/**
	 * Displays all items in inventory
	 */
	public void listItems() {
		System.out.println("List of Inventory Items: ");
		for(Item i: list)
			System.out.println(i);
	}
	
	/**
	 * Displays items with their quantites available; if stock, is 0 don't display
	 */
	public void listQuantity() {
		System.out.println("List of Inventory Items In Stock:");
		for(Item i: list) {
			if(i.getStock() != 0)
				System.out.println(i + " \tQuantity: " + i.getStock());
		}
			
	}
	
	/**
	 * Search for item in inventory by name; if name is in multiple item names, display all items with name in it
	 * @param name Item name to search
	 * @return ArrayList of items with name in it
	 */
	public ArrayList<Item> searchItem(String name) {
		ArrayList<Item> i = new ArrayList<Item>();
		
		//loop through item list for name
		for(Item inventoryItem : list) {
			//makes both the item's name and the search name both lower case to compare better
			if(inventoryItem.getName().toLowerCase().contains(name.toLowerCase()))
				i.add(inventoryItem);
		}
		
		//if name matches any items, notify user 
		if(!i.isEmpty()) {
			System.out.println("Found "+ name + " in inventory.");
			for(Item found: i)
				System.out.println(found);
		}
		else
			System.out.println("Did not find "+ name + " in inventory.");
		
		return i;
	}
	
	/**
	 * Search for item in inventory by id
	 * @param searchId Item id
	 * @return Item if id matches with one in inventory
	 */
	public Item searchItem(int searchId) {
		Item i = null;
		
		for(Item inventoryItem : list) {
			if(inventoryItem.getId() == searchId) {
				i = inventoryItem;
				break;
			}
		}
		
		if(i != null) {
			System.out.println("Found Item ID ["+ searchId + "] in inventory.");
			System.out.println(i);
		}
		else
			System.out.println("Did not find Item ID ["+ searchId + "] in inventory.");
		
		return i;
	}
	
	/**
	 * Check inventory to see if item stocks are above ITEM COUNT, if below create an order to restock
	 */
	public boolean checkInventory() {
		//check whether need a restock or not
		boolean stat = false;
		ArrayList<Item> restockItems = new ArrayList<Item>();
		
		for(Item inventoryItem : list) {
			//any items below item count will create their own order line
			if(inventoryItem.getStock() < ITEM_COUNT) {
				stat = true;
				restockItems.add(inventoryItem);
			}
		}
		
		//if any order lines created, create an order; add to inventory orders list
		if(stat) {
			Order newOrder = new Order(restockItems);
			orders.add(newOrder);
		}
		
		return stat;
	}
	
	/**
	 * Get the inventory item lsit
	 * @return Item list
	 */
	public ArrayList<Item> getList() {
		return list;
	}
	
	/**
	 * Set the inventory item list
	 * @param list Item list
	 */
	public void setList(ArrayList<Item> list) {
		this.list = list;
	}
	
	/**
	 * Get the inventory order list
	 * @return
	 */
	public ArrayList<Order> getOrders(){
		return orders;
	}
	
	/**
	 * Set the inventory order list
	 * @param orders Order List
	 */
	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}
}
