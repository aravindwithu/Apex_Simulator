package Processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Main.Main;
import Stages.Decode;
import Stages.Execute;
import Stages.Fetch;
import Stages.MemoryStage;
import Stages.Stage;
import Stages.WriteBack;

public class Control {
	
	private List<ClockListener> clockListeners = new ArrayList<ClockListener>();
	private List<ProcessListener> processListeners = new ArrayList<ProcessListener>();
	
	public boolean isPipelineStalled = false;
	
	protected Memory memory;
	protected ArchitecturalRegister register;
	
	protected Fetch fetch;
	protected Decode decode;
	protected Execute execute;
	protected MemoryStage memoryStage;
	protected WriteBack writeBack;
	public ClockedEvent e;
	public boolean debug = false;
	
	public static int INS_COUNT;
	
	public Control(String file) {
		memory = new Memory(file);
		register = new ArchitecturalRegister();
		
		writeBack = new WriteBack(this);
		memoryStage = new MemoryStage(this);
		execute = new Execute(this);
		decode = new Decode(this);
		fetch = new Fetch(this);
		e = new ClockedEvent();
		Control.INS_COUNT = 0;
	}
	
	
	public void addClockListener(ClockListener clockListener){
		clockListeners.add(clockListener);
	}
	
	public void removeClockListener(ClockListener clockListener){
		clockListeners.remove(clockListeners.indexOf(clockListener));
	}
	
	public void addProcessListener(ProcessListener processListener){
		processListeners.add(processListener);
	}
	
	public void removeProcessListener(ProcessListener processListener){
		processListeners.remove(processListeners.indexOf(processListener));
	}

	private void doProcess() {
		Iterator<ProcessListener> listenerItr = processListeners.iterator();
		while(listenerItr.hasNext()){
			ProcessListener listener = listenerItr.next();
			try{
				listener.process();
			} catch(Exception e){
				e.printStackTrace();
				System.err.println("Faulty instruction at : "+((Stage)listener).getPc().read());
			}
		}
	}
	
	private void doClockChange(ClockedEvent e){
		Iterator<ClockListener> listenerItr = clockListeners.iterator();
		while(listenerItr.hasNext()){
			listenerItr.next().clockChanged(e);
		}
	}

	public void step(){
		doProcess();
		doClockChange(e);
		e.incrementClock();
		setIsPipelineStalled();
		if(debug)
			Main.displayDebug();
	}
	
	private void setIsPipelineStalled() {
		List<Long> dests = new ArrayList<Long>();
		if(execute.getInstruction() != null && execute.getInstruction().getDest() != null)
			dests.add(execute.getInstruction().getDest());
		if(memoryStage.getInstruction() != null && memoryStage.getInstruction().getDest() != null)
			dests.add(memoryStage.getInstruction().getDest());
		
		/*if(writeBack.getInstruction() != null)
			dests.add(writeBack.getInstruction().getDest());
		*/
		
		if(decode.getInstruction() != null && (dests.contains(decode.getInstruction().getSrc1Add()) || dests.contains(decode.getInstruction().getSrc2Add()))){
			isPipelineStalled = true;
		} else {
			isPipelineStalled = false;
		}
	}


	public Memory getMemory() {
		return memory;
	}


	public ArchitecturalRegister getRegister() {
		return register;
	}


	public Fetch getFetch() {
		return fetch;
	}


	public Decode getDecode() {
		return decode;
	}


	public Execute getExecute() {
		return execute;
	}


	public MemoryStage getMemoryStage() {
		return memoryStage;
	}


	public WriteBack getWriteBack() {
		return writeBack;
	}
	
}
