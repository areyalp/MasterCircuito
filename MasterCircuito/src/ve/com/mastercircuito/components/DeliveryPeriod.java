package ve.com.mastercircuito.components;

public class DeliveryPeriod {
	
	private Integer id;
	private String period;
	
	public DeliveryPeriod() {
		super();
	}
	
	public DeliveryPeriod(Integer id, String period) {
		super();
		this.id = id;
		this.period = period;
	}
	
	public DeliveryPeriod(DeliveryPeriod deliveryPeriodInfo) {
		this.id = deliveryPeriodInfo.getId();
		this.period = deliveryPeriodInfo.getPeriod();
	}

	public String getPeriod() {
		return period;
	}
	
	public void setPeriod(String period) {
		this.period = period;
	}
	
	public Integer getId() {
		return id;
	}
	
}
