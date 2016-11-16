package Stages;

import Processor.Processor;
import Processor.CycleListener;
import Processor.ProcessListener;
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
		
			instruction = processor.decode.instruction;
			if(instruction != null && instruction.opcode.ordinal() < 9)
			{
				instruction = null;
				return;			
			}		
			pc.write(processor.decode.pc.read());
			processor.decode.readSources();	
			
			if(processor.isPipelineStalled && instruction != null){
				if(instruction.src1 != null || instruction.src2 != null){
					
					if(processor.writeBack.instruction != null  && processor.writeBack.instruction.dest != null
						   && processor.writeBack.instruction.dest.intValue() == instruction.src1Add){
					   instruction.src1 = instruction.src1Add != null ? processor.register.readReg(instruction.src1Add.intValue()) : null;
					   processor.isPipelineStalled = false;
					}
					
					if(processor.memoryStage.instruction != null  && processor.memoryStage.instruction.dest != null
						   && processor.memoryStage.instruction.dest.intValue() == instruction.src1Add){
					   instruction.src1 = processor.memoryStage.result.temRread();
					   processor.isPipelineStalled = false;
					}
					
				   if(processor.delay.instruction != null && processor.delay.instruction.dest != null
						   && processor.delay.instruction.dest.intValue() == instruction.src1Add){
					   instruction.src1 = processor.delay.result.temRread();
					   processor.isPipelineStalled = false;
				   }	
			   }
				
			}
			
			if(processor.isPipelineStalled){
				instruction = null;
				return;	
			}
			
			if(instruction != null){
				
				switch(instruction.opcode.ordinal()){						
				case 9: //BZ, 			
					//if(instruction.src1 != null && instruction.src1 == 0)
					if(processor.isZero){
						processor.fetch.clearStage(instruction.literal, true);
						processor.decode.clearStage();
						processor.isPipelineStalled = true;
					}
					break;
				case 10: //BNZ,			
					//if(instruction.src1 != null && instruction.src1 != 0)
					if(!processor.isZero){
					processor.fetch.clearStage(instruction.literal,true);
					processor.decode.clearStage();
					processor.isPipelineStalled = true;
					}				
					break;
				case 11: //JUMP, 
					processor.fetch.clearStage(instruction.literal + instruction.src1,false);
					processor.decode.clearStage();
					processor.isPipelineStalled = true;
					break;
				case 12: //BAL, 
					if(processor.decode.pc != null)
						processor.register.setReg_X(processor.decode.pc.read()+1);
					processor.fetch.clearStage(instruction.src1+instruction.literal, false);
					processor.decode.clearStage();
					break;
				case 13: //HALT, STALL
					break;
				}
			}
		
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
	
	public CycleListener pcValue(){
		return pc;
	}
	
	@Override
	public String toString() {
		return instruction == null ? Constants.STALL.name() : instruction.toString();
	}

}
