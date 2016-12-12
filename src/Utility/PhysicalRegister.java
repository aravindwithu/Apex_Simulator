package Utility;

public class PhysicalRegister {
	private long regValue;	
	private boolean isValid;
	private boolean availability;
	private int zFlag;
	
	/**
	 * Constructor for Register initializes the registers.
	 */
	public PhysicalRegister() {
		//reset all registers to 0
		regValue = 0;
		isValid = false;
		availability = true;
		zFlag = -1;
	}
	
	/**
	 * gets  Register value.
	 */
	public long getRegValue() {
		return regValue;
	}
	
	/**
	 * gets  IsValid value.
	 */
	public boolean getIsValid() {
		return isValid;
	}
	
	/**
	 * gets  Availability value.
	 */
	public boolean getAvailability() {
		return availability;
	}

	/**
	 * sets  Reg value.
	 */
	public void setRegValue(long reg_V) {
		regValue = reg_V;
	}
	
	/**
	 * sets IsValid value.
	 */
	public void setIsValid(boolean statusVal) {
		isValid = statusVal;
	}
	
	/**
	 * sets Availability value.
	 */
	public void setAvailability(boolean availabilityVal) {
		availability = availabilityVal;
	}
	
	/**
	 * gets ZFlag value.
	 */
	public int getZFlag() {
		return zFlag;
	}
	
	/**
	 * sets ZFlag value.
	 */
	public void setZFlag(int zFlagVal) {
		zFlag = zFlagVal;
	}
}
