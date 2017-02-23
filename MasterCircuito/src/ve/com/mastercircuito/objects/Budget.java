package ve.com.mastercircuito.objects;

import java.sql.Date;
import java.util.ArrayList;

public class Budget {
	
	private Integer id;
	private String code;
	private Date date;
	private Integer expiryDays;
	private Client client;
	private String workName;
	private PaymentMethod paymentMethod;
	private User seller;
	private DispatchPlace dispatchPlace;
	private Integer deliveryTime;
	private DeliveryPeriod deliveryPeriod;
	private Boolean tracing;
	private BudgetStage stage;
	private String notes;
	
	private ArrayList<Switch> switches;
	private ArrayList<Box> boxes;
	private ArrayList<Board> boards;
	
	
	public Budget() {
		super();
	}
	
	public Budget(Integer id, String code, Date date, Integer expiryDays, Client client, String workName,
			PaymentMethod paymentMethod, User seller, DispatchPlace dispatchPlace, Integer deliveryTime,
			DeliveryPeriod deliveryPeriod, Boolean tracing, BudgetStage stage, String notes, ArrayList<Switch> switches,
			ArrayList<Box> boxes, ArrayList<Board> boards) {
		super();
		this.id = id;
		this.code = code;
		this.date = date;
		this.expiryDays = expiryDays;
		this.client = client;
		this.workName = workName;
		this.paymentMethod = paymentMethod;
		this.seller = seller;
		this.dispatchPlace = dispatchPlace;
		this.deliveryTime = deliveryTime;
		this.deliveryPeriod = deliveryPeriod;
		this.tracing = tracing;
		this.stage = stage;
		this.notes = notes;
		this.switches = switches;
		this.boxes = boxes;
		this.boards = boards;
	}
	
	public Budget(Integer id, String code, Date date, Integer expiryDays, Client client, String workName,
			PaymentMethod paymentMethod, User seller, DispatchPlace dispatchPlace, Integer deliveryTime,
			DeliveryPeriod deliveryPeriod, Boolean tracing, BudgetStage stage) {
		super();
		this.id = id;
		this.code = code;
		this.date = date;
		this.expiryDays = expiryDays;
		this.client = client;
		this.workName = workName;
		this.paymentMethod = paymentMethod;
		this.seller = seller;
		this.dispatchPlace = dispatchPlace;
		this.deliveryTime = deliveryTime;
		this.deliveryPeriod = deliveryPeriod;
		this.tracing = tracing;
		this.stage = stage;
	}

	
	public Budget(Budget selectedValue) {
		this.id = selectedValue.getId();
		this.code = selectedValue.getCode();
		this.date = selectedValue.getDate();
		this.expiryDays = selectedValue.getExpiryDays();
		this.client = selectedValue.getClient();
		this.workName = selectedValue.getWorkName();
		this.paymentMethod = selectedValue.getPaymentMethod();
		this.seller = selectedValue.getSeller();
		this.dispatchPlace = selectedValue.getDispatchPlace();
		this.deliveryTime = selectedValue.getDeliveryTime();
		this.deliveryPeriod = selectedValue.getDeliveryPeriod();
		this.tracing = selectedValue.getTracing();
		this.stage = selectedValue.getStage();
	}

	public Integer getId() {
		return id;
	}

	
	public void setId(Integer id) {
		this.id = id;
	}
	

	public String getCode() {
		return code;
	}

	
	public void setCode(String code) {
		this.code = code;
	}

	
	public Date getDate() {
		return date;
	}

	
	public void setDate(Date date) {
		this.date = date;
	}

	
	public Integer getExpiryDays() {
		return expiryDays;
	}

	
	public void setExpiryDays(Integer expiryDays) {
		this.expiryDays = expiryDays;
	}

	
	public Client getClient() {
		return client;
	}

	
	public void setClient(Client client) {
		this.client = client;
	}

	
	public String getWorkName() {
		return workName;
	}

	
	public void setWorkName(String workName) {
		this.workName = workName;
	}

	
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	
	public User getSeller() {
		return seller;
	}

	
	public void setSeller(User seller) {
		this.seller = seller;
	}

	
	public DispatchPlace getDispatchPlace() {
		return dispatchPlace;
	}

	
	public void setDispatchPlace(DispatchPlace dispatchPlace) {
		this.dispatchPlace = dispatchPlace;
	}

	
	public Integer getDeliveryTime() {
		return deliveryTime;
	}

	
	public void setDeliveryTime(Integer deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	
	public DeliveryPeriod getDeliveryPeriod() {
		return deliveryPeriod;
	}

	
	public void setDeliveryPeriod(DeliveryPeriod deliveryPeriod) {
		this.deliveryPeriod = deliveryPeriod;
	}

	
	public Boolean getTracing() {
		return tracing;
	}

	
	public void setTracing(Boolean tracing) {
		this.tracing = tracing;
	}

	
	public BudgetStage getStage() {
		return stage;
	}

	
	public void setStage(BudgetStage stage) {
		this.stage = stage;
	}

	
	public String getNotes() {
		return notes;
	}

	
	public void setNotes(String notes) {
		this.notes = notes;
	}

	
	public ArrayList<Switch> getSwitches() {
		return switches;
	}

	
	public void setSwitches(ArrayList<Switch> switches) {
		this.switches = switches;
	}

	
	public ArrayList<Box> getBoxes() {
		return boxes;
	}

	
	public void setBoxes(ArrayList<Box> boxes) {
		this.boxes = boxes;
	}

	
	public ArrayList<Board> getBoards() {
		return boards;
	}

	
	public void setBoards(ArrayList<Board> boards) {
		this.boards = boards;
	}
	
	public String toString() {
		return this.getCode() + " \t\t " + this.getDate().toString() + " \t\t " + this.getClient().getClient();
	}
	
}
