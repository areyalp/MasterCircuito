package ve.com.mastercircuito.objects;

public class Client {
	
	private Integer id;
	private String client;
	private String code;
	private String representative;
	private String rif;
	private String address;
	private String phone;
	private String email;
	private String facebookProfile;
	private String twitterUser;
	private String instagramUser;
	
	public Client() {
		super();
	}

	public Client(Integer id, String client, String code, String representative, String rif, String address,
			String phone, String email, String facebookProfile, String twitterUser, String instagramUser) {
		super();
		this.id = id;
		this.client = client;
		this.code = code;
		this.representative = representative;
		this.rif = rif;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.facebookProfile = facebookProfile;
		this.twitterUser = twitterUser;
		this.instagramUser = instagramUser;
	}
	
	public Client(Client clientInfo) {
		this.id = clientInfo.id;
		this.client = clientInfo.client;
		this.code = clientInfo.code;
		this.representative = clientInfo.representative;
		this.rif = clientInfo.rif;
		this.address = clientInfo.address;
		this.phone = clientInfo.phone;
		this.email = clientInfo.email;
		this.facebookProfile = clientInfo.facebookProfile;
		this.twitterUser = clientInfo.twitterUser;
		this.instagramUser = clientInfo.instagramUser;
	}

	public String getClient() {
		return this.client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRepresentative() {
		return this.representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public String getRif() {
		return this.rif;
	}

	public void setRif(String rif) {
		this.rif = rif;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFacebookProfile() {
		return this.facebookProfile;
	}

	public void setFacebookProfile(String facebookProfile) {
		this.facebookProfile = facebookProfile;
	}

	public String getTwitterUser() {
		return this.twitterUser;
	}

	public void setTwitterUser(String twitterUser) {
		this.twitterUser = twitterUser;
	}

	public String getInstagramUser() {
		return this.instagramUser;
	}

	public void setInstagramUser(String instagramUser) {
		this.instagramUser = instagramUser;
	}

	public Integer getId() {
		return this.id;
	}
	
}