package ve.com.mastercircuito.print;

/**
 * Information about the buyer in purchase order
 *
 */
public class BuyerInformation {
	
	String name;
	String address;
	String city;
	String postCode;
	String country;
	
	public BuyerInformation() {
		
	}
	
	public BuyerInformation(String name,String address,String city,String postCode,String country) {
		this.name=name;
		this.address=address;
		this.city=city;
		this.postCode=postCode;
		this.country=country;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

}
