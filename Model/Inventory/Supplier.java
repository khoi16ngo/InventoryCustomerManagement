package Model.Inventory;

/**
 * Stores supplier's information within fields
 * @author Khoi
 * @version 1.0
 * @since October 10, 2020
 *
 */

public class Supplier {
	
	/**
	 * Supplier's id
	 */
	private int id;
	
	/**
	 * Supplier's company name
	 */
	private String company;
	
	private String type;
	
	/**
	 * Supplier's address
	 */
	private String address;
	
	/**
	 * Supplier's contact representative
	 */
	private String contact;
	
	private String phone;
	
	/**
	 * Constructs supplier 
	 * @param id Supplier id
	 * @param comp Supplier company name
	 * @param adr Supplier address
	 * @param con Supplier representative
	 */
	public Supplier(int id, String comp, String type, String adr, String con, String phone) {
		setId(id);
		setCompany(comp);
		setType(type);
		setAddress(adr);
		setContact(con);
		setPhone(phone);
	}
	
	/**
	 * Get supplier id
	 * @return Supplier id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set supplier id
	 * @param id Supplier id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Get supplier company name
	 * @return Company name
	 */
	public String getCompany() {
		return company;
	}
	
	/**
	 * Set supplier company name
	 * @param company Company name
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	
	/**
	 * Get supplier address
	 * @return Supplier address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Set supplier address
	 * @param address Supplier address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Get supplier representative
	 * @return Supplier representative
	 */
	public String getContact() {
		return contact;
	}
	
	/**
	 * Set supplier representative
	 * @param contact Supplier representative
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ID: " + id + "\tCompany: " + company + "\tAddress: " + address + "\tContact: " + contact;
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
}
