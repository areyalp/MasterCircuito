package ve.com.mastercircuito.objects;

import java.util.ArrayList;

public class ControlBoard extends Product {
	
	private String name;
	private ControlBoardType type;
	private Installation installation;
	private Nema nema;
	private BoardVoltage voltage;
	private int phases;
	private boolean ground;
	
	private BarCapacity barCapacity;
	private BarType barType;
	private Circuits circuits;
	private Interruption interruption;
	private LockType lockType;
	private ArrayList<Material> materials;
	
	private double price;
	private String comments;
	private boolean active;
	
	private ArrayList<Switch> switches;
	
	public ControlBoard() {
		super();
	}
	
	public ControlBoard(int id, String name, ControlBoardType type, Installation installation, Nema nema, BarCapacity barCapacity,
			BarType barType, Circuits circuits, BoardVoltage voltage, int phases, boolean ground,
			Interruption interruption, LockType lockType,
			ArrayList<Material> materials, double price, String comments, boolean active, ArrayList<Switch> switches) {
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
	
	public ControlBoardType getType() {
		return type;
	}
	
	public void setType(ControlBoardType type) {
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
	
	public int getPhases() {
		return phases;
	}
	
	public void setPhases(int phases) {
		this.phases = phases;
	}
	
	public boolean getGround() {
		return ground;
	}
	
	public void setGround(boolean ground) {
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
	
	public ArrayList<Material> getMaterials() {
		return materials;
	}
	
	public void setMaterials(ArrayList<Material> materials) {
		this.materials = materials;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public ArrayList<Switch> getSwitches() {
		return switches;
	}
	
	public void setSwitches(ArrayList<Switch> switches) {
		this.switches = switches;
	}
	
}
