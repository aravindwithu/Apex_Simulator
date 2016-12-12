package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class ALU2 implements ProcessListener{
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	
	CycleListener result;
	
	/**
	 * Constructor for ALU2 stage initializes PC(instruction Address), result(like a latch which has results of the stage).
	 * @param processor a Processor object.
	 */
	public ALU2(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}
	
	/**
	 * ALU2 process method performs relevant operations such as register-register (add, sub, .. etc), load, and store and 
	 * writes the result to the destination register temporarily. 
	 * Register-to-register instructions: ADD, SUB, MOVC, MUL, AND, OR, EX-OR (all done on the Integer ALU in two cycles(2nd cycle here)). 
	 * You can assume that the result of multiplying two registers will fit into a single register.
	 */
	public void process() {
		pc.write(processor.fALU1.pc.read());
		instruction = processor.fALU1.instruction;
		if(instruction != null){	
		   switch(instruction.opCode.ordinal()){
			case 0: //add
				result.write(instruction.src1+instruction.src2);
				instruction.destVal = instruction.src1+instruction.src2;
				break;
			case 1:	//sub
				result.write(instruction.src1-instruction.src2);
				instruction.destVal = instruction.src1-instruction.src2;
				break;
			case 2: // MUL
				result.write(instruction.src1*instruction.src2);
				instruction.destVal = instruction.src1*instruction.src2;
				break;
			case 3: //MOVC
				result.write(instruction.literal);
				instruction.destVal = instruction.literal;
				break;
			case 4://MOV
				result.write(instruction.src1);
				instruction.destVal = instruction.src1;
				break;			
			case 5: //AND
				result.write(instruction.src1 & instruction.src2);
				instruction.destVal = instruction.src1 & instruction.src2;
				break;
			case 6:	//OR
				result.write(instruction.src1 | instruction.src2);
				instruction.destVal = instruction.src1 | instruction.src2;
				break;
			case 7:	//XOR
				result.write(instruction.src1 ^ instruction.src2);
				instruction.destVal = instruction.src1 ^ instruction.src2;
				break;
				
			/*case 8: //LOAD
				if(instruction.literal == null){	//LOAD rdest, rscr1, rscr2
					result.write(instruction.src1 + instruction.src2);
				} else {								//LOAD rdest, rscr1, literal
					result.write(instruction.src1 + instruction.literal);
				}
				break;
			case 9: //Store
				if(instruction.isLiteral){
					result.write(instruction.src2 + instruction.literal);}
				else {
					result.write(instruction.src1 + instruction.src2);}
				break;		*/
			}
			
		   
		   if(processor.register.getZReg() != -1 && instruction.destVal == 0 && processor.register.getZReg() == instruction.dest){
			   processor.register.setZFlag(instruction.dest.intValue(), 0);
		   }
		   else if(processor.register.getZReg() != -1 && instruction.destVal != 0 && processor.register.getZReg() == instruction.dest){
			   processor.register.setZFlag(instruction.dest.intValue(), 1);
		   }	
		   
			/*if(result.temRread() == 0 && processor.isBranchZ){
				processor.isZero = true;
			}*/
		}
	}

	/**
	 * clearStage method clears the ALU2 stage.
	 */
	public void clearStage() {
		pc.write(0);
		result.write(0);
		instruction = null;
	}
	
	/**
	 * pcValue method returns the pc Value(instruction address) of the ALU2 stage.
	 * @return long value of the pc Value(instruction address)
	 */
	public Long pcValue(){
		return pc.read();
	}
	
	/**
	 * toString method returns the instruction currently in ALU2 as string if instruction is not null or returns the IDLE constants.
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
