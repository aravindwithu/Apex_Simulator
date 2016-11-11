package Stages;

import Processor.Control;
import Processor.Latch;
import Processor.ProcessListener;
import Utility.Instruction;

public abstract class Stage implements ProcessListener{
	protected Control control;
	protected Instruction instruction;
	protected Latch pc;
	public Instruction getInstruction() {
		return instruction;
	}
	
	public abstract void clearStage();

	public Control getControl() {
		return control;
	}

	public void setControl(Control control) {
		this.control = control;
	}

	public Latch getPc() {
		return pc;
	}

	public void setPc(Latch pc) {
		this.pc = pc;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}
	
}
