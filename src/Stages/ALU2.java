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
	
	public ALU2(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}
	
	public void process() {
		pc.write(processor.fALU1.pc.read());
		instruction = processor.fALU1.instruction;
		if(instruction != null){			
			switch(instruction.opCode.ordinal()){
			case 0: //add
				result.write(instruction.src1+instruction.src2);
				break;
			case 1:	//sub
				result.write(instruction.src1-instruction.src2);
				break;
			case 2: // MUL
				result.write(instruction.src1*instruction.src2);
				break;
			case 3: //MOVC
				result.write(instruction.literal);
				break;
			case 4://MOV
				result.write(instruction.src1);
				break;			
			case 5: //AND
				result.write(instruction.src1 & instruction.src2);
				break;
			case 6:	//OR
				result.write(instruction.src1 | instruction.src2);
				break;
			case 7:	//XOR
				result.write(instruction.src1 ^ instruction.src2);
				break;
			case 8: //LOAD
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
				break;		
			}
			
			if(result.temRread() == 0 && processor.isBranchZ){
				processor.isZero = true;
			}
		}
	}

	public void clearStage() {
		pc.write(0);
		result.write(0);
		instruction = null;
	}
	
	public Long pcValue(){
		return pc.read();
	}
	
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
