package Stages;

import Processor.Control;
import Processor.Latch;
import Utility.INS_OPCODE;

public class Decode extends Stage {
	
	//Latch pc;
	
	public Decode(Control control) {
		pc = new Latch(control);
		control.addProcessListener(this);
		this.control = control;
	}

	public void process() {
		if(control.isPipelineStalled) return;
		try {
			instruction = control.getFetch().instruction;
			pc.write(control.getFetch().pc.read());
			readSources();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readSources(){
			try {
				if(instruction != null){
					instruction.setSrc1(instruction.getSrc1Add() != null ? control.getRegister().readReg(instruction.getSrc1Add().intValue()) : null);
					instruction.setSrc2(instruction.getSrc2Add() != null ? control.getRegister().readReg(instruction.getSrc2Add().intValue()) : null);
					if(instruction.getOpcode() == INS_OPCODE.STORE && instruction.getDest() == null &&!instruction.isLiteral()){
						instruction.setDest(control.getRegister().readReg(instruction.getDestAdd().intValue()));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public void clearStage() {
		pc.write(0);
		instruction = null;
	}
	
	@Override
	public String toString() {
		return instruction == null ? INS_OPCODE.STALL.name() : instruction.toString();
	}
}
