package ClientController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Model.Customers.Commercial;
import Model.Customers.Customer;
import Model.Customers.Residential;
import View.CustomerView;

public class CustomerViewController{
	
	private CustomerView view;
	private ClientController client;
	
	public CustomerViewController(CustomerView view1, ClientController client1) {
		view = view1;
		client = client1;
		
		//Add listeners to view
		view.addSearchListener(new SearchListener());
		view.addDeleteListener(new DeleteListener());
		view.addModifyListener(new ModifyListener());
		view.addAddListener(new AddListener());
		view.addQuitListener(new QuitListener());
		view.addSearchListListener(new SearchSelectListener());
		view.addClearListener(new ClearListener());
	}
	
	//---------------------------------------------------------------------------------------
    // FOR SEARCHING CUSTOMERS BY ID, NAME, OR TYPE
    //---------------------------------------------------------------------------------------
	class SearchListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Customer> custStrings = new ArrayList<Customer>();
			String searchType = view.getSearchType();
			if(searchType == null)
				view.showError("Please select a search by type!");
			else {
				String searchText = view.getSearchText();
				
				if(searchType.equalsIgnoreCase("id")) {
					if(isInteger(searchText)) {
						int id = Integer.parseInt(searchText);
						Customer reCust = client.searchCustomerById(id);
						if(reCust != null)
							custStrings.add(reCust);
						else
							view.showError("Customer \'" + id + "\' not found!");
					}
					else
						view.showError("Please insert a valid number in search field!");
				}
				else if(searchType.equalsIgnoreCase("name")) {
					ArrayList<Customer> reCusts = client.searchCustomerByName(searchText);
					if(!reCusts.isEmpty())
						custStrings.addAll(reCusts);
					else
						view.showError("Customers named \'" + searchText + "\' not found!");
					
				}
				else if(searchType.equalsIgnoreCase("type")) {
					ArrayList<Customer> reCusts = client.searchCustomerByType(searchText);
					if(!reCusts.isEmpty())
						custStrings.addAll(reCusts);
					else
						view.showError("Customer type \'" + searchText + "\' not found!");
				}
				
				view.getListModel().clear();
				String restr = "Id, FirstName, LastName, Type, Phone, Address, PostalCode";
				view.getListModel().addElement(restr);
				for(Customer c: custStrings) {
					String str = "";
					str += c.getCustId() + ", ";
					str += c.getFirstName() + ", ";
					str += c.getLastName() + ", ";
					str += c.getType() + ", ";
					str += c.getPhone() + ", ";
					str += c.getAdr() + ", ";
					str += c.getPostal();
					
					view.getListModel().addElement(str);
				}
					
			}
			
		}
		
	}
	
	//---------------------------------------------------------------------------------------
    // FOR DELETING CUSTOMER
    //---------------------------------------------------------------------------------------
	class DeleteListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String deleteId = view.getCustomerIdText();
			if(isInteger(deleteId)) {
				int id = Integer.parseInt(deleteId);
				if(client.deleteCustomer(id))
					view.showError("Deleted customer id " + id);
				else
					view.showError("Did not delete customer id " + id);
			}
			else
				view.showError("Please insert a valid number in customer id field!");
		}
		
	}
	
	//---------------------------------------------------------------------------------------
    // FOR MODIFYING CUSTOMER
    //---------------------------------------------------------------------------------------
	class ModifyListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String modId = view.getCustomerIdText();
			if(isInteger(modId)) {
				int id = Integer.parseInt(modId);
				String fname = view.getFirstNameText();
				String lname = view.getLastNameText();
				String type = view.getCustomerTypeText();
				String phone = view.getPhoneText();
				String adr = view.getAddressText();
				String postal = view.getPostalText();
				
				if(fname.length() > 20)
					view.showError("First name too long!");
				else if (lname.length() > 20)
					view.showError("Last name too long!");
				else if (phone.length() > 12)
					view.showError("Phone number too long!");
				else if (adr.length() > 50)
					view.showError("Address too long!");
				else if (postal.length() > 7)
					view.showError("Postal code too long!");
				else {
					
					Customer cust;
					if(type.equalsIgnoreCase("Residential"))
						 cust = new Residential(id, lname, fname, type, phone, adr, postal);
					else if(type.equalsIgnoreCase("Commercial"))
						 cust = new Commercial(id, lname, fname, type, phone, adr, postal);
					else 
						 cust = new Customer(id, lname, fname, type, phone, adr, postal);
					
					if(client.modifyCustomer(cust))
						view.showError("Modified customer id " + id);
					else
						view.showError("Did not modify customer id " + id);
				}
			}
			else
				view.showError("Please insert a valid number in customer id field!");
		}
		
	}
	
	//---------------------------------------------------------------------------------------
    // FOR ADDING CUSTOMER
    //---------------------------------------------------------------------------------------
	class AddListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String modId = view.getCustomerIdText();
			if(isInteger(modId)) {
				int id = Integer.parseInt(modId);
				String fname = view.getFirstNameText();
				String lname = view.getLastNameText();
				String type = view.getCustomerTypeText();
				String phone = view.getPhoneText();
				String adr = view.getAddressText();
				String postal = view.getPostalText();
				
				if(fname.length() > 20)
					view.showError("First name too long!");
				else if (lname.length() > 20)
					view.showError("Last name too long!");
				else if (phone.length() > 12)
					view.showError("Phone number too long!");
				else if (adr.length() > 50)
					view.showError("Address too long!");
				else if (postal.length() > 7)
					view.showError("Postal code too long!");
				else {
					
					Customer cust;
					if(type.equalsIgnoreCase("Residential"))
						 cust = new Residential(id, lname, fname, type, phone, adr, postal);
					else if(type.equalsIgnoreCase("Commercial"))
						 cust = new Commercial(id, lname, fname, type, phone, adr, postal);
					else 
						 cust = new Customer(id, lname, fname, type, phone, adr, postal);
					
					if(client.addCustomer(cust))
						view.showError("Added customer id " + id);
					else
						view.showError("Did not add customer id " + id);
				}
			}
			else
				view.showError("Please insert a valid number in customer id field!");
		}
		
	}
		
	//---------------------------------------------------------------------------------------
    // FOR QUITING FRAME USING QUIT BUTTON
    //---------------------------------------------------------------------------------------
	class QuitListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			client.closeClient();
			SwingUtilities.getWindowAncestor(view).dispose();
		}
		
	}
	
	//---------------------------------------------------------------------------------------
    // FOR POPULATING CUSTOMER INFORMATION AFTER SELECTING FROM SEARCH RESULT
    //---------------------------------------------------------------------------------------
	class SearchSelectListener implements ListSelectionListener{
		@Override
		public void valueChanged(ListSelectionEvent e) {
			String value = view.getSearchList().getSelectedValue();
			if(value != null) {
				String[] arrVal = value.split(",\\s+");
				view.getCustomerIdField().setText(arrVal[0]);
	    		view.getFirstNameField().setText(arrVal[1]);
	    		view.getLastNameField().setText(arrVal[2]);
	    		if(arrVal[3].equalsIgnoreCase("Residential"))
	    			view.getCustomerTypeField().setSelectedIndex(0);
	    		else if(arrVal[3].equalsIgnoreCase("Commercial"))
	    			view.getCustomerTypeField().setSelectedIndex(1);
	    		view.getPhoneField().setText(arrVal[4]);
	    		view.getAddressField().setText(arrVal[5]);
	    		view.getPostalField().setText(arrVal[6]);
			}
		}
	}
	
	//---------------------------------------------------------------------------------------
    // FOR CLEARING CUSTOMER INFORMATION FIELDS
    //---------------------------------------------------------------------------------------
	class ClearListener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		view.getCustomerIdField().setText("");
    		view.getFirstNameField().setText("");
    		view.getLastNameField().setText("");
    		view.getAddressField().setText("");
    		view.getPostalField().setText("");
    		view.getPhoneField().setText("");
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
}
