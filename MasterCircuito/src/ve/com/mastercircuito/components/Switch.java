package ve.com.mastercircuito.components;

public class Switch extends Product {
	
	private Integer brandId;
	private String brand;
	private Integer typeId;
	private String type;
	private String reference;
	private Integer phases;
	private Integer currentId;
	private String current;
	private Integer voltageId;
	private String voltage;
	private Integer interruptionId;
	private String interruption;
	private Double price;
	private Boolean active;
	private Integer containerId;
	private Integer quantity;
	
	public Switch() {
		super();
	}
	
	public Switch(Integer id, Integer brandId, String brand, Integer typeId, String type, String reference, Integer phases,
			Integer currentId, String current, Integer voltageId, String voltage, Integer interruptionId,
			String interruption, Double price, Boolean active, Integer containerId, Integer quantity) {
		super();
		this.setId(id);
		this.brandId = brandId;
		this.brand = brand;
		this.typeId = typeId;
		this.type = type;
		this.reference = reference;
		this.phases = phases;
		this.currentId = currentId;
		this.current = current;
		this.voltageId = voltageId;
		this.voltage = voltage;
		this.interruptionId = interruptionId;
		this.interruption = interruption;
		this.price = price;
		this.active = active;
		this.containerId = containerId;
		this.quantity = quantity;
	}

	public Integer getBrandId() {
		return brandId;
	}

	private void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(Integer brandId, String brand) {
		this.setBrandId(brandId);
		this.brand = brand;
	}

	public Integer getTypeId() {
		return typeId;
	}

	private void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getType() {
		return type;
	}

	public void setType(Integer typeId, String type) {
		this.setTypeId(typeId);
		this.type = type;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getPhases() {
		return phases;
	}

	public void setPhases(Integer phases) {
		this.phases = phases;
	}

	public Integer getCurrentId() {
		return currentId;
	}

	private void setCurrentId(Integer currentId) {
		this.currentId = currentId;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(Integer currentId, String current) {
		this.setCurrentId(currentId);
		this.current = current;
	}

	public Integer getVoltageId() {
		return voltageId;
	}

	private void setVoltageId(Integer voltageId) {
		this.voltageId = voltageId;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(Integer voltageId, String voltage) {
		this.setVoltageId(voltageId);
		this.voltage = voltage;
	}

	public Integer getInterruptionId() {
		return interruptionId;
	}

	private void setInterruptionId(Integer interruptionId) {
		this.interruptionId = interruptionId;
	}

	public String getInterruption() {
		return interruption;
	}

	public void setInterruption(Integer interruptionId, String interruption) {
		this.setInterruptionId(interruptionId);
		this.interruption = interruption;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getContainerId() {
		return containerId;
	}

	public void setContainerId(Integer containerId) {
		this.containerId = containerId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}
