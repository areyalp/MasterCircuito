package ve.com.mastercircuito.components;

public class Box {
	
	private Integer id;
	private BoxType type;
	private Installation installation;
	private Nema nema;
	private Integer pairs;
	private Sheet sheet;
	private Finish finish;
	private Color color;
	private Integer height;
	private Integer width;
	private Integer depth;
	private MeasureUnits units;
	private Caliber caliber;
	private String caliberComments;
	private LockType lockType;
	private Double price;
	private String comments;
	private Boolean active;
	
	public Box() {
		super();
	}

	public Box(Integer id, BoxType type, Installation installation, Nema nema, Integer pairs, Sheet sheet,
			Finish finish, Color color, Integer height, Integer width, Integer depth, MeasureUnits units,
			Caliber caliber, String caliberComments, LockType lockType, Double price, String comments, Boolean active) {
		super();
		this.id = id;
		this.type = type;
		this.installation = installation;
		this.nema = nema;
		this.pairs = pairs;
		this.sheet = sheet;
		this.finish = finish;
		this.color = color;
		this.height = height;
		this.width = width;
		this.depth = depth;
		this.units = units;
		this.caliber = caliber;
		this.caliberComments = caliberComments;
		this.lockType = lockType;
		this.price = price;
		this.comments = comments;
		this.active = active;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BoxType getType() {
		return type;
	}

	public void setType(BoxType type) {
		this.type = type;
	}

	public Installation getInstallation() {
		return installation;
	}

	public void setInstallation(Installation installation) {
		this.installation = installation;
	}

	public Nema getNema() {
		return nema;
	}

	public void setNema(Nema nema) {
		this.nema = nema;
	}

	public Integer getPairs() {
		return pairs;
	}

	public void setPairs(Integer pairs) {
		this.pairs = pairs;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public Finish getFinish() {
		return finish;
	}

	public void setFinish(Finish finish) {
		this.finish = finish;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public MeasureUnits getUnits() {
		return units;
	}

	public void setUnits(MeasureUnits units) {
		this.units = units;
	}

	public Caliber getCaliber() {
		return caliber;
	}

	public void setCaliber(Caliber caliber) {
		this.caliber = caliber;
	}

	public String getCaliberComments() {
		return caliberComments;
	}

	public void setCaliberComments(String caliberComments) {
		this.caliberComments = caliberComments;
	}

	public LockType getLockType() {
		return lockType;
	}

	public void setLockType(LockType lockType) {
		this.lockType = lockType;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
