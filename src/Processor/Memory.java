package Processor;

import java.util.ArrayList;
import java.util.List;
import Utility.Constants;
import Utility.FileProcessor;
import Utility.Instruction;

public class Memory {
	private long []memory = new long[Constants.memSize];
	private List<Instruction> instructions;
	
	public Memory(String file) {
		for(int i=0; i < Constants.memSize; ++i){
			memory[i] = 0;
		}
		instructions = new FileProcessor(file).fetchInstructions();
	}
	
	public void clearInstructions(){
		instructions.clear();
	}
	
	public Instruction getInstruction(long index){
		Instruction instruction = null;
		int effectiveAddress = (int) (index-Constants.startAddress);
		if(effectiveAddress>0)
		{
			effectiveAddress = effectiveAddress/4;			
		}
		if(instructions.size()>effectiveAddress)
			instruction = instructions.get(effectiveAddress);
		return instruction;
	}

	public long readMem(int index) throws Exception{
		if(index >= 0 && index < Constants.memSize)
			return memory[index];
		else
			throw new Exception("Illegal memory location, trying to access MEM["+index+"]");
	}

	public long writeMem(int index, long data) throws Exception{
		if(index >= 0 && index < Constants.memSize){
			memory[index] = data;
		} else {
			throw new Exception("Invalid memory location!!");
		}
		return memory[index];
	}

	public List<Long> readFirst100(){
		return readMemory(0, 100);
	}

	public List<Long> readMemory(int startIndex){
		return readMemory(startIndex, Constants.memSize);
	}
	
	public List<Long> readMemory(int startIndex, int lastIndex){
		List<Long> data = new ArrayList<Long>();
		for(int i=startIndex; i < lastIndex; ++i){
			data.add(memory[i]);
		}
		return data;
	}
}
