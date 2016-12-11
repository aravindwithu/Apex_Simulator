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
	
	/**
	 * process method - using scanner class reads the input to initialize,simulate,display or exit and process the relevant execution
	 */
	static void process(){
		Scanner in = new Scanner(System.in);
		while(true){			
			System.out.println("Enter command (i->Initialize, s->Simulate <n>, d->Display, e->exit): ");			
			String command = in.nextLine();
			if(command.equalsIgnoreCase(Constants.INITIALIZE) || command.equalsIgnoreCase("i")){
				processor = new Processor(INS_FILE);
				Processor.INS_COUNT = 0;
			} 
			else if(command.equalsIgnoreCase(Constants.DISPLAY) || command.equalsIgnoreCase("d")){
				display();
			}			
			else if(command.contains(Constants.SIMULATE) || command.contains("s")){
				try{
					int n  = Integer.parseInt(command.split(" ")[1].trim());					
					for(int i=0; i<n; i++){					
							processor.doProcess();
					}					
				} 
				catch (Exception e){
					System.out.println("Please enter correct number of cycles.");
				}
				
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
			stageStr ="ALU1 Stage";
			if(processor.fALU1.instruction != null){				
				pcStr = Long.toString(processor.fALU1.pc.read());
				}		
			state = processor.fALU1.toString();
			break;
		case 3:
			stageStr ="BranchFU Stage";
			if(processor.branchFU.instruction != null){				
				pcStr = Long.toString(processor.branchFU.pc.read());				
				}	
			state = processor.branchFU.toString();
			break;
		case 4:
			stageStr ="ALU2 Stage";
			if(processor.fALU2.instruction != null){				
				pcStr = Long.toString(processor.fALU2.pc.read());				
				}		
			state = processor.fALU2.toString();
			break;
		case 5:
			stageStr ="MULFU Stage";
			if(processor.multiplicationFU.instruction != null){
				pcStr = Long.toString(processor.multiplicationFU.pc.read());
				}	
			state = processor.multiplicationFU.toString();
			break;
		case 6:
			stageStr ="LSFU Stage";
			if(processor.lSFU.instruction != null){
				pcStr = Long.toString(processor.lSFU.pc.read());
				}
			state = processor.lSFU.toString();
			break;
		case 7:
			stageStr ="WriteBack Stage";
			if(processor.writeBack.instruction != null){				
				pcStr = Long.toString(processor.writeBack.pc.read());				
				}	
			state = processor.writeBack.toString();
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
		System.out.println(formatDisp(Constants.Stage.DECODE)+"\n");
		System.out.print(formatDisp(Constants.Stage.ALU1));
		System.out.println(formatDisp(Constants.Stage.BRANCHFU));
		System.out.println(formatDisp(Constants.Stage.ALU2));
		System.out.println(formatDisp(Constants.Stage.MULFU)+"\n");
		System.out.println(formatDisp(Constants.Stage.LSFU));
		System.out.println(formatDisp(Constants.Stage.WRITEBACK));
		
		
		System.out.println("---------------------------------------------------IQ Entry-----------------------------------------------------------------------------------------");
		//System.out.println("--------------------------------------------------FrontendRAT---------------------------------------------------------------------------------------");
		int countIQ = Constants.IQ_COUNT;
		try {
			for(int i=0; i < countIQ; i++){
				if(processor.IQEntry.readIQEntry(i).opCode != null){
					System.out.print("IQ-"+i+" : "+processor.IQEntry.readIQEntry(i).toString()+"\t");}
				else{
					System.out.print("IQ-"+i+" : "+"Empty"+"\t	");}
			}
		}
		catch(Exception e){ e.printStackTrace();}
		System.out.println();
		
		
		System.out.println("------------------------------------------------Front End Table Entry-------------------------------------------------------------------------------------------");
		int countFET = Constants.RAT_COUNT;
		try {
			for(int i=0; i < countFET; i++){
				
					System.out.print("R-"+i+" : P"+processor.register.getFrontEndPhyReg(i)+"\t");
				
			}
		}
		catch(Exception e){ e.printStackTrace();}
		System.out.println();
		
		
		
		
		//displayRegisters
		System.out.println("---------------------------------------------------URF Data-----------------------------------------------------------------------------------------");
		//System.out.println("--------------------------------------------------FrontendRAT---------------------------------------------------------------------------------------");
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
				
		//printMemory
		System.out.print("----------------------------------------------First 100 Memory Locations-------------------------------------------------------------------------------");
		List<Long> _100Memory = processor.memory.readFirst100();
		for(int i=0; i < 10; i++){
			System.out.println();
			for(int j=0; j < 10; j++){
				System.out.print("Mem["+(i*10+j)+"]"+" : " + _100Memory.get(i*10+j)+"\t");
			}
		}
		System.out.println("\n--------------------------------------------------------------------------------------------------------------------------------------------");

	}

}
