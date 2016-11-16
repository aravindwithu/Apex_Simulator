package Stages;

import Processor.Processor;
import Processor.CycleListener;
import Processor.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class Decode implements ProcessListener {
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	
	//Latch pc;
	
	public Decode(Processor processor) {
		pc = new CycleListener(processor);
		processor.processListeners.add(this);
		this.processor = processor;
	}

	public void process() {
		if(processor.isPipelineStalled){return;}
		try {
			instruction = processor.fetch.instruction;
			pc.write(processor.fetch.pc.read());
			readSources();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readSources(){
			try {
				if(instruction != null){
					instruction.src1 = instruction.src1Add != null ? processor.register.readReg(instruction.src1Add.intValue()) : null;
					instruction.src2 = instruction.src2Add != null ? processor.register.readReg(instruction.src2Add.intValue()) : null;
					if(instruction.opcode == Constants.STORE && instruction.dest == null &&!instruction.isLiteral){
						instruction.dest = processor.register.readReg(instruction.destAdd.intValue());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public void clearStage() {
		pc.write(0);
		instruction = null;
	}
	
	public CycleListener pcValue(){
		return pc;
	}
	
	@Override
	public String toString() {
		return instruction == null ? Constants.STALL.name() : instruction.toString();
	}
}
