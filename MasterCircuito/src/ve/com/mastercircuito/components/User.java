package ve.com.mastercircuito.components;

import org.joda.time.DateTime;

import ve.com.mastercircuito.db.Db;

public class User {
	
	private Integer id;
	private String username;
	private String password;
	private String passport;
	private UserType userType;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private DateTime dateCreated;
	private Boolean status;
	
	public User() {
		super();
	}
	
	public User(Integer id, String username, String password, String passport, UserType userType, String firstName,
			String lastName, String email, String phone, DateTime dateCreated, Boolean status) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.passport = passport;
		this.userType = userType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.dateCreated = dateCreated;
		this.status = status;
	}
	
	public User(User sellerInfo) {
		this.id = sellerInfo.getId();
		this.username = sellerInfo.getUsername();
		this.password = sellerInfo.getPassword();
		this.passport = sellerInfo.getPassport();
		this.userType = sellerInfo.getUserType();
		this.firstName = sellerInfo.getFirstName();
		this.lastName = sellerInfo.getLastName();
		this.email = sellerInfo.getEmail();
		this.phone = sellerInfo.getPhone();
		this.dateCreated = sellerInfo.getDateCreated();
		this.status = sellerInfo.getStatus();
	}

	public User(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassport() {
		return this.passport;
	}
	
	public void setPassport(String passport) {
		this.passport = passport;
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
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void fetchUserInfo() {
		if(null != username) {
			Db db = new Db();
			User user = db.fetchUserInfo(username);
			this.setId(user.getId());
			this.setUsername(user.getUsername());
			this.setPassword(user.getPassword());
			this.setPassport(user.getPassport());
			this.setUserType(user.getUserType());
			this.setFirstName(user.getFirstName());
			this.setLastName(user.getLastName());
			this.setEmail(user.getEmail());
			this.setPhone(user.getPhone());
			this.setDateCreated(user.getDateCreated());
			this.setStatus(user.getStatus());
		}
	}
	
}
