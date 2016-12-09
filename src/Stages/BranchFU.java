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
	//Latch pc;
	CycleListener result;
	
	private boolean isStalled;

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
		
			/*if(processor.isHalt == true){
				instruction = null;
				return;
			}*/
			
			instruction = processor.decode.instruction;
			if(instruction != null && instruction.opCode.ordinal() < 10){
				instruction = null;
				return;			
			}		
			
			if(instruction != null){
				 if(instruction.src1Stall && instruction.src2Stall){
					  isStalled = true;
				  }
			}
			
			processor.decode.readSources();	
			
			if(isStalled && instruction != null){
				if(instruction.src1 != null || instruction.src2 != null){
					
					if(instruction.src1Add!=null && processor.writeBack.instruction != null  && processor.writeBack.instruction.dest != null
						   && processor.writeBack.instruction.dest.intValue() == instruction.src1Add){
					   instruction.src1 = processor.register.readReg(instruction.src1Add.intValue());
					   isStalled = false;
					}					
					if(instruction.src1Add!=null && processor.memoryStage.instruction != null  && processor.memoryStage.instruction.dest != null
						   && processor.memoryStage.instruction.dest.intValue() == instruction.src1Add){
					   instruction.src1 = processor.memoryStage.result.temRread();
					   isStalled = false;
					}	
			   }		
			}
						
			if(isStalled){
					instruction = null;
					return;									
			}
					
			if(instruction!=null && instruction.opCode == Constants.OpCode.HALT){				
				processor.isHalt = true;				
			}
			pc.write(processor.decode.pc.read());	
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
