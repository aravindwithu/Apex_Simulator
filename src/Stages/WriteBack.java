package Stages;

import Apex_Simulator.Main;
import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class WriteBack implements ProcessListener{
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;

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
				if(instruction.opCode == Constants.OpCode.HALT){
					processor.cL.cycle++;
					processor.memory.clearInstructions();
					processor.memoryStage.clearStage();
					Main.display();
					System.err.println("Aborting execution! HALT encountered."+"\n");
					processor.isHalt = false;
					//System.exit(0);
				} else if((instruction.opCode.ordinal() < Constants.OpCode.STORE.ordinal() || instruction.opCode == Constants.OpCode.MOV)){
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
