package Model.Inventory;

public class NonElectrical extends Item{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NonElectrical(int id, String name, String type, int stock, float price, int supplierId) {
		super(id, name, type, stock, price, supplierId);
	}

}
