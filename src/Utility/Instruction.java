package Utility;

public class Instruction {
	
	private INS_OPCODE opcode;
	private Long dest;
	private Long src1;
	private Long src2;
	private Long literal;
	private Long destAdd;
	private boolean isLiteral;
	
	private Long src1Add;
	private Long src2Add;
	
	public INS_OPCODE getOpcode() {
		return opcode;
	}
	public void setOpcode(INS_OPCODE opcode) {
		this.opcode = opcode;
	}
	public Long getDest() {
		return dest;
	}
	public void setDest(Long dest) {
		this.dest = dest;
	}
	public Long getSrc1() {
		return src1;
	}
	public void setSrc1(Long src1) {
		this.src1 = src1;
	}
	public Long getSrc2() {
		return src2;
	}
	public void setSrc2(Long src2) {
		this.src2 = src2;
	}
	public Long getLiteral() {
		return literal;
	}
	public void setLiteral(Long literal) {
		this.literal = literal;
	}
	public Long getSrc1Add() {
		return src1Add;
	}
	public void setSrc1Add(Long src1Add) {
		this.src1Add = src1Add;
	}
	public Long getSrc2Add() {
		return src2Add;
	}
	public void setSrc2Add(Long src2Add) {
		this.src2Add = src2Add;
	}
	
	public Long getDestAdd() {
		return destAdd;
	}
	public void setDestAdd(Long destAdd) {
		this.destAdd = destAdd;
	}
	public boolean isLiteral() {
		return isLiteral;
	}
	public void setLiteral(boolean isLiteral) {
		this.isLiteral = isLiteral;
	}
	public static INS_OPCODE getInstructionOpcode(String strOp){
		INS_OPCODE result = null;
		if(strOp .equalsIgnoreCase("ex-or")) return INS_OPCODE.XOR;
		
		for(INS_OPCODE code : INS_OPCODE.values()){
			if(code.name().equalsIgnoreCase(strOp)){
				result = code;
				break;
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		
			String str = "(";
			if(this.src1 != null){
				str += "R"+this.src1Add+" = "+this.src1;
			}
			
			if(this.src2 != null){
				str += ", R"+this.src2Add+" = "+this.src2;
			}
			
			str += ")";
			
			switch(this.opcode.ordinal()){
			case 0: //add
				return this.opcode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
			case 1:	//sub
				return this.opcode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
			case 2: //MOVC
				return this.opcode+" R"+this.dest + " "+this.literal+ (str.length() > 2 ? str : "");
			case 3: // MUL
				return this.opcode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
			case 4: //AND
				return this.opcode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
			case 5:	//OR
				return this.opcode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
			case 6:	//XOR
				return this.opcode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
			case 7: //LOAD
				return this.opcode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
			case 8: //Store
				if(this.isLiteral)
					return this.opcode+" R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
				else
					return this.opcode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
			case 9: //BZ, 
				return this.opcode+" R"+this.src1Add + " "+this.literal+ (str.length() > 2 ? str : "");
			case 10: //BNZ,
				return this.opcode+" R"+this.src1Add + " "+this.literal+ (str.length() > 2 ? str : "");
			case 11: //JUMP, 
				return this.opcode+ " "+(this.src1Add > Constants.ARCH_REGISTER_COUNT - 1 ? "X" : "R"+this.src1Add)+" "+this.literal;
			case 12: //BAL, 
				return this.opcode+" R"+this.src1Add+" "+this.literal+ (str.length() > 2 ? str : "");
			case 13: //HALT, STALL
				return "HALT";
			case 15:
				return this.opcode+ " R"+this.destAdd+" R"+this.src1Add;
			}
		return null;
	}
}
