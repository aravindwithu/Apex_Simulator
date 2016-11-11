package Processor;

import java.util.ArrayList;
import java.util.List;
import Utility.Constants;
import Utility.FileProcessor;
import Utility.Instruction;

public class Memory {
	private long []memory = new long[Constants.RAM_SIZE];
	private List<Instruction> instructions;
	
	public Memory(String file) {
		for(int i=0; i < Constants.RAM_SIZE; ++i){
			memory[i] = 0;
		}
		instructions = new FileProcessor(file).fetchInstructions();
	}
	
	public void clearInstructions(){
		instructions.clear();
	}
	
	public Instruction getInstruction(long index){
		Instruction instruction = null;
		int effectiveAddress = (int) (index-Constants.STRT_INST_ADDRESS);
		if(instructions.size()>effectiveAddress)
			instruction = instructions.get(effectiveAddress);
		return instruction;
	}
	
	/**
	 * Read single memory location
	 * @return - data stored 
	 */
	public long readMem(int index) throws Exception{
		if(index >= 0 && index < Constants.RAM_SIZE)
			return memory[index];
		else
			throw new Exception("Illegal memory location, trying to access MEM["+index+"]");
	}
	
	/**
	 * Writes data value to a specific memory location.
	 * @param index - memory location to be stored
	 * @param data -  value to be stored
	 * @return - data written to index memory location.
	 * @throws Exception
	 */
	public long writeMem(int index, long data) throws Exception{
		if(index >= 0 && index < Constants.RAM_SIZE){
			memory[index] = data;
		} else {
			throw new Exception("Invalid memory location!!");
		}
		return memory[index];
	}
	
	/**
	 * Read out first 100 memory locations
	 * @return {@link List} of {@link Long}'s 
	 */
	public List<Long> readFirst100(){
		return readMemory(0, 100);
	}
	
	/**
	 * Reads memory locations beginning from beginIndex till last address of memory
	 * @param beginIndex
	 * @return returns list of bytes stored from 
	 */
	public List<Long> readMemory(int beginIndex){
		return readMemory(beginIndex, Constants.RAM_SIZE);
	}
	
	/**
	 * Read memory locations beginning from beginIndex till endIndex (Excluding)
	 * @param beginIndex
	 * @param endIndex
	 * @return return list of bytes read out
	 */
	public List<Long> readMemory(int beginIndex, int endIndex){
		List<Long> data = new ArrayList<Long>();
		for(int i=beginIndex; i < endIndex; ++i){
			data.add(memory[i]);
		}
		return data;
	}
}
