package ve.com.mastercircuito.objects;

import java.sql.Timestamp;
import java.util.ArrayList;

import ve.com.mastercircuito.db.Db;

public class WorkOrder {
	
	private Integer id;
	private Integer productionOrderId;
	private Integer productId;
	private Integer productTypeId;
	private Integer creatorId;
	private Integer authorizerId;
	private Timestamp dateProcessed;
	private Timestamp dateFinished;
	private Boolean processed;

	public WorkOrder() {
		super();
	}

	public WorkOrder(Integer id, Integer productionOrderId, Integer productId, Integer productTypeId, Integer creatorId,
			Integer authorizerId, Timestamp dateProcessed, Timestamp dateFinished, Boolean processed) {
		super();
		this.id = id;
		this.productionOrderId = productionOrderId;
		this.productId = productId;
		this.productTypeId = productTypeId;
		this.creatorId = creatorId;
		this.authorizerId = authorizerId;
		this.dateProcessed = dateProcessed;
		this.dateFinished = dateFinished;
		this.processed = processed;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductionOrderId() {
		return productionOrderId;
	}

	public void setProductionOrderId(Integer productionOrderId) {
		this.productionOrderId = productionOrderId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
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

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}
	
	public String toString() {
		return "O/T " + this.getId();
	}

	public static Integer pushToDb(Integer productionOrderId, Integer productId, Integer productTypeId, Integer userId) {
		Db db = new Db();
		db.insert("INSERT INTO work_orders (production_order_id, product_id, product_type_id, creator_id) VALUES (" + productionOrderId + ", " + productId + ", " + productTypeId + ", " + userId + ")");
		return db.getInsertId();
	}

	public void pullFromDb(Integer orderId) {
		Db db = new Db();
		WorkOrder workOrder = db.pullWorkOrder(orderId);
		this.setId(orderId);
		this.setProductionOrderId(workOrder.getProductionOrderId());
		this.setProductId(workOrder.getProductId());
		this.setProductTypeId(workOrder.getProductTypeId());
		this.setCreatorId(workOrder.getCreatorId());
		this.setAuthorizerId(workOrder.getAuthorizerId());
		this.setDateProcessed(workOrder.getDateProcessed());
		this.setDateFinished(workOrder.getDateFinished());
		this.setProcessed(workOrder.getProcessed());
	}

	public static ArrayList<WorkOrder> pullAllFromDb(Integer orderId) {
		Db db = new Db();
		ArrayList<WorkOrder> workOrders = db.pullAllWorkOrders(orderId);
		return workOrders;
	}
	
}
