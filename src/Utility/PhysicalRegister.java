package Utility;

public class PhysicalRegister {
	private long regValue;	
	private boolean isValid;
	private boolean availability;
	
	/**
	 * Constructor for Register initializes the registers.
	 */
	public PhysicalRegister() {
		//reset all registers to 0
		regValue = 0;
		isValid = false;
		availability = true;
	}
	
	public long getRegValue() {
		return regValue;
	}
	
	public boolean getIsValid() {
		return isValid;
	}
	
	public boolean getAvailability() {
		return availability;
	}

	public void setRegValue(long reg_V) {
		regValue = reg_V;
	}
	
	public void setIsValid(boolean statusVal) {
		isValid = statusVal;
	}
	
	public void setAvailability(boolean availabilityVal) {
		availability = availabilityVal;
	}
}
