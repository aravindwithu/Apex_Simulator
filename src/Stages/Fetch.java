package Stages;

import Processor.Control;
import Processor.Latch;
import Utility.Constants;
import Utility.INS_OPCODE;

public class Fetch extends Stage {
	
	//Latch pc;
	private long nextPc;

	public Fetch(Control control) {
		this.control = control;
		pc = new Latch(control);
		nextPc = Constants.STRT_INST_ADDRESS;
		control.addProcessListener(this);
	}

	public void process() {
		if(control.isPipelineStalled) return;
		
		pc.write(nextPc);
		instruction = control.getMemory().getInstruction(nextPc);
		if(instruction != null)
			nextPc++;
	}
	
	@Override
	public void clearStage() {
		nextPc = Constants.STRT_INST_ADDRESS;
		instruction = null;
	}
	
	public void clearStage(Long newFetchAdd, boolean isOffset){
		if(isOffset)
			nextPc = control.getDecode().pc.read() + newFetchAdd;
		else
			nextPc = newFetchAdd;
		instruction = null;
	}
	
	@Override
	public String toString() {
		return instruction == null ? INS_OPCODE.STALL.name() : instruction.toString();
	}

}
