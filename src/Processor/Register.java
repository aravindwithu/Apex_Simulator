package Processor;

import Utility.Constants;

public class Register {
	private long []registers = new long[Constants.regCount];
	//private long reg_X;
	
	public Register() {
		//reset all registers to 0
		for(int i=0; i < Constants.regCount; ++i){
			registers[i] = 0;
		}
	}
	
	public long getReg_X() {
		return registers[Constants.regCount-1];
	}

	public void setReg_X(long reg_X) {
		registers[Constants.regCount-1] = reg_X;
	}

	public long writeReg(int index, long data) throws Exception{
		if(index >= 0 && index < Constants.regCount)
			return this.registers[index] = data;
		else
			throw new Exception("Illegal register address : R"+index);
	}
	
	public long readReg(int index) throws Exception{
		if(index >= 0 && index < Constants.regCount)
			return this.registers[index];
		else
			throw new Exception("Illegal register address : R"+index);
	}
}
