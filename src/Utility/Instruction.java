package Utility;

public class Instruction {
	
	public Constants.OpCode opCode;
	public Long dest;
	public Long src1;
	public Long src2;
	public Long literal;
	public Long destAdd;
	public boolean isLiteral;	
	public Long src1Add;
	public Long src2Add;
	public boolean src1Stall = false; 
	public boolean src2Stall = false;
	
	public static Constants.OpCode getInstructionOpcode(String strOp){
		Constants.OpCode result = null;
		if(strOp .equalsIgnoreCase("ex-or")) return Constants.OpCode.EXOR;
		
		for(Constants.OpCode code : Constants.OpCode.values()){
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
			
			switch(this.opCode.ordinal()){
			case 0: //add
				return this.opCode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
			case 1:	//sub
				return this.opCode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
				
			case 2: // MUL
				return this.opCode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
				
			case 3: //MOVC
				return this.opCode+" R"+this.dest + " "+this.literal+ (str.length() > 2 ? str : "");
			
			case 4: // MOV
				return this.opCode+ " R"+this.destAdd+" R"+this.src1Add;
				
			case 5: //AND
				return this.opCode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
			case 6:	//OR
				return this.opCode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
			case 7:	//XOR
				return this.opCode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
			case 8: //LOAD
				
				if(this.isLiteral)
					return this.opCode+" R"+this.dest + " R"+this.src1Add+" "+this.literal + (str.length() > 2 ? str : "");
				else
					return this.opCode+" R"+this.dest + " R"+this.src1Add+" R"+this.src2Add + (str.length() > 2 ? str : "");
				
				
			case 9: //Store
				if(this.isLiteral)
					return this.opCode+" R"+this.src1Add+" R"+this.src2Add + " "+this.literal + (str.length() > 2 ? str : "");
				else
					return this.opCode+" R"+this.src1Add + " R"+this.src2Add+" R"+this.dest + (str.length() > 2 ? str : "");
			case 10: //BZ,
				if(src1Add != null){
				return this.opCode+" R"+this.src1Add + " "+this.literal+ (str.length() > 2 ? str : "");
				}
				else{
					return this.opCode+" "+this.literal+ (str.length() > 2 ? str : "");
				}
			case 11: //BNZ,
				if(src1Add != null){
				return this.opCode+" R"+this.src1Add + " "+this.literal+ (str.length() > 2 ? str : "");
				} 
				else{
					return this.opCode+" "+this.literal+ (str.length() > 2 ? str : "");
				}
			case 12: //JUMP, 
				return this.opCode+ " "+(this.src1Add > Constants.REG_COUNT - 1 ? "X" : "R"+this.src1Add)+" "+this.literal;
			case 13: //BAL, 
				return this.opCode+" R"+this.src1Add+" "+this.literal+ (str.length() > 2 ? str : "");
			case 14: //HALT, STALL
				return "HALT";
			
			}
		return null;
	}
}
