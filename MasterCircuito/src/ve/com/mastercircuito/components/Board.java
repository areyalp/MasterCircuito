package ve.com.mastercircuito.components;

import java.util.ArrayList;

public class Board extends Product {
	
	private String name;
	private BoardType type;
	private Installation installation;
	private Nema nema;
	private BarCapacity barCapacity;
	private BarType barType;
	private Circuits circuits;
	private BoardVoltage voltage;
	private Integer phases;
	private Boolean ground;
	private Interruption interruption;
	private LockType lockType;
	private ArrayList<Integer> mainSwitchId;
	private ArrayList<Material> materials;
	private Double price;
	private String comments;
	private Boolean active;
	
	private ArrayList<Switch> switches;
	
	public Board() {
		super();
	}
	
	public Board(Integer id, String name, BoardType type, Installation installation, Nema nema, BarCapacity barCapacity,
			BarType barType, Circuits circuits, BoardVoltage voltage, Integer phases, Boolean ground,
			Interruption interruption, LockType lockType, ArrayList<Integer> mainSwitchId,
			ArrayList<Material> materials, Double price, String comments, Boolean active, ArrayList<Switch> switches) {
		super();
		this.setId(id);
		this.name = name;
		this.type = type;
		this.installation = installation;
		this.nema = nema;
		this.barCapacity = barCapacity;
		this.barType = barType;
		this.circuits = circuits;
		this.voltage = voltage;
		this.phases = phases;
		this.ground = ground;
		this.interruption = interruption;
		this.lockType = lockType;
		this.mainSwitchId = mainSwitchId;
		this.materials = materials;
		this.price = price;
		this.comments = comments;
		this.active = active;
		this.switches = switches;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BoardType getType() {
		return type;
	}
	
	public void setType(BoardType type) {
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
	
	public BarCapacity getBarCapacity() {
		return barCapacity;
	}
	
	public void setBarCapacity(BarCapacity barCapacity) {
		this.barCapacity = barCapacity;
	}
	
	public BarType getBarType() {
		return barType;
	}
	
	public void setBarType(BarType barType) {
		this.barType = barType;
	}
	
	public Circuits getCircuits() {
		return circuits;
	}
	
	public void setCircuits(Circuits circuits) {
		this.circuits = circuits;
	}
	
	public BoardVoltage getVoltage() {
		return voltage;
	}
	
	public void setVoltage(BoardVoltage voltage) {
		this.voltage = voltage;
	}
	
	public Integer getPhases() {
		return phases;
	}
	
	public void setPhases(Integer phases) {
		this.phases = phases;
	}
	
	public Boolean getGround() {
		return ground;
	}
	
	public void setGround(Boolean ground) {
		this.ground = ground;
	}
	
	public Interruption getInterruption() {
		return interruption;
	}
	
	public void setInterruption(Interruption interruption) {
		this.interruption = interruption;
	}
	
	public LockType getLockType() {
		return lockType;
	}
	
	public void setLockType(LockType lockType) {
		this.lockType = lockType;
	}
	
	public ArrayList<Integer> getMainSwitchId() {
		return mainSwitchId;
	}
	
	public void setMainSwitchId(ArrayList<Integer> mainSwitchId) {
		this.mainSwitchId = mainSwitchId;
	}
	
	public ArrayList<Material> getMaterials() {
		return materials;
	}
	
	public void setMaterials(ArrayList<Material> materials) {
		this.materials = materials;
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
	
	public ArrayList<Switch> getSwitches() {
		return switches;
	}
	
	public void setSwitches(ArrayList<Switch> switches) {
		this.switches = switches;
	}
	
}
