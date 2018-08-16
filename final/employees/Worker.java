package employees;

import head.PickingRequest;

public class Worker {
  private String name;
  private boolean status;
  private PickingRequest currTask;

  public Worker(String name){
    this.name = name;
    this.status = true;
  }

  public boolean isReady() {
    return this.status;
  }

  public String getName() {
    return this.name;
  }

  public void setTask(PickingRequest req) {
	  currTask = req;
	  this.status = false;
  }
  
  public PickingRequest getTask() {
	  return currTask;
  }
  
  public void setBusy() { 
	  this.status = false;
  }

  public void setReady() {
    this.status = true;
  }
}
