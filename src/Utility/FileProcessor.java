package Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {
	private File file;
	
	/**
	 * Constructor for FileProcessor initializes file object with relevant instruction file.
	 * @param fileName a instruction file set in string format.
	 */
	public FileProcessor(String fileName) {
		file = new File(fileName);
	}

	/**
	 * fetchInstructions method process the file object and stores the instructions in array format in a instruction array list.
	 */
	public List<Instruction> fetchInstructions(){
		List<Instruction> instructions = new ArrayList<Instruction>();
		List<String> fileData = readFile();
		for (String fileStr : fileData){
			if(parseLine(fileStr) != null)
				instructions.add(parseLine(fileStr));
		}
		return instructions;
	}
	
	/**
	 * parseLine method parse the each instruction and returns it to the fetchInstructions method.
	 * sets the opcode and register identification fields in instruction class.
	 */
	private Instruction parseLine(String ins){
		Instruction instruction = null;
		if(ins != null && ins.length() > 0){
			instruction = new Instruction();
			String []ops = ins.split(Constants.SEPARATOR1);
			instruction.opCode = Instruction.getInstructionOpcode(ops[0].trim());
			
			if(instruction.opCode == Constants.OpCode.HALT){
				return instruction;
			}
			ins = ins.substring(ins.indexOf(" ")+1);
			String []params = ins.split(Constants.SEPARATOR2);
			
			if(isLiteral(params)) { 
				instruction.isLiteral = true;
			}
			else {
				instruction.isLiteral = false;
			}
			for(int i=0; i< params.length; ++i){
				if(params[i].trim().equalsIgnoreCase("X")){
					params[i] = "R16";
				}
			}
			
			if(instruction.opCode == Constants.OpCode.STORE && params != null && params.length > 0){
				if(!instruction.isLiteral){					
					instruction.src1Add = Long.parseLong(params[0].substring(1).trim());
					instruction.src2Add = Long.parseLong(params[1].substring(1).trim());
					instruction.dest = Long.parseLong(params[2].substring(1).trim());
				} else {
					instruction.src1Add = Long.parseLong(params[0].substring(1).trim());
					instruction.src2Add = Long.parseLong(params[1].substring(1).trim());
					instruction.literal = Long.parseLong(params[2].substring(1).trim());
				}
			} 
			
			else if(instruction.opCode == Constants.OpCode.LOAD && params != null && params.length > 0){
				if(!instruction.isLiteral){
					instruction.dest = Long.parseLong(params[0].substring(1).trim());
					instruction.src1Add = Long.parseLong(params[1].substring(1).trim());
					instruction.src2Add = Long.parseLong(params[2].substring(1).trim());
				} else {
					instruction.dest = Long.parseLong(params[0].substring(1).trim());
					instruction.src1Add = Long.parseLong(params[1].substring(1).trim());
					instruction.literal = Long.parseLong(params[2].substring(1).trim());
				}
			} 
			
			else if((instruction.opCode == Constants.OpCode.JUMP || instruction.opCode == Constants.OpCode.BAL) && params != null && params.length > 0){
				instruction.src1Add = Long.parseLong(params[0].substring(1).trim());
				instruction.literal = Long.parseLong(params[1].substring(1).trim());
			} else if(instruction.opCode == Constants.OpCode.BZ || instruction.opCode == Constants.OpCode.BNZ){
				
				if(params[0].substring(0).trim() == "R"){
				instruction.src1Add = Long.parseLong(params[0].substring(1).trim());
				instruction.literal = Long.parseLong(params[1].substring(1).trim());
				}
				else{
				instruction.literal = Long.parseLong(params[0].substring(1).trim());
				}
				
				
			} else if(params != null && params.length > 0 && instruction.opCode == Constants.OpCode.MOV){
				instruction.dest = Long.parseLong(params[0].trim().substring(1));
				instruction.src1Add = Long.parseLong(params[1].trim().substring(1));
			} else if(instruction.opCode == Constants.OpCode.HALT){
				
			} else if(params != null && params.length > 0){
				for(String param : params){
					if(param.contains(Constants.REG_PREFIX)){
						if(instruction.dest == null){
							instruction.dest = Long.parseLong(param.substring(1).trim());
						} else if(instruction.src1Add == null){
							instruction.src1Add = Long.parseLong(param.substring(1).trim());
						} else {
							instruction.src2Add = Long.parseLong(param.substring(1).trim());
						}
					}  else {
						instruction.literal = Long.parseLong(param.substring(1).trim());
					}
				}
			}
		}
		return instruction;
	}
	
	/**
	 * isLiteral method checks whether a instruction contains literal values or not
	 * @param params string array of a single instruction(opcodes, registers and literals if any)
	 * @return boolean value
	 */
	private boolean isLiteral(String []params){
		boolean result = false;
		for(String param : params){
			if(!param.contains (Constants.REG_PREFIX)){
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * readFile method stores the instructions in array list from file using buffered reader.
	 */
	private List<String> readFile(){
		List<String> instructions = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		    String line;
		    while ((line = br.readLine()) != null) {
		       instructions.add(line);
		    }
		    br.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		} 
		return instructions;
	}
}
