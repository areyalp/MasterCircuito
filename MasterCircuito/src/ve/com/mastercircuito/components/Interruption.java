package ve.com.mastercircuito.components;

public class Interruption {
	
	private Integer id;
	private Integer interruption;

	public Interruption() {
		super();
	}

	public Interruption(Integer id, Integer interruption) {
		super();
		this.id = id;
		this.interruption = interruption;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getInterruption() {
		return interruption;
	}

	public void setInterruption(Integer interruption) {
		this.interruption = interruption;
	}
	
}
