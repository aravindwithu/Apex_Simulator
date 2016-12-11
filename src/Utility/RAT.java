package Utility;

public class RAT {

	private long phyReg;
	private boolean status;

	/**
	 * Constructor for RAT initializes the RAT.
	 */
	public RAT() {
		//reset all RAT
		phyReg = -1;
		status = false;
	}

	public long getRATPhyReg() {
		return phyReg;
	}
	
	public boolean getRATStatus() {
		return status;
	}

	public void setRATPhyReg(long phy_R) {
		// TODO Auto-generated method stub
		phyReg = phy_R;
	}

	public void setRATStatus(boolean statusVal) {
		// TODO Auto-generated method stub
		status = statusVal;
	}
}
