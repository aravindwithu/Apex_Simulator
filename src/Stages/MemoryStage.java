package Stages;

import Main.Main;
import Processor.Control;
import Processor.Latch;
import Utility.INS_OPCODE;

public class MemoryStage extends Stage {
	
	//Latch pc;
	Latch result;
	
	public MemoryStage(Control control) {
		pc = new Latch(control);
		result = new Latch(control);
		this.control = control;
		control.addProcessListener(this);
	}

	public void process() {
		try {
			instruction = control.getExecute().instruction;
			pc.write(control.getExecute().pc.read());
			if(instruction != null){
				if(instruction.getOpcode() == INS_OPCODE.STORE){
					if(instruction.isLiteral())
						control.getMemory().writeMem(control.getExecute().result.read().intValue(), instruction.getSrc1());
					else
						control.getMemory().writeMem(control.getExecute().result.read().intValue(), instruction.getDest());
				} else if(instruction.getOpcode() == INS_OPCODE.LOAD){
					result.write(control.getMemory().readMem(control.getExecute().result.read().intValue()));
				} else {				
					result.write(control.getExecute().result.read());
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println(control.getExecute().pc.read()+"==>"+instruction);
			Main.displayRegisters();
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
