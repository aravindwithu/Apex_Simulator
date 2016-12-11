package Apex_Simulator;
import Utility.PhysicalRegister;
import Utility.RAT;
import Utility.Constants;

public class UnifiedRegisterFile {
	
	private PhysicalRegister []Regs = null;	
	public RAT []frontEndRAT = null;
	public RAT []backEndRAT = null;
		
	/**
	 * Constructor for URF initializes the physical registers.
	 */
	public UnifiedRegisterFile() {
		//reset all physical registers.
		Regs = new PhysicalRegister[Constants.REG_COUNT];
		for(int i=0; i < Constants.REG_COUNT; ++i){
			Regs[i] = new PhysicalRegister();			
		}			
		//reset all front end table
		frontEndRAT = new RAT[Constants.RAT_COUNT];
		for(int i=0; i < Constants.RAT_COUNT; ++i){
			frontEndRAT[i] = new RAT();
			}
		//reset all back end table
		backEndRAT = new RAT[Constants.RAT_COUNT];
		for(int i=0; i < Constants.RAT_COUNT; ++i){
			backEndRAT[i] = new RAT();
			}
	}
	
	/**
	 * getReg_X method returns the last register R16 reserved for X register
	 * @return register R16 reserved for register X 
	 */
	public long getReg_X() {
		return Regs[Constants.REG_COUNT-1].getRegValue();
	}

	/**
	 * setReg_X method sets the last register R16 reserved for X register with given value
	 * @param reg_X of type long, value of register R16 reserved for register X 
	 */
	public void setReg_X(long reg_X) {
		Regs[Constants.REG_COUNT-1].setRegValue(reg_X);
	}	

	/**
	 * writeReg method writes the register value to the relevant register
	 * @param index of type int, specifies the register (from R0 to R15) for which the value should be written
	 * @param data of type long, contains data or value needed to be written to the given register
	 */
	public void writeReg(int index, long data) throws Exception{
		if(index >= 0 && index < Constants.REG_COUNT){
			Regs[index].setRegValue(data);}
		else{
			throw new Exception("Illegal register address : R"+index);}
	}
	
	/**
	 * readReg method reads the register value from the given register
	 * @param index of type int, specifies the register (from R0 to R15) from which the value should be read
	 */
	public long readReg(int index) throws Exception{
		if(index >= 0 && index < Constants.REG_COUNT){
			return this.Regs[index].getRegValue();}
		else{
			throw new Exception("Illegal register address : R"+index);}
	}
	
	public void setRegStatus(int index, boolean data) throws Exception{
		if(index >= 0 && index < Constants.REG_COUNT){
			Regs[index].setStatus(data);}
		else{
			throw new Exception("Illegal register address : R"+index);}
	}
	
	public boolean getRegStatus(int index) throws Exception{
		if(index >= 0 && index < Constants.REG_COUNT){
			return this.Regs[index].getStatus();}
		else{
			throw new Exception("Illegal register address : R"+index);}
	}
	
	public void setRegAvailability(int index, boolean data) throws Exception{
		if(index >= 0 && index < Constants.REG_COUNT){
			Regs[index].setAvailability(data);}
		else{
			throw new Exception("Illegal register address : R"+index);}
	}
	
	public boolean getRegAvailability(int index) throws Exception{
		if(index >= 0 && index < Constants.REG_COUNT){
			return this.Regs[index].getAvailability();}
		else{
			throw new Exception("Illegal register address : R"+index);}
	}
	
	public long setFrontEndPhyReg(int index) throws Exception{
		if(index >= 0 && index < Constants.RAT_COUNT)
		{
			for(int RegAdd=0; RegAdd<Constants.REG_COUNT; RegAdd++){
				if(Regs[RegAdd].getAvailability()){
					this.frontEndRAT[index].setRATPhyReg(RegAdd);
					Regs[RegAdd].setAvailability(false);
					break;
				}
			}
			return this.frontEndRAT[index].getRATPhyReg();
		}
		else{
			throw new Exception("Illegal register address : R"+index);}
	}	
	
	public long getFrontEndPhyReg(int index) throws Exception{
		if(index >= 0 && index < Constants.RAT_COUNT)
			return this.frontEndRAT[index].getRATPhyReg();
		else
			throw new Exception("Illegal register address : R"+index);
	}
	
	public void setBackEndPhyReg(int index) throws Exception{
		if(index >= 0 && index < Constants.RAT_COUNT)
		{
			for(int RegAdd=0; RegAdd<Constants.REG_COUNT; RegAdd++){
				if(Regs[RegAdd].getAvailability()){
					this.backEndRAT[index].setRATPhyReg(RegAdd);
					Regs[RegAdd].setAvailability(false);
					break;
				}
			}
		}
		else{
			throw new Exception("Illegal register address : R"+index);}
	}	
	
	public long getBackEndPhyReg(int index) throws Exception{
		if(index >= 0 && index < Constants.RAT_COUNT)
			return this.backEndRAT[index].getRATPhyReg();
		else
			throw new Exception("Illegal register address : R"+index);
	}
}
