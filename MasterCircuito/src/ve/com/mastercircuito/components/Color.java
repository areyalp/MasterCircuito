package ve.com.mastercircuito.components;

public class Color {
	
	private Integer id;
	private String color;

	public Color() {
		super();
	}

	public Color(Integer id, String color) {
		super();
		this.id = id;
		this.color = color;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
}
