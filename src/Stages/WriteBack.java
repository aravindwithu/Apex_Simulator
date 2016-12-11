package Stages;

import java.util.ArrayList;
import java.util.List;

import Apex_Simulator.Apex_Simulator;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Apex_Simulator.Processor;
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

			List<Instruction> instructionList = new ArrayList<Instruction>();
			if(processor.multiplicationFU.instruction != null && processor.mulResultFoundCheck==true){
				System.out.println("inside write back stage");
				instruction = processor.multiplicationFU.instruction;
				System.out.println("MultiplicationFU.mulResult"+ MultiplicationFU.mulResult);
				result.write(MultiplicationFU.mulResult);
				pc.write(processor.multiplicationFU.pc.read());
				Processor.mulCount = 0;
				processor.multiplicationFU.instruction = null;
			}else if(processor.branchFU.instruction != null){
				instructionList.add(processor.branchFU.instruction);
			}else if(processor.fALU2.instruction != null){
				instructionList.add(processor.fALU2.instruction);
			}
			if(instructionList.size() > 0){
				for (Instruction instructionObj : instructionList) {
					instruction = instructionObj;
					//pc.write(processor.multiplicationFU.pc.read()); need to analyse how pc vlaue can be taken form whch Fu
					if(instruction != null){
						if(instruction.opCode == Constants.OpCode.HALT){
							processor.cL.cycle++;
							processor.memory.clearInstructions();
							//processor.multiplicationFU.clearStage(); nned to analse previous FU and clear it
							Apex_Simulator.display();
							System.out.println("Aborting execution! HALT encountered.");
							processor.isHalt = false;
						} else if((instruction.opCode.ordinal() < Constants.OpCode.STORE.ordinal() || instruction.opCode == Constants.OpCode.MOV)){
							//processor.register.writeReg(instruction.dest.intValue(), processor.multiplicationFU.result.read()); // need to analse previous Fu and update register
						}
						++Processor.INS_COUNT;
					}

				}
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
