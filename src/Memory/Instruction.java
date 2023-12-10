package Memory;

public class Instruction {
	int iteration;
	String instruction;
	String destinationRegister;
	String j;
	String k;
	int issue;
	String executionComplete;
	int writeResult;
	int result;
	int count;
	
	public Instruction() {
		iteration = 0;
		instruction = "";
		destinationRegister = "";
		j = "";
		k = "";
		issue = -1;
		executionComplete = "";
		writeResult = -1;
		result = -1;
		count = -1;
	}

	public int getIteration() {
		return iteration;
	}
	public void setIteration(int value) {
		iteration = value;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String value) {
		instruction = value;
	}
	public String getDestinationRegister() {
		return destinationRegister;
	}
	public void setDestinationRegister(String value) {
		destinationRegister = value;
	}
	public String getJ() {
		return j;
	}
	public void setJ(String value) {
		j = value;
	}
	public String getK() {
		return k;
	}
	public void setK(String value) {
		k = value;
	}
	public String getIssue() {
		if(issue==-1)
			return "";
		else
			return ""+issue;
	}
	public void setIssue(int value) {
		issue = value;
	}
	public String getExecutionComplete() {
		return executionComplete;
	}
	public void setExecutionStart(int value) {
		executionComplete = value + "..";
	}
	public void setExecutionComplete(int value) {
		executionComplete += value;
	}
	public String getWriteResult() {
		if(writeResult==-1)
			return "";
		else
			return ""+writeResult;
	}
	public void setWriteResult(int value) {
		writeResult = value;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int value) {
		result = value;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int value) {
		count = value;
	}
	public void decrementCount() {
		count--;
	}
	
	public String getOneInstruction() {
		String value = iteration + " | " + instruction + " | " + destinationRegister + " | " + j + " | " + k + " | ";
		if(issue!=-1)
			value+= issue + " | " + executionComplete + " | ";
		else
			value+= "   | " + executionComplete + " | ";
		if(writeResult!=-1)
			value+= writeResult;
		System.out.println(value);
		return value;
	}
}
