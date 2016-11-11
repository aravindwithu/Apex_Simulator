package Stages;

import Main.Main;
import Processor.Control;
import Processor.Latch;
import Utility.INS_OPCODE;

public class WriteBack extends Stage{

	//Latch pc;
	Latch result;
	
	public WriteBack(Control control) {
		pc = new Latch(control);
		result = new Latch(control);
		this.control = control;
		control.addProcessListener(this);
	}

	public void process() {
		try {
			instruction = control.getMemoryStage().instruction;
			pc.write(control.getMemoryStage().pc.read());
			if(instruction != null){
				if(instruction.getOpcode() == INS_OPCODE.HALT){
					control.e.incrementClock();
					control.getMemory().clearInstructions();
					control.getMemoryStage().clearStage();
					Main.display();
					System.err.println("Aborting execution! HALT encountered.");
					System.exit(0);
				} else if((instruction.getOpcode().ordinal() < INS_OPCODE.STORE.ordinal() || instruction.getOpcode() == INS_OPCODE.MOV)){
					control.getRegister().writeReg(instruction.getDest().intValue(), control.getMemoryStage().result.read());
				}
				++Control.INS_COUNT;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
