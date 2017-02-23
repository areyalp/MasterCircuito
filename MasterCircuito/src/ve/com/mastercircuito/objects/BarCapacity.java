package ve.com.mastercircuito.objects;

public class BarCapacity {
	
	private Integer id;
	private Integer barCapacity;

	public BarCapacity() {
		super();
	}

	public BarCapacity(Integer id, Integer barCapacity) {
		super();
		this.id = id;
		this.barCapacity = barCapacity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBarCapacity() {
		return barCapacity;
	}

	public void setBarCapacity(Integer barCapacity) {
		this.barCapacity = barCapacity;
	}
	
}
