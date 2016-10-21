package ve.com.mastercircuito.components;

import java.sql.Date;

public class ProductionOrder {
	
	private Integer id;
	private Integer budgetId;
	private Date dateProcessed;
	private Date dateFinished;
	private Integer producerId;
	private Integer authorizerId;

	public ProductionOrder() {
		super();
	}

	public ProductionOrder(Integer id, Integer budgetId, Date dateProcessed, Date dateFinished, Integer producerId,
			Integer authorizerId) {
		super();
		this.id = id;
		this.budgetId = budgetId;
		this.dateProcessed = dateProcessed;
		this.dateFinished = dateFinished;
		this.producerId = producerId;
		this.authorizerId = authorizerId;
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

	public Date getDateProcessed() {
		return dateProcessed;
	}

	public void setDateProcessed(Date dateProcessed) {
		this.dateProcessed = dateProcessed;
	}

	public Date getDateFinished() {
		return dateFinished;
	}

	public void setDateFinished(Date dateFinished) {
		this.dateFinished = dateFinished;
	}

	public Integer getProducerId() {
		return producerId;
	}

	public void setProducerId(Integer producerId) {
		this.producerId = producerId;
	}

	public Integer getAuthorizerId() {
		return authorizerId;
	}

	public void setAuthorizerId(Integer authorizerId) {
		this.authorizerId = authorizerId;
	}
	
}
