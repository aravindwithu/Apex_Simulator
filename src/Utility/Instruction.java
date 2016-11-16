package Utility;

public class Instruction {
	
	public Constants opcode;
	public Long dest;
	public Long src1;
	public Long src2;
	public Long literal;
	public Long destAdd;
	public boolean isLiteral;	
	public Long src1Add;
	public Long src2Add;
	
	public static Constants getInstructionOpcode(String strOp){
		Constants result = null;
		if(strOp .equalsIgnoreCase("ex-or")) return Constants.XOR;
		
		for(Constants code : Constants.values()){
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
				
				if(this.isLiteral)
					return this.opcode+" R"+this.dest + " R"+this.src1Add+" "+this.literal + (str.length() > 2 ? str : "");
				else
					return this.opcode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
				
				
			case 8: //Store
				if(this.isLiteral)
					return this.opcode+" R"+this.src1Add+" R"+this.src2Add + " "+this.literal + (str.length() > 2 ? str : "");
				else
					return this.opcode+" R"+this.src1Add + " R"+this.src2Add+" R"+this.dest + (str.length() > 2 ? str : "");
			case 9: //BZ,
				if(src1Add != null){
				return this.opcode+" R"+this.src1Add + " "+this.literal+ (str.length() > 2 ? str : "");
				}
				else{
					return this.opcode+" "+this.literal+ (str.length() > 2 ? str : "");
				}
			case 10: //BNZ,
				if(src1Add != null){
				return this.opcode+" R"+this.src1Add + " "+this.literal+ (str.length() > 2 ? str : "");
				} 
				else{
					return this.opcode+" "+this.literal+ (str.length() > 2 ? str : "");
				}
			case 11: //JUMP, 
				return this.opcode+ " "+(this.src1Add > Constants.regCount - 1 ? "X" : "R"+this.src1Add)+" "+this.literal;
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
