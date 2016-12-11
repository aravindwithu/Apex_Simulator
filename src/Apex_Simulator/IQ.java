package Apex_Simulator;

import Utility.Constants;
import Utility.Instruction;

public class IQ {
	
	private Instruction []IQEntry = new Instruction[Constants.IQ_COUNT];
	private int IQIndex = 0;

	/**
	 * Constructor for IQ initializes the IQEntry.
	 */
	public IQ() {
		//reset all IQEntrys	
		for(int i=0; i < Constants.IQ_COUNT; ++i){
			IQEntry[i] = new Instruction();			
		}	
		IQIndex = 0;
	}
	
	public boolean writeIQEntry(Instruction data){
		if(IQIndex >= 0 && IQIndex < Constants.REG_COUNT){		
			IQEntry[IQIndex] = data;
			IQIndex++;
			return true;}
		else{
			  return false;}
		}	
	
	public Instruction readIQEntry(int index) throws Exception{
		if(index >= 0 && index < Constants.IQ_COUNT)
			if(this.IQEntry[index] != null){
			return this.IQEntry[index];}
			else{ return null;
			}
		else
			throw new Exception("Illegal register address : R"+index);
	}
}
