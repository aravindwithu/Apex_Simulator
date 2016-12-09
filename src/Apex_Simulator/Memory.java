package Apex_Simulator;

import java.util.ArrayList;
import java.util.List;
import Utility.Constants;
import Utility.FileProcessor;
import Utility.Instruction;

public class Memory {
	private long []memory = new long[Constants.MEM_SIZE];
	private List<Instruction> instructions;
	
	/**
	 * Constructor for Memory initializes the Memory.
	 * @param file of string type to be processed and relevant results are stored in instruction array list in memory.
	 */
	public Memory(String file) {
		for(int i=0; i < Constants.MEM_SIZE; ++i){
			memory[i] = 0;
		}
		instructions = new FileProcessor(file).fetchInstructions();
	}
	
	/**
	 * clearInstructions method clears the instruction in the memory
	 */
	public void clearInstructions(){
		instructions.clear();
	}
	
	/**
	 * getInstruction method calculates and return the instruction to be fetched along with the address
	 * @param  index current instruction address to be fetched
	 * @return  instruction contains instruction and instruction address
	 */
	public Instruction getInstruction(long index){
		Instruction instruction = null;
		int effectiveAddress = (int) (index-Constants.START_ADDRESS);
		if(effectiveAddress>0)
		{
			effectiveAddress = effectiveAddress/4;			
		}
		if(instructions.size()>effectiveAddress)
			instruction = instructions.get(effectiveAddress);
		return instruction;
	}

	/**
	 * readMem method reads the value for the given memory index
	 * @param  index of int type to specify memory location
	 */
	public long readMem(int index) throws Exception{
		if(index >= 0 && index < Constants.MEM_SIZE)
			return memory[index];
		else
			throw new Exception("Illegal memory location, trying to access MEM["+index+"]");
	}
	
	/**
	 * writeMem method writes the data to the memory location
	 * @param  index of type int which specify memory location
	 * @param  data of type long that will be stored in given memory location
	 * @return  of long type, contains written memory location
	 */
	public long writeMem(int index, long data) throws Exception{
		if(index >= 0 && index < Constants.MEM_SIZE){
			memory[index] = data;
		} else {
			throw new Exception("Invalid memory location!!");
		}
		return memory[index];
	}
	
	/**
	 * readFirst100 method reads the first 100 memory locations
	 * @param  stage of type Constants.Stage
	 */
	public List<Long> readFirst100(){
		return readMemory(0, 100);
	}
	
	/**
	 * readMemory method reads the memory locations from start index to the last index
	 * @param  startIndex defines the start index from which the memory need to be read
	 * @param  lastIndex defines the last index from which the memory need to be read
	 * @returns array list of long contains memory locations read
	 */
	public List<Long> readMemory(int startIndex, int lastIndex){
		List<Long> data = new ArrayList<Long>();
		for(int i=startIndex; i < lastIndex; ++i){
			data.add(memory[i]);
		}
		return data;
	}
}
