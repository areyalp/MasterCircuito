package ve.com.mastercircuito.objects;

import java.sql.Timestamp;

import ve.com.mastercircuito.db.Db;

public class ProductionOrder {
	
	private Integer id;
	private Integer budgetId;
	private Timestamp dateProcessed;
	private Timestamp dateFinished;
	private Integer creatorId;
	private Integer authorizerId;
	private Boolean processed;

	public ProductionOrder() {
		super();
	}

	public ProductionOrder(Integer id, Integer budgetId, Timestamp dateProcessed, Timestamp dateFinished, Integer creatorId,
			Integer authorizerId, Boolean processed) {
		super();
		this.id = id;
		this.budgetId = budgetId;
		this.dateProcessed = dateProcessed;
		this.dateFinished = dateFinished;
		this.creatorId = creatorId;
		this.authorizerId = authorizerId;
		this.processed = processed;
	}

	public ProductionOrder(ProductionOrder productionOrder) {
		super();
		this.id = productionOrder.getId();
		this.budgetId = productionOrder.getBudgetId();
		this.dateProcessed = productionOrder.getDateProcessed();
		this.dateFinished = productionOrder.getDateFinished();
		this.creatorId = productionOrder.getCreatorId();
		this.authorizerId = productionOrder.getAuthorizerId();
		this.processed = productionOrder.getProcessed();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBudgetId() {
		return budgetId;
	}

	public void setBudgetId(Integer budgetId) {
		this.budgetId = budgetId;
	}

	public Timestamp getDateProcessed() {
		return dateProcessed;
	}

	public void setDateProcessed(Timestamp dateProcessed) {
		this.dateProcessed = dateProcessed;
	}

	public Timestamp getDateFinished() {
		return dateFinished;
	}

	public void setDateFinished(Timestamp dateFinished) {
		this.dateFinished = dateFinished;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getAuthorizerId() {
		return authorizerId;
	}
	
	public void setAuthorizerId(Integer authorizerId) {
		this.authorizerId = authorizerId;
	}
	
	public Boolean getProcessed() {
		return processed;
	}
	
	public Boolean isProcessed() {
		return this.getProcessed();
	}
	
	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}
	
	public static Integer pushToDb(Integer budgetId, Integer creatorId) {
		Db db = new Db();
		db.insert("INSERT INTO production_orders (budget_id, creator_id) VALUES (" + budgetId + ", " + creatorId + ")");
		return db.getInsertId();
	}
	
	public void pullFromDb(Integer orderId) {
		Db db = new Db();
		ProductionOrder productionOrder = db.pullProductionOrder(orderId);
		this.setId(orderId);
		this.setBudgetId(productionOrder.getBudgetId());
		this.setDateProcessed(productionOrder.getDateProcessed());
		this.setDateFinished(productionOrder.getDateFinished());
		this.setCreatorId(productionOrder.getCreatorId());
		this.setAuthorizerId(productionOrder.getAuthorizerId());
		this.setProcessed(productionOrder.getProcessed());
	}
	
	public String toString() {
		return "O/P " + this.getId();
	}
	
	public static Boolean exists(Integer budgetId) {
		Db db = new Db();
		return db.productionOrderExists(budgetId);
	}

	public void pullByBudget(Integer budgetId) {
		Db db = new Db();
		ProductionOrder productionOrder = db.pullProductionOrderByBudget(budgetId);
		this.setId(productionOrder.getId());
		this.setBudgetId(budgetId);
		this.setDateProcessed(productionOrder.getDateProcessed());
		this.setDateFinished(productionOrder.getDateFinished());
		this.setCreatorId(productionOrder.getCreatorId());
		this.setAuthorizerId(productionOrder.getAuthorizerId());
		this.setProcessed(productionOrder.getProcessed());
	}
	
}
