package ve.com.mastercircuito.objects;

public class MeasureUnits {
	
	private Integer id;
	private String units;

	public MeasureUnits() {
		super();
	}

	public MeasureUnits(Integer id, String units) {
		super();
		this.id = id;
		this.units = units;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	
}
