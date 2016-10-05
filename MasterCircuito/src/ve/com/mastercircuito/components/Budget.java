package ve.com.mastercircuito.components;

import java.util.ArrayList;

import org.joda.time.DateTime;

public class Budget {
	
	private Integer id;
	private String code;
	private DateTime date;
	private Integer expiryDays;
	private Client client;
	private String workName;
	private PaymentMethod paymentMethod;
	private Seller seller;
	private DispatchPlace dispatchPlace;
	private Integer deliveryTime;
	private DeliveryPeriod deliveryPeriod;
	private Boolean tracing;
	private BudgetStage stage;
	private String notes;
	
	ArrayList<MCSwitch> switches;
	ArrayList<MCBox> boxes;
	ArrayList<MCBoard> boards;
	
	
	public Budget() {
		super();
	}
	
	
	
}
