package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class Manager {

	public double calculateTotalSalesByDay(Connection con, java.sql.Date date) {
		double totalSales = 0.0;
		String query = "SELECT SUM(TotalAmount) AS TotalSales " + "FROM cust_order " + "WHERE OrderDate = ?";

		try (PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setDate(1, u(date));
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				totalSales = rs.getDouble("TotalSales");
			}

		} catch (SQLException ex) {
			System.out.println("Error calculating total sales for the day: " + ex.getMessage());
		}

		return totalSales;
	}

	public int countOrdersByDay(Connection con, java.sql.Date date) {
		int numberOfOrders = 0;
		String query = "SELECT COUNT(Order_ID) AS NumberOfOrders " + "FROM cust_order " + "WHERE OrderDate = ?";

		try (PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setDate(1, u(date));
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				numberOfOrders = rs.getInt("NumberOfOrders");
			}

		} catch (SQLException ex) {
			System.out.println("Error counting orders for the day: " + ex.getMessage());
		}

		return numberOfOrders;
	}

	public PieChart generateTotalSalesByMonthReport(Connection con, int year) {
		String query = "SELECT MONTH(OrderDate) AS Month, SUM(TotalAmount) AS TotalSales " + "FROM cust_order "
				+ "WHERE YEAR(OrderDate) = ? " + "GROUP BY MONTH(OrderDate)";

		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

		try (PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, year);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int monthNumber = rs.getInt("Month");
				Month month = Month.of(monthNumber);
				double totalSales = rs.getDouble("TotalSales");

				pieChartData.add(new PieChart.Data(
						month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " ($" + totalSales + " sales)",
						totalSales));
			}
		} catch (SQLException ex) {
			System.out.println("Error generating total sales by month report: " + ex.getMessage());
		}

		PieChart pieChart = new PieChart(pieChartData);
		pieChart.setTitle("Total Sales by Month for " + year);

		return pieChart;
	}

	public BarChart<String, Number> generateTopSellingProductsReport(Connection con) {
		String query = "SELECT p.Brand, p.Color, SUM(op.Quantity) AS TotalQuantity " + "FROM OrderProduct op "
				+ "JOIN Product p ON op.ProductID = p.ProductID " + "GROUP BY p.Brand, p.Color "
				+ "ORDER BY TotalQuantity DESC " + "LIMIT 5";

		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
		barChart.setTitle("Top-Selling Products");

		xAxis.setLabel("Product");
		yAxis.setLabel("Total Quantity Sold");

		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Top 5 Products");

		try (PreparedStatement pstmt = con.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String brand = rs.getString("Brand");
				String color = rs.getString("Color");
				int totalQuantity = rs.getInt("TotalQuantity");

				String productName = brand + " (" + color + ")";
				series.getData().add(new XYChart.Data<>(productName, totalQuantity));
			}

			if (series.getData().isEmpty()) {
				System.out.println("No product sales data found.");
			}
		} catch (SQLException ex) {
			System.out.println("Error generating top-selling products report: " + ex.getMessage());
		}

		barChart.getData().add(series);
		return barChart;
	}

	public void insertOrderProduct(Connection con, int orderId, int productId, int quantity) {
		String query = "INSERT INTO OrderProduct (Order_ID, ProductID, Quantity) VALUES (?, ?, ?)";
		try (Connection conn = con; PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, orderId);
			ps.setInt(2, productId);
			ps.setInt(3, quantity);
			ps.executeUpdate();
			System.out.println("Insert successful!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteOrderProduct(int orderId, int productId, Connection con) {
		String query = "DELETE FROM OrderProduct WHERE Order_ID = ? AND ProductID = ?";
		try (Connection conn = con; PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, orderId);
			ps.setInt(2, productId);
			ps.executeUpdate();
			System.out.println("Delete successful!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateOrderProduct(int orderId, int productId, int newQuantity, Connection con) {
		String query = "UPDATE OrderProduct SET Quantity = ? WHERE Order_ID = ? AND ProductID = ?";
		try (Connection conn = con; PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, newQuantity);
			ps.setInt(2, orderId);
			ps.setInt(3, productId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean updateAccessory(Connection con, Accessories acc, int id) {
		try (Statement st = con.createStatement()) {
			String accessoryQuery = "UPDATE Accessory SET Acc_Type = '" + acc.getType() + "', Acc_Benefite = '"
					+ acc.getBenefite() + "' WHERE ProductID = " + id;
			int rowsUpdatedAccessory = st.executeUpdate(accessoryQuery);

			String productQuery = "UPDATE Product SET brand = '" + acc.getBrand() + "', price = " + acc.getPrice()
					+ ", " + "color = '" + acc.getColor() + "', stockQuantity = " + acc.getStockQuantity()
					+ " WHERE ProductID = " + id;
			int rowsUpdatedProduct = st.executeUpdate(productQuery);

			return rowsUpdatedAccessory > 0 && rowsUpdatedProduct > 0;
		} catch (SQLException ex) {
			System.out.println("Error updating accessory record: " + ex.getMessage());
			return false;
		} finally {
			try {
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public boolean insertAccessories(Connection con, Accessories acc) {
		try {
			con.setAutoCommit(false); 

			String productQuery = "INSERT INTO Product (brand, price, color, stockQuantity) VALUES ('" + acc.getBrand()
					+ "', " + acc.getPrice() + ", '" + acc.getColor() + "', " + acc.getStockQuantity() + ")";
			try (Statement st = con.createStatement()) {
				int rowsInsertedProduct = st.executeUpdate(productQuery, Statement.RETURN_GENERATED_KEYS);

				if (rowsInsertedProduct > 0) {
					int productID = 0;
					try (ResultSet generatedKeys = st.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							productID = generatedKeys.getInt(1);
						}
					}

					String accessoryQuery = "INSERT INTO Accessory (ProductID, Acc_Type, Acc_Benefite) VALUES ("
							+ productID + ", '" + acc.getType() + "', '" + acc.getBenefite() + "')";
					int rowsInsertedAccessory = st.executeUpdate(accessoryQuery);

					if (rowsInsertedAccessory > 0) {
						con.commit(); 
						return true;
					} else {
						con.rollback(); 
						return false;
					}
				} else {
					con.rollback(); 
					return false;
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback(); 
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			return false;
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public boolean insertEmployee(Connection con, Employee e) {
		try (Statement st = con.createStatement();) {

			String query = "INSERT INTO Employee (Emp_Name, Email, Phone, Address, Emp_Role) VALUES ('" + e.getName()
					+ "', '" + e.getEmail() + "', '" + e.getPhone() + "', '" + e.getAddress() + "', '" + e.getEmpRole()
					+ "')";

			int rowsInserted = st.executeUpdate(query);

			return true;

		} catch (SQLException ex) {
			System.out.println(ex);
			return false;

		} finally {
			try {
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public boolean updateEmployee(Connection con, Employee e, int id) {
		try (Statement st = con.createStatement();) {

			String query = "UPDATE Employee SET Emp_Name = '" + e.getName() + "', " + "Email = '" + e.getEmail() + "', "
					+ "Phone = '" + e.getPhone() + "', " + "Address = '" + e.getAddress() + "', " + "Emp_Role = '"
					+ e.getEmpRole() + "' " + "WHERE Emp_Id = " + id;

			int rowsUpdated = st.executeUpdate(query);

			if (rowsUpdated > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException ex) {
			System.out.println("Error updating employee record: " + ex.getMessage());
			return false;
		} finally {
			try {
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public int deleteEmployee(Connection con, int empId) throws SQLException {
		String sql = "DELETE FROM Employee WHERE Emp_ID = " + empId;
		try (Statement st = con.createStatement()) {
			int rowsDeleted = st.executeUpdate(sql);
			return rowsDeleted;
		} catch (SQLException e) {
			System.out.println("Error deleting employee: " + e.getMessage());
			throw e;
		}
	}

	/////////////////////////////////////////////////////////

	public boolean insertLog(Connection con, int ID, LogIn_Emp l) {
		try {
			String query = "INSERT INTO Login_Data (user_id,Username, User_Password) VALUES (? ,?, ?)";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, ID);
			pstmt.setString(2, l.getUserName());
			pstmt.setString(3, l.getUserPassword());
			int rowsInserted = pstmt.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException ex) {
			System.out.println("Error inserting login record: " + ex.getMessage());
			return false;
		} finally {
			try {
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public boolean deleteLog(Connection con, int userID) {
		try {
			String query = "DELETE FROM Login_Data WHERE User_ID = ?";
			PreparedStatement pstmt = con.prepareStatement(query);

			pstmt.setInt(1, userID);

			int rowsDeleted = pstmt.executeUpdate();

			return rowsDeleted > 0;
		} catch (SQLException ex) {
			System.out.println("Error deleting login record: " + ex.getMessage());
			return false;
		} finally {
			try {
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public boolean updateLog(Connection con, int ID, String uName, String uPas) {
		try {
			String query = "UPDATE Login_Data SET Username = ?, User_Password = ? WHERE User_ID = ?";
			PreparedStatement pstmt = con.prepareStatement(query);

			pstmt.setString(1, uName);
			pstmt.setString(2, uPas);
			pstmt.setInt(3, ID);

			int rowsUpdated = pstmt.executeUpdate();

			return rowsUpdated > 0;
		} catch (SQLException ex) {
			System.out.println("Error updating login record: " + ex.getMessage());
			return false;
		} finally {
			try {
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	//////////////////////////////////////////////////////////

	public boolean insertCustomer(Connection con, Customer customer) {
		try (Statement st = con.createStatement();) {
			String query = "INSERT INTO Customer (Cust_Name,Phone, Address) VALUES ('" + customer.getName() + "', '"
					+ customer.getPhone() + "', '" + customer.getAddress() + "')";

			int rowsInserted = st.executeUpdate(query);
			return true;

		} catch (SQLException ex) {
			System.out.println(ex);
			return false;
		} finally {
			try {
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public boolean updateCustomer(Connection con, int iD, String Name, String Phone, String Address) {
		try (Statement st = con.createStatement();) {
			String query = "UPDATE Customer SET Cust_Name = '" + Name + "', " + "Phone = '" + Phone + "', "
					+ "Address = '" + Address + "' " + "WHERE Cust_ID = " + iD;

			int rowsUpdated = st.executeUpdate(query);

			return rowsUpdated > 0;
		} catch (SQLException ex) {
			System.out.println("Error updating customer record: " + ex.getMessage());
			return false;
		} finally {
			try {
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public int deleteCustomer(Connection con, int custId) throws SQLException {
		String deletePC = "DELETE FROM customerphone WHERE Cust_ID = ?"; 
		String deleteC = "DELETE FROM Customer WHERE Cust_ID = ?";
		try (PreparedStatement CPStmt = con.prepareStatement(deletePC);
				PreparedStatement CStmt = con.prepareStatement(deleteC)) {
			CPStmt.setInt(1, custId);
			CPStmt.executeUpdate();
			CStmt.setInt(1, custId);
			return CStmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error deleting customer: " + e.getMessage());
			throw e;
		}
	}
	////////////////////////////////////////

	public boolean insertPhone(Connection con, Phone phone) {
		try (Statement st = con.createStatement()) {
			String productQuery = "INSERT INTO Product (brand, price, color, stockQuantity) VALUES ('"
					+ phone.getBrand() + "', " + phone.getPrice() + ", '" + phone.getColor() + "', "
					+ phone.getStockQuantity() + ")";
			int rowsInsertedProduct = st.executeUpdate(productQuery);
			int productID = 0;
			try (Statement stt = con.createStatement()) {
				String idSelect = "SELECT ProductID FROM Product WHERE Brand = '" + phone.getBrand() + "' AND Color = '"
						+ phone.getColor() + "'"; 
				ResultSet rs = stt.executeQuery(idSelect);

				if (rs.next()) {
					productID = rs.getInt("ProductID");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			if (rowsInsertedProduct > 0) {
				String phoneQuery = "INSERT INTO Mobile (ProductID, Model, Mob_Storage) VALUES (" + productID + ", '"
						+ phone.getModel() + "', " + phone.getStorage() + ")";
				int rowsInsertedPhone = st.executeUpdate(phoneQuery);

				if (rowsInsertedPhone > 0) {
					return true;
				} else {
					st.executeUpdate("DELETE FROM Product WHERE ProductID = LAST_INSERT_ID()");
					return false;
				}
			} else {
				return false;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public int deleteAccessory(Connection con, int id) throws SQLException {
		String sql = "DELETE FROM Accessory WHERE ProductID = " + id;
		try (Statement st = con.createStatement()) {
			int rowsDeleted = st.executeUpdate(sql);
			return rowsDeleted;
		} catch (SQLException e) {
			System.out.println("Error deleting accessory: " + e.getMessage());
			throw e;
		}
	}

	public int deletePhone(Connection con, int id) throws SQLException {
		String sql = "DELETE FROM Mobile WHERE productid = " + id;
		try (Statement st = con.createStatement()) {
			int rowsDeleted = st.executeUpdate(sql);
			return rowsDeleted;
		} catch (SQLException e) {
			System.out.println("Error deleting phone: " + e.getMessage());
			throw e;
		}
	}

	public double calculateTotalAmount(Connection con, int orderId) {
		String query = "SELECT SUM(p.Price * op.Quantity) AS TotalAmount " + "FROM OrderProduct op "
				+ "JOIN Product p ON op.ProductID = p.ProductID " + "WHERE op.Order_ID = ?";

		try (PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, orderId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getDouble("TotalAmount");
			}
		} catch (SQLException ex) {
			System.out.println("Error calculating total amount: " + ex.getMessage());
		}
		return 0.0;
	}

	public boolean updateOrder(Connection con, Order r, int ID) {
		String query = "UPDATE cust_order SET Cust_ID = ?, OrderDate = ? WHERE Order_ID = ?";
		try (PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, r.getCustomerID());
			pstmt.setDate(2, u(r.getDate()));
			pstmt.setInt(3, ID);
			int rowsUpdated = pstmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException ex) {
			System.out.println("Error updating order record: " + ex.getMessage());
			return false;
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public int checkLogin(Connection con, String username, String password) {
		String query = "SELECT e.Emp_Role FROM Login_Data l JOIN Employee e ON l.User_ID = e.Emp_ID WHERE l.Username = ? AND l.User_Password = ?";
		try (PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String role = rs.getString("Emp_Role");
				if ("manager".equalsIgnoreCase(role)) {
					return 1;
				} else if ("employee".equalsIgnoreCase(role)) {
					return -1;
				} else {
					return 0;
				}
			} else {
				return 0;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public boolean updateProduct(Connection con, Product prod) {
		try (Statement st = con.createStatement()) {
			String query = "UPDATE Product SET brand = '" + prod.getBrand() + "', " + "price = '" + prod.getPrice()
					+ "', " + "color = '" + prod.getColor() + "', " + "stockQuantity = stockQuantity + "
					+ prod.getStockQuantity() + " " + "WHERE productid = " + prod.getID();

			int rowsUpdated = st.executeUpdate(query);

			return rowsUpdated > 0;
		} catch (SQLException ex) {
			System.out.println("Error updating product record: " + ex.getMessage());
			return false;
		} finally {
			try {
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public boolean updatePhone(Connection con, Phone p, int id) {
		try (Statement st = con.createStatement()) {
			String mobileQuery = "UPDATE Mobile SET model = '" + p.getModel() + "', mob_storage = " + p.getStorage()
					+ " WHERE productid = " + id;
			int rowsUpdatedMobile = st.executeUpdate(mobileQuery);
			String productQuery = "UPDATE Product SET brand = '" + p.getBrand() + "', price = " + p.getPrice()
					+ ", color = '" + p.getColor() + "', stockQuantity = " + p.getStockQuantity()
					+ " WHERE ProductID = " + id;
			int rowsUpdatedProduct = st.executeUpdate(productQuery);

			return rowsUpdatedMobile > 0 && rowsUpdatedProduct > 0;
		} catch (SQLException ex) {
			System.out.println("Error updating phone record: " + ex.getMessage());
			return false;
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	///////////////////////////////////////////////////////////

	public int deleteOrder(Connection con, int orderID) throws SQLException {
		String sql = "DELETE FROM cust_order WHERE Order_ID = " + orderID;
		try (Statement st = con.createStatement()) {
			int rowsDeleted = st.executeUpdate(sql);
			System.out.println("Order was deleted successfully");
			return rowsDeleted;
		} catch (SQLException e) {
			System.out.println("Error deleting order: " + e.getMessage());
			throw e;
		}
	}

	public boolean insertOrder(Connection con, Order r) {
		String query = "INSERT INTO cust_order (OrderDate, Cust_ID) VALUES (?, ?)";
		try (PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setDate(1, u(r.getDate()));
			pstmt.setInt(2, r.getCustomerID());
			int rowsInserted = pstmt.executeUpdate();

			return rowsInserted > 0;
		} catch (SQLException ex) {
			System.out.println("Error inserting order: " + ex.getMessage());
			return false;
		}
	}

	public java.sql.Date u(java.util.Date u) {

		return new java.sql.Date(u.getTime());

	}

}