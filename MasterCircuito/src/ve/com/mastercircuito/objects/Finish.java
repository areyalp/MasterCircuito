package ve.com.mastercircuito.objects;

public class Finish {
	
	private Integer id;
	private String finish;

	public Finish() {
		super();
	}

	public Finish(Integer id, String finish) {
		super();
		this.id = id;
		this.finish = finish;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}
	
}
