package ve.com.mastercircuito.components;

public class Circuits {
	
	private Integer id;
	private Integer circuits;

	public Circuits() {
		super();
	}

	public Circuits(Integer id, Integer circuits) {
		super();
		this.id = id;
		this.circuits = circuits;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCircuits() {
		return circuits;
	}

	public void setCircuits(Integer circuits) {
		this.circuits = circuits;
	}
	
}
