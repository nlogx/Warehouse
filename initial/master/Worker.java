package master;

public class Worker {
	protected String name;
	protected boolean status;
	
	public Worker(String name) {
		this.name = name;
		this.status = false;
	}
	
	public void setStatus(boolean sta) {
		this.status = sta;
	}
	
	public boolean getStatus() {
		return status;
	}	
}
