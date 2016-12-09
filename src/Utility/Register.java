package Utility;

public class Register {
	private long regValue;
	//private long reg_X;
	
	/**
	 * Constructor for Register initializes the registers.
	 */
	public Register() {
		//reset all registers to 0
		regValue = 0;
	}
	
	/**
	 * getReg_X method returns the last register R16 reserved for X register
	 * @return register R16 reserved for register X 
	 */
	public long getReg_Val() {
		return regValue;
	}

	/**
	 * setReg_X method sets the last register R16 reserved for X register with given value
	 * @param reg_X of type long, value of register R16 reserved for register X 
	 */
	public void setReg_Val(long reg_X) {
		// TODO Auto-generated method stub
		regValue = reg_X;
	}
}
