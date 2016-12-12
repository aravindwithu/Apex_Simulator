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
		for(int i=0; i < Constants.ROB_COUNT; ++i){
			ROBEntry[i] = new Instruction();			
		}		
		ROBIndex = 0;
	}
	//write an entry into ROB
	public void writeROBEntry(Instruction data) throws Exception{
		if(ROBIndex >= 0 && ROBIndex < Constants.ROB_COUNT)
		{
			ROBEntry[ROBIndex] = data;
			ROBIndex++;
		}
		else{
			throw new Exception("Illegal ROB index : "+ROBIndex);}
	}
	//read an entry from ROB
	public Instruction[] readROBEntry(){
		return ROBEntry;
	}
	//read an entry from ROB
	public Instruction readROBEntry(int index) throws Exception{
		if(index >= 0 && index < Constants.ROB_COUNT)
			return this.ROBEntry[index];
		else
			throw new Exception("Illegal register address : R"+index);
	}
	//remove an entry from ROB
	public void removeROBEntry() throws Exception{
		int index = 0;
		if(index >= 0 && index < Constants.ROB_COUNT){
			if(this.ROBEntry[index] != null){
				int incIndex = Constants.ROB_COUNT - index -1;
				for(int i = 1; i < incIndex; i++){
					ROBEntry[index] = ROBEntry[i];
					if(ROBEntry[i+1].opCode == null){
						ROBEntry[i] = new Instruction();
					  break;}
					index++;
				}				
				ROBEntry[Constants.ROB_COUNT - 1] = new Instruction();				
				ROBIndex--;
			}
		}
		else{
			throw new Exception("Illegal IQ Index : "+index);}
	}
	//set status field and target address if brach is taken
	public void setBranchTaken(Constants.OpCode brnOpcode, boolean isBrnTaken, long brnTrgAdd){
		for (Instruction rOB : ROBEntry) {
			if(rOB.opCode == brnOpcode){
				rOB.isBrnTaken = isBrnTaken;
				rOB.brnTrgAdd = brnTrgAdd;
			}
		}
	}
}
