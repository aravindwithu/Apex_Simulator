package Apex_Simulator;

import java.util.List;
import java.util.Scanner;
import Utility.Constants;

public class Apex_Simulator {
	private static Processor processor;
	private static String INS_FILE;
	
	/**
	 * main method gets instruction file and initiate the apex simulator program and calls the process method,
	 * @param  args instruction text file is the 1st argument
	 */
	public static void main(String[] args) {
		if(args.length > 0){
			INS_FILE = args[0];
			process();
		} else {
			System.out.println("Need an instruction file!!!");
		}
	}
	
	/** //false checkin ALU1
	 * process method - using scanner class reads the input to initialize,simulate,display or exit and process the relevant execution
	 */
	static void process(){
		Scanner in = new Scanner(System.in);
		while(true){
			System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("Enter command");
			System.out.println(" i->Initialize, \n s->Simulate <n>,\n d->Display, \n u->Set_URF_size <n>,\n m->Print_map_tables,\n iq->Print_IQ,");
			System.out.println(" rob->Print_ROB,\n urf->Print_URF,\n mem->Print_Memory <a1> <a2>,\n p->Print_Stats,\n e->Exit ");
			System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------");
			String command = in.nextLine();
			if(command.equalsIgnoreCase(Constants.INITIALIZE) || command.equalsIgnoreCase("i")){
				processor = new Processor(INS_FILE);
				Processor.INS_COUNT = 0;
			} 
			else if(command.equalsIgnoreCase(Constants.DISPLAY) || command.equalsIgnoreCase("d")){
				display();
			}			
			else if(command.contains(Constants.SIMULATE)|| command.contains("s")){
				try{
					int n  = Integer.parseInt(command.split(" ")[1].trim());					

					for(int i=0; i<n; i++){	
							processor.doProcess();
					}					
				} 
				catch (Exception e){
					e.printStackTrace();
					System.out.println("Please enter correct number of cycles.");
				}
				
			} 
			else if(command.equalsIgnoreCase("u")){
				Constants.REG_COUNT = in.nextInt()+1;
				System.out.println("URF Size has been set to "+ Constants.REG_COUNT+" including X Register");
			}
			else if(command.equalsIgnoreCase("m")){
				System.out.println("------------------------------------------------Front End Table Entry----------------------------------------------------------------------------------------");
				int countFET = Constants.RAT_COUNT;
				try {
					for(int i=0; i < countFET; i++){
							System.out.print("R-"+i+" : P"+processor.register.getFrontEndPhyReg(i)+"\t");
							if ((i + 1) % 8 == 0) {
								System.out.println();
							}						
					}
				}
				catch(Exception e){ e.printStackTrace();}
				System.out.println();
				System.out.println("------------------------------------------------Back End Table Entry----------------------------------------------------------------------------------------");
				int countBET = Constants.RAT_COUNT;
				try {
					for(int i=0; i < countBET; i++){
							System.out.print("R-"+i+" : P"+processor.register.getBackEndPhyReg(i)+"\t");
							if ((i + 1) % 8 == 0) {
								System.out.println();
							}						
					}
				}
				catch(Exception e){ e.printStackTrace();}
				System.out.println();
				
			}else if(command.equalsIgnoreCase("IQ")){
				System.out.println("---------------------------------------------------IQ Entry-----------------------------------------------------------------------------------------");
				int countIQ = Constants.IQ_COUNT;
				try {
					for(int i=0; i < countIQ; i++){
						if(processor.iQ.readIQEntry(i).opCode != null){
							System.out.print("IQ-"+i+" : "+processor.iQ.readIQEntry(i).toString()+"\n");}
						else{
							System.out.print("IQ-"+i+" : "+"Empty"+"\n");}
					}
				}
				catch(Exception e){ e.printStackTrace();}
				System.out.println();
			}else if(command.equalsIgnoreCase("rob")){
				System.out.println("---------------------------------------------------ROB Entry----------------------------------------------------------------------------------------");
				int countROB = Constants.ROB_COUNT;
				try {
					for(int i=0; i < countROB; i++){
						if(processor.rOB.readROBEntry(i).opCode != null){
							System.out.println("ROB-"+i+" : "+processor.rOB.readROBEntry(i).toString()+"\t ROB Commit Status "+processor.rOB.readROBEntry(i).isROBCommit);}
						else{
							System.out.println("ROB-"+i+" : "+"Empty");}
					}
				}
				catch(Exception e){ e.printStackTrace();}
				System.out.println();
			}else if(command.equalsIgnoreCase("urf")){
				System.out.println("---------------------------------------------------URF Data-----------------------------------------------------------------------------------------");
				int count = Constants.REG_COUNT - 1;
				try {
					for(int i=0; i < 8; i++){
							System.out.print("P"+i+"  : "+processor.register.readReg(i)+"\t	");
					}
					System.out.print("\n");
					for(int i=8; i < 16; i++){
						System.out.print("P"+i+" : "+processor.register.readReg(i)+"\t	");
					}			
					System.out.print("\n");
					//System.out.print("--------------------------------------------------BackendRAT---------------------------------------------------------------------------------------");
					System.out.print("\n");
					for(int i=16; i < 24; i++){
						System.out.print("P"+i+" : "+processor.register.readReg(i)+"\t	");
					}
					System.out.print("\n");
					for(int i=24; i < 32; i++){
						System.out.print("P"+i+" : "+processor.register.readReg(i)+"\t	");
					}
					
					System.out.println("X : "+processor.register.readReg(count)+"\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(command.equalsIgnoreCase("mem")){
				int address1 = in.nextInt();
				int address2 = in.nextInt();
				System.out.print("----------------------------------------------Memory Locations from a1 to a2-------------------------------------------------------------------------------");
				List<Long> _100Memory = processor.memory.readFirst100(address1,address2);
				for(int i=0; i < 10; i++){
					System.out.println();
					for(int j=0; j < 10; j++){
						System.out.print("Mem["+(i*10+j)+"]"+" : " + _100Memory.get(i*10+j)+"\t");
					}
				}
				System.out.println("\n--------------------------------------------------------------------------------------------------------------------------------------------");

			}else if(command.equalsIgnoreCase("p")){
				System.out.println("----------------------------------------------Pipeline Stats-------------------------------------------------------------------------------");
				float CPI = (5 + Processor.INS_COUNT - 1)/Processor.INS_COUNT;
				System.out.println("IPC :"+ 1/CPI);
				System.out.println("Number of cycles for which dispatched has stalled : "+Processor.stallCount);
				System.out.println("Cycles for which issues have not taken place : "+Processor.noIssueCount);
				System.out.println("Number of LOAD instructions committed: " + Processor.loadCommitCount);
				System.out.println("Number of STORE instructions committed: " + Processor.storeCommitCount);
			}
			else if(command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("e")){
				System.out.println("Apex simulator exited");
				break;
			}
			else{
				System.out.println("Please enter correct command.");
			}
		}
	}
	
	/**
	 * formatDisp method gets stage constants and format the relevant display information for given stage
	 * @param  stage of type Constants.Stage
	 */
	public static String formatDisp(Constants.Stage stage){
		String pcStr = "0";
		String stageStr = "";
		String state = "";
		switch(stage.ordinal()){
		case 0:
			stageStr = "Fetch Stage";
			if(processor.fetch.instruction != null){				
				pcStr = Long.toString(processor.fetch.pc.read());
				}			
			state = processor.fetch.toString().replace('P', 'R');
			break;
		case 1:
			stageStr ="Decode Stage";
			if(processor.decode.instruction != null){
				pcStr = Long.toString(processor.decode.pc.read());				
				}	
			state = processor.decode.toString();
			break;
		case 2:
			stageStr ="Dispatch Stage";
			if(processor.dispatch.instruction != null){
				pcStr = Long.toString(processor.dispatch.pc.read());				
				}	
			state = processor.dispatch.toString();
			break;
		case 3:
			stageStr ="ALU1 Stage";
			if(processor.fALU1.instruction != null){				
				pcStr = Long.toString(processor.fALU1.pc.read());
				}		
			state = processor.fALU1.toString();
			break;		
		case 4:
			stageStr ="ALU2 Stage";
			if(processor.fALU2.instruction != null){				
				pcStr = Long.toString(processor.fALU2.pc.read());				
				}		
			state = processor.fALU2.toString();
			break;
		case 5:
			stageStr ="BranchFU Stage";
			if(processor.branchFU.instruction != null){				
				pcStr = Long.toString(processor.branchFU.pc.read());				
				}	
			state = processor.branchFU.toString();
			break;
		case 6:
			stageStr ="Mul FU Stage";
			if(processor.multiplicationFU.instruction != null){
				pcStr = Long.toString(processor.multiplicationFU.pc.read());
				}	
			state = processor.multiplicationFU.toString();
			break;
		case 7:
			stageStr ="LSFU1 Stage";
			if(processor.lSFU1.instruction != null){
				pcStr = Long.toString(processor.lSFU1.pc.read());
				}
			state = processor.lSFU1.toString();
			break;
		case 8:
			stageStr ="LFSU2 Stage";
			if(processor.writeBack.instruction != null){				
				pcStr = Long.toString(processor.lSFU2.pc.read());				
				}	
			state = processor.lSFU2.toString();
			break;
		case 9:
			stageStr ="WriteBack Stage";
			if(processor.writeBack.instruction != null){				
				pcStr = Long.toString(processor.writeBack.pc.read());				
				}	
			state = processor.writeBack.toString();
			break;
		case 10:
			stageStr ="ROBCommit Stage";
			if(processor.rOBCommit.instruction != null){				
				pcStr = Long.toString(processor.rOBCommit.pc.read());				
				}	
			state = processor.rOBCommit.toString();
			break;
		
	}
		stageStr += "	==> PC : ";
		
		String returnStr = 
				stageStr
				+ pcStr
				+" ==> "+ state;
		
		return String.format("%-"+ 64 + "s", returnStr);
		
	}
	
	/**
	 * display method displays the status of last simulation(displays each stage,reg & mem informations)
	 */
	public static void display(){
		//displayPipeline
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("Clock cycles : "+processor.cL.cycle);
		System.out.println(formatDisp(Constants.Stage.FETCH));
		System.out.println(formatDisp(Constants.Stage.DECODE));
		System.out.println(formatDisp(Constants.Stage.DISPATCH)+"\n");
		System.out.print(formatDisp(Constants.Stage.ALU1));		
		System.out.println(formatDisp(Constants.Stage.BRANCHFU));
		System.out.print(formatDisp(Constants.Stage.ALU2));
		System.out.println(formatDisp(Constants.Stage.LSFU1));
		System.out.print(formatDisp(Constants.Stage.MULTIPLICATIONFU));
		System.out.println(formatDisp(Constants.Stage.LSFU2)+"\n");		
		System.out.println(formatDisp(Constants.Stage.WRITEBACK));
		System.out.println(formatDisp(Constants.Stage.ROBCOMMIT));	
	}

}
