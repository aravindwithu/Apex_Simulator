package Stages;

import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Apex_Simulator.Processor;
import Utility.Constants;
import Utility.Instruction;

public class ROBCommit implements ProcessListener{
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;

	CycleListener result;
		
	public ROBCommit(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}

	public void process() {
		try {
			instruction = null;
			while(true){
				if(processor.rOB.readROBEntry(0).opCode != null){
						if(processor.rOB.readROBEntry(0).isROBCommit)
						{
							processor.rOB.readROBEntry(0).isROBCommit = false;							
							instruction = processor.rOB.readROBEntry(0);	
							if(instruction.opCode != Constants.OpCode.STORE){
								processor.register.setBackEndPhyReg(instruction.archdest.intValue(), instruction.dest.intValue());
							}
							processor.rOB.removeROBEntry();
						}	
						else{
							break;
						}
					}
				else{
					break;
				}				
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * clearStage method clears the WriteBack stage.
	 */
	public void clearStage() {
		pc.write(0);
		result.write(0);
		instruction = null;
	}
	
	/**
	 * pcValue method returns the pc Value(instruction address) of the WriteBack stage.
	 * @return long value of the pc Value(instruction address)
	 */
	public Long pcValue(){
		return pc.read();
	}
	
	/**
	 * toString method returns the instruction currently in WriteBack as string if instruction is not null or returns the IDLE constants.
	 * @return String of the instruction or IDLE constants
	 */
	@Override
	public String toString() {
		if(instruction == null){
			return Constants.OpCode.IDLE.name();
		}
		else{
			return instruction.toString();
		}
	}
}
