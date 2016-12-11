package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class MemoryStage implements ProcessListener {
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	
	CycleListener result;
	
	/**
	 * Constructor for Memory stage initializes PC(instruction Address), result(like a latch which has results of the stage).
	 * @param processor a Processor object.
	 */
	public MemoryStage(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}

	/**
	 * MemoryStage process method performs the memory operations for LOAD and STORE.
	 * fetches data from memory for LOAD and writes data to memory for STORE. 
	 */
	public void process() {
		try {			
			if(processor.fALU2.instruction != null){
				instruction = processor.fALU2.instruction;
				pc.write(processor.fALU2.pc.read());
							
				if(instruction.opCode == Constants.OpCode.STORE){
					
					if(instruction.isLiteral){
						instruction.src1 = processor.register.readReg(instruction.src1Add.intValue());
						processor.memory.writeMem(processor.fALU2.result.read().intValue(), instruction.src1);
						}
					else{
						processor.memory.writeMem(processor.fALU2.result.read().intValue(), instruction.dest);
					}
					
					
				} else if(instruction.opCode == Constants.OpCode.LOAD){
					result.write(processor.memory.readMem(processor.fALU2.result.read().intValue()));
				} else {				
					result.write(processor.fALU2.result.read());
				}
				
			}
			/*else if(processor.delay.instruction != null){
				instruction = processor.delay.instruction;
				pc.write(processor.delay.pc.read());
			}*/	
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

	/**
	 * clearStage method clears the Memory stage.
	 */
	public void clearStage() {
		pc.write(0);
		result.write(0);
		instruction = null;
	}
	
	/**
	 * pcValue method returns the pc Value(instruction address) of the Memory stage.
	 * @return long value of the pc Value(instruction address)
	 */
	public Long pcValue(){
		return pc.read();
	}
	
	/**
	 * toString method returns the instruction currently in Memory as string if instruction is not null or returns the IDLE constants.
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
