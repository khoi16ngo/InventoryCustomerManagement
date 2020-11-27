package Model.Inventory;

public class International extends Supplier{

	private float importTax;
	
	public International(int id, String comp, String type, String adr, String con, String phone, float importTax) {
		super(id, comp, type, adr, con, phone);
		setImportTax(importTax);
	}

	public float getImportTax() {
		return importTax;
	}

	public void setImportTax(float importTax) {
		this.importTax = importTax;
	}
	
}
