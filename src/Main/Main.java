package Main;

import java.util.List;
import java.util.Scanner;

import Processor.Control;
import Utility.Constants;

public class Main {
	private static Control CONTROL;
	private static String INS_FILE;
	public static void main(String[] args) {
		if(args.length > 0){
			CONTROL = new Control(args[0]);
			if(args.length > 1 && args[1] != null && args[1].contains("debug"))
				CONTROL.debug = true;
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
			if(command.equalsIgnoreCase(Constants.COM_INITIALIZE) || command.equalsIgnoreCase("i")){
				initialize();
			} else if(command.equalsIgnoreCase(Constants.COM_DISPLAY) || command.equalsIgnoreCase("d")){
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
		CONTROL = new Control(INS_FILE);
		Control.INS_COUNT = 0;
	} 
	
	static void simulate(int n){
		while(n-- > 0){
			CONTROL.step();
		}
	}
	
	public static void displayDebug(){
		//System.out.print(CONTROL.getExecute().getInstruction() + "  ==>  ");
		if(CONTROL.getWriteBack().getInstruction()!=null){
			int count = Constants.ARCH_REGISTER_COUNT;
			for(int i=0; i < count; ++i){
				try {
					System.out.print(CONTROL.getRegister().readReg(i)+"\t");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println();
		}
	}
	
	public static void display(){
		displayPipeline();
		printMemory();
		displayRegisters();
		System.out.println("CPI : "+ (float)Control.INS_COUNT/CONTROL.e.getClockTime());
	}
	
	private static void printMemory() {
		System.out.println("=======================================================================");
		System.out.println("=======================First 100 Memory Locations======================");
		System.out.println("=======================================================================");
		List<Long> _100Memory = CONTROL.getMemory().readFirst100();
		for(int i=0; i < 10; ++i){
			System.out.println();
			for(int j=0; j < 10; ++j){
				System.out.print("Mem["+(i*10+j)+"]"+" : " + _100Memory.get(i*10+j)+"\t");
			}
		}
		System.out.println("\n=======================================================================");
	}

	private static void displayPipeline(){
		System.out.println("=============================================================================");
		System.out.println("Clock cycles : "+CONTROL.e.getClockTime());
		System.out.println("Fetch Stage 	==> PC : "+(CONTROL.getFetch().getInstruction() != null ? CONTROL.getFetch().getPc().read() : 0)+" ==> "+CONTROL.getFetch());
		System.out.println("Decode Stage 	==> PC : "+(CONTROL.getDecode().getInstruction() != null ? CONTROL.getDecode().getPc().read() : 0)+" ==> "+CONTROL.getDecode());
		System.out.println("Execute Stage 	==> PC : "+(CONTROL.getExecute().getInstruction() != null ? CONTROL.getExecute().getPc().read() : 0)+" ==> "+CONTROL.getExecute());
		System.out.println("Memory Stage 	==> PC : "+(CONTROL.getMemoryStage().getInstruction() != null ? CONTROL.getMemoryStage().getPc().read() : 0)+" ==> "+CONTROL.getMemoryStage());
		System.out.println("Writeback Stage ==> PC : "+(CONTROL.getWriteBack().getInstruction() != null ? CONTROL.getWriteBack().getPc().read()  : 0)+ " ==> "+CONTROL.getWriteBack());
	}
	
	public static void displayRegisters(){
		System.out.println("============================Registers Data=============================");
		System.out.println("=======================================================================");
		int count = Constants.ARCH_REGISTER_COUNT - 1;
		try {
			for(int i=0; i < count; ++i){
					System.out.print("R"+i+" : "+CONTROL.getRegister().readReg(i)+"\t");
			}
			System.out.print("X : "+CONTROL.getRegister().readReg(count));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n=======================================================================");
	}
}
