package Apex_Simulator;

import Utility.Constants;
import Utility.Instruction;

public class ROB {
	
	private Instruction []ROBEntry = new Instruction[Constants.ROB_COUNT];
	private int ROBIndex = 0;

	/**
	 * Constructor for IQ initializes the IQEntry.
	 */
	public ROB() {
		//reset all IQEntrys	
		ROBEntry = new Instruction[Constants.ROB_COUNT];
		for(int i=0; i < Constants.IQ_COUNT; ++i){
			ROBEntry[i] = new Instruction();			
		}		
		ROBIndex = 0;
	}
	
	public void writeIQEntry(Instruction data) throws Exception{
		if(ROBIndex >= 0 && ROBIndex < Constants.IQ_COUNT)
		{
			ROBEntry[ROBIndex] = data;
			ROBIndex++;
		}
		else{
			throw new Exception("Illegal ROB index : "+ROBIndex);}
	}
	
	public Instruction readIQEntry(int index) throws Exception{
		if(index >= 0 && index < Constants.IQ_COUNT)
			return this.ROBEntry[index];
		else
			throw new Exception("Illegal register address : R"+index);
	}
}
