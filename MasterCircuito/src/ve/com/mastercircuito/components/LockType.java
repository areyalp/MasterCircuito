package ve.com.mastercircuito.components;

public class LockType {
	
	private Integer id;
	private String lockType;

	public LockType() {
		super();
	}

	public LockType(Integer id, String lockType) {
		super();
		this.id = id;
		this.lockType = lockType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLockType() {
		return lockType;
	}

	public void setLockType(String lockType) {
		this.lockType = lockType;
	}
	
}
