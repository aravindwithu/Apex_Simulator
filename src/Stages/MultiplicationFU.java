package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class MultiplicationFU implements ProcessListener{
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	public int mulCount;
	//Latch pc;
	CycleListener result;
	/**
	 * Constructor for BranchFU stage initializes PC(instruction Address), result(like a latch which has results of the stage).
	 * @param processor a Processor object.
	 */
	public MultiplicationFU(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}
	
	/**
	 * BranchFU process method performs relevant control operations such as branching (BZ, BNZ, BAL, JUMP), and Halt
	 * Control flow instructions: BZ, BNZ, JUMP, BAL, HALT. Instructions following a BZ, BNZ, JUMP and BAL instruction in the pipeline 
	 * should be flushed on a taken branch. The zero flag (Z) is set only by arithmetic instructions in ALU. 
	 */
	
	public void process() {
		try{
			if(processor.multiplicationFU.mulCount == 0){				
				Instruction tempIns = null;
				int countIQ = Constants.IQ_COUNT;
				int IQInsAdd = 0;
		
				for(int i=0; i < countIQ; i++){
					if(processor.iQ.readIQEntry(i).opCode != null){
							if( !processor.iQ.readIQEntry(i).inExecution
								&& !processor.iQ.readIQEntry(i).src1Stall
								&& !processor.iQ.readIQEntry(i).src2Stall)
							{
								//processor.iQ.readIQEntry(i).inExecution = true;
								tempIns = processor.iQ.readIQEntry(i);						    
							    //processor.iQ.removeIQEntry(i);	
								IQInsAdd = i;
							    break;
							}else{
								Processor.noIssueCount++;
							}
						}
					else{
						break;
					}
					
				}
				
				if(tempIns == null){	
					instruction = null;
					return;
				}
							
				//instruction = processor.decode.instruction;
						
				if(tempIns != null && tempIns.opCode.ordinal() == 2)
				{
					processor.iQ.readIQEntry(IQInsAdd).inExecution = true;
					instruction = tempIns;
					processor.iQ.removeIQEntry(IQInsAdd);		
				}
				else{
					instruction = null;
					return;
				}	
									
				//	processor.decode.readSources();	
				
				if(instruction != null){				

					pc.write(instruction.insPc);
						
					
				}			
			}
			if(processor.multiplicationFU.mulCount == 2){
				//mulResult = (int)(instruction.src1*instruction.src2); nned to check why instruction's values are not obtained
				result.write(instruction.src1*instruction.src2);
				instruction.destVal = instruction.src1*instruction.src2;
				processor.multiplicationFU.mulCount++;
			}
			else{
				processor.multiplicationFU.mulCount++;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}
	/**
	 * clearStage method clears the BranchFU stage.
	 */
	public void clearStage() {
		pc.write(0);
		result.write(0);
		instruction = null;
	}
	
	/**
	 * pcValue method returns the pc Value(instruction address) of the BranchFU stage.
	 * @return long value of the pc Value(instruction address)
	 */
	public Long pcValue(){
		return pc.read();
	}
	
	/**
	 * toString method returns the instruction currently in BranchFU as string if instruction is not null or returns the IDLE constants.
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
