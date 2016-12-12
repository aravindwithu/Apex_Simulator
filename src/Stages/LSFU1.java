package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class LSFU1 implements ProcessListener {
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	public static int getNextInstuction = -1;
	CycleListener result;
	
	/**
	 * Constructor for LSFU1 stage initializes PC(instruction Address), result(like a latch which has results of the stage).
	 * @param processor a Processor object.
	 */
	public LSFU1(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}

	/**
	 * LSFU1 process method performs the memory operations for LOAD and STORE.
	 * fetches data from IQ for LOAD and STORE instructions. 
	 */
	public void process() {
		try {			
			if(getNextInstuction==-1){
			Instruction tempIns = null;
			int countIQ = Constants.IQ_COUNT;
			int IQInsAdd = 0;
	
			for(int i=0; i < countIQ; i++){
				if(processor.iQ.readIQEntry(i).opCode != null){
						if( !processor.iQ.readIQEntry(i).inExecution
							&& !processor.iQ.readIQEntry(i).src1Stall
							&& !processor.iQ.readIQEntry(i).src2Stall)
						{
							tempIns = processor.iQ.readIQEntry(i);		
							IQInsAdd = i;
						    break;
						}else{
							Processor.noIssueCount++;
						}
					}
				else{
					break;
				}
				
			}
			
			if(tempIns == null){	
				instruction = null;
				return;
			}
		
			if(tempIns != null && (tempIns.opCode.ordinal() == 8 || tempIns.opCode.ordinal() == 9))
			{
				processor.iQ.readIQEntry(IQInsAdd).inExecution = true;
				instruction = tempIns;
				processor.iQ.removeIQEntry(IQInsAdd);		
			}
			else{
				instruction = null;
				return;
			}
			
			switch(instruction.opCode.ordinal()){
			case 8:
				if(instruction.literal == null){	//LOAD rdest, rscr1, rscr2
					result.write(instruction.src1 + instruction.src2);
					instruction.destVal = instruction.src1+instruction.src2;
				}else{								//LOAD rdest, rscr1, literal
					result.write(instruction.src1 + instruction.literal);
					instruction.destVal = (long)(instruction.src1+instruction.literal);
				}
				break;
			case 9:
				if(instruction.isLiteral){
					result.write(instruction.src2 + instruction.literal);
					instruction.destVal = instruction.src1+instruction.literal;
				}else {
					result.write(instruction.src1 + instruction.src2);
					instruction.destVal = instruction.src1+instruction.src2;
				}
				break;
			}
				
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
