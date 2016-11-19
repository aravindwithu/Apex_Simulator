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

	public Delay(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}
	
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

	public void clearStage() {
		pc.write(0);
		result.write(0);
		instruction = null;
	}
	
	public Long pcValue(){
		return pc.read();
	}
	
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
