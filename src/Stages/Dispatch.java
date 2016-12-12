package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class Dispatch implements ProcessListener {
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	
	/**
	 * Constructor for Decode stage initializes PC(instruction Address), result(like a latch which has results of the stage).
	 * @param processor a Processor object.
	 */
	public Dispatch(Processor processor) {
		pc = new CycleListener(processor);
		processor.processListeners.add(this);
		this.processor = processor;
	}

	/** //false checkin ALU1
	 * Decode process method performs relevant action for halt, stall and decodes the necessary instruction. 
	 */
	public void process() {
	try {
		
		if(processor.isHalt == true){
			instruction = null;
			return;
		}	
		instruction = null;	
		if(processor.isStalled){return;}	
		if(processor.decode.instruction != null){
			pc.write(processor.decode.pc.read());
			instruction = processor.decode.instruction;
			instruction.insPc = processor.decode.pc.read(); 
			readSources();
			if(processor.iQ.writeIQEntry(instruction)){	
				processor.rOB.writeROBEntry(instruction);}
		}	
		else{return;}
			
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
						System.out.println(instruction.src1Add.intValue());
						instruction.src1 = processor.register.readReg(instruction.src1Add.intValue());
						 if(!processor.register.getIsRegValid(instruction.src1Add.intValue())){
							 instruction.src1 = (long)0;
						}
						 
					}
					else{
						instruction.src1 = null;
					}
					
					if(instruction.src2Add != null){
						instruction.src2 = processor.register.readReg(instruction.src2Add.intValue());
						if(!processor.register.getIsRegValid(instruction.src2Add.intValue())){
							instruction.src2 = (long)0;
						}
					}
					else{
						instruction.src2 = null;
					}
					
					/*if(instruction.opCode == Constants.OpCode.STORE && instruction.dest == null && !instruction.isLiteral){
						instruction.dest = processor.register.readReg(instruction.destAdd.intValue());
					}*/					
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
