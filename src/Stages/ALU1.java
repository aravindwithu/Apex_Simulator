package Stages;

import Processor.Processor;
import Processor.CycleListener;
import Processor.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class ALU1 implements ProcessListener{
	//Latch pc;
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;	
	CycleListener result;	

	public ALU1(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}
	
	public void process() {
		try{
		
			instruction = processor.decode.instruction;
		
			if(instruction != null && instruction.opcode.ordinal() >= 9)
			{
				instruction = null;
				return;			
			}		
			processor.decode.readSources();
			pc.write(processor.decode.pc.read());
			if(processor.isPipelineStalled && instruction != null){
				if(instruction.src1 != null || instruction.src2 != null){
					
				  if(processor.writeBack.instruction != null  && processor.writeBack.instruction.dest != null
						   && processor.writeBack.instruction.dest.intValue() == instruction.src1Add){
					   instruction.src1 = instruction.src1Add != null ? processor.register.readReg(instruction.src1Add.intValue()) : null;
					   processor.isPipelineStalled = false;
				   }
				  else if(processor.writeBack.instruction != null && processor.writeBack.instruction.dest != null
						   && processor.writeBack.instruction.dest.intValue()  == instruction.src2Add){
					   instruction.src2 = instruction.src2Add != null ? processor.register.readReg(instruction.src2Add.intValue()) : null;
					   processor.isPipelineStalled = false;	
				   } 
					
				  if(processor.memoryStage.instruction != null  && processor.memoryStage.instruction.dest != null
						   && processor.memoryStage.instruction.dest.intValue() == instruction.src1Add){
					   instruction.src1 = processor.memoryStage.result.temRread();
					   processor.isPipelineStalled = false;
				   }
				  else if(processor.memoryStage.instruction != null  && processor.memoryStage.instruction.dest != null
						   && processor.memoryStage.instruction.dest.intValue()  == instruction.src2Add){
					   instruction.src2 = processor.memoryStage.result.temRread();
					   processor.isPipelineStalled = false;
				   } 
				   
				   if(processor.fALU2.instruction != null && processor.fALU2.instruction.dest != null
						   && processor.fALU2.instruction.dest.intValue() == instruction.src1Add
						   && processor.fALU2.instruction.opcode != Constants.LOAD){
					   instruction.src1 = processor.fALU2.result.temRread();
					   processor.isPipelineStalled = false;
				   }
				   else if(processor.fALU2.instruction != null  && processor.fALU2.instruction.dest != null
						   && processor.fALU2.instruction.dest.intValue()  == instruction.src2Add
						   && processor.fALU2.instruction.opcode != Constants.LOAD){
					   instruction.src2 = processor.fALU2.result.temRread();
					   processor.isPipelineStalled = false;
				   }
			   }			
			}
			
			if(processor.isPipelineStalled){
				instruction = null;
				return;	
			}
		
		}
		catch(Exception e)
		{
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
		return instruction == null ? (Constants.STALL.name()+ "          ") : instruction.toString();
	}

}
