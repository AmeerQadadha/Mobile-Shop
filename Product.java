package application;

public class Product {

	private int ID;
	private String brand;
	private double price;
	private String color;
	private int stockQuantity;

	public Product(int iD, String brand) {
		ID = iD;
		this.brand = brand;
	}

	public Product() {
	}

	public Product(int ID, String brand, double price, String color, int stockQuantity) {
		this.ID = ID;
		this.brand = brand;
		this.price = price;
		this.color = color;
		this.stockQuantity = stockQuantity;
	}

	public Product(String brand, double price, String color, int stockQuantity) {
		super();
		this.brand = brand;
		this.price = price;
		this.color = color;
		this.stockQuantity = stockQuantity;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

}
