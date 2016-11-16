package Stages;

import Main.Main;
import Processor.Processor;
import Processor.CycleListener;
import Processor.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class MemoryStage implements ProcessListener {
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	
	//Latch pc;
	CycleListener result;
	
	public MemoryStage(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}

	public void process() {
		try {			
			if(processor.fALU2.instruction != null){
				instruction = processor.fALU2.instruction;
				pc.write(processor.fALU2.pc.read());
							
				if(instruction.opcode == Constants.STORE){
					if(instruction.isLiteral)
						processor.memory.writeMem(processor.fALU2.result.read().intValue(), instruction.src1);
					else
						processor.memory.writeMem(processor.fALU2.result.read().intValue(), instruction.dest);
				} else if(instruction.opcode == Constants.LOAD){
					result.write(processor.memory.readMem(processor.fALU2.result.read().intValue()));
				} else {				
					result.write(processor.fALU2.result.read());
				}
				
			}
			else if(processor.delay.instruction != null){
				instruction = processor.delay.instruction;
				pc.write(processor.delay.pc.read());
			}	
			else
			{
				instruction = null;
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println(processor.fALU2.pc.read()+"==>"+instruction);
			//Main.displayRegisters();
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
