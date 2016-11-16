package Main;

import java.util.List;
import java.util.Scanner;

import Processor.Processor;
import Utility.Constants;

public class Main {
	private static Processor processor;
	private static String INS_FILE;
	public static void main(String[] args) {
		if(args.length > 0){
			processor = new Processor(args[0]);
			if(args.length > 1 && args[1] != null && args[1].contains("debug"))
				processor.debug = true;
			INS_FILE = args[0];
			process();
		} else {
			System.out.println("Need an instruction file!!!");
		}
	}
	
	static void process(){
		Scanner in = new Scanner(System.in);
		while(true){
			
			System.out.println("Enter command (Initialize, Simulate <n>, Display, exit): ");
			String command = in.nextLine();
			if(command.equalsIgnoreCase(Constants.initialize) || command.equalsIgnoreCase("i")){
				initialize();
			} else if(command.equalsIgnoreCase(Constants.display) || command.equalsIgnoreCase("d")){
				display();
			} else if(command.contains("imulate") || command.contains("s")){
				int n = 0;
				try{
					n = Integer.parseInt(command.split(" ")[1].trim());
				} catch (NumberFormatException e){
					System.err.println("Didn't get number of cycles.");
				}
				simulate(n);
			} else if(command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("e")){
				break;
			}
		}
	}
	
	static void initialize(){
		processor = new Processor(INS_FILE);
		Processor.INS_COUNT = 0;
	} 
	
	static void simulate(int n){
		while(n-- > 0){
			processor.doProcess();
		}
	}
	
	public static void displayDebug(){
		//System.out.print(processor.getExecute().getInstruction() + "  ==>  ");
		if(processor.writeBack.instruction!=null){
			int count = Constants.regCount;
			for(int i=0; i < count; ++i){
				try {
					System.out.print(processor.register.readReg(i)+"\t");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println();
		}
	}
	
	public static void display(){
		//displayPipeline
		System.out.println("------------------------------------------------------------------------------------------------");
		System.out.println("Clock cycles : "+processor.cL.cycle);
		System.out.println("Fetch Stage 	==> PC : "+(processor.fetch.instruction != null ? processor.fetch.pc.read() : 0)+" ==> "+processor.fetch);
		System.out.println("Decode Stage 	==> PC : "+(processor.decode.instruction != null ? processor.decode.pc.read() : 0)+" ==> "+processor.decode);
		System.out.print("ALU1 Stage 	==> PC : "+(processor.fALU1.instruction != null ? processor.fALU1.pc.read() : 0)+" ==> "+processor.fALU1);
		System.out.println("			 BranchFU ==> PC : "+(processor.branchFU.instruction != null ? processor.branchFU.pc.read() : 0)+" ==> "+processor.branchFU);
		System.out.print("ALU2 Stage 	==> PC : "+(processor.fALU2.instruction != null ? processor.fALU2.pc.read() : 0)+" ==> "+processor.fALU2);
		System.out.println("			 Delay 	 ==> PC : "+(processor.delay.instruction != null ? processor.delay.pc.read() : 0)+" ==> "+processor.delay);
		System.out.println("Memory Stage 	==> PC : "+(processor.memoryStage.instruction != null ? processor.memoryStage.pc.read() : 0)+" ==> "+processor.memoryStage);
		System.out.println("Writeback Stage ==> PC : "+(processor.writeBack.instruction != null ? processor.writeBack.pc.read()  : 0)+ " ==> "+processor.writeBack+"\n");
		
		//displayRegisters
		System.out.println("------------------------------------Registers Data----------------------------------------------");
		int count = Constants.regCount - 1;
		try {
			for(int i=0; i < count; ++i){
					System.out.print("R"+i+" : "+processor.register.readReg(i)+"\t");
			}
			System.out.println("X : "+processor.register.readReg(count)+"\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		//printMemory
		System.out.print("--------------------------------First 100 Memory Locations--------------------------------------");
		List<Long> _100Memory = processor.memory.readFirst100();
		for(int i=0; i < 10; ++i){
			System.out.println();
			for(int j=0; j < 10; ++j){
				System.out.print("Mem["+(i*10+j)+"]"+" : " + _100Memory.get(i*10+j)+"\t");
			}
		}
		System.out.println("\n------------------------------------------------------------------------------------------------");
					
		//CPI
		//System.out.println("CPI : "+ (float)Processor.INS_COUNT/processor.clock.getClockTime());
	}
	

}
