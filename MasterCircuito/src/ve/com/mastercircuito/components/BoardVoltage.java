package ve.com.mastercircuito.components;

public class BoardVoltage {
	
	private Integer id;
	private String voltage;

	public BoardVoltage() {
		super();
	}

	public BoardVoltage(Integer id, String voltage) {
		super();
		this.id = id;
		this.voltage = voltage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	
}
