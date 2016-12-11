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

	private long nextPc;

	/**
	 * Constructor for Fetch stage initializes PC(instruction Address), result(like a latch which has results of the stage).
	 * @param processor a Processor object.
	 */
	public Fetch(Processor processor) {
		this.processor = processor;
		pc = new CycleListener(processor);
		nextPc = Constants.START_ADDRESS;
		processor.processListeners.add(this);
	}

	/**
	 * Fetch process method fetches the next instruction by instruction address from the instructions aray list - get instruction method 
	 * which process the instruction array list .
	 */
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
					nextPc = nextPc + 4;
		}
	}
	
	/**
	 * clearStage method clears the Fetch stage.
	 */
	public void clearStage() {
		nextPc = Constants.START_ADDRESS;
		instruction = null;
	}
	
	/**
	 * clearStage method gets the new instruction address from branchFU to fetch when the branch is taken.
	 * @param newFetchAdd of type long new fetch instruction address.
	 */
	public void clearStage(Long newFetchAdd){	
		nextPc = newFetchAdd;
		instruction = null;
	}
	
	/**
	 * pcValue method returns the pc Value(instruction address) of the Fetch stage.
	 * @return long value of the pc Value(instruction address)
	 */
	public Long pcValue(){
		return pc.read();
	}
	
	/**
	 * toString method returns the instruction currently in Fetch as string if instruction is not null or returns the IDLE constants.
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
