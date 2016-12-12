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
	public List<Instruction> instructionList;

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
		instructionList = new ArrayList<Instruction>();
	}

	/**
	 * WriteBack process method performs the register write operations.
	 * The registers are written when the instruction enters the write back stage in same cycle. 
	 * Aborts the simulation when HALT instruction is encountered.
	 */
	public void process() {
		try {
			instructionList = new ArrayList<Instruction>();
			if(processor.multiplicationFU.instruction != null && processor.multiplicationFU.mulCount == 3){
				instructionList.add(processor.multiplicationFU.instruction);
				processor.multiplicationFU.mulCount = 0;
				processor.multiplicationFU.instruction = null;
			}		
			
			
			if(processor.fALU2.instruction != null){
				instructionList.add(processor.fALU2.instruction);
			}
			
			if(processor.branchFU.instruction != null){
				instructionList.add(processor.branchFU.instruction);
			}
			
			if(processor.lSFU2.instruction != null && LSFU1.getNextInstuction == 1){
				instructionList.add(processor.lSFU2.instruction);
				LSFU1.getNextInstuction = -1;
				processor.lSFU2.instruction = null;
			}
			
			if(instructionList.size() > 0){
				for (Instruction instructionObj : instructionList) {
					instruction = instructionObj;
					//pc.write(instruction.insPc);
					if(instruction != null){
						if(instruction.opCode == Constants.OpCode.HALT){
							processor.cL.cycle++;
							processor.memory.clearInstructions();
							Apex_Simulator.display();
							System.out.println("Aborting execution! HALT encountered.");
							processor.isHalt = false;
						}
										
						else if(instruction.opCode.ordinal() < Constants.OpCode.STORE.ordinal()){
							processor.register.writeReg(instruction.dest.intValue(), instruction.destVal); // need to analse previous Fu and update register
						}
						//System.out.println(instruction.opCode + "-ol " + instruction.isROBCommit);
						instruction.isROBCommit = true;
						System.out.println(instruction.opCode+" " +instruction.dest + "-n " + instruction.isROBCommit);
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
		instructionList = null;
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
			String insString = "";
			for (Instruction ins : instructionList) {
				insString = insString + "	" + ins.toString();
			}
			return insString;
		}
	}
}
