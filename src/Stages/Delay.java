package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class Delay implements ProcessListener{
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	
	//Latch pc;
	CycleListener result;

	/**
	 * Constructor for Delay stage initializes PC(instruction Address), result(like a latch which has results of the stage).
	 * @param processor a Processor object.
	 */
	public Delay(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}
	
	/**
	 * Delay process method acts as an one cycle delay for the Branch FU stage. 
	 */
	public void process() {		
		pc.write(processor.branchFU.pc.read());
		instruction = processor.branchFU.instruction;
		
		if(instruction != null){
			
			switch(instruction.opCode.ordinal()){						
			case 10: //BZ, 							
				if(processor.isZero){						
					processor.fetch.clearStage(pc.temRread() + instruction.literal);
					processor.decode.clearStage();	
					processor.isBranchZ = false;
				}
				else{
					processor.isBranchZ = false;
				}
				break;
			case 11: //BNZ,			
				if(!processor.isZero){						
					processor.fetch.clearStage(pc.temRread() + instruction.literal);
					processor.decode.clearStage();
					processor.isBranchZ = false;
				}
				else{
					processor.isBranchZ = false;
				}
				break;
			case 12: //JUMP, 
				processor.fetch.clearStage(instruction.literal + instruction.src1);
				processor.decode.clearStage();
				break;
			case 13: //BAL, 
				if(processor.decode.pc != null){
					processor.register.setReg_X(processor.decode.pc.read());
					}
					processor.fetch.clearStage(instruction.src1+instruction.literal);
					processor.decode.clearStage();
				break;
			case 14: //HALT
				//processor.isHalt = true;
				break;
			}
		}
	}

	/**
	 * clearStage method clears the Delay stage.
	 */
	public void clearStage() {
		pc.write(0);
		result.write(0);
		instruction = null;
	}
	
	/**
	 * pcValue method returns the pc Value(instruction address) of the Delay stage.
	 * @return long value of the pc Value(instruction address)
	 */
	public Long pcValue(){
		return pc.read();
	}
	
	/**
	 * toString method returns the instruction currently in Delay as string if instruction is not null or returns the IDLE constants.
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
