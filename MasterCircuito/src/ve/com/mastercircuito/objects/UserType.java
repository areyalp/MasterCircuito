package ve.com.mastercircuito.objects;

public class UserType {
	
	private Integer id;
	private String type;
	
	public UserType() {
		
	}
	
	public UserType(Integer id, String type) {
		super();
		this.id = id;
		this.type = type;
	}
	
	public UserType(UserType userTypeInfo) {
		this.id = userTypeInfo.getId();
		this.type = userTypeInfo.getType();
	}

	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getId() {
		return this.id;
	}
	
}
