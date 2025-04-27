package application;

import java.util.Date;

public class Order {

	private int orderID;
	private Date date;
	private double price;
	private int CustomerID;

	public Order() {
	}

	public Order(Date date, int CustomerID) {
		this.date = date;
		this.CustomerID = CustomerID;
	}

	public Order(int orderID, Date date, double price, int customerID) {
		this.orderID = orderID;
		this.date = date;
		this.price = price;
		CustomerID = customerID;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}

	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", date=" + date + ", price=" + price + ", CustomerID=" + CustomerID + "]";
	}

}
