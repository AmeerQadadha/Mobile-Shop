package application;

public class Customer {
	private int ID;
	private String name;
	private String phone;
	private String address;

	public Customer() {
	}

	public Customer(String name, String phone, String address) {
		super();
		this.name = name;
		this.phone = phone;
		this.address = address;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
