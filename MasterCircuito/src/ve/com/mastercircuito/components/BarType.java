package ve.com.mastercircuito.components;

public class BarType {
	
	private Integer id;
	private String barType;

	public BarType() {
		super();
	}

	public BarType(Integer id, String barType) {
		super();
		this.id = id;
		this.barType = barType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBarType() {
		return barType;
	}

	public void setBarType(String barType) {
		this.barType = barType;
	}
	
}
