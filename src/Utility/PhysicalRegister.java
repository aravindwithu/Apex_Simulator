package Utility;

public class PhysicalRegister {
	private long regValue;	
	private boolean status;
	private boolean availability;
	
	/**
	 * Constructor for Register initializes the registers.
	 */
	public PhysicalRegister() {
		//reset all registers to 0
		regValue = 0;
		status = false;
		availability = true;
	}
	
	public long getRegValue() {
		return regValue;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public boolean getAvailability() {
		return availability;
	}

	public void setRegValue(long reg_V) {
		regValue = reg_V;
	}
	
	public void setStatus(boolean statusVal) {
		status = statusVal;
	}
	
	public void setAvailability(boolean availabilityVal) {
		availability = availabilityVal;
	}
}
