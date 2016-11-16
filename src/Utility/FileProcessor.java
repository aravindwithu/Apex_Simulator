package Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileProcessor {
	private File file;
	public FileProcessor(String fileName) {
		this(new File(fileName));
	}
	
	public FileProcessor(File file) {
		this.file = file;
	}

	public List<Instruction> fetchInstructions(){
		List<Instruction> instructions = new ArrayList<Instruction>();
		List<String> fileStrings = readFile();
		Iterator<String> stringItr = fileStrings.iterator();
		while(stringItr.hasNext()){
			String ins = stringItr.next();
			Instruction instruction = parseLine(ins);
			if(instruction != null)
				instructions.add(instruction);
		}
		return instructions;
	}
	
	private Instruction parseLine(String ins){
		Instruction instruction = null;
		if(ins != null && ins.length() > 0){
			instruction = new Instruction();
			String []ops = ins.split(Constants.separator1);
			instruction.opcode = Instruction.getInstructionOpcode(ops[0].trim());
			
			if(instruction.opcode == Constants.HALT){
				return instruction;
			}
			ins = ins.substring(ins.indexOf(" ")+1);
			String []params = ins.split(Constants.separator2);
			
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
			
			if(instruction.opcode == Constants.STORE && params != null && params.length > 0){
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
			
			else if(instruction.opcode == Constants.LOAD && params != null && params.length > 0){
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
			
			else if((instruction.opcode == Constants.JUMP || instruction.opcode == Constants.BAL) && params != null && params.length > 0){
				instruction.src1Add = Long.parseLong(params[0].substring(1).trim());
				instruction.literal = Long.parseLong(params[1].substring(1).trim());
			} else if(instruction.opcode == Constants.BZ || instruction.opcode == Constants.BNZ){
				
				if(params[0].substring(0).trim() == "R"){
				instruction.src1Add = Long.parseLong(params[0].substring(1).trim());
				instruction.literal = Long.parseLong(params[1].substring(1).trim());
				}
				else{
				instruction.literal = Long.parseLong(params[0].substring(1).trim());
				}
				
				
			} else if(params != null && params.length > 0 && instruction.opcode == Constants.MOV){
				instruction.dest = Long.parseLong(params[0].trim().substring(1));
				instruction.src1Add = Long.parseLong(params[1].trim().substring(1));
			} else if(instruction.opcode == Constants.HALT){
				
			} else if(params != null && params.length > 0){
				for(String param : params){
					if(param.contains(Constants.regPrefix)){
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
	
	private boolean isLiteral(String []params){
		boolean result = false;
		for(String param : params){
			if(!param.contains (Constants.regPrefix)){
				result = true;
				break;
			}
		}
		return result;
	}
	
	private List<String> readFile(){
		List<String> instructions = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		    String line;
		    while ((line = br.readLine()) != null) {
		       instructions.add(line);
		    }
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		} finally{
			if(br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return instructions;
	}
}
