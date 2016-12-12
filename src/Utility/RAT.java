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

	/**
	 * gets RAT Physical Reg.
	 */
	public long getRATPhyReg() {
		return phyReg;
	}
	
	/**
	 * gets RAT Status Reg.
	 */
	public boolean getRATStatus() {
		return status;
	}

	/**
	 * sets RAT Physical Reg.
	 */
	public void setRATPhyReg(long phy_R) {
		// TODO Auto-generated method stub
		phyReg = phy_R;
	}
	
	/**
	 * sets RAT Status Reg.
	 */
	public void setRATStatus(boolean statusVal) {
		// TODO Auto-generated method stub
		status = statusVal;
	}
}
