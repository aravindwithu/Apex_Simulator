package Apex_Simulator;

import java.util.ArrayList;
import java.util.List;
import Stages.Decode;
import Stages.ALU1;
import Stages.ALU2;
import Stages.BranchFU;
import Stages.Delay;
import Stages.Fetch;
import Stages.MemoryStage;
import Stages.WriteBack;
import Utility.Constants;

public class Processor {	
	public static int INS_COUNT;	
	public Memory memory;
	public Register register;
	public CycleListener cL;
	public Fetch fetch;
	public Decode decode;
	public ALU1 fALU1;
	public ALU2 fALU2;
	public BranchFU branchFU;
	public Delay delay;
	public MemoryStage memoryStage;
	public WriteBack writeBack;
	public boolean isStalled = false;
	public boolean isZero = false;
	public boolean isBranchZ = false;
	public boolean isHalt = false;
	public List<CycleListener> cycleListener = new ArrayList<CycleListener>();
	public List<ProcessListener> processListeners = new ArrayList<ProcessListener>();
		
	public Processor(String file) {
		memory = new Memory(file);
		register = new Register();			
		writeBack = new WriteBack(this);
		memoryStage = new MemoryStage(this);
		delay = new Delay(this);
		fALU2 = new ALU2(this);
		branchFU = new BranchFU(this);
		fALU1 = new ALU1(this);		
		decode = new Decode(this);
		fetch = new Fetch(this);
		cL = new CycleListener(this);
		Processor.INS_COUNT = 0;
	}

	public void doProcess(){
		for (ProcessListener processListItr : processListeners) {
			try{
				processListItr.process();
			} catch(Exception e){
				e.printStackTrace();
				System.err.println("Faulty instruction at: " + processListItr.pcValue());
			}
		}
		
		for (CycleListener cycleListItr : cycleListener){
			cycleListItr.ChangeCycle(cL);		
		}
		
		cL.cycle++;
		isStalled = false;
		if(fALU1.instruction != null && decode.instruction != null && fALU1.instruction.dest != null)
		{			
			if(decode.instruction.src1Add == fALU1.instruction.dest){
				isStalled = true;
				decode.instruction.src1Stall = true;}
			if(decode.instruction.src2Add == fALU1.instruction.dest){
				isStalled = true;
				decode.instruction.src2Stall = true;}			
		}
		
		if(fALU2.instruction != null && decode.instruction != null && fALU2.instruction.dest != null
				&& (decode.instruction.src1Add == fALU2.instruction.dest || decode.instruction.src2Add == fALU2.instruction.dest))
		{			
			if(decode.instruction.src1Add == fALU2.instruction.dest){
				isStalled = true;
				decode.instruction.src1Stall = true;}
			if(decode.instruction.src2Add == fALU2.instruction.dest){
				isStalled = true;
				decode.instruction.src2Stall = true;}				
		}
	  
		if(memoryStage.instruction != null && decode.instruction != null && memoryStage.instruction.dest != null
				&& (decode.instruction.src1Add == memoryStage.instruction.dest || decode.instruction.src2Add == memoryStage.instruction.dest))
		{
			if(decode.instruction.src1Add == memoryStage.instruction.dest){
				isStalled = true;
				decode.instruction.src1Stall = true;}
			if(decode.instruction.src2Add == memoryStage.instruction.dest){
				isStalled = true;
				decode.instruction.src2Stall = true;}			
		}		
		
		if(decode.instruction != null && (decode.instruction.opCode == Constants.OpCode.BZ || decode.instruction.opCode == Constants.OpCode.BNZ)
				&& !isBranchZ){
			isStalled = true;	
			isZero = false;
			isBranchZ = true;
		}		
		

	}
}
