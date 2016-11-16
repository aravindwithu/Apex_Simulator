package Utility;

public enum Constants {
	ADD, SUB, MOVC, MUL, AND, OR, XOR, LOAD, STORE,  BZ, BNZ, JUMP, BAL, HALT, STALL, MOV;	
	
	public static final String regPrefix = "R";
	public static final String literalPrefix = "#";
	public static final int memSize = 10000;
	public static final int regCount = 17;	//16 Reg + 1 Reg X
	public static final long startAddress = 4000;	
	public static final String initialize = "Initialize";
	public static final String imulate = "Simulate";
	public static final String display = "Display";
	public static final String separator1 = " ";
	public static final String separator2 = ", ";
	
}


