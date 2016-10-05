package ve.com.mastercircuito.components;

public class MCSwitch {
	
	private Integer id;
	private Integer brandId;
	private Integer typeId;
	private String model;
	private Integer phases;
	private Integer currentId;
	private Integer voltageId;
	private Integer interruptionId;
	private Double price;
	private Boolean active;
	
	public MCSwitch() {
		super();
	}
	
	public MCSwitch(Integer id, Integer brandId, Integer typeId, String model, Integer phases, Integer currentId,
			Integer voltageId, Integer interruptionId, Double price, Boolean active) {
		super();
		this.id = id;
		this.brandId = brandId;
		this.typeId = typeId;
		this.model = model;
		this.phases = phases;
		this.currentId = currentId;
		this.voltageId = voltageId;
		this.interruptionId = interruptionId;
		this.price = price;
		this.active = active;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public Integer getBrandId() {
		return this.brandId;
	}
	
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	
	public Integer getTypeId() {
		return this.typeId;
	}
	
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	
	public String getModel() {
		return this.model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public Integer getPhases() {
		return this.phases;
	}
	
	public void setPhases(Integer phases) {
		this.phases = phases;
	}
	
	public Integer getCurrentId() {
		return this.currentId;
	}
	
	public void setCurrentId(Integer currentId) {
		this.currentId = currentId;
	}
	
	public Integer getVoltageId() {
		return this.voltageId;
	}
	
	public void setVoltageId(Integer voltageId) {
		this.voltageId = voltageId;
	}
	
	public Integer getInterruptionId() {
		return this.interruptionId;
	}
	
	public void setInterruptionId(Integer interruptionId) {
		this.interruptionId = interruptionId;
	}
	
	public Double getPrice() {
		return this.price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Boolean getActive() {
		return this.active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
