package ve.com.mastercircuito.objects;

public class Nema {
	
	private Integer id;
	private String nema;

	public Nema() {
		super();
	}

	public Nema(Integer id, String nema) {
		super();
		this.id = id;
		this.nema = nema;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNema() {
		return nema;
	}

	public void setNema(String nema) {
		this.nema = nema;
	}
	
}
