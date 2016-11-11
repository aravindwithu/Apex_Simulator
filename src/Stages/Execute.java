package Stages;

import Processor.Control;
import Processor.Latch;
import Utility.INS_OPCODE;

public class Execute extends Stage{
	//Latch pc;
	Latch result;

	public Execute(Control control) {
		pc = new Latch(control);
		result = new Latch(control);
		this.control = control;
		control.addProcessListener(this);
	}
	
	public void process() {
		
		if(control.isPipelineStalled){
			instruction = null;
			return;
		}
		pc.write(control.getDecode().pc.read());
		control.getDecode().readSources();
		instruction = control.getDecode().instruction;
		if(instruction != null){
			
			switch(instruction.getOpcode().ordinal()){
			case 0: //add
				result.write(instruction.getSrc1()+instruction.getSrc2());
				break;
			case 1:	//sub
				result.write(instruction.getSrc1()-instruction.getSrc2());
				break;
			case 2: //MOVC
				result.write(instruction.getLiteral());
				break;
			case 3: // MUL
				result.write(instruction.getSrc1()*instruction.getSrc2());
				break;
			case 4: //AND
				result.write(instruction.getSrc1() & instruction.getSrc2());
				break;
			case 5:	//OR
				result.write(instruction.getSrc1() | instruction.getSrc2());
				break;
			case 6:	//XOR
				result.write(instruction.getSrc1() ^ instruction.getSrc2());
				break;
			case 7: //LOAD
				if(instruction.getLiteral() == null){	//LOAD rdest, rscr1, rscr2
					result.write(instruction.getSrc1() + instruction.getSrc2());
				} else {								//LOAD rdest, rscr1, literal
					result.write(instruction.getSrc1() + instruction.getLiteral());
				}
				break;
			case 8: //Store
				if(instruction.isLiteral())
					result.write(instruction.getSrc2() + instruction.getLiteral());
				else 
					result.write(instruction.getSrc1() + instruction.getSrc2());
				break;
			case 9: //BZ, 
				if(instruction.getSrc1() == 0){
					control.getFetch().clearStage(instruction.getLiteral(), true);
					control.getDecode().clearStage();
					control.isPipelineStalled = true;
				}
				break;
			case 10: //BNZ,
				if(instruction.getSrc1() != 0){
					control.getFetch().clearStage(instruction.getLiteral(),true);
					control.getDecode().clearStage();
					control.isPipelineStalled = true;
				}
				break;
			case 11: //JUMP, 
				control.getFetch().clearStage(instruction.getLiteral() + instruction.getSrc1(),false);
				control.getDecode().clearStage();
				control.isPipelineStalled = true;
				break;
			case 12: //BAL, 
				if(control.getDecode().pc != null)
					control.getRegister().setReg_X(control.getDecode().pc.read()+1);
				control.getFetch().clearStage(instruction.getSrc1()+instruction.getLiteral(), false);
				control.getDecode().clearStage();
				break;
			case 13: //HALT, STALL
				break;
			case 15://MOV
				result.write(instruction.getSrc1());
				break;
			}
		}
	}
	
	@Override
	public void clearStage() {
		pc.write(0);
		result.write(0);
		instruction = null;
	}
	
	@Override
	public String toString() {
		return instruction == null ? INS_OPCODE.STALL.name() : instruction.toString();
	}

}
