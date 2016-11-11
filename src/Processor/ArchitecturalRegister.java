package Processor;

import Utility.Constants;

public class ArchitecturalRegister {
	private long []registers = new long[Constants.ARCH_REGISTER_COUNT];
	//private long reg_X;
	
	public ArchitecturalRegister() {
		//reset all registers to 0
		for(int i=0; i < Constants.ARCH_REGISTER_COUNT; ++i){
			registers[i] = 0;
		}
	}
	
	public long getReg_X() {
		return registers[Constants.ARCH_REGISTER_COUNT-1];
	}

	public void setReg_X(long reg_X) {
		registers[Constants.ARCH_REGISTER_COUNT-1] = reg_X;
	}

	public long writeReg(int index, long data) throws Exception{
		if(index >= 0 && index < Constants.ARCH_REGISTER_COUNT)
			return this.registers[index] = data;
		else
			throw new Exception("Illegal register address : R"+index);
	}
	
	public long readReg(int index) throws Exception{
		if(index >= 0 && index < Constants.ARCH_REGISTER_COUNT)
			return this.registers[index];
		else
			throw new Exception("Illegal register address : R"+index);
	}
}
