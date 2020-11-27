package ClientController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Model.Inventory.Electrical;
import Model.Inventory.Item;
import Model.Inventory.NonElectrical;
import Model.Inventory.Order;
import Model.Inventory.OrderLine;
import View.InventoryView;

public class InventoryViewController {
	private InventoryView view;
	private ClientController client;
	
	public InventoryViewController(InventoryView view2, ClientController client2) {
		view = view2;
		client = client2;
		
		//Add listeners to view
				view.addSearchListener(new SearchListener());
				view.addDeleteListener(new DeleteListener());
				view.addAddListener(new AddListener());
				view.addClearListener(new ClearListener());
				view.addQuitListener(new QuitListener());
				view.addCheckListener(new CheckListener());
				view.addOrderListener(new OrderListener());
				view.addSearchListListener(new SearchListListener());
				view.addDecreaseListener(new DecreaseListener());
				view.addItemChangeListener(new ItemChangeListener());
	}
	
	class SearchListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Item> itemStrings = new ArrayList<Item>();
			String searchType = view.getSearchTypeField();
			if(searchType == null)
				view.showError("Please select a search by type!");
			else {
				String searchText = view.getSearchText();
				
				if(searchType.equalsIgnoreCase("id")) {
					if(isInteger(searchText)) {
						int id = Integer.parseInt(searchText);
						Item reItem = client.searchToolById(id);
						if(reItem != null)
							itemStrings.add(reItem);
						else
							view.showError("Tool \'" + id + "\' not found!");
					}
					else
						view.showError("Please insert a valid number in search field!");
				}
				else if(searchType.equalsIgnoreCase("name")) {
					ArrayList<Item> reItems = client.searchToolByName(searchText);
					if(!reItems.isEmpty())
						itemStrings.addAll(reItems);
					else
						view.showError("Tools named \'" + searchText + "\' not found!");
					
				}
				
				view.getListModel().clear();
				String restr = "Id, Name, Type, Price, Stock, SupplierId, PowerType";
				view.getListModel().addElement(restr);
				for(Item i: itemStrings) {
					String str = "";
					str += i.getId() + ", ";
					str += i.getName() + ", ";
					str += i.getType() + ", ";
					str += i.getPrice() + ", ";
					str += i.getStock() + ", ";
					str += i.getSupplierId();
					if(i instanceof Electrical)
						str +=  ", " + ((Electrical) i).getPowerType();
					
					view.getListModel().addElement(str);
				}
					
			}
		}
	}
	
	class DeleteListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String deleteId = view.getToolIdText();
			if(isInteger(deleteId)) {
				int id = Integer.parseInt(deleteId);
				if(client.deleteTool(id))
					view.showError("Deleted tool id " + id);
				else
					view.showError("Did not delete tool id " + id);
			}
			else
				view.showError("Please insert a valid number in tool id field!");
		}
	}
	
	class AddListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String addId = view.getToolIdText();
			if(isInteger(addId)) {
				int id = Integer.parseInt(addId);
				String name = view.getToolNameText();
				String quantity = view.getStockText();
				if(isInteger(quantity)) {
					int stock = Integer.parseInt(quantity);
					String price = view.getPriceText();
					if(isFloat(price)) {
						float pri = Float.parseFloat(price);
						String supplier = view.getSupplierText();
						if(isInteger(supplier)) {
							int supp = Integer.parseInt(supplier);
							String type = view.getToolTypeText();
							Item addItem;
							if(type.equalsIgnoreCase("Electrical")) {
								String power = view.getPowerText();
								
								if(power.length() > 20)
									view.showError("Power Type too long!");
								else {
									addItem = new Electrical(id, name, type, stock, pri, supp, power);
									if(client.addTool(addItem))
										view.showError("Added tool id " + id);
									else
										view.showError("Did not add tool id " + id);
								}
							}
							else if (type.equalsIgnoreCase("Non-Electrical")) {
								addItem = new NonElectrical(id, name, type, stock, pri, supp);
								if(client.addTool(addItem))
									view.showError("Added tool id " + id);
								else
									view.showError("Did not add tool id " + id);
							}
							else {
								addItem = new Item(id, name, type, stock, pri, supp);
								if(client.addTool(addItem))
									view.showError("Added tool id " + id);
								else
									view.showError("Did not add tool id " + id);
							}
						}
						else
							view.showError("Please insert a valid number in supplier field!");
					}
					else
						view.showError("Please insert a valid float number in price field!");
				}
				else
					view.showError("Please insert a valid number in quantity field!");
				
			}
			else
				view.showError("Please insert a valid number in tool id field!");
		}
	}
	
	class ClearListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.getToolIdField().setText("");
			view.getToolIdField().setEnabled(true);
			
			view.getToolNameField().setText("");
			view.getToolNameField().setEnabled(true);
			
			view.getStockField().setText("");
			view.getStockField().setEnabled(true);
			
			view.getPriceField().setText("");
			view.getPriceField().setEnabled(true);
			
			view.getSupplierField().setText("");
			view.getSupplierField().setEnabled(true);
			
			view.getPowerField().setText("");
			view.getPowerField().setEnabled(true);
			
			view.getToolTypeField().setEnabled(true);
		}
	}
	
	class QuitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			client.closeClient();
			SwingUtilities.getWindowAncestor(view).dispose();
		}
	}
	
	class CheckListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Item> itemStrings = new ArrayList<Item>();
			ArrayList<Item> reItems = client.checkInventory();
			if(!reItems.isEmpty())
				itemStrings.addAll(reItems);
			else
				view.showError("Tool inventory empty!");
				
		
			view.getListModel().clear();
			String restr = "Id, Name, Type, Price, Stock, SupplierId, (Optional)PowerType";
			view.getListModel().addElement(restr);
			for(Item i: itemStrings) {
				String str = "";
				str += i.getId() + ", ";
				str += i.getName() + ", ";
				str += i.getType() + ", ";
				str += i.getPrice() + ", ";
				str += i.getStock() + ", ";
				str += i.getSupplierId();
				if(i instanceof Electrical)
					str +=  ", " + ((Electrical) i).getPowerType();
				
				view.getListModel().addElement(str);
			}
		}
	}
	
	class OrderListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String date = view.getOrderText();
			String regex = "^\\d\\d\\d\\d-\\d\\d-\\d\\d$";
			if(Pattern.matches(regex, date)) {
				Order ord = client.searchOrderByDate(date);
				if(ord == null)
					view.showError("Order \'" + date + "\' does not exist!");
				else {
					writeFileOrder(ord);
					view.showError("Order \'" + date + "\' printed!");
				}
			}
			else
				view.showError("Please enter a valid date in form yyyy-MM-dd!");
		}
	}
	
	class SearchListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			String value = view.getSearchList().getSelectedValue();
			if(value != null) {
				String[] arrVal = value.split(",\\s+");
				view.getToolIdField().setText(arrVal[0]);
				if(view.getToolIdField().isEnabled())
					view.getToolIdField().setEnabled(false);
				
	    		view.getToolNameField().setText(arrVal[1]);
	    		if(view.getToolNameField().isEnabled())
	    			view.getToolNameField().setEnabled(false);
	    		
	    		if(arrVal.length == 7) {
	    			view.getToolTypeField().setSelectedIndex(0);
	    			view.getPowerField().setText(arrVal[6]);
	    		}
	    		else {
	    			view.getToolTypeField().setSelectedIndex(1);
	    			view.getPowerField().setText("");
	    		}
	    		if(view.getToolTypeField().isEnabled())
	    			view.getToolTypeField().setEnabled(false);
	    		if(view.getPowerField().isEnabled())
	    			view.getPowerField().setEnabled(false);
	    		
	    		view.getPriceField().setText(arrVal[3]);
	    		if(view.getPriceField().isEnabled())
	    			view.getPriceField().setEnabled(false);
	    		
	    		view.getStockField().setText(arrVal[4]);
	    		if(view.getStockField().isEnabled())
	    			view.getStockField().setEnabled(false);
	    		
	    		view.getSupplierField().setText(arrVal[5]);
	    		if(view.getSupplierField().isEnabled())
	    			view.getSupplierField().setEnabled(false);
			}
		}
	}
	
	class ItemChangeListener implements ItemListener{
	    @Override
	    public void itemStateChanged(ItemEvent event) {
	       if (event.getStateChange() == ItemEvent.SELECTED) {
	          String item = (String) event.getItem();
	          if(item.equalsIgnoreCase("Electrical"))
	        	  view.getPowerField().setEnabled(true);
	          else {
	        	  view.getPowerField().setEnabled(false);
	          }
	       }
	    }       
	}
	
	class DecreaseListener implements ActionListener{
	    @Override
	    public void actionPerformed(ActionEvent event) {
	    	String result = (String)JOptionPane.showInputDialog(view, "Decrease Quantity By:", "Decrease Tool Quantity", JOptionPane.PLAIN_MESSAGE, null, null, "1");
			if(result != null && result.length() > 0 && isInteger(result)){
				String id = view.getToolIdText();
			   	if(isInteger(id)) {
				   client.decreaseTool(Integer.parseInt(id), Integer.parseInt(result));
			   	}
			   else
				   view.showError("Please insert a valid number in tool id field!");
			}
	       
	    }       
	}
	
	private boolean isInteger(String s) {
		boolean isValidInteger = false;
		
	    try {
	    	Integer.parseInt(s);
	         isValidInteger = true;
	    } catch (NumberFormatException ex) {
	         // s is not an integer
	    }
	 
	    return isValidInteger;
	}
	private boolean isFloat(String s) {
		boolean isValidFloat = false;
		
	    try {
	    	Float.parseFloat(s);
	    	isValidFloat = true;
	    } catch (NumberFormatException ex) {
	         // s is not a float
	    }
	 
	    return isValidFloat;
	}
	private void writeFileOrder(Order ord) {
		String[] date = ord.getDate().split("-");
		String fileName = "Order_"+ord.getId()+"_"+date[0]+"_"+date[1]+"_"+date[2]+".txt";
		createFile(fileName);
        PrintWriter out;
		try {
			out = new PrintWriter(fileName);
			
	        out.println("ORDER [" + ord.getId() + "], Date: " + ord.getDate());
	        
	        for(OrderLine line: ord.getOrders()) 
	        	out.println(line.getItem() + " => RESTOCK: " + line.getRestock());

	        out.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			view.showError("Error in printing order!");
		}
	}
	
	private void createFile(String fileName) {
		try {
		      File myObj = new File(fileName);
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
}
