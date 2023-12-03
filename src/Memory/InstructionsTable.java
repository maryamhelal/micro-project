package Memory;

public class InstructionsTable {
	int[] iteration;
	String[] instructions;
	String[] destinationRegister;
	String[] j;
	String[] k;
	int[] issue;
	String[] executionComplete;
	int[] writeResult;
	boolean iter;
	
	public InstructionsTable(int n) {
		iteration = new int[n];
		instructions = new String[n];
		destinationRegister = new String[n];
		j = new String[n];
		k = new String[n];
		issue = new int[n];
		executionComplete = new String[n];
		writeResult = new int[n];
		iter = false;
		for(int i=0;i<n;i++) {
			iteration[i]=-1;
			instructions[i]="";
			destinationRegister[i]="";
			j[i]="";
			k[i]="";
			issue[i]=-1;
			executionComplete[i]="";
			writeResult[i]=-1;
		}
	}
	
	public String getIteration(int n) {
		return instructions[n];
	}
	public void incrementIteration(int index) {
		iteration[index]++;
	}
	public String[] getInstructions() {
		return instructions;
	}
	public void setInstructions(String[] value) {
		instructions  = value;
	}
	public String getDestinationRegister(int n) {
		return destinationRegister[n];
	}
	public void setDestinationRegister(int index, String value) {
		destinationRegister[index] = value;
	}
	public String getJ(int n) {
		return j[n];
	}
	public void setJ(int index, String value) {
		j[index] = value;
	}
	public String getK(int n) {
		return k[n];
	}
	public void setK(int index, String value) {
		k[index] = value;
	}
	public int[] getIssue() {
		return issue;
	}
	public void setIssue(int index, int value) {
		issue[index] = value;
	}
	public String[] getExecutionComplete() {
		return executionComplete;
	}
	public void setExecutionStart(int index, int value) {
		String v = value + "..";
		executionComplete[index] = v;
	}
	public void setExecutionComplete(int index, int value) {
		executionComplete[index] += value;
	}
	public int[] getWriteResult() {
		return writeResult;
	}
	public void setWriteResult(int index, int value) {
		writeResult[index] = value;
	}
	public String toString() {
		System.out.println("----------------------Instructions Table----------------------");
		if(!iter) {
			System.out.println("Instruction |  j   |  k   | Issue | Execution Complete | Write Result");
			for(int i=0;i<instructions.length;i++) {
				System.out.print(instructions[i]);
				System.out.print(" | ");
				System.out.print(destinationRegister[i]);
				System.out.print(" | ");
				System.out.print(j[i]);
				System.out.print(" | ");
				System.out.print(k[i]);
				System.out.print(" |   ");
				if(issue[i]!=-1)
					System.out.print(issue[i]);
				System.out.print("   |        ");
				System.out.print(executionComplete[i]);
				System.out.print("        |      ");
				if(writeResult[i]!=-1)
					System.out.println(writeResult[i]);
				else
					System.out.println();
				//System.out.println(instructions[i] + " | "  + destinationRegister[i] + " | " + j[i] + " | "  + k[i] + " |   " + issue[i] + "   |        " + executionComplete[i] + "        |      " + writeResult[i]);
			}
		} else {
			System.out.println("Iteration# | Instruction |  j   |  k   | Issue | Execution Complete | Write Result");
			for(int i=0;i<instructions.length;i++) {
				System.out.print("     ");
				if(iteration[i]!=-1)
					System.out.print(iteration[i]);
				System.out.print("     | ");
				System.out.print(instructions[i]);
				System.out.print(" | ");
				System.out.print(destinationRegister[i]);
				System.out.print(" | ");
				System.out.print(j[i]);
				System.out.print(" | ");
				System.out.print(k[i]);
				System.out.print(" |   ");
				if(issue[i]!=-1)
					System.out.print(issue[i]);
				System.out.print("   |        ");
				System.out.print(executionComplete[i]);
				System.out.print("        |      ");
				if(writeResult[i]!=-1)
					System.out.println(writeResult[i]);
				else
					System.out.println();
				//System.out.println("     " + iteration[i] + "     | " + instructions[i] + " | "  + destinationRegister[i] + " | "  + j[i] + " | "  + k[i] + " |   " + issue[i] + "   |        " + executionComplete[i] + "        |      " + writeResult[i]);
			}
		}
		return "";
	}
}
