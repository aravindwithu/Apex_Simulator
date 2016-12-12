package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class BranchFU implements ProcessListener{
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	CycleListener result;

	/**
	 * Constructor for BranchFU stage initializes PC(instruction Address), result(like a latch which has results of the stage).
	 * @param processor a Processor object.
	 */
	public BranchFU(Processor processor) {
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
					
			instruction = null;		
			
			Instruction tempIns = null;
			int countIQ = Constants.IQ_COUNT;
			int IQInsAdd = 0;
	
			for(int i=0; i < countIQ; i++){
				if(processor.iQ.readIQEntry(i).opCode != null){
						if( !processor.iQ.readIQEntry(i).inExecution
							&& !processor.iQ.readIQEntry(i).src1Stall
							&& !processor.iQ.readIQEntry(i).src2Stall)
						{
							tempIns = processor.iQ.readIQEntry(i);	
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
		
			if(tempIns != null && (tempIns.opCode == Constants.OpCode.BZ || tempIns.opCode == Constants.OpCode.BNZ)){
								
				if(processor.register.getZReg() == -1 && processor.register.getZFlag((int)processor.register.getZReg()) == -1)
				{				
					return;
				}			
				
				else if((processor.fALU1.instruction != null && processor.register.getZReg() == processor.fALU1.instruction.dest))
				{
					return;
				}
				
			}
			
		
			
			if(tempIns != null && tempIns.opCode.ordinal() > 9)
			{
				processor.iQ.readIQEntry(IQInsAdd).inExecution = true;
				instruction = tempIns;
				processor.iQ.removeIQEntry(IQInsAdd);		
			}
			else{
				instruction = null;
				return;
			}	
				
			if(instruction != null){
				pc.write(instruction.insPc);
				switch(instruction.opCode.ordinal()){						
				case 10: //BZ, 							
					if(processor.register.getZFlag((int)processor.register.getZReg()) == 0){	
						processor.rOB.setBranchTaken(instruction.opCode, true, instruction.insPc+instruction.literal);
						processor.decode.clearStage();
						processor.dispatch.clearStage();
						if(processor.iQ.readIQEntry(IQInsAdd+1).opCode != null){
							processor.iQ.flushIQEntry(IQInsAdd + 1);
							}
						processor.isStalled = true;
						instruction.isROBCommit = true;

					}

					break;
				case 11: //BNZ,			
					if(processor.register.getZFlag((int)processor.register.getZReg()) == 1){
						processor.rOB.setBranchTaken(instruction.opCode, true, instruction.insPc+instruction.literal);
						processor.decode.clearStage();
						processor.dispatch.clearStage();
						if(processor.iQ.readIQEntry(IQInsAdd+1).opCode != null){
						processor.iQ.flushIQEntry(IQInsAdd + 1);
						}
						processor.isStalled = true;
					}
					break;
				case 12: //JUMP, 
					processor.rOB.setBranchTaken(instruction.opCode, true, instruction.src1+instruction.literal);
					processor.decode.clearStage();
					processor.dispatch.clearStage();
					if(processor.iQ.readIQEntry(IQInsAdd+1).opCode != null){
						processor.iQ.flushIQEntry(IQInsAdd + 1);
						}
					processor.isStalled = true;
					break;
				case 13: //BAL, 
					if(processor.decode.pc != null){						
						    processor.register.setReg_X(instruction.insPc);
						}
						processor.rOB.setBranchTaken(instruction.opCode, true, instruction.src1+instruction.literal);
						processor.decode.clearStage();
						processor.dispatch.clearStage();
						if(processor.iQ.readIQEntry(IQInsAdd+1).opCode != null){
							processor.iQ.flushIQEntry(IQInsAdd + 1);
							}
						processor.isStalled = true;
					break;
				case 14: //HALT
					break;
				}
			}				
			
			if(instruction!=null && instruction.opCode == Constants.OpCode.HALT){				
				processor.isHalt = true;
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
