package Memory;

public class RegisterFile {
	String[] tag;
	String[] q;
	int[] content;
	int[] changed;
	int[] line;
	
	public RegisterFile() {
		int n = 64;
		tag = new String[n];
		q = new String[n];
		content = new int[n];
		changed = new int[n];
		line = new int[n];
		for(int i=0;i<n;i++) {
			q[i] = "0";
			content[i] = 1;
			line[i] = -1;
			if(i<32) {
				tag[i] = "F"+i;
			} else {
				tag[i] = "R"+(i-32);
				if(i==32)
					content[i] = 0;
			}
		}
	}
	
	//getters for GUI
	public String[] getTag() {
		return tag;
	}
	public String[] getQ() {
		return q;
	}
	public int[] getContent() {
		return content;
	}

	//getters and setters for Main
	
	//given register name returns tag stored in Q 
	//used in issuing to check dependency of source registers
	//used in write step to check if there is a register waiting for a certain instruction (and write result to it in setContent)
	//used in execution for BNEZ only to check dependency
	public String getQ(String register) {
		for(int i=0;i<32;i++) {
			if(tag[i].equals(register))
				return q[i];
			if(tag[i+32].equals(register))
				return q[i+32];
		}
		return "0";
	}
	//sets Q of a given register (destination of instruction) with a tag value and stores line of instruction from instructionsTable
	//used in issuing step
	public void setQ(String register, String value, int line) {
		for(int i=0;i<32;i++) {
			if(tag[i].equals(register)) {
				q[i] = value;
				this.line[i] = line;
			}
			if(tag[i+32].equals(register) && i!=0) {
				q[i+32] = value;
				this.line[i+32] = line;
			}
		}
	}
	//given register name returns content of register 
	//used in issuing step for all instructions, and in execution for BNEZ only
	public int getContent(String register) {
		for(int i=0;i<32;i++) {
			if(tag[i].equals(register))
				return content[i];
			if(tag[i+32].equals(register))
				return content[i+32];
		}
		return -1;
	}
	//sets content of a given register and sets Q back to zero and line number to -1
	//used in write step
	public void setContent(String register, int val) {
		for(int i=0;i<32;i++) {
			if(tag[i].equals(register)) {
				content[i] = val;
				q[i] = "0";
				changed[i] = 1;
				line[i] = -1;
			}
			if(tag[i+32].equals(register) && i!=0) {
				content[i+32] = val;
				q[i+32] = "0";
				changed[i+32] = 1;
				line[i+32] = -1;
			}
		}
	}
	//gets line of instruction that has a tag in Q
	//used in issuing step but we shouldn't have used it as issuing is in order
	public int getLine(String register) {
		for(int i=0;i<32;i++) {
			if(tag[i].equals(register))
				return line[i];
			if(tag[i+32].equals(register))
				return line[i+32];
		}
		return -1;
	}
	
	public String toString() {
		System.out.println("---------------------Register File---------------------");
		System.out.println("Tag | Qi | Content");
		for(int i=0;i<tag.length;i++) {
			if(i==0)
				System.out.println("-------Floating-point-------");
			if(i==32)
				System.out.println("-----------Integer-----------");
			if(changed[i]!=0)
				System.out.println(tag[i] + "  | " + q[i] + " | " + content[i] + " --> changed");
			else
				System.out.println(tag[i] + "  | " + q[i] + " | " + content[i]);
		}
		return "";
	}
}
