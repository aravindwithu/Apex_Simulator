package Apex_Simulator;
import Utility.PhysicalRegister;
import Utility.RAT;
import Utility.Constants;

public class UnifiedRegisterFile {
	
	private PhysicalRegister []Regs = null;	
	private RAT []frontEndRAT = null;
	private RAT []backEndRAT = null;
	private RAT zReg = null;
	private RAT xReg = null;
	
		
	/**
	 * Constructor for URF initializes the physical registers.
	 */
	public UnifiedRegisterFile() {
		//reset all physical registers.
		Regs = new PhysicalRegister[Constants.REG_COUNT];
		for(int i=0; i < Constants.REG_COUNT; ++i){
			Regs[i] = new PhysicalRegister();			
		}	
		System.out.println(Constants.REG_COUNT - 1);
		Regs[Constants.REG_COUNT-1].setRegValue(Constants.START_ADDRESS);
		Regs[Constants.REG_COUNT-1].setIsValid(true);
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
		
		zReg = new RAT();
		xReg = new RAT();
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
			//System.out.println(Regs[index].getRegValue());
			return this.Regs[index].getRegValue();}
		else{
			throw new Exception("Illegal register address : R"+index);}
	}
	
	public void setIsRegValid(int index, boolean data) throws Exception{
		if(index >= 0 && index < Constants.REG_COUNT){
			Regs[index].setIsValid(data);}
		else{
			throw new Exception("Illegal register address : R"+index);}
	}
	
	public boolean getIsRegValid(int index) throws Exception{
		if(index >= 0 && index < Constants.REG_COUNT){
			return this.Regs[index].getIsValid();}
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
	
	public void setBackEndPhyReg(int archReg, int phyReg) throws Exception{
		if(archReg >= 0 && archReg < Constants.RAT_COUNT)
		{
			if(backEndRAT[archReg].getRATPhyReg() == -1){
				backEndRAT[archReg].setRATPhyReg(phyReg);
				Regs[phyReg].setIsValid(true);
			}
			else{
				Regs[(int)(backEndRAT[archReg].getRATPhyReg())].setAvailability(true);
				backEndRAT[archReg].setRATPhyReg(phyReg);
				Regs[phyReg].setIsValid(true);
			}
			
		}
		else{
			throw new Exception("Illegal arch back end RAT register address : "+archReg);}
		
	}	
	
	public long getBackEndPhyReg(int index) throws Exception{
		if(index >= 0 && index < Constants.RAT_COUNT)
			return this.backEndRAT[index].getRATPhyReg();
		else
			throw new Exception("Illegal register address : R"+index);
	}
	
	public long getZReg(){
		return zReg.getRATPhyReg();
	}
	
	public void setZReg(long data){
		 zReg.setRATPhyReg(data);
	}
	
	public void setZFlag(int phyReg,int data){
		Regs[phyReg].setZFlag(data);
	}
	
	public int getZFlag(int phyReg){
		if(Regs[phyReg].getZFlag() != -1){
		return Regs[phyReg].getZFlag();}
		else{
			return -1;
		}
	}
	
	public void setAllFrontEntTable(RAT[] newRAT){
		frontEndRAT = newRAT;
	}
	
	public RAT[] getAllBackEntTable(){
		return backEndRAT;
	}
	
	public void clearBackEndTable(){
		backEndRAT = new RAT[Constants.RAT_COUNT];
		for(int i=0; i < Constants.RAT_COUNT; ++i){
			backEndRAT[i] = new RAT();
			}
		
		zReg = new RAT();
	}
	
	public long setXRegPhyReg(){
		xReg.setRATPhyReg(Constants.REG_COUNT-1); 
		return xReg.getRATPhyReg();
	}
	
	public long getXRegPhyRegAdd(){
		return Constants.REG_COUNT -1;
	}
	
}
