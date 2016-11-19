package Apex_Simulator;

public class CycleListener {
	private long tempResult;
	private long finalResult;
	public Integer cycle;

	public CycleListener(Processor processor) {
		cycle = 0;
		processor.cycleListener.add(this);		
	}
	
	public Long read() {
		return finalResult;
	}
	
	public Long temRread() {
		return tempResult;
	}

	public void write(long result) {
		this.tempResult = result;		
	}

	public void ChangeCycle(CycleListener cL) {
		finalResult = tempResult;
	}

}
