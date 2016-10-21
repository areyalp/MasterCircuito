package ve.com.mastercircuito.components;

import org.joda.time.DateTime;

public class Seller {
	
	private Integer id;
	private String username;
	private String passport;
	private String password;
	private UserType userType;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private DateTime dateCreated;
	private Boolean status;
	
	public Seller() {
		
	}
	
	public Seller(Integer id, String username, String passport, String password, UserType userType, String firstName,
			String lastName, String email, String phone, DateTime dateCreated, Boolean status) {
		super();
		this.id = id;
		this.username = username;
		this.passport = passport;
		this.password = password;
		this.userType = userType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.dateCreated = dateCreated;
		this.status = status;
	}
	
	public Seller(Seller sellerInfo) {
		this.id = sellerInfo.getId();
		this.username = sellerInfo.getUsername();
		this.passport = sellerInfo.getPassport();
		this.password = sellerInfo.getPassword();
		this.userType = sellerInfo.getUserType();
		this.firstName = sellerInfo.getFirstName();
		this.lastName = sellerInfo.getLastName();
		this.email = sellerInfo.getEmail();
		this.phone = sellerInfo.getPhone();
		this.dateCreated = sellerInfo.getDateCreated();
		this.status = sellerInfo.getStatus();
	}

	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassport() {
		return this.passport;
	}
	
	public void setPassport(String passport) {
		this.passport = passport;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserType getUserType() {
		return this.userType;
	}
	
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public DateTime getDateCreated() {
		return this.dateCreated;
	}
	
	public void setDateCreated(DateTime dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public Boolean getStatus() {
		return this.status;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public Integer getId() {
		return this.id;
	}
	
}
