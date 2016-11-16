package Stages;

import Processor.Processor;
import Processor.CycleListener;
import Processor.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class Fetch implements ProcessListener {
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	
	//Latch pc;
	private long nextPc;

	public Fetch(Processor processor) {
		this.processor = processor;
		pc = new CycleListener(processor);
		nextPc = Constants.startAddress;
		processor.processListeners.add(this);
	}

	public void process() {
		if(processor.isPipelineStalled){return;}
		
		pc.write(nextPc);
		instruction = processor.memory.getInstruction(nextPc);
		if(instruction != null)
			nextPc = nextPc + 4;
	}
	
	public void clearStage() {
		nextPc = Constants.startAddress;
		instruction = null;
	}
	
	public void clearStage(Long newFetchAdd, boolean isOffset){
		long a = processor.decode.pc.read();
		if(isOffset)
			nextPc = processor.decode.pc.read() + newFetchAdd;
		else
			nextPc = newFetchAdd;
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
