package ve.com.mastercircuito.objects;

public class Material extends Product{
	
	private int id;
	private String material;
	private double price;

	public Material() {
		super();
	}

	public Material(Integer id, String material, Double price) {
		super();
		this.id = id;
		this.material = material;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
}
