package ve.com.mastercircuito.components;

import java.sql.Timestamp;

public class Seller extends User {

	public Seller() {
		super();
	}

	public Seller(Integer id, String username, String passport, String password, UserType userType, String firstName,
			String lastName, String email, String phone, Timestamp dateCreated, Boolean status) {
		super(id, username, passport, password, userType, firstName, lastName, email, phone, dateCreated, status);
	}

	public Seller(User sellerInfo) {
		super(sellerInfo);
	}

}
