package Processor;

public class ClockedEvent {
	private Integer clockTime;
	
	public ClockedEvent() {
		this(0);
	}

	public ClockedEvent(int time) {
		this.clockTime = time;
	}
	
	public void incrementClock(){
		++clockTime;
	}

	public Integer getClockTime() {
		return clockTime;
	}

	public void setClockTime(Integer clockTime) {
		this.clockTime = clockTime;
	}
}
