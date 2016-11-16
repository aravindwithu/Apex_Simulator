package Processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Main.Main;
import Stages.Decode;
import Stages.ALU1;
import Stages.ALU2;
import Stages.BranchFU;
import Stages.Delay;
import Stages.Fetch;
import Stages.MemoryStage;
import Stages.WriteBack;
import Processor.CycleListener;

public class Processor {	
	public List<CycleListener> cycleListener = new ArrayList<CycleListener>();
	public List<ProcessListener> processListeners = new ArrayList<ProcessListener>();
	public boolean isPipelineStalled = false;
	public boolean debug = false;	
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
	public boolean isZero = false;	
		
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
	//doProcess();
		Iterator<ProcessListener> processListItr = processListeners.iterator();
		while(processListItr.hasNext()){
			ProcessListener listener = processListItr.next();
			try{
				listener.process();
			} catch(Exception e){
				e.printStackTrace();
				//System.err.println("Faulty instruction at : "+((Stage)listener).pc.read());
				System.err.println("Faulty instruction at: " + listener.pcValue());
			}
		}
		
		//doChangeIns();
		Iterator<CycleListener> cycleListItr = cycleListener.iterator();
		while(cycleListItr.hasNext()){
			cycleListItr.next().ChangeCycle(cL);		
		}		
		
		++cL.cycle;
		
		//setIsPipelineStalled();
		List<Long> dests = new ArrayList<Long>();
		if(fALU1.instruction != null && fALU1.instruction.dest != null)
			dests.add(fALU1.instruction.dest);
		
		if(fALU2.instruction != null && fALU2.instruction.dest != null)
			dests.add(fALU2.instruction.dest);
		
		if(memoryStage.instruction != null && memoryStage.instruction.dest != null)
			dests.add(memoryStage.instruction.dest);		
			
		if(decode.instruction != null && (dests.contains(decode.instruction.src1Add) || dests.contains(decode.instruction.src2Add))){
			isPipelineStalled = true;
		} else {
			isPipelineStalled = false;
		}
		
		if(debug)
			Main.displayDebug();
	}
}
