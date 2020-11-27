package ServerController;

import java.util.ArrayList;

import Model.Shop;
import Model.Inventory.Item;
import Model.Inventory.Order;
import ServerController.DatabaseController.ShopManager;

public class InventoryModelController {
	
	private ShopManager db;
	private Shop myShop;
	
	public InventoryModelController(ShopManager db, Shop myShop) {
		setDb(db);
		setMyShop(myShop);
	}
	
	public boolean addItem(Item i) {
		myShop.getInv().addItem(i);
		return db.addItem(i);
	}
	
	public boolean deleteItem(int i) {
		myShop.getInv().deleteItem(i);
		return db.deleteItem(i);
	}
	
	public Item searchItemById(int id) {
		return db.searchTool(id);
	}
	
	public ArrayList<Item> searchItemByName(String name){
		return db.searchTool(name);
	}
	
	public ArrayList<Item> checkInventory() {
		if(myShop.getInv().checkInventory()) {
			int index = myShop.getInv().getOrders().size();
			Order ord = myShop.getInv().getOrders().get(index - 1);
			db.addOrder(ord);
		}
		return db.getItems();
	}
	
	public Order getOrder(String date) {
		return db.getOrder(date);
	}
	

	public boolean decreaseTool(int decreaseId, int decreaseQuant) {
		return db.decreaseTool(decreaseId, decreaseQuant);
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
