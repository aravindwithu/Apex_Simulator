package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
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
		nextPc = Constants.START_ADDRESS;
		processor.processListeners.add(this);
	}

	public void process() {
		
		if(processor.isHalt == true){
			instruction = null;
			return;
		}
		
		if(processor.isStalled){
			return;
			}
			
		pc.write(nextPc);
		instruction = processor.memory.getInstruction(nextPc);
		if(instruction != null){
			nextPc = nextPc + 4;}		
	}
	
	public void clearStage() {
		nextPc = Constants.START_ADDRESS;
		instruction = null;
	}
	
	public void clearStage(Long newFetchAdd){	
		nextPc = newFetchAdd;
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
