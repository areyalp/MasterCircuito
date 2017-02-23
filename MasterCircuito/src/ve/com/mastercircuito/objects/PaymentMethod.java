package ve.com.mastercircuito.objects;

public class PaymentMethod {
	
	private Integer id;
	private String method;
	
	public PaymentMethod() {
		super();
	}
	
	public PaymentMethod(Integer id, String method) {
		super();
		this.id = id;
		this.method = method;
	}
	
	public PaymentMethod(PaymentMethod paymentMethodInfo) {
		this.id = paymentMethodInfo.id;
		this.method = paymentMethodInfo.method;
	}

	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getMethod() {
		return this.method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
}
