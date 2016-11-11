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
			String []ops = ins.split(Constants.OPS_SEPARATOR);
			instruction.setOpcode(Instruction.getInstructionOpcode(ops[0].trim()));
			
			if(instruction.getOpcode() == INS_OPCODE.HALT){
				return instruction;
			}
			ins = ins.substring(ins.indexOf(" ")+1);
			String []params = ins.split(Constants.OPS_SEPARATOR);
			
			if(isLiteral(params)) instruction.setLiteral(true);
			else instruction.setLiteral(false);
			
			for(int i=0; i< params.length; ++i){
				if(params[i].trim().equalsIgnoreCase("X")){
					params[i] = "R8";
				}
			}
			
			if(instruction.getOpcode() == INS_OPCODE.STORE && params != null && params.length > 0){
				if(!instruction.isLiteral()){
					instruction.setDestAdd(Long.parseLong(params[0].substring(1).trim()));
					instruction.setSrc1Add(Long.parseLong(params[1].substring(1).trim()));
					instruction.setSrc2Add(Long.parseLong(params[2].substring(1).trim()));
				} else {
					instruction.setSrc1Add(Long.parseLong(params[0].substring(1).trim()));
					instruction.setSrc2Add(Long.parseLong(params[1].substring(1).trim()));
					instruction.setLiteral(Long.parseLong(params[2].trim()));
				}
			} else if((instruction.getOpcode() == INS_OPCODE.JUMP || instruction.getOpcode() == INS_OPCODE.BAL) && params != null && params.length > 0){
				instruction.setSrc1Add(Long.parseLong(params[0].substring(1).trim()));
				instruction.setLiteral(Long.parseLong(params[1]));
			} else if(instruction.getOpcode() == INS_OPCODE.BZ || instruction.getOpcode() == INS_OPCODE.BNZ){
				instruction.setSrc1Add(Long.parseLong(params[0].substring(1).trim()));
				instruction.setLiteral(Long.parseLong(params[1].trim()));
			} else if(params != null && params.length > 0 && instruction.getOpcode() == INS_OPCODE.MOV){
				instruction.setDest(Long.parseLong(params[0].trim().substring(1)));
				instruction.setSrc1Add(Long.parseLong(params[1].trim().substring(1)));
			} else if(instruction.getOpcode() == INS_OPCODE.HALT){
				
			} else if(params != null && params.length > 0){
				for(String param : params){
					if(param.contains(Constants.REGISTER_PREFIX)){
						if(instruction.getDest() == null){
							instruction.setDest(Long.parseLong(param.substring(1).trim()));
						} else if(instruction.getSrc1Add() == null){
							instruction.setSrc1Add(Long.parseLong(param.substring(1).trim()));
						} else {
							instruction.setSrc2Add(Long.parseLong(param.substring(1).trim()));
						}
					}  else {
						instruction.setLiteral(Long.parseLong(param.trim()));
					}
				}
			}
		}
		return instruction;
	}
	
	private boolean isLiteral(String []params){
		boolean result = false;
		for(String param : params){
			if(!param.contains(Constants.REGISTER_PREFIX)){
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
