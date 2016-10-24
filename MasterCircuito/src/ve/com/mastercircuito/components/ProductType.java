package ve.com.mastercircuito.components;

public class ProductType {
	
	public static final Integer SWITCH_TYPE = 1;
	public static final Integer BOX_TYPE = 2;
	public static final Integer BOARD_TYPE = 3;
	public static final Integer MATERIAL_TYPE = 4;
	public static final Integer SPECIALS_TYPE = 5;
	
	private Integer id;
	private String type;
	private String table;

	public ProductType() {
		super();
	}

	public ProductType(Integer id, String type, String table) {
		super();
		this.id = id;
		this.type = type;
		this.table = table;
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

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
}
