package Memory;

public class Instruction {
	String iteration;
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
		iteration = "";
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

	public String getIteration() { //GUI
		if(iteration.equals("0"))
			return "";
		else
			return iteration;
	}
	public void setIteration(int value) { //set every time loop is entered
		iteration = ""+value;
	}
	public String getInstruction() { //GUI
		return instruction;
	}
	public void setInstruction(String value) { //used in loadInstruction, stores operation
		instruction = value;
	}
	public String getDestinationRegister() { //used in issue step and GUI
		return destinationRegister;
	}
	public void setDestinationRegister(String value) { //used in loadInstruction
		destinationRegister = value;
	}
	public String getJ() { //used in issue step and GUI
		return j;
	}
	public void setJ(String value) { //used in loadInstruction
		j = value;
	}
	public String getK() { //used in issue step and GUI
		return k;
	}
	public void setK(String value) { //used in loadInstruction
		k = value;
	}
	public String getIssue() { //GUI
		if(issue==-1)
			return "";
		else
			return ""+issue;
	}
	public void setIssue(int value) { //used in issue step
		issue = value;
	}
	public String getExecutionComplete() { //GUI
		return executionComplete;
	}
	public void setExecutionStart(int value) { //used in execute step when starts executing
		executionComplete = value + "..";
	}
	public void setExecutionComplete(int value) { //used in execute step when count reaches 0
		executionComplete += value;
	}
	public String getWriteResult() { //GUI
		if(writeResult==-1)
			return "";
		else
			return ""+writeResult;
	}
	public void setWriteResult(int value) { //used in write step
		writeResult = value;
	}
	public int getResult() { //used in write step
		return result;
	}
	public void setResult(int value) { //used in execute step
		result = value;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int value) { //sets count to maximum clock cycles set by the user according to operation (checked in Main)
		count = value;
	}
	public void decrementCount() { //decreases count every time executeMethod is entered until 0 is reached so can execute
		count--;
	}
	
	public String getOneInstruction() {
		String value = getIteration() + " | " + instruction + " | " + destinationRegister + " | " + j + " | " + k + " | ";
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
