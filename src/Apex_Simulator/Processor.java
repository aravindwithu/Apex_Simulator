package Apex_Simulator;

import java.util.ArrayList;
import java.util.List;

import Stages.*;
import Utility.Constants;
import Utility.Instruction;

public class Processor {	
	public static int INS_COUNT;
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
	//public Delay delay;
	//public MemoryStage memoryStage;
	public LSFU1 lSFU1;
	public LSFU2 lSFU2;
	public MultiplicationFU multiplicationFU; 
	public WriteBack writeBack;
	public ROBCommit rOBCommit;
	public IQ iQ;
	public ROB rOB;	
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
		iQ = new IQ();
		rOB = new ROB();
		rOBCommit = new ROBCommit(this);
		writeBack = new WriteBack(this);
		
		//memoryStage = new MemoryStage(this);
		//delay = new Delay(this);
		
		lSFU2 = new LSFU2(this);		
		lSFU1 = new LSFU1(this);
		multiplicationFU = new MultiplicationFU(this);
		branchFU = new BranchFU(this);
		fALU2 = new ALU2(this);
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
		//isStalled = false;
		Instruction chkInds= null;
		/*if(decode.instruction !=null){
			chkInds = decode.instruction;}*/
		

		int countIQ = Constants.IQ_COUNT;
		for(int i=0; i < countIQ; i++){
			try{ //false checkin ALU1
				if( this.iQ.readIQEntry(i).opCode != null){
					chkInds = this.iQ.readIQEntry(i);
				}
				else{break;}

			
			if(chkInds !=null){
				chkInds.stallIn = Constants.Stage.EMPTY;
				chkInds.src1FwdValIn = Constants.Stage.EMPTY;
				chkInds.src2FwdValIn = Constants.Stage.EMPTY;
				chkInds.src1Stall = false;
				chkInds.src2Stall = false;				
				
				if(writeBack.instruction != null && chkInds != null && writeBack.instruction.dest != null
						&& (chkInds.src1Add == writeBack.instruction.dest || chkInds.src2Add == writeBack.instruction.dest))
				{
					if(chkInds.src1Add == writeBack.instruction.dest
							&& chkInds.opCode != Constants.OpCode.STORE){					
						    chkInds.src1FwdValIn = Constants.Stage.WRITEBACK;
						    chkInds.src1 = writeBack.instruction.destVal;
						}
					if(chkInds.src2Add == writeBack.instruction.dest){					
						chkInds.src2FwdValIn = Constants.Stage.WRITEBACK;		
						chkInds.src2 = writeBack.instruction.destVal;
						}			
				}
				
				if(lSFU2.instruction != null && chkInds != null && lSFU2.instruction.dest != null
						&& (chkInds.src1Add == lSFU2.instruction.dest || chkInds.src2Add == lSFU2.instruction.dest))
				{
					if(chkInds.src1Add == lSFU2.instruction.dest
							&& chkInds.opCode != Constants.OpCode.STORE){					
						    chkInds.src1FwdValIn = Constants.Stage.LSFU2;
						    chkInds.src1 = lSFU2.instruction.destVal;
						}
					if(chkInds.src2Add == lSFU2.instruction.dest){					
						chkInds.src2FwdValIn = Constants.Stage.LSFU2;		
						chkInds.src2 = lSFU2.instruction.destVal;
						}			
				}
				
				if(lSFU1.instruction != null && chkInds != null && lSFU1.instruction.dest != null)
				{			
					if(chkInds.src1Add == lSFU1.instruction.dest
							&& chkInds.opCode != Constants.OpCode.STORE){
						//isStalled = true;									
						chkInds.src1Stall = true;
						}
					if(chkInds.src2Add == lSFU1.instruction.dest){
						//isStalled = true;					
						chkInds.src2Stall = true;
						}			
				}
				
				if(fALU2.instruction != null && chkInds != null && fALU2.instruction.dest != null
						&& (chkInds.src1Add == fALU2.instruction.dest || chkInds.src2Add == fALU2.instruction.dest))
				{			
					if(chkInds.src1Add == fALU2.instruction.dest 
							&& chkInds.opCode != Constants.OpCode.STORE){					
						       chkInds.src1FwdValIn = Constants.Stage.ALU2;
						       chkInds.src1 = fALU2.instruction.destVal;
						}
					if(chkInds.src2Add == fALU2.instruction.dest){					
						      chkInds.src2FwdValIn = Constants.Stage.ALU2;	
						      chkInds.src2 = fALU2.instruction.destVal;
						}				
				}
				
				if(fALU1.instruction != null && chkInds != null && fALU1.instruction.dest != null)
				{			
					if(chkInds.src1Add == fALU1.instruction.dest
							&& chkInds.opCode != Constants.OpCode.STORE){
						//isStalled = true;									
						chkInds.src1Stall = true;
						}
					if(chkInds.src2Add == fALU1.instruction.dest){
						//isStalled = true;					
						chkInds.src2Stall = true;
						}			
				}
				
				if(fALU2.instruction != null && chkInds != null && fALU2.instruction.dest != null 
						&& fALU2.instruction.opCode == Constants.OpCode.LOAD)
				{			
					if(chkInds.src1Add == fALU2.instruction.dest
							&& chkInds.opCode != Constants.OpCode.STORE){
						//isStalled = true;									
						chkInds.src1Stall = true;
						}
					if(chkInds.src2Add == fALU2.instruction.dest){
						//isStalled = true;					
						chkInds.src2Stall = true;
						}			
				}	
				
//				if(chkInds != null && (chkInds.opCode == Constants.OpCode.BZ || chkInds.opCode == Constants.OpCode.BNZ)
//						&& !isBranchZ){
//					isStalled = true;	
//					isZero = false;
//					isBranchZ = true;
//				}
			}		
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		}
	}
}
