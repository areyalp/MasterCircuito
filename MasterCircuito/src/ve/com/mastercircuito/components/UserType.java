package ve.com.mastercircuito.components;

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
