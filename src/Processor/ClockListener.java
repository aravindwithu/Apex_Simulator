package Processor;

import java.util.EventListener;

public interface ClockListener extends EventListener{
	public void clockChanged(ClockedEvent e);
}
