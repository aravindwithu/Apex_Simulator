package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class MultiplicationFU implements ProcessListener{
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	public static int mulResult;
	public static long mulPCValue;
	//Latch pc;
	CycleListener result;
	/**
	 * Constructor for BranchFU stage initializes PC(instruction Address), result(like a latch which has results of the stage).
	 * @param processor a Processor object.
	 */
	public MultiplicationFU(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}
	
	/**
	 * BranchFU process method performs relevant control operations such as branching (BZ, BNZ, BAL, JUMP), and Halt
	 * Control flow instructions: BZ, BNZ, JUMP, BAL, HALT. Instructions following a BZ, BNZ, JUMP and BAL instruction in the pipeline 
	 * should be flushed on a taken branch. The zero flag (Z) is set only by arithmetic instructions in ALU. 
	 */
	
	
	public void process() {
		instruction = null;
		
				if(Processor.mulCount == 0){
					int countIQ = Constants.IQ_COUNT;
					//instruction = processor.decode.instruction;
					try{
					for(int i=0; i < countIQ; i++){
			               //false checkin ALU1
							if(processor.IQEntry.readIQEntry(i).opCode != null){
									if((processor.IQEntry.readIQEntry(i).opCode.ordinal() == 2)
										&& !processor.IQEntry.readIQEntry(i).inExecution
										&& !processor.IQEntry.readIQEntry(i).src1Stall
										&& !processor.IQEntry.readIQEntry(i).src2Stall)
									{
									   processor.IQEntry.readIQEntry(i).inExecution = true;
									   instruction = processor.IQEntry.readIQEntry(i);
									   break;
									}	
								}
							else{
								break;
							}
							
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				
				}
				
				if(instruction == null)
				{
					return;				
				}
				/*if(instruction != null && instruction.opCode.ordinal() != 2)
				{
					instruction = null;
					return;			
				}
				
				if(instruction != null){
					 if(instruction.src1Stall || instruction.src2Stall){
						 instruction = null;
							return;	
					  }
				}*/
						
			//	processor.decode.readSources();	
				if(instruction != null){
					
					if(instruction.src1 != null){	
						if(instruction.src1FwdValIn == Constants.Stage.ALU2){
							if(instruction.src1Add!=null && processor.lSFU.instruction != null  
									&& processor.lSFU.instruction.dest != null
									   && processor.lSFU.instruction.dest.intValue() == instruction.src1Add
									   && processor.lSFU.instruction.opCode != Constants.OpCode.LOAD){
								   instruction.src1 = processor.lSFU.result.temRread();	
								   instruction.src1Stall = false;
							   }	
							
						}
						
						if(instruction.src1FwdValIn == Constants.Stage.LSFU){
							if( instruction.src1Add!=null && processor.writeBack.instruction != null  
									&& processor.writeBack.instruction.dest != null
									   && processor.writeBack.instruction.dest.intValue() == instruction.src1Add){					  
								   try {
									instruction.src1 = processor.register.readReg(instruction.src1Add.intValue());
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								   instruction.src1Stall = false;
							   }
							
						}
						
					}
					if(instruction.src2 != null){	
						if(instruction.src2FwdValIn == Constants.Stage.ALU2){
							if(instruction.src2Add!=null && processor.lSFU!=null && processor.lSFU.instruction != null 
									&& processor.lSFU.instruction.dest != null
									   && processor.lSFU.instruction.dest.intValue()  == instruction.src2Add
									   && processor.lSFU.instruction.opCode != Constants.OpCode.LOAD){
								   instruction.src2 = processor.lSFU.result.temRread();	
								   instruction.src2Stall = false;
							   }
							
						}
						
						if(instruction.src2FwdValIn == Constants.Stage.LSFU){
							if(instruction.src2Add != null && processor.writeBack.instruction != null 
									&& processor.writeBack.instruction.dest != null
									   && processor.writeBack.instruction.dest.intValue()  == instruction.src2Add){
								   try {
									instruction.src2 = processor.register.readReg(instruction.src2Add.intValue());
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								   instruction.src2Stall = false;
							   }
							
						}
					}
					if(Processor.mulCount!=0){
						pc.write(mulPCValue);
					}else{
						mulPCValue = processor.decode.pc.read();
						pc.write(processor.decode.pc.read());
					}
					if(Processor.mulCount == 2){
						//mulResult = (int)(instruction.src1*instruction.src2); nned to check why instruction's values are not obtained
						
						processor.mulResultFoundCheck = true;
					}else{
						Processor.mulCount++;
					}
				}
			}
	/**
	 * clearStage method clears the BranchFU stage.
	 */
	public void clearStage() {
		pc.write(0);
		result.write(0);
		instruction = null;
	}
	
	/**
	 * pcValue method returns the pc Value(instruction address) of the BranchFU stage.
	 * @return long value of the pc Value(instruction address)
	 */
	public Long pcValue(){
		return pc.read();
	}
	
	/**
	 * toString method returns the instruction currently in BranchFU as string if instruction is not null or returns the IDLE constants.
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
