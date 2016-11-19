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

	public BranchFU(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}
	
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
			
			processor.decode.readSources();	
			
			if(processor.isStalled && instruction != null){
				if(instruction.src1 != null || instruction.src2 != null){
					
					if(instruction.src1Add!=null && processor.writeBack.instruction != null  && processor.writeBack.instruction.dest != null
						   && processor.writeBack.instruction.dest.intValue() == instruction.src1Add){
					   instruction.src1 = processor.register.readReg(instruction.src1Add.intValue());
					   processor.isStalled = false;
					}					
					if(instruction.src1Add!=null && processor.memoryStage.instruction != null  && processor.memoryStage.instruction.dest != null
						   && processor.memoryStage.instruction.dest.intValue() == instruction.src1Add){
					   instruction.src1 = processor.memoryStage.result.temRread();
					   processor.isStalled = false;
					}	
			   }		
			}
						
			if(processor.isStalled){
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
