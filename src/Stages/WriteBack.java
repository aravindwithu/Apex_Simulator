package Stages;

import Apex_Simulator.Main;
import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class WriteBack implements ProcessListener{
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;

	CycleListener result;
	
	/**
	 * Constructor for Write Back stage initializes PC(instruction Address), result(like a latch which has results of the stage).
	 * @param processor a Processor object.
	 */
	public WriteBack(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}

	/**
	 * WriteBack process method performs the register write operations.
	 * The registers are written when the instruction enters the write back stage in same cycle. 
	 * Aborts the simulation when HALT instruction is encountered.
	 */
	public void process() {
		try {
			instruction = processor.memoryStage.instruction;
			pc.write(processor.memoryStage.pc.read());
			if(instruction != null){
				if(instruction.opCode == Constants.OpCode.HALT){
					processor.cL.cycle++;
					processor.memory.clearInstructions();
					processor.memoryStage.clearStage();
					Main.display();
					System.out.println("Aborting execution! HALT encountered.");
					processor.isHalt = false;
					//System.exit(0);
				} else if((instruction.opCode.ordinal() < Constants.OpCode.STORE.ordinal() || instruction.opCode == Constants.OpCode.MOV)){
					processor.register.writeReg(instruction.dest.intValue(), processor.memoryStage.result.read());
				}
				++Processor.INS_COUNT;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * clearStage method clears the WriteBack stage.
	 */
	public void clearStage() {
		pc.write(0);
		result.write(0);
		instruction = null;
	}
	
	/**
	 * pcValue method returns the pc Value(instruction address) of the WriteBack stage.
	 * @return long value of the pc Value(instruction address)
	 */
	public Long pcValue(){
		return pc.read();
	}
	
	/**
	 * toString method returns the instruction currently in WriteBack as string if instruction is not null or returns the IDLE constants.
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
