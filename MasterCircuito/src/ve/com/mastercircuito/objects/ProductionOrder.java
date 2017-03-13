package ve.com.mastercircuito.objects;

import java.sql.Timestamp;

import ve.com.mastercircuito.db.Db;

public class ProductionOrder {
	
	private int id;
	private int budgetId;
	private Timestamp dateProcessed;
	private Timestamp dateFinished;
	private int creatorId;
	private int authorizerId;
	private boolean processed;

	public ProductionOrder() {
		super();
	}
	
	public ProductionOrder(int budgetId) {
		ProductionOrder productionOrder = Db.pullProductionOrderByBudgetId(budgetId);
		this.setId(productionOrder.getId());
		this.setBudgetId(budgetId);
		this.setDateProcessed(productionOrder.getDateProcessed());
		this.setDateFinished(productionOrder.getDateFinished());
		this.setCreatorId(productionOrder.getCreatorId());
		this.setAuthorizerId(productionOrder.getAuthorizerId());
		this.setProcessed(productionOrder.getProcessed());
	}

	public ProductionOrder(int id, int budgetId, Timestamp dateProcessed, Timestamp dateFinished, int creatorId,
			int authorizerId, boolean processed) {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBudgetId() {
		return budgetId;
	}

	public void setBudgetId(int budgetId) {
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

	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	public int getAuthorizerId() {
		return authorizerId;
	}
	
	public void setAuthorizerId(int authorizerId) {
		this.authorizerId = authorizerId;
	}
	
	public boolean getProcessed() {
		return processed;
	}
	
	public boolean isProcessed() {
		return this.getProcessed();
	}
	
	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
	
	public static int pushToDb(int budgetId, int creatorId) {
		Db db = new Db();
		db.insert("INSERT INTO production_orders (budget_id, creator_id) VALUES (" + budgetId + ", " + creatorId + ")");
		return db.getInsertId();
	}
	
	public void pullFromDb(int orderId) {
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
	
	public static boolean exists(int orderId) {
		return Db.productionOrderExists(orderId);
	}

	public static ProductionOrder getByBudgetId(int budgetId) {
		return Db.pullProductionOrderByBudgetId(budgetId);
	}

	public static boolean existsByBudgetId(Integer budgetId) {
		return Db.productionOrderExistsByBudgetId(budgetId);
	}
	
}
