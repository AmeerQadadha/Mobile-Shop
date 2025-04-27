package application;

public class Phone extends Product {

	private String model;
	private int storage;

	public Phone() {

	}

	public Phone(int ID, String brand, String model, int storage) {
		super(ID, brand);
		this.model = model;
		this.storage = storage;
	}

	public Phone(String brand, double price, String color, int stockQuantity, String model, int storage) {
		super(brand, price, color, stockQuantity);
		this.model = model;
		this.storage = storage;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getStorage() {
		return storage;
	}

	public void setStorage(int storage) {
		this.storage = storage;
	}

	@Override
	public int getID() {
		return super.getID();
	}

	@Override
	public String getBrand() {
		return super.getBrand();
	}

	@Override
	public double getPrice() {
		return super.getPrice();
	}

	@Override
	public String getColor() {
		return super.getColor();
	}

	@Override
	public int getStockQuantity() {
		return super.getStockQuantity();
	}

}
