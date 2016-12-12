package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class Decode implements ProcessListener {
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	
	/** //false checkin ALU1
	 * Constructor for Decode stage initializes PC(instruction Address), result(like a latch which has results of the stage).
	 * @param processor a Processor object.
	 */
	public Decode(Processor processor) {
		pc = new CycleListener(processor);
		processor.processListeners.add(this);
		this.processor = processor;
	}

	/**
	 * Decode process method performs relevant action for halt, stall and decodes the necessary instruction. 
	 */
	public void process() {
	try {
		
		if(processor.isHalt == true){
			instruction = null;
			return;
		}	
			
		if(processor.isStalled){return;}
		
			pc.write(processor.fetch.pc.read());
			instruction = processor.fetch.instruction;
			if(instruction != null){
				readSources();
				
			}
	} 
	catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * ReadSources method reads the source registers of the instruction and fetches the same from register file. 
	 */
	public void readSources(){
			try {
				if(instruction != null){
					if(instruction.src1Add != null){
						instruction.archsrc1Add = instruction.src1Add;
						if(instruction.opCode != Constants.OpCode.JUMP){
						instruction.src1Add = processor.register.getFrontEndPhyReg(instruction.src1Add.intValue());
						}
						else{
							instruction.src1Add = processor.register.getXRegPhyRegAdd();
						}
					}
				
					
					if(instruction.src2Add != null){
						instruction.archsrc2Add = instruction.src2Add;
						instruction.src2Add = processor.register.getFrontEndPhyReg(instruction.src2Add.intValue());
					}
				
					
					if(instruction.dest != null){	
						instruction.archdest = instruction.dest;
						instruction.dest = processor.register.setFrontEndPhyReg(instruction.dest.intValue());						
					}
					
					if(instruction.opCode.ordinal() > 9 && processor.dispatch.instruction != null){
						processor.register.setZReg(processor.dispatch.instruction.dest.intValue());
					}
				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * clearStage method clears the Decode stage.
	 */
	public void clearStage() {
		pc.write(0);
		instruction = null;
	}
	
	/**
	 * pcValue method returns the pc Value(instruction address) of the Decode stage.
	 * @return long value of the pc Value(instruction address)
	 */
	public Long pcValue(){
		return pc.read();
	}
	
	/**
	 * toString method returns the instruction currently in Decode as string if instruction is not null or returns the IDLE constants.
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
