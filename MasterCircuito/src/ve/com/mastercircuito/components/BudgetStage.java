package ve.com.mastercircuito.components;

public class BudgetStage {
	
	private Integer id;
	private String stage;
	
	public BudgetStage() {
		super();
	}
	
	public BudgetStage(Integer id, String stage) {
		super();
		this.id = id;
		this.stage = stage;
	}
	
	public String getStage() {
		return this.stage;
	}
	
	public void setStage(String stage) {
		this.stage = stage;
	}
	
	public Integer getId() {
		return this.id;
	}
	
}
