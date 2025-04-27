package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class controller {

	Manager m = new Manager();

	@FXML
	private TextField InsertCusaddress;

	@FXML
	private TextField InsertCusname;

	@FXML
	private TextField InsertCusphone;

	@FXML
	public void InsertCustomer(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to insert Custemmer?");
		confirmAlert.setContentText("This will insert Custemmer . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (InsertCusname.getText().isEmpty() || InsertCusphone.getText().isEmpty()
						|| InsertCusaddress.getText().isEmpty()) {
					ResAlert("Error !", "one or more TextField is empty.");
					return;
				}
				String cusphone = InsertCusphone.getText().trim();
				String cusname = InsertCusname.getText().trim();
				String cusaddress = InsertCusname.getText().trim();
				if (!cusphone.matches("\\d+")) {
					ResAlert("Error !", "phone number shouldn't have letters.");
					return;
				}
				if (cusname.matches(".*\\d+.*")) {
					ResAlert("Error !", "Customer name should not contain numbers.");
					return;
				}

				else {
					CusTV.getItems().clear();
					m.insertCustomer(con(), new Customer(cusname, cusphone, cusaddress));
					getDataCustomer();
					InsertCusname.clear();
					InsertCusphone.clear();
					InsertCusaddress.clear();
				}
			}
		});

	}

	@FXML
	private TableView<OrderProduct> OrdPTV = new TableView<>();

	@FXML
	private TableColumn<OrderProduct, Integer> OrdPTVr1 = new TableColumn<>();

	@FXML
	private TableColumn<OrderProduct, Integer> OrdPTVr2 = new TableColumn<>();

	@FXML
	private TableColumn<OrderProduct, Integer> OrdPTVr3 = new TableColumn<>();

	/**/

	@FXML
	private TextField UpdateCusName;

	@FXML
	private TextField UpdateCusaddress;

	@FXML
	private TextField UpdateCusphone;

	@FXML
	public void UpdateCustomer(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to update Custemmer?");
		confirmAlert.setContentText("This will update Custemmer . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (CusTV.getSelectionModel().getSelectedItem() != null) {
					Customer s = CusTV.getSelectionModel().getSelectedItem();
					int ID = s.getID();
					String newName = UpdateCusName.getText().trim();
					String newNumber = UpdateCusphone.getText().trim();
					if (UpdateCusName.getText().isEmpty() || UpdateCusphone.getText().isEmpty()
							|| UpdateCusaddress.getText().isEmpty()) {
						ResAlert("Error !", "one or more TextField is empty.");
						return;
					}
					if (newName.matches(".*\\d+.*")) {
						ResAlert("Error !", "Customer name should not contain numbers.");
						return;
					}
					if (!newNumber.matches("\\d+")) {
						ResAlert("Error !", "phone number shouldn't have letters.");
						return;
					} else {
						try {
							m.updateCustomer(con(), ID, UpdateCusName.getText(), UpdateCusphone.getText(),
									UpdateCusaddress.getText());
							CusTV.getItems().clear();
							getDataCustomer();

						} catch (NumberFormatException e) {

							e.printStackTrace();
						}

						UpdateCusName.clear();
						UpdateCusphone.clear();
						UpdateCusaddress.clear();

					}
				} else {
					ResAlert("Error !", "no select  customer to update");
				}
			}
		});

	}

	@FXML
	public void DeleteCustomer(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to delete Custemmer?");
		confirmAlert.setContentText("This will delete Custemmer . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (CusTV.getSelectionModel().getSelectedItem() != null) {
					Customer s = CusTV.getSelectionModel().getSelectedItem();
					int ID = s.getID();
					try {
						m.deleteCustomer(con(), ID);
						CusTV.getItems().clear();
						getDataCustomer();

					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (SQLException e) {

						e.printStackTrace();
					}
				} else {
					ResAlert("Error !", "no selected  customer to Delete");
				}
				UpdateCusName.clear();
				UpdateCusphone.clear();
				UpdateCusaddress.clear();
			}
		});

	}

	/**/

	@FXML
	private TextField InsertEmpAddress;

	@FXML
	private TextField InsertEmpEmail;

	@FXML
	private TextField InsertEmpName;

	@FXML
	private TextField InsertEmpPhone;

	@FXML
	private ComboBox<String> Compo = new ComboBox<>();

	@FXML
	public void InsertEmployee(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to insert Employee?");
		confirmAlert.setContentText("This will insert Employee . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (InsertEmpName.getText().isEmpty() || InsertEmpEmail.getText().isEmpty()
						|| InsertEmpPhone.getText().isEmpty() || InsertEmpAddress.getText().isEmpty()) {
					ResAlert("Error !", "one or more TextField is empty.");
				} else {
					m.insertEmployee(con(), new Employee(InsertEmpName.getText(), InsertEmpEmail.getText(),
							InsertEmpPhone.getText(), InsertEmpAddress.getText(), "Employee"));

					EmpTV.getItems().clear();
					getEmployees();

					InsertEmpAddress.clear();
					InsertEmpEmail.clear();
					InsertEmpName.clear();
					InsertEmpPhone.clear();

				}
			}
		});
	}

	@FXML
	private TextField UpdateEmpName;

	@FXML
	private TextField UpdateEmpaddress;

	@FXML
	private TextField UpdateEmpemail;

	@FXML
	private TextField UpdateEmpphone;

	@FXML
	private ComboBox<String> Compo1 = new ComboBox<>();

	@FXML
	public void UpdateEmployee(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to update Employee?");
		confirmAlert.setContentText("This will update Employee . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (EmpTV.getSelectionModel().getSelectedItem() != null) {
					Employee s = EmpTV.getSelectionModel().getSelectedItem();
					int ID = s.getID();
					if (UpdateEmpName.getText().isEmpty() || UpdateEmpemail.getText().isEmpty()
							|| UpdateEmpphone.getText().isEmpty() || UpdateEmpaddress.getText().isEmpty()) {
						ResAlert("Error !", "one or more TextField is empty.");
					} else {
						try {
							m.updateEmployee(con(), new Employee(UpdateEmpName.getText(), UpdateEmpemail.getText(),
									UpdateEmpphone.getText(), UpdateEmpaddress.getText(), "Employee"), ID);

							EmpTV.getItems().clear();
							getEmployees();
							UpdateEmpaddress.clear();
							UpdateEmpemail.clear();
							UpdateEmpName.clear();
							UpdateEmpphone.clear();

						} catch (NumberFormatException e) {

							e.printStackTrace();
						}
						UpdateEmpName.clear();
						UpdateEmpemail.clear();
						UpdateEmpphone.clear();
						UpdateEmpaddress.clear();
					}
				} else {
					ResAlert("Error !", "no selected Employee to update");
				}
			}
		});

	}

	@FXML
	public void DeleteEmployee(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to delete Employee?");
		confirmAlert.setContentText("This will delete Employee . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (EmpTV.getSelectionModel().getSelectedItem() != null) {
					Employee s = EmpTV.getSelectionModel().getSelectedItem();
					int ID = s.getID();
					try {
						m.deleteEmployee(con(), ID);
						EmpTV.getItems().clear();
						getEmployees();
					} catch (NumberFormatException e) {

						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					ResAlert("Error !", "no selected Employee to Delete");
				}
				UpdateEmpName.clear();
				UpdateEmpemail.clear();
				UpdateEmpphone.clear();
				UpdateEmpaddress.clear();
				Compo.getSelectionModel().clearSelection();
				Compo.setPromptText("Select Role");
			}
		});
	}

	@FXML
	private TextField phoneBrand;

	@FXML
	private TextField phoneSQ;

	@FXML
	private TextField phoneStorage;

	@FXML
	private TextField phonecolor;

	@FXML
	private TextField phonemodel;

	@FXML
	private TextField phoneprice;

	@FXML
	public void InsertPhone(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to insert phone?");
		confirmAlert.setContentText("This will inssert phone . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (phoneBrand.getText().isEmpty() || phoneprice.getText().isEmpty() || phonecolor.getText().isEmpty()
						|| phoneSQ.getText().isEmpty() || phonemodel.getText().isEmpty()
						|| phoneStorage.getText().isEmpty()) {
					ResAlert("Error !", "one or more TextFields is empty.");
				} else {
					try {
						m.insertPhone(con(),
								new Phone(phoneBrand.getText(), Double.parseDouble(phoneprice.getText()),
										phonecolor.getText(), Integer.parseInt(phoneSQ.getText()), phonemodel.getText(),
										Integer.parseInt(phoneStorage.getText())));
					} catch (NumberFormatException e) {

						ResAlert("Error !", "You have to enter numbers in the number textfields");
						return;
					}

					ProductTV1.getItems().clear();
					getDataPhones();

					phoneBrand.clear();
					phoneprice.clear();
					phonecolor.clear();
					phoneSQ.clear();
					phonemodel.clear();
					phoneStorage.clear();
				}
			}
		});

	}

	@FXML
	public void UpdatePhone(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to update phone?");
		confirmAlert.setContentText("This will update phone . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (ProductTV1.getSelectionModel().getSelectedItem() != null) {

					Phone s = ProductTV1.getSelectionModel().getSelectedItem();
					int ID = s.getID();
					if (phoneBrand.getText().isEmpty() || phoneprice.getText().isEmpty()
							|| phonecolor.getText().isEmpty() || phoneSQ.getText().isEmpty()
							|| phonemodel.getText().isEmpty() || phoneStorage.getText().isEmpty()) {
						ResAlert("Error !", "one or more TextField is empty.");
					} else {
						try {
							m.updatePhone(con(),
									new Phone(phoneBrand.getText(), Double.parseDouble(phoneprice.getText()),
											phonecolor.getText(), Integer.parseInt(phoneSQ.getText()),
											phonemodel.getText(), Integer.parseInt(phoneStorage.getText())),
									ID);
						} catch (NumberFormatException e) {

							ResAlert("Error !", "You have to enter numbers in the number textfields");
							return;
						}

						ProductTV1.getItems().clear();
						getDataPhones();
						phoneBrand.clear();
						phoneprice.clear();
						phonecolor.clear();
						phoneSQ.clear();
						phonemodel.clear();
						phoneStorage.clear();
					}
				} else {
					ResAlert("Error !", "no selected  phone to update");
				}
			}
		});

	}

	@FXML
	public void DeletePhone(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to delete phone?");
		confirmAlert.setContentText("This will delete phone . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (ProductTV1.getSelectionModel().getSelectedItem() != null) {
					Phone s = ProductTV1.getSelectionModel().getSelectedItem();
					int ID = s.getID();

					try {
						m.deletePhone(con(), ID);
						ProductTV1.getItems().clear();
						getDataPhones();
						phoneBrand.clear();
						phoneprice.clear();
						phonecolor.clear();
						phoneSQ.clear();
						phonemodel.clear();
						phoneStorage.clear();
					} catch (NumberFormatException | SQLException e) {
						e.printStackTrace();
					}
				} else {
					ResAlert("Error !", "no selected phone to Delete");
				}
			}
		});

	}

	@FXML
	private TextField AccBrand;

	@FXML
	private TextField AccColor;

	@FXML
	private TextField AccPrice;

	@FXML
	private TextField AccSQ;

	@FXML
	private TextField Accbene;

	@FXML
	private TextField Acctype;

	@FXML
	public void InsertAcc(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to insert Accessories?");
		confirmAlert.setContentText("This will insert Accessories . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (AccBrand.getText().isEmpty() || AccPrice.getText().isEmpty() || AccColor.getText().isEmpty()
						|| AccSQ.getText().isEmpty() || Acctype.getText().isEmpty() || Accbene.getText().isEmpty()) {
					ResAlert("Error !", "one or more TextField is empty.");
				} else {
					m.insertAccessories(con(),
							new Accessories(AccBrand.getText(), Double.parseDouble(AccPrice.getText()),
									AccColor.getText(), Integer.parseInt(AccSQ.getText()), Acctype.getText(),
									(Accbene.getText())));

					ProductTV2.getItems().clear();

					getDataAccessories();
					AccBrand.clear();
					AccColor.clear();
					AccPrice.clear();
					AccSQ.clear();
					Acctype.clear();
					Accbene.clear();
				}
			}
		});

	}

	@FXML
	public void UpdateAcc(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to update Accessories?");
		confirmAlert.setContentText("This will update Accessories . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (ProductTV2.getSelectionModel().getSelectedItem() != null) {
					Accessories s = ProductTV2.getSelectionModel().getSelectedItem();
					int ID = s.getID();
					if (AccBrand.getText().isEmpty() || AccPrice.getText().isEmpty() || AccColor.getText().isEmpty()
							|| AccSQ.getText().isEmpty() || Acctype.getText().isEmpty()
							|| Accbene.getText().isEmpty()) {
						ResAlert("Error !", "one or more TextField is empty.");
					} else {
						Accessories A = new Accessories(AccBrand.getText(), Double.parseDouble(AccPrice.getText()),
								phonecolor.getText(), Integer.parseInt(AccSQ.getText()), Acctype.getText(),
								(Accbene.getText()));
						m.updateAccessory(con(), A, ID);

						ProductTV2.getItems().clear();
						getDataAccessories();

						AccBrand.clear();
						AccColor.clear();
						AccPrice.clear();
						AccSQ.clear();
						Acctype.clear();
						Accbene.clear();
					}
				} else {
					ResAlert("Error !", "no selected Accessory to update");
				}
			}
		});

	}

	@FXML
	public void DeleteAcc(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to delete Accessories?");
		confirmAlert.setContentText("This will delete Accessories . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (ProductTV2.getSelectionModel().getSelectedItem() != null) {
					Accessories s = ProductTV2.getSelectionModel().getSelectedItem();
					int ID = s.getID();

					try {
						m.deleteAccessory(con(), ID);
						ProductTV2.getItems().clear();
						getDataAccessories();
						AccBrand.clear();
						AccColor.clear();
						AccPrice.clear();
						AccSQ.clear();
						Acctype.clear();
						Accbene.clear();
					} catch (NumberFormatException | SQLException e) {

						e.printStackTrace();
					}
				} else {
					ResAlert("Error !", "no selected Accessory to Delete");
				}
			}
		});

	}

	@FXML
	private TextField POrderID;

	@FXML
	private TextField PProductID;

	@FXML
	private TextField PQuantity;

	@FXML
	public void InsertOP(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to insert order product?");
		confirmAlert.setContentText("This will insert order product . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (POrderID.getText().isEmpty() || PProductID.getText().isEmpty() || PQuantity.getText().isEmpty()) {
					ResAlert("Error !", "one or more TextField is empty.");
				} else {

					m.insertOrderProduct(con(), Integer.parseInt(POrderID.getText()),
							Integer.parseInt(PProductID.getText()), Integer.parseInt(PQuantity.getText()));

					OrdPTV.getItems().clear();
					getDataOrderProduct();

					POrderID.clear();
					PProductID.clear();
					PQuantity.clear();
				}
			}
		});

	}

	@FXML
	private TextField PUpdateQuantity;

	@FXML
	public void UpdateOP(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to update the order product?");
		confirmAlert.setContentText("This will update the order product.");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (OrdPTV.getSelectionModel().getSelectedItem() != null) {
					OrderProduct s = OrdPTV.getSelectionModel().getSelectedItem();
					int PID = s.getProductId();
					int OID = s.getOrderId();
					m.updateOrderProduct(OID, PID, Integer.parseInt(PUpdateQuantity.getText()), con());
					OrdPTV.getItems().clear();
					getDataOrderProduct();
					PUpdateQuantity.clear();
				}
			} else {
				ResAlert("Error !", "No selected Order Product to update.");

			}
		});
	}

	@FXML
	public void DeleteOP(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to delete order product?");
		confirmAlert.setContentText("This will delete order product . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (OrdPTV.getSelectionModel().getSelectedItem() != null) {
					OrderProduct s = OrdPTV.getSelectionModel().getSelectedItem();
					int PID = s.getProductId();
					int OID = s.getOrderId();

					try {
						m.deleteOrderProduct(OID, PID, con());
						OrdPTV.getItems().clear();
						getDataOrderProduct();
						POrderID.clear();
						PProductID.clear();
						PQuantity.clear();

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					ResAlert("Error !", "no selected Order of Product to Deleet");
				}
			}
		});

	}

	@FXML
	private TextField OrderDate;

	@FXML
	private TextField OrderID1;

	@FXML
	public void InsertOrder(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to insert order?");
		confirmAlert.setContentText("This will insert order.");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (OrderID1.getText().isEmpty()) {
					ResAlert("Error !", "TextField is empty.");
				} else {
					try {
						LocalDate currentDate = LocalDate.now();
						Date sqlDate = java.sql.Date.valueOf(currentDate);
						Order order = new Order(sqlDate, Integer.parseInt(OrderID1.getText()));
						m.insertOrder(con(), order);
						OrdTV.getItems().clear();
						getDataOrder();
						OrderID1.clear();
					} catch (Exception e) {
						ResAlert("Error !", e.getMessage());
					}
				}
			}
		});
	}

	@FXML
	private TextField UpdateCustomerDate;

	@FXML
	private TextField UpdateCustomerID;

	/**/
	@FXML
	public void UpdateO(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to update the order?");
		confirmAlert.setContentText("This will update the order.");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (OrdTV.getSelectionModel().getSelectedItem() != null) {
					Order selectedOrder = OrdTV.getSelectionModel().getSelectedItem();
					if (UpdateCustomerID.getText().isBlank()) {
						ResAlert("Error !", "TextField is empty.");
					} else {
						try {
							LocalDate currentDate = LocalDate.now();
							Date sqlDate = java.sql.Date.valueOf(currentDate);
							int orderID = selectedOrder.getOrderID();
							Order updatedOrder = new Order(sqlDate, Integer.parseInt(UpdateCustomerID.getText()));
							m.updateOrder(con(), updatedOrder, orderID);
							OrdTV.getItems().clear();
							getDataOrder();
							UpdateCustomerID.clear();
						} catch (Exception e) {
							ResAlert("Error !", e.getMessage());
						}
					}
				} else {
					ResAlert("Error !", "No selected order to update.");
				}
			}
		});
	}

	@FXML
	public void DeleteOrder(ActionEvent event) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation interface");
		confirmAlert.setHeaderText("Are you sure you want to delete order?");
		confirmAlert.setContentText("This will delete order . ");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				if (OrdTV.getSelectionModel().getSelectedItem() != null) {
					Order s = OrdTV.getSelectionModel().getSelectedItem();
					int OID = s.getOrderID();
					try {
						m.deleteOrder(con(), OID);
						OrdTV.getItems().clear();
						getDataOrder();

					} catch (Exception e) {

						e.printStackTrace();
					}
				} else {
					ResAlert("Error !", "no selected Order to Delete");
				}
			}
		});

	}

	/////////////////////////////////

	public void getDataOrderProduct() {

		Connection conn = con();
		ObservableList<OrderProduct> list = FXCollections.observableArrayList();
		String query = "SELECT o.Order_ID, o.ProductID, o.Quantity FROM OrderProduct o";

		try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				int orderId = rs.getInt("Order_ID");
				int productId = rs.getInt("ProductID");
				int quantity = rs.getInt("Quantity");

				OrderProduct orderProduct = new OrderProduct(orderId, productId, quantity);
				list.add(orderProduct);
			}
			OrdPTV.setItems(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@FXML
	private TableView<Order> DayTV = new TableView<>();

	@FXML
	private TableColumn<Order, Double> Amountday = new TableColumn<>();

	@FXML
	private TableColumn<Order, Integer> CustomerIDday = new TableColumn<>();

	@FXML
	private TableColumn<Order, String> Dateday = new TableColumn<>();

	@FXML
	private TableColumn<Order, Integer> IDday = new TableColumn<>();

	@FXML
	private TableView<Phone> ProductTV1 = new TableView<>();
	@FXML
	private TableView<Accessories> ProductTV2 = new TableView<>();

	@FXML
	private TableView<Employee> EmpTV = new TableView<>();

	@FXML
	private TableColumn<Phone, Integer> ProductTV1R1 = new TableColumn<>();

	@FXML
	private TableColumn<Phone, String> ProductTV1R2 = new TableColumn<>();

	@FXML
	private TableColumn<Phone, String> ProductTV1R3 = new TableColumn<>();

	@FXML
	private TableColumn<Phone, String> ProductTV1R4 = new TableColumn<>();

	@FXML
	private TableColumn<Phone, String> ProductTV1R5 = new TableColumn<>();

	@FXML
	private TableColumn<Phone, String> ProductTV1R6 = new TableColumn<>();

	@FXML
	private TableColumn<Phone, String> ProductTV1R7 = new TableColumn<>();

	/////////////////////////////////////

	@FXML
	private TableColumn<Accessories, Integer> ProductTV2R1 = new TableColumn<>();

	@FXML
	private TableColumn<Accessories, String> ProductTV2R2 = new TableColumn<>();

	@FXML
	private TableColumn<Accessories, String> ProductTV2R3 = new TableColumn<>();

	@FXML
	private TableColumn<Accessories, String> ProductTV2R4 = new TableColumn<>();

	@FXML
	private TableColumn<Accessories, String> ProductTV2R5 = new TableColumn<>();

	@FXML
	private TableColumn<Accessories, String> ProductTV2R6 = new TableColumn<>();

	@FXML
	private TableColumn<Accessories, String> ProductTV2R7 = new TableColumn<>();
//////////////////////////
	@FXML
	private TableColumn<Employee, Integer> EmpTVr1 = new TableColumn<>();

	@FXML
	private TableColumn<Employee, String> EmpTVr2 = new TableColumn<>();

	@FXML
	private TableColumn<Employee, String> EmpTVr3 = new TableColumn<>();

	@FXML
	private TableColumn<Employee, String> EmpTVr4 = new TableColumn<>();

	@FXML
	private TableColumn<Employee, String> EmpTVr5 = new TableColumn<>();

	@FXML
	private TableColumn<Employee, String> EmpTVr6 = new TableColumn<>();
	///////////////////////
	@FXML
	private TableView<Customer> CusTV = new TableView<>();

	@FXML
	private TableColumn<Customer, Integer> CusTVr1 = new TableColumn<>();

	@FXML
	private TableColumn<Customer, String> CusTVr2 = new TableColumn<>();

	@FXML
	private TableColumn<Customer, String> CusTVr3 = new TableColumn<>();

	@FXML
	private TableColumn<Customer, String> CusTVr4 = new TableColumn<>();

	///////////////////////////////////
	@FXML
	private TableView<Order> OrdTV = new TableView<>();

	@FXML
	private TableColumn<Order, Integer> OrdTVr1 = new TableColumn<>();

	@FXML
	private TableColumn<Order, String> OrdTVr2 = new TableColumn<>();

	@FXML
	private TableColumn<Order, Double> OrdTVr3 = new TableColumn<>();

	@FXML
	private TableColumn<Order, Integer> OrdTVr4 = new TableColumn<>();

	ObservableList<Accessories> i = FXCollections.observableArrayList();

	@FXML
	public void OpenCustomer(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/NewCustomer.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@FXML
	public void OpenEmployee(ActionEvent event) {

		try {
			Parent root = FXMLLoader.load(getClass().getResource("/NewEmployee.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@FXML
	public void OpenOP(ActionEvent event) {

		try {
			Parent root = FXMLLoader.load(getClass().getResource("/NewOrderProducts.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@FXML
	public void OpenOrder(ActionEvent event) {

		try {
			OrdTV.getItems().clear();
			getDataOrder();
			Parent root = FXMLLoader.load(getClass().getResource("/NewOrder.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public void Openyear(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/Year.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public void Openmonth(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/Month.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@FXML
	private BarChart<String, Number> BAR;

	public void Opentop(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/top.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public void Openoutof(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/outof.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@FXML
	private PieChart piechart;

	@FXML
	private TextField yearTF;

	public void ShowYear() {
		PieChart newPieChart = m.generateTotalSalesByMonthReport(con(), Integer.parseInt(yearTF.getText()));
		piechart.setData(newPieChart.getData());

		if (piechart.getScene() != null) {
			applyStyles();
		} else {
			piechart.sceneProperty().addListener((obs, oldScene, newScene) -> {
				if (newScene != null) {
					applyStyles();
				}
			});
		}
	}

	private void applyStyles() {
		Node chartLabel = piechart.lookup(".chart-pie-label");
		if (chartLabel != null) {
			chartLabel.setStyle("-fx-text-fill: #0d203b;");
		}
		Node tooltip = piechart.lookup(".tooltip");
		if (tooltip != null) {
			tooltip.setStyle("-fx-text-fill: white; -fx-background-color: black;");
		}
	}

	@FXML
	public void OpenProduct(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/NewProduct.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	static int s = 9;

	@FXML
	public void BackToManeger(ActionEvent event) {

		if (s == 1) {

			try {

				Parent root = FXMLLoader.load(getClass().getResource("/Maneger.fxml"));
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (Exception e) {

				e.printStackTrace();

			}
		}

		else if (s == -1) {
			try {

				Parent root = FXMLLoader.load(getClass().getResource("/Employee2.fxml"));
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (Exception e) {

				e.printStackTrace();

			}

		}

	}

	@FXML
	public void main(ActionEvent event) {

		s = m.checkLogin(con(), text.getText(), pass.getText());

		if (text.getText().isBlank() || pass.getText().isBlank()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Input Error");
			alert.setHeaderText("Wrong Data entered");
			alert.setContentText(" Either User name  or the password is Empty");
			alert.show();
			return;

		}

		if (s == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Input Error");
			alert.setHeaderText("Wrong Data entered");
			alert.setContentText(" Either User name is Wrong or the password is wrong");
			alert.show();
			return;

		}

		if (s == 1) {

			try {

				Parent root = FXMLLoader.load(getClass().getResource("/Maneger.fxml"));
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (Exception e) {

				e.printStackTrace();

			}

		}

		if (s == -1) {

			try {

				Parent root = FXMLLoader.load(getClass().getResource("/Employee2.fxml"));
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (Exception e) {

				e.printStackTrace();

			}

		}

	}

	@FXML
	public void LogOut(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/Login2.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void getDataPhones() {
		Connection conn = con();
		ObservableList<Phone> list = FXCollections.observableArrayList();
		String query = "SELECT p.ProductID, p.Brand, p.Price, p.Color, p.StockQuantity, m.Model, m.Mob_Storage "
				+ "FROM Product p JOIN Mobile m ON p.ProductID = m.ProductID";

		try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Phone phone = new Phone();
				phone.setID(rs.getInt("ProductID"));
				phone.setBrand(rs.getString("Brand"));
				phone.setPrice(rs.getDouble("Price"));
				phone.setColor(rs.getString("Color"));
				phone.setStockQuantity(rs.getInt("StockQuantity"));
				phone.setModel(rs.getString("Model"));
				phone.setStorage(rs.getInt("Mob_Storage"));

				list.add(phone);
			}
			ProductTV1.setItems(list);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static Boolean eye = false;

	@FXML
	public void eye() {

		eye = !eye;

		if (eye) {
			pass.setVisible(true);
		} else {
			pass.setVisible(false);
		}
	}

	////////////////////////////////////

	public void getDataAccessories() {
		Connection conn = con();
		ObservableList<Accessories> list = FXCollections.observableArrayList();
		String query = "SELECT p.ProductID, p.Brand, p.Price, p.Color, p.StockQuantity, a.Acc_Type,a.Acc_Benefite "
				+ "FROM Product p JOIN Accessory a ON p.ProductID = a.ProductID";

		try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Accessories accessory = new Accessories();
				accessory.setID(rs.getInt("ProductID"));
				accessory.setBrand(rs.getString("Brand"));
				accessory.setPrice(rs.getDouble("Price"));
				accessory.setColor(rs.getString("Color"));
				accessory.setStockQuantity(rs.getInt("StockQuantity"));
				accessory.setType(rs.getString("Acc_Type"));
				accessory.setBenefite(rs.getString("Acc_Benefite"));

				list.add(accessory);
			}
			ProductTV2.setItems(list);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private TextField Brand;
	@FXML
	private TextField Price;
	@FXML
	private TextField Color;
	@FXML
	private TextField stockQuantity;
	@FXML
	private TextField Model;
	@FXML
	private TextField Storage;
	@FXML
	private TextField ProductID;
	@FXML
	private TextField ProductID2;
	@FXML
	private TextField NewBrand;
	@FXML
	private TextField NewPrice;
	@FXML
	private TextField NewColor;
	@FXML
	private TextField NewstockQuantity;
	@FXML
	private TextField NewModel;

	@FXML
	private TextField insertEmployee;

	@FXML
	private TextField InsertEmployeePH;
	@FXML
	private TextField InsertEmployeeAddress;
	@FXML
	private TextField InsertEmployeeEmail;

	@FXML
	private TextField IDEmp;

	@FXML
	private TextField IDEmp2;
	@FXML
	private TextField Empname2;
	@FXML
	private TextField EmpPhone2;
	@FXML
	private TextField empAddress2;
	@FXML
	private TextField EmployeeEmail2;

	@FXML
	private TextField Totalamount;
	@FXML
	private TextField Date;

	@FXML
	private TextField Quantity;
	@FXML
	private TextField OrderID;

	@FXML
	private TextField ProductID1;

	@FXML
	public void InsertOrderProduct(ActionEvent event) {

		m.insertOrderProduct(con(), Integer.parseInt(OrderID.getText()), Integer.parseInt(ProductID1.getText()),
				Integer.parseInt(Quantity.getText()));

		OrderID.clear();
		ProductID1.clear();
		Quantity.clear();

	}

	@FXML
	private PasswordField pass;

	@FXML
	private TextField text;

	public static Connection con() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/phoneShop", "root",
					"ameer1221147");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("Select * From Customer");
			return con;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@FXML
	private TextField userid;

	@FXML
	private TextField username;

	@FXML
	private TextField password;

	@FXML
	public void SignUp(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/SignUp.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@FXML
	public void create(ActionEvent event) {

		LogIn_Emp kkdkl = new LogIn_Emp(username.getText(), password.getText());
		m.insertLog(con(), Integer.parseInt(userid.getText()), kkdkl);
		username.clear();
		password.clear();
		userid.clear();

	}

	@FXML
	public void Back(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/NewEmployee.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@FXML
	public void Customer(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/Customer.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@FXML
	public void Order(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/Order.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@FXML
	public void Paymentmethod(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/Paymentmethod.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@FXML
	public void Employee(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/Employee.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@FXML
	public void Product(ActionEvent event) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/Product.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public void getEmployees() {
		Connection conn = con();
		ObservableList<Employee> list = FXCollections.observableArrayList();
		String query = "SELECT Emp_ID, Emp_Name, Email, Phone, Address, Emp_Role FROM Employee";

		try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Employee employee = new Employee();
				employee.setID(rs.getInt("Emp_ID"));
				employee.setName(rs.getString("Emp_Name"));
				employee.setEmail(rs.getString("Email"));
				employee.setPhone(rs.getString("Phone"));
				employee.setAddress(rs.getString("Address"));
				employee.setEmpRole(rs.getString("Emp_Role"));

				list.add(employee);
			}
			EmpTV.setItems(list);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getDataCustomer() {
		Connection conn = con();
		ObservableList<Customer> list = FXCollections.observableArrayList();
		String query = "SELECT Cust_ID, Cust_Name, Phone, Address FROM Customer";

		try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Customer customer = new Customer();
				customer.setID(rs.getInt("Cust_ID"));
				customer.setName(rs.getString("Cust_Name"));
				customer.setPhone(rs.getString("Phone"));
				customer.setAddress(rs.getString("Address"));

				list.add(customer);
			}
			CusTV.setItems(list);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getDataOrder() {
		Connection conn = con();
		ObservableList<Order> list = FXCollections.observableArrayList();
		String query = "SELECT o.Order_ID, o.OrderDate, o.TotalAmount, c.Cust_ID " + "FROM cust_Order o "
				+ "JOIN Customer c ON o.Cust_ID = c.Cust_ID";

		try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				int orderID = rs.getInt("Order_ID");
				Date orderDate = rs.getDate("OrderDate");
				double totalAmount = m.calculateTotalAmount(conn, rs.getInt("Order_ID"));
				int custID = rs.getInt("Cust_ID");

				Order order = new Order(orderID, orderDate, totalAmount, custID);
				list.add(order);
			}
			OrdTV.setItems(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private TextField PhoneSearch;

	@FXML
	public void SearchPhones() {
		if (PhoneSearch.getText().isBlank()) {
			return;
		}
		Connection conn = con();
		ObservableList<Phone> list = FXCollections.observableArrayList();
		String query = "SELECT p.ProductID, p.Brand, p.Price, p.Color, p.StockQuantity, m.Model, m.Mob_Storage "
				+ "FROM Product p JOIN Mobile m ON p.ProductID = m.ProductID WHERE p.Brand LIKE ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, PhoneSearch.getText() + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Phone phone = new Phone();
					phone.setID(rs.getInt("ProductID"));
					phone.setBrand(rs.getString("Brand"));
					phone.setPrice(rs.getDouble("Price"));
					phone.setColor(rs.getString("Color"));
					phone.setStockQuantity(rs.getInt("StockQuantity"));
					phone.setModel(rs.getString("Model"));
					phone.setStorage(rs.getInt("Mob_Storage"));
					list.add(phone);
				}
				ProductTV1.getItems().clear();
				ProductTV1.setItems(list);
				PhoneSearch.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void Reloadphone() {
		ProductTV1.getItems().clear();
		getDataPhones();
	}

	@FXML
	private TextField AccSearch;

	@FXML
	public void SearchAccessories() {
		if (AccSearch.getText().isBlank()) {
			return;
		}
		Connection conn = con();
		ObservableList<Accessories> list = FXCollections.observableArrayList();
		String query = "SELECT p.ProductID, p.Brand, p.Price, p.Color, p.StockQuantity, a.Acc_Type, a.Acc_Benefite "
				+ "FROM Product p JOIN Accessory a ON p.ProductID = a.ProductID WHERE p.Brand LIKE ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, AccSearch.getText() + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Accessories accessory = new Accessories();
					accessory.setID(rs.getInt("ProductID"));
					accessory.setBrand(rs.getString("Brand"));
					accessory.setPrice(rs.getDouble("Price"));
					accessory.setColor(rs.getString("Color"));
					accessory.setStockQuantity(rs.getInt("StockQuantity"));
					accessory.setType(rs.getString("Acc_Type"));
					accessory.setBenefite(rs.getString("Acc_Benefite"));

					list.add(accessory);
				}
				ProductTV2.getItems().clear();
				ProductTV2.setItems(list);
				AccSearch.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void Reloadacc() {

		ProductTV2.getItems().clear();
		getDataAccessories();
	}

	@FXML
	private TextField CusSearch;

	@FXML
	public void SearchCustomer() {
		if (CusSearch.getText().isBlank()) {
			return;
		}
		Connection conn = con();
		ObservableList<Customer> list = FXCollections.observableArrayList();
		String query = "SELECT Cust_ID, Cust_Name, Phone, Address FROM Customer WHERE Cust_Name LIKE ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, CusSearch.getText() + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Customer customer = new Customer();
					customer.setID(rs.getInt("Cust_ID"));
					customer.setName(rs.getString("Cust_Name"));
					customer.setPhone(rs.getString("Phone"));
					customer.setAddress(rs.getString("Address"));

					list.add(customer);
				}
				CusTV.getItems().clear();
				CusTV.setItems(list);
				CusSearch.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ReloadCus() {

		CusTV.getItems().clear();
		getDataCustomer();
	}

	@FXML
	private TextField OrderSearch;

	@FXML
	public void searchOrder() {
		if (OrderSearch.getText().isBlank()) {
			return;
		}
		Connection conn = con();
		ObservableList<Order> list = FXCollections.observableArrayList();
		String query = "SELECT o.Order_ID, o.OrderDate, o.TotalAmount, c.Cust_ID FROM cust_Order o "
				+ "JOIN Customer c ON o.Cust_ID = c.Cust_ID WHERE o.Order_ID LIKE ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, OrderSearch.getText() + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int orderID = rs.getInt("Order_ID");
					Date orderDate = rs.getDate("OrderDate");
					double totalAmount = m.calculateTotalAmount(conn, rs.getInt("Order_ID"));
					int custID = rs.getInt("Cust_ID");

					Order order = new Order(orderID, orderDate, totalAmount, custID);
					list.add(order);
				}
				OrdTV.getItems().clear();
				OrdTV.setItems(list);
				OrderSearch.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ReloadOrder() {

		OrdTV.getItems().clear();
		getDataOrder();
	}

	@FXML
	private TextField EmpSearch;

	public void SearchEmployees() {
		if (EmpSearch.getText().isBlank()) {
			return;
		}
		Connection conn = con();
		ObservableList<Employee> list = FXCollections.observableArrayList();
		String query = "SELECT Emp_ID, Emp_Name, Email, Phone, Address, Emp_Role FROM Employee WHERE Emp_Name LIKE ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, EmpSearch.getText() + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Employee employee = new Employee();
					employee.setID(rs.getInt("Emp_ID"));
					employee.setName(rs.getString("Emp_Name"));
					employee.setEmail(rs.getString("Email"));
					employee.setPhone(rs.getString("Phone"));
					employee.setAddress(rs.getString("Address"));
					employee.setEmpRole(rs.getString("Emp_Role"));

					list.add(employee);
				}
				EmpTV.getItems().clear();
				EmpTV.setItems(list);
				EmpSearch.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ReloadEmp() {

		EmpTV.getItems().clear();
		getEmployees();
	}

	@FXML
	public void initialize() {

		CusTV.setOnMouseClicked(e -> {

			Customer o = CusTV.getSelectionModel().getSelectedItem();

			try {
				UpdateCusName.setText(o.getName());
				UpdateCusphone.setText(o.getPhone());
				UpdateCusaddress.setText(o.getAddress());
			} catch (Exception e1) {
			}

		});

		OrdTV.setOnMouseClicked(e -> {

			Order o = OrdTV.getSelectionModel().getSelectedItem();

			try {
				UpdateCustomerID.setText(o.getCustomerID() + "");
			} catch (Exception e1) {
			}

		});

		EmpTV.setOnMouseClicked(e -> {
			Employee emp = EmpTV.getSelectionModel().getSelectedItem();

			try {
				UpdateEmpName.setText(emp.getName());
				UpdateEmpemail.setText(emp.getEmail());
				UpdateEmpphone.setText(emp.getPhone());
				UpdateEmpaddress.setText(emp.getAddress());
				Compo1.setValue(emp.getEmpRole());
			} catch (Exception e1) {
			}

		});

		ProductTV2.setOnMouseClicked(e -> {
			Accessories acc = ProductTV2.getSelectionModel().getSelectedItem();

			try {
				AccBrand.setText(acc.getBrand());
				AccColor.setText(acc.getColor());
				AccPrice.setText(acc.getPrice() + "");
				AccSQ.setText(acc.getStockQuantity() + "");
				Acctype.setText(acc.getType());
				Accbene.setText(acc.getBenefite());
			} catch (Exception e1) {
			}

		});
		ProductTV1.setOnMouseClicked(e -> {
			Phone acc = ProductTV1.getSelectionModel().getSelectedItem();

			try {
				phoneBrand.setText(acc.getBrand());
				phoneprice.setText(acc.getPrice() + "");
				phonecolor.setText(acc.getColor());
				phoneSQ.setText(acc.getStockQuantity() + "");
				phonemodel.setText(acc.getModel());
				phoneStorage.setText(acc.getStorage() + "");

			} catch (Exception e1) {
			}

		});

		Compo.getItems().addAll("Employee", "Manager");
		Compo1.getItems().addAll("Employee", "Manager");
		Compo.setPromptText("Select Role");
		Compo1.setPromptText("Select Role");

		IDday.setCellValueFactory(new PropertyValueFactory<>("orderID"));
		Dateday.setCellValueFactory(new PropertyValueFactory<>("date"));
		CustomerIDday.setCellValueFactory(new PropertyValueFactory<>("price"));
		Amountday.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));

		OrdTVr1.setCellValueFactory(new PropertyValueFactory<>("orderID"));
		OrdTVr2.setCellValueFactory(new PropertyValueFactory<>("date"));
		OrdTVr3.setCellValueFactory(new PropertyValueFactory<>("price"));
		OrdTVr4.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));

		ProductTV1R1.setCellValueFactory(new PropertyValueFactory<>("ID"));
		ProductTV1R2.setCellValueFactory(new PropertyValueFactory<>("brand"));
		ProductTV1R3.setCellValueFactory(new PropertyValueFactory<>("price"));
		ProductTV1R4.setCellValueFactory(new PropertyValueFactory<>("color"));
		ProductTV1R5.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
		ProductTV1R6.setCellValueFactory(new PropertyValueFactory<>("model"));
		ProductTV1R7.setCellValueFactory(new PropertyValueFactory<>("storage"));

		ProductTV2R1.setCellValueFactory(new PropertyValueFactory<>("ID"));
		ProductTV2R2.setCellValueFactory(new PropertyValueFactory<>("brand"));
		ProductTV2R3.setCellValueFactory(new PropertyValueFactory<>("price"));
		ProductTV2R4.setCellValueFactory(new PropertyValueFactory<>("color"));
		ProductTV2R5.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
		ProductTV2R6.setCellValueFactory(new PropertyValueFactory<>("type"));
		ProductTV2R7.setCellValueFactory(new PropertyValueFactory<>("benefite"));

		EmpTVr1.setCellValueFactory(new PropertyValueFactory<>("ID"));
		EmpTVr2.setCellValueFactory(new PropertyValueFactory<>("name"));
		EmpTVr3.setCellValueFactory(new PropertyValueFactory<>("email"));
		EmpTVr4.setCellValueFactory(new PropertyValueFactory<>("phone"));
		EmpTVr5.setCellValueFactory(new PropertyValueFactory<>("address"));
		EmpTVr6.setCellValueFactory(new PropertyValueFactory<>("empRole"));

		CusTVr1.setCellValueFactory(new PropertyValueFactory<>("ID"));
		CusTVr2.setCellValueFactory(new PropertyValueFactory<>("name"));
		CusTVr3.setCellValueFactory(new PropertyValueFactory<>("phone"));
		CusTVr4.setCellValueFactory(new PropertyValueFactory<>("address"));

		OrdPTVr1.setCellValueFactory(new PropertyValueFactory<>("orderId"));
		OrdPTVr2.setCellValueFactory(new PropertyValueFactory<>("productId"));
		OrdPTVr3.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		getDataAccessories();
		getDataPhones();
		getEmployees();
		getDataCustomer();
		getDataOrder();
		getDataOrderProduct();

		BarChart<String, Number> newBarChart = m.generateTopSellingProductsReport(con());

		if (BAR != null) {
			BAR.setData(newBarChart.getData());
		} else {

		}
	}

	public void generateOrdersByDayReport(Connection con, LocalDate date) {
		String query = "SELECT Order_ID, OrderDate, TotalAmount, Cust_ID " + "FROM cust_order "
				+ "WHERE YEAR(OrderDate) = ? AND MONTH(OrderDate) = ? AND DAY(OrderDate) = ? " + "ORDER BY OrderDate";

		ObservableList<Order> orders = FXCollections.observableArrayList();
		try (PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, date.getYear());
			pstmt.setInt(2, date.getMonthValue());
			pstmt.setInt(3, date.getDayOfMonth());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int orderId = rs.getInt("Order_ID");
				Date orderDate = rs.getDate("OrderDate");
				double totalAmount = rs.getDouble("TotalAmount");
				int customerId = rs.getInt("Cust_ID");
				System.out.println(orderId);
				orders.add(new Order(orderId, orderDate, totalAmount, customerId));
			}
			if (DayTV != null) {
				DayTV.setItems(orders);
				DayTV.refresh();
			} else {
				System.out.println("DayTV TableView is not initialized.");
			}

		} catch (SQLException ex) {
			System.out.println("Error generating orders by day report: " + ex.getMessage());
		}
	}

	@FXML
	private DatePicker DateIDDay;

	@FXML
	private TextField SalaryO;

	@FXML
	private TextField cOrder;

	public void Showmonth() {
		SalaryO.clear();
		cOrder.clear();
		DayTV.getItems().clear();
		LocalDate localDate = DateIDDay.getValue();
		if (localDate != null) {
			java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
			generateOrdersByDayReport(con(), localDate);
			double sal = m.calculateTotalSalesByDay(con(), sqlDate);
			int count = m.countOrdersByDay(con(), sqlDate);
			SalaryO.setText(sal + "");
			cOrder.setText(count + "");
		} else {
			System.out.println("Please select a date.");
		}
	}

	private void ResAlert(String st, String discribtion) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(st);
		alert.setContentText(discribtion);
		alert.setHeaderText(null);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setPrefSize(370, 170);
		dialogPane.setStyle("-fx-border-color: red; " + "-fx-border-width: 3; " + "-fx-padding: 13; "
				+ "-fx-font-size: 12px; " + "-fx-font-family: 'Arial';");

		alert.showAndWait();
	}

}
