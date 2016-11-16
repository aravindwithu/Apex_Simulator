package Stages;

import Processor.Processor;
import Processor.CycleListener;
import Processor.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class ALU2 implements ProcessListener{
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	
	//Latch pc;
	CycleListener result;
	
	public ALU2(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}
	
	public void process() {
		processor.isZero = false;
		pc.write(processor.fALU1.pc.read());
		instruction = processor.fALU1.instruction;
		pc.write(processor.fALU1.pc.read());
		if(instruction != null){			
			switch(instruction.opcode.ordinal()){
			case 0: //add
				result.write(instruction.src1+instruction.src2);
				break;
			case 1:	//sub
				result.write(instruction.src1-instruction.src2);
				break;
			case 2: //MOVC
				result.write(instruction.literal);
				break;
			case 3: // MUL
				result.write(instruction.src1*instruction.src2);
				break;
			case 4: //AND
				result.write(instruction.src1 & instruction.src2);
				break;
			case 5:	//OR
				result.write(instruction.src1 | instruction.src2);
				break;
			case 6:	//XOR
				result.write(instruction.src1 ^ instruction.src2);
				break;
			case 7: //LOAD
				if(instruction.literal == null){	//LOAD rdest, rscr1, rscr2
					result.write(instruction.src1 + instruction.src2);
				} else {								//LOAD rdest, rscr1, literal
					result.write(instruction.src1 + instruction.literal);
				}
				break;
			case 8: //Store
				if(instruction.isLiteral){
					result.write(instruction.src2 + instruction.literal);}
				else {
					result.write(instruction.src1 + instruction.src2);}
				break;
			case 13: //HALT, STALL
				break;
			case 15://MOV
				result.write(instruction.src1);
				break;
			}
			
			if(result.temRread() == 0){
				processor.isZero = true;}
		}
	}

	public void clearStage() {
		pc.write(0);
		result.write(0);
		instruction = null;
	}
	
	public CycleListener pcValue(){
		return pc;
	}
	
	@Override
	public String toString() {
		return instruction == null ? (Constants.STALL.name()+ "          ") : instruction.toString();
	}

}
