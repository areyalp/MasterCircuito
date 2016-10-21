package ve.com.mastercircuito.components;

public class Caliber {
	
	private Integer id;
	private Integer caliber;

	public Caliber() {
		super();
	}

	public Caliber(Integer id, Integer caliber) {
		super();
		this.id = id;
		this.caliber = caliber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCaliber() {
		return caliber;
	}

	public void setCaliber(Integer caliber) {
		this.caliber = caliber;
	}
	
}
