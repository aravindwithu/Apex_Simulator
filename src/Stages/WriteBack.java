package Stages;

import Main.Main;
import Processor.Processor;
import Processor.CycleListener;
import Processor.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class WriteBack implements ProcessListener{
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;

	//Latch pc;
	CycleListener result;
	
	public WriteBack(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}

	public void process() {
		try {
			instruction = processor.memoryStage.instruction;
			pc.write(processor.memoryStage.pc.read());
			if(instruction != null){
				if(instruction.opcode == Constants.HALT){
					++processor.cL.cycle;
					processor.memory.clearInstructions();
					processor.memoryStage.clearStage();
					Main.display();
					System.err.println("Aborting execution! HALT encountered.");
					System.exit(0);
				} else if((instruction.opcode.ordinal() < Constants.STORE.ordinal() || instruction.opcode == Constants.MOV)){
					processor.register.writeReg(instruction.dest.intValue(), processor.memoryStage.result.read());
				}
				++Processor.INS_COUNT;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		return instruction == null ? Constants.STALL.name() : instruction.toString();
	}
}
