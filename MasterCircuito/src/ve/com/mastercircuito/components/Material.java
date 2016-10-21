package ve.com.mastercircuito.components;

public class Material {
	
	private Integer id;
	private String material;
	private Double price;

	public Material() {
		super();
	}

	public Material(Integer id, String material, Double price) {
		super();
		this.id = id;
		this.material = material;
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
}
