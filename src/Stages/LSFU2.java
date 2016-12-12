package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class LSFU2 implements ProcessListener {
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	
	CycleListener result;
	
	/**
	 * Constructor for Memory stage initializes PC(instruction Address), result(like a latch which has results of the stage).
	 * @param processor a Processor object.
	 */
	public LSFU2(Processor processor) {
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
	
				if(processor.lSFU.instruction != null){
					instruction = processor.lSFU.instruction;
					
					if(instruction != null && instruction.opCode.ordinal() != 8 && instruction.opCode.ordinal() != 9){
						instruction = null;
						return;			
					}
					
					if(instruction != null){
						 if(instruction.src1Stall || instruction.src2Stall){
							 instruction = null;
								return;	
						  }
					}
					
					pc.write(processor.lSFU.pc.read());

					switch(instruction.opCode.ordinal()){
						case 8:
							if(instruction.literal == null){	//LOAD rdest, rscr1, rscr2
								result.write(instruction.src1 + instruction.src2);
								instruction.destVal = instruction.src1+instruction.src2;
							}else{								//LOAD rdest, rscr1, literal
								result.write(instruction.src1 + instruction.literal);
								instruction.destVal = instruction.src1+instruction.src2;
							}
							break;
						case 9:
							if(instruction.isLiteral){
								result.write(instruction.src2 + instruction.literal);
								instruction.destVal = instruction.src1+instruction.src2;
							}else {
								result.write(instruction.src1 + instruction.src2);
								instruction.destVal = instruction.src1+instruction.src2;
							}
							break;
					}
									
						if(instruction.opCode == Constants.OpCode.STORE){
							
							if(instruction.isLiteral){
								instruction.src1 = processor.register.readReg(instruction.src1Add.intValue());/*
								processor.memory.writeMem(processor.lSFU.result.read().intValue(), instruction.src1);
								processor.memory.writeCacheMem(processor.lSFU.result.read().intValue(), instruction.src1.intValue());*/

								processor.memory.writeMem(instruction.destVal.intValue(), instruction.src1);
								processor.memory.writeCacheMem(instruction.destVal.intValue(), instruction.src1.intValue());
								}
							else{
								processor.memory.writeMem(instruction.destVal.intValue(), instruction.dest);
								processor.memory.writeCacheMem(instruction.destVal.intValue(), instruction.src1.intValue());
							}
							
							
						} else if(instruction.opCode == Constants.OpCode.LOAD){
							if(processor.memory.readCacheMem(instruction.destVal.intValue())!=0){
								instruction.destVal = processor.memory.readCacheMem(instruction.destVal.intValue());
							}else{
								instruction.destVal = processor.memory.readMem(instruction.destVal.intValue());
							}
						} /*else {				
							result.write(processor.lSFU.result.read());
						}*/
						
					}
			/*else if(processor.branchFU.instruction != null){
				instruction = processor.branchFU.instruction;
				pc.write(processor.branchFU.pc.read());
			}
			else if(processor.multiplicationFU.instruction != null && processor.mulResultFoundCheck==true){
				instruction = processor.multiplicationFU.instruction;
				result.write(MultiplicationFU.mulResult);
				pc.write(processor.multiplicationFU.pc.read());
				Processor.mulCount = 0;
				processor.multiplicationFU.instruction = null;
			}*/
			else
			{	
				instruction = null;
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
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
