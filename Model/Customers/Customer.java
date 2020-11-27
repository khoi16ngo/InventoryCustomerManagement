package Model.Customers;

import java.io.Serializable;

public class Customer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int custId;
	private String lastName;
	private String firstName;
	private String type;
	private String phone;
	private String adr;
	private String postal;
	
	public Customer(int custId, String lastName, String firstName, String type, String phone, String adr, String postal) {
		setCustId(custId);
		setLastName(lastName);
		setFirstName(firstName);
		setType(type);
		setPhone(phone);
		setAdr(adr);
		setPostal(postal);
	}

	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAdr() {
		return adr;
	}

	public void setAdr(String adr) {
		this.adr = adr;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}
	
	@Override
	public String toString() {
		String restr = "";
		restr += custId + "\t";
		restr += lastName + "\t";
		restr += firstName + "\t";
		restr += type + "\t";
		restr += phone + "\t";
		restr += adr + "\t";
		restr += postal;
		return restr;
	}
}
