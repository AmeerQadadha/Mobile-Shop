package application;

public class Accessories extends Product {

	private String type;
	private String benefite;

	public Accessories() {
	}

	public Accessories(int ID, String brand, double price, String color, int stockQuantity) {
		super(ID, brand, price, color, stockQuantity);
		// TODO Auto-generated constructor stub
	}

	public Accessories(int iD, String brand) {
		super(iD, brand);
		// TODO Auto-generated constructor stub
	}

	public Accessories(String brand, double price, String color, int stockQuantity) {
		super(brand, price, color, stockQuantity);
		// TODO Auto-generated constructor stub
	}

	public Accessories(int ID, String brand, double price, String color, int stockQuantity, String type,
			String benefite) {
		super(ID, brand, price, color, stockQuantity);
		this.type = type;
		this.benefite = benefite;
	}

	public Accessories(String brand, double price, String color, int stockQuantity, String type, String benefite) {
		super(brand, price, color, stockQuantity);
		this.type = type;
		this.benefite = benefite;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBenefite() {
		return benefite;
	}

	public void setBenefite(String benefite) {
		this.benefite = benefite;
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
