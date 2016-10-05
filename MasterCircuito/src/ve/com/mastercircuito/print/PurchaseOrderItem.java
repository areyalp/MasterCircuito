package ve.com.mastercircuito.print;

public class PurchaseOrderItem {
String article;
String description;
int quantity;

public PurchaseOrderItem() {
	
}

public PurchaseOrderItem(String article,String desc,int qty) {
	this.article=article;
	this.description=desc;
	this.quantity=qty;
}

public String getArticle() {
	return article;
}
public void setArticle(String article) {
	this.article = article;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public int getQuantity() {
	return quantity;
}
public void setQuantity(int quantity) {
	this.quantity = quantity;
}
}
