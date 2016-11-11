package Processor;

public class Latch implements ClockListener {
	private long slave;
	private long master;
	
	public Latch(Control control) {
		control.addClockListener(this);
	}
	
	public Long read() {
		return master;
	}


	public void write(long master) {
		this.slave = master;
	}


	public void clockChanged(ClockedEvent e) {
		master = slave;
	}

}
