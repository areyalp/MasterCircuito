package ve.com.mastercircuito.print;

import java.util.Date;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PurchaseOrderHeader")
public class PurchaseOrderHeader {

	Date orderDate;
	String orderNumber;
	BuyerInformation buyer;
	PurchaseOrderItem[] items;
	
	public PurchaseOrderHeader()  {
		
	}
	
	public PurchaseOrderHeader(String number) {
		this.orderNumber=number;
		this.orderDate=Calendar.getInstance().getTime();
	}
	
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public BuyerInformation getBuyer() {
		return buyer;
	}
	public void setBuyer(BuyerInformation buyer) {
		this.buyer = buyer;
	}
	public PurchaseOrderItem[] getItems() {
		return items;
	}
	public void setItems(PurchaseOrderItem[] items) {
		this.items = items;
	}
}
