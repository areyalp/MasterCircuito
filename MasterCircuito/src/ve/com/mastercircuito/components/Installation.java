package ve.com.mastercircuito.components;

public class Installation {
	
	private Integer id;
	private String installation;
	
	public Installation() {
		super();
	}

	public Installation(Integer id, String installation) {
		super();
		this.id = id;
		this.installation = installation;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInstallation() {
		return installation;
	}

	public void setInstallation(String installation) {
		this.installation = installation;
	}
	
}
