package ve.com.mastercircuito.objects;

public class BoardType {
	
	private Integer id;
	private String type;

	public BoardType() {
		super();
	}

	public BoardType(Integer id, String type) {
		super();
		this.id = id;
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
