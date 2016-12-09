package Apex_Simulator;

public class CycleListener {
	private long tempResult;
	private long finalResult;
	public Integer cycle;

	/**
	 * Constructor for CycleListener counts and keeps track of the cycle, instruction address, results of each stages.
	 * @param processor object of the processor.
	 */
	public CycleListener(Processor processor) {
		cycle = 0;
		processor.cycleListener.add(this);		
	}
	
	/**
	 * reads the final result just before incrementing the cycle.
	 */
	public Long read() {
		return finalResult;
	}
	
	/**
	 * reads the temporary  result of the different stages in middle of the cycle.
	 */
	public Long temRread() {
		return tempResult;
	}

	/**
	 * Writes the temporary  result of the different stages in middle of the cycle.
	 */
	public void write(long result) {
		this.tempResult = result;		
	}
	
	/**
	 * Writes temporary  result of the different to the final result of different stages when cycle is changed.
	 * @param cL CycleListener object from processor.
	 */
	public void ChangeCycle(CycleListener cL) {
		finalResult = tempResult;
	}

}
