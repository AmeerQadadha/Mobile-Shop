package application;

public class Employee {

	private int ID;
	private String name;
	private String email;
	private String phone;
	private String address;
	private String empRole;

	public Employee() {
	}

	public Employee(String name, String email, String phone, String address, String empRole) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.empRole = empRole;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getEmpRole() {
		return empRole;
	}

	public void setEmpRole(String empRole) {
		this.empRole = empRole;
	}

}
