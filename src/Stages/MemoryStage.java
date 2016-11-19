package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class MemoryStage implements ProcessListener {
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	
	CycleListener result;
	
	public MemoryStage(Processor processor) {
		pc = new CycleListener(processor);
		result = new CycleListener(processor);
		this.processor = processor;
		processor.processListeners.add(this);
	}

	public void process() {
		try {			
			if(processor.fALU2.instruction != null){
				instruction = processor.fALU2.instruction;
				pc.write(processor.fALU2.pc.read());
							
				if(instruction.opCode == Constants.OpCode.STORE){
					if(instruction.isLiteral)
						processor.memory.writeMem(processor.fALU2.result.read().intValue(), instruction.src1);
					else
						processor.memory.writeMem(processor.fALU2.result.read().intValue(), instruction.dest);
				} else if(instruction.opCode == Constants.OpCode.LOAD){
					result.write(processor.memory.readMem(processor.fALU2.result.read().intValue()));
				} else {				
					result.write(processor.fALU2.result.read());
				}
				
			}
			else if(processor.delay.instruction != null){
				instruction = processor.delay.instruction;
				pc.write(processor.delay.pc.read());
			}	
			else
			{
				instruction = null;
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println(processor.fALU2.pc.read()+"==>"+instruction);
			//Main.displayRegisters();
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
