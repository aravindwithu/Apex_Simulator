package Stages;

import Apex_Simulator.Processor;
import Apex_Simulator.CycleListener;
import Apex_Simulator.ProcessListener;
import Utility.Constants;
import Utility.Instruction;

public class Decode implements ProcessListener {
	
	public Processor processor;
	public Instruction instruction;
	public CycleListener pc;
	
	public Decode(Processor processor) {
		pc = new CycleListener(processor);
		processor.processListeners.add(this);
		this.processor = processor;
	}

	public void process() {
	try {
		
		if(processor.isHalt == true){
			instruction = null;
			return;
		}
			
		if(processor.isStalled){return;}
		
			pc.write(processor.fetch.pc.read());
			instruction = processor.fetch.instruction;
			readSources();			
	} 
	catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readSources(){
			try {
				if(instruction != null){
					if(instruction.src1Add != null){
						instruction.src1 = processor.register.readReg(instruction.src1Add.intValue());
					}
					else{
						instruction.src1 = null;
					}
					
					if(instruction.src2Add != null){
						instruction.src2 = processor.register.readReg(instruction.src2Add.intValue());
					}
					else{
						instruction.src2 = null;
					}
					
					if(instruction.opCode == Constants.OpCode.STORE && instruction.dest == null && !instruction.isLiteral){
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
