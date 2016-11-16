package Stages;

import Processor.Processor;
import Processor.CycleListener;
import Processor.ProcessListener;
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
		pc.write(processor.branchFU.pc.read());		
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
