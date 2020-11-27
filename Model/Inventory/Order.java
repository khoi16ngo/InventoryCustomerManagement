package Model.Inventory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random; 

/**
 * Order that stores OrderLines that are specified
 * @author Khoi
 * @version 1.0
 * @since October 10, 2020
 *
 */

public class Order implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Number to restock to add to item quantity
	 */
	private static final int RESTOCK = 50;
		
	/**
	 * Order id
	 */
	private int id;
	
	/**
	 * Order date
	 */
	private String date;
	
	/**
	 * List of order lines in a order
	 */
	private ArrayList<OrderLine> orders;	
	
	/**
	 * Construct order
	 * @param items Items to restock
	 */
	public Order(ArrayList<Item> items) {
		ArrayList<OrderLine> list = new ArrayList<OrderLine>();
		for(Item i: items) {
			//for any item, add the restock number to its current item stock
			OrderLine line = new OrderLine(i, RESTOCK - i.getStock());
			list.add(line);
		}
		
		setOrders(list);
		setId(randId());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   	setDate(formatter.format(new Date()));  
	}
	
	private int randId() {
		Random r = new Random( System.currentTimeMillis() );
	    return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
	}

	public Order(int id, String date, ArrayList<OrderLine> lines) {
		setOrders(lines);
		setId(id);
		setDate(date);  
	}

	/**
	 * Add order line 
	 * @param line OrderLine to add
	 */
	public void addOrders(OrderLine line) {
		orders.add(line);
		System.out.println(line + " added to orders.");
	}
	
	/**
	 * Delete order line; must be in list to delete
	 * @param line OrderLine to delete
	 */
	public void deleteOrder(OrderLine line) {
		if(orders.remove(line))
			System.out.println(line + " deleted from orders.");
		else
			System.out.println(line + " not in orders and wasn't deleted.");
	}
	
	/**
	 * Get order lines in order
	 * @return ArrayList of order lines in order
	 */
	public ArrayList<OrderLine> getOrders() {
		return orders;
	}
	
	/**
	 * Set order lines in order
	 * @param orders ArrayList of order lines in order
	 */
	public void setOrders(ArrayList<OrderLine> orders) {
		this.orders = orders;
	}
	
	/**
	 * Get order id
	 * @return Order id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set order id
	 * @param id Order id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Get order date
	 * @return Order date
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * Set order date
	 * @param date2 Order date
	 */
	public void setDate(String date2) {
		this.date = date2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String str = "Order ID: " + id + ", Date: " + date + "\n";
		for(OrderLine line: orders) {
			str += line + "\n";
		}
		return str;
	}
}
