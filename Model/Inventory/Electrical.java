package Model.Inventory;

public class Electrical extends Item {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String powerType;
	
	public Electrical(int id, String name, String type, int stock, float price, int supplierId, String powerType) {
		super(id, name, type, stock, price, supplierId);
		setPowerType(powerType);
	}

	public String getPowerType() {
		return powerType;
	}

	public void setPowerType(String powerType) {
		this.powerType = powerType;
	}

}
