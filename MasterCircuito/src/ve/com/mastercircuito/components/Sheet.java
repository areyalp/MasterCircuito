package ve.com.mastercircuito.components;

public class Sheet {
	
	private Integer id;
	private String sheet;

	public Sheet() {
		super();
	}

	public Sheet(Integer id, String sheet) {
		super();
		this.id = id;
		this.sheet = sheet;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSheet() {
		return sheet;
	}

	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	
}
