package Apex_Simulator;

import java.util.ArrayList;
import java.util.List;
import Stages.*;
import Utility.Constants;

public class Processor {	
	public static int INS_COUNT;	
	public static int mulCount = 0;
	public boolean mulResultFoundCheck = false;
	public Memory memory;
	public UnifiedRegisterFile register;
	public CycleListener cL;
	public Fetch fetch;
	public Decode decode;
	public Dispatch dispatch;
	public ALU1 fALU1;
	public ALU2 fALU2;
	public BranchFU branchFU;
	public MultiplicationFU multiplicationFU; 
	public Delay delay;
	public MemoryStage memoryStage;
	public LSFU lSFU;
	public WriteBack writeBack;
	public IQ IQEntry;
	public ROB ROBEntry;	
	public boolean isZero = false;
	public boolean isBranchZ = false;
	public boolean isHalt = false;
	public boolean isStalled = false;
	public List<CycleListener> cycleListener = new ArrayList<CycleListener>();
	public List<ProcessListener> processListeners = new ArrayList<ProcessListener>();
	
	/**
	 * Constructor for Processor initializes the Processor and also all the stages objects, memory, registers.
	 * @param file of string type to be processed and relevant results are stored in instruction array list in memory.
	 */
	public Processor(String file) {
		memory = new Memory(file);
		register = new UnifiedRegisterFile();
		IQEntry = new IQ();
		ROBEntry = new ROB();
		writeBack = new WriteBack(this);
		//memoryStage = new MemoryStage(this);
		//delay = new Delay(this);
		lSFU = new LSFU(this);
		fALU2 = new ALU2(this);
		branchFU = new BranchFU(this);
		multiplicationFU = new MultiplicationFU(this);
		fALU1 = new ALU1(this);	
		dispatch = new Dispatch(this);
		decode = new Decode(this);
		fetch = new Fetch(this);
		cL = new CycleListener(this);
		Processor.INS_COUNT = 0;
	}

	/**
	 * doProcess method performs process for each stage, increments the cycle, sets the isSstallflag (based on stall check logic), and
	 * sets the src1Stall and src2Stall flags of the respective decode instruction.
	 * The stall check logic checks whether the src1 and src2 of the decode instruction is equal to the destination
	 * of the ALU1, ALU2, memory stage instructions.  
	 */
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
		if(decode.instruction !=null){
		decode.instruction.stallIn = Constants.Stage.EMPTY;
		decode.instruction.src1FwdValIn = Constants.Stage.EMPTY;
		decode.instruction.src2FwdValIn = Constants.Stage.EMPTY;
		decode.instruction.src1Stall = false;
		decode.instruction.src2Stall = false;
		
			if(memoryStage.instruction != null && decode.instruction != null && memoryStage.instruction.dest != null
					&& (decode.instruction.src1Add == memoryStage.instruction.dest || decode.instruction.src2Add == memoryStage.instruction.dest))
			{
				if(decode.instruction.src1Add == memoryStage.instruction.dest
						&& decode.instruction.opCode != Constants.OpCode.STORE){					
					decode.instruction.src1FwdValIn = Constants.Stage.LSFU;					
					}
				if(decode.instruction.src2Add == memoryStage.instruction.dest){					
					decode.instruction.src2FwdValIn = Constants.Stage.LSFU;					
					}			
			}
			
			if(fALU2.instruction != null && decode.instruction != null && fALU2.instruction.dest != null
					&& (decode.instruction.src1Add == fALU2.instruction.dest || decode.instruction.src2Add == fALU2.instruction.dest))
			{			
				if(decode.instruction.src1Add == fALU2.instruction.dest 
						&& decode.instruction.opCode != Constants.OpCode.STORE){					
					decode.instruction.src1FwdValIn = Constants.Stage.ALU2;					
					}
				if(decode.instruction.src2Add == fALU2.instruction.dest){					
					decode.instruction.src2FwdValIn = Constants.Stage.ALU2;					
					}				
			}
			
			if(fALU1.instruction != null && decode.instruction != null && fALU1.instruction.dest != null)
			{			
				if(decode.instruction.src1Add == fALU1.instruction.dest
						&& decode.instruction.opCode != Constants.OpCode.STORE){
					isStalled = true;									
					decode.instruction.src1Stall = true;
					}
				if(decode.instruction.src2Add == fALU1.instruction.dest){
					isStalled = true;					
					decode.instruction.src2Stall = true;
					}			
			}
			
			if(fALU2.instruction != null && decode.instruction != null && fALU2.instruction.dest != null 
					&& fALU2.instruction.opCode == Constants.OpCode.LOAD)
			{			
				if(decode.instruction.src1Add == fALU2.instruction.dest
						&& decode.instruction.opCode != Constants.OpCode.STORE){
					isStalled = true;									
					decode.instruction.src1Stall = true;
					}
				if(decode.instruction.src2Add == fALU2.instruction.dest){
					isStalled = true;					
					decode.instruction.src2Stall = true;
					}			
			}	
			
			if(decode.instruction != null && (decode.instruction.opCode == Constants.OpCode.BZ || decode.instruction.opCode == Constants.OpCode.BNZ)
					&& !isBranchZ){
				isStalled = true;	
				isZero = false;
				isBranchZ = true;
			}
		}

	}
}
