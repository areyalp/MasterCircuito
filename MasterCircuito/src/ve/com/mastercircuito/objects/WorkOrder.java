package ve.com.mastercircuito.objects;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import ve.com.mastercircuito.db.Db;

public class WorkOrder {
	
	private int id;
	private int productionOrderId;
	private int productId;
	private int productTypeId;
	private int creatorId;
	private int authorizerId;
	private Timestamp dateProcessed;
	private Timestamp dateFinished;
	private boolean processed;

	public WorkOrder() {
		super();
	}
	
	public WorkOrder(int orderId) {
		Db db = new Db();
		WorkOrder workOrder = db.pullWorkOrder(orderId);
		if(workOrder.getId() > 0) {
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
		try {
			db.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public WorkOrder(int orderId, int budgetId) {
		Db db = new Db();
		WorkOrder workOrder = db.pullWorkOrder(orderId, budgetId);
		if(workOrder.getId() > 0) {
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
		try {
			db.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public WorkOrder(int id, int productionOrderId, int productId, int productTypeId, int creatorId,
			int authorizerId, Timestamp dateProcessed, Timestamp dateFinished, boolean processed) {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductionOrderId() {
		return productionOrderId;
	}

	public void setProductionOrderId(int productionOrderId) {
		this.productionOrderId = productionOrderId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
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

	public boolean getProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
	
	public String toString() {
		return "O/T " + this.getId();
	}

	public static int pushToDb(int productionOrderId, int productId, int productTypeId, int userId) {
		Db db = new Db();
		db.insert("INSERT INTO work_orders (production_order_id, product_id, product_type_id, creator_id) VALUES (" + productionOrderId + ", " + productId + ", " + productTypeId + ", " + userId + ")");
		try {
			db.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return db.getInsertId();
	}
	
	public static Integer pushToDb(int productionOrderId, ArrayList<Product> materials, Integer userId) {
		Db db = new Db();
		String rows = "";
		Integer orderId = WorkOrder.pushToDb(productionOrderId, 0, 6, userId);
		for(Product product:materials) {
			Integer productTypeId = (product instanceof Switch)?1:5;
			rows += "('" + orderId + "', '" + productTypeId + "', '" + product.getId() + "'),";
		}
		// Check line below
		String lastChar = rows.substring(rows.length() - 1);
		if(lastChar.equalsIgnoreCase(",")) {
			rows = rows.substring(0, rows.length() - 1);
		}
		db.insert("INSERT INTO work_order_products (work_order_id, type_id, product_id) "
				+ "VALUES " + rows);
		try {
			db.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderId;
	}

	public static WorkOrder pullFromDb(int orderId) {
		Db db = new Db();
		WorkOrder workOrder = db.pullWorkOrder(orderId);
		try {
			db.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return workOrder;
	}

	public static ArrayList<WorkOrder> pullAllFromDb(int orderId) {
		Db db = new Db();
		ArrayList<WorkOrder> workOrders = db.pullAllWorkOrders(orderId);
		return workOrders;
	}
	
}
