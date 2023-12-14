package Process;

import java.util.Scanner;
import Memory.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
	static int mulcount;
	static int addcount;
	static int loadcount;
	static int storecount;
	static int mulspace;
	static int addspace;
	static int loadspace;
	static int storespace;
	int clock;
	int line;
	int iterations;
	Memory mainMemory;
	String fetchresult = "";
	String issueresult = "";
	String executeresult = "";
	String writeresult = "";
	ArrayList<Instruction> instructionTable = new ArrayList<>();
	Queue<String> fetch = new LinkedList<String>();
	Queue<String> issue = new LinkedList<String>();
	Queue<String> execute = new LinkedList<String>();
	Queue<String> write = new LinkedList<String>();
	ReservationStations reservationStations;
	RegisterFile registerFile;
	boolean stopFetching;
	
	public Main(int[] input) {
		setInput(input);
		clock = 0;
		line = 0;
		iterations = 0;
		mainMemory = new Memory("Instructions");
		reservationStations = new ReservationStations(mulspace,addspace,loadspace,storespace);
		registerFile = new RegisterFile();
		stopFetching = false;
	}

	public void setInput(int[] values) { //takes all 8 from the user and sets to variables, used here and in GUI
		mulspace = values[0];
		addspace = values[1];
		loadspace = values[2];
		storespace = values[3];
		mulcount = values[4];
		addcount = values[5];
		loadcount = values[6];
		storecount = values[7];
	}
	
	//getters for GUI
	public String getFetchQueue() {
		return fetchresult;
	}
	public String getIssueQueue() {
		return issueresult;
	}
	public String getExecuteQueue() {
		return executeresult;
	}
	public String getWriteQueue() {
		return writeresult;
	}
	public ArrayList<Instruction> getInstructionTable() {
		return instructionTable;
	}
	public Memory getMainMemory() {
		return mainMemory;
	}
	public int getClock() {
		return clock;
	}
	public ReservationStations getReservationStations() {
		return reservationStations;
	}
	public RegisterFile getRegisterFile() {
		return registerFile;
	}
	
	public void run() { //called in Main
		while(runOne()) {
			System.out.println("************************************************************");
		}
	}
	
	public boolean runOne() { //called in GUI with every "NEXT" button click, and here in run method inside a while loop
		if(clock==1000) { //to not have an infinite loop
			System.out.println("Timed out");
			return false;
		}
		if(!fetch.isEmpty() || !issue.isEmpty() || !execute.isEmpty() || !write.isEmpty() || clock == 0) {
			System.out.println("********************** Clock Cycle: " + clock + " **********************");
			if(line < mainMemory.getOperations().length) { //checks length of text file
				if(!mainMemory.getLabel(line) && !stopFetching) { //if line contains an instruction and is not waiting for a branch result
					fetchMethod();
				} else if(mainMemory.getLabel(line)) { //if label in text file
					iterations++; //every time label is read
					loadInstruction(line); //but do not add to fetch as it does not enter Tomasulo architecture
					line++; //go to next line to not waste fetching in a cycle
					fetchMethod();
				}
			}
			//used to print queues here and in GUI
			fetchresult = "";
			if(!fetch.isEmpty()) {
				for(String value: fetch) {
					fetchresult+= value.split(" ")[1] + " ";
				}
			}
			issueresult = "";
			if(!issue.isEmpty()) {
				for(String value: issue) {
					issueresult+= value.split(" ")[1] + " ";
				}
			}
			executeresult = "";
			if(!execute.isEmpty()) {
				for(String value: execute) {
					executeresult+= value.split(" ")[1] + " ";
				}
			}
			writeresult = "";
			if(!write.isEmpty()) {
				for(String value: write) {
					writeresult+= value.split(" ")[1] + " ";
				}
			}
			printQueues(); //at the end
			
			//reversely put so all instructions have a clock cycle between each step and the next
			if(!write.isEmpty() && clock>=3) {
				writeMethod();
			}
			if(!execute.isEmpty() && clock>=2) {
				executeMethod();
			}
			if(!issue.isEmpty() && clock>=1) {
				issueMethod();
			}
			if(!fetch.isEmpty()) {
				isOccupied();
			}
			
			print(); //at the end
			clock++;
			return true; //as long as there are still cycles left
		}
		clock-=clock;
		return false; //when all instructions are done
	}

	public int loadInstruction(int line) { //for all text file lines
		Instruction instruction = new Instruction();
		String op = mainMemory.getOperationsWithLocation(line);
		instruction.setIteration(iterations);
		instruction.setInstruction(op);
		if(op.startsWith("ADDI") || op.startsWith("SUBI")) {
			instruction.setDestinationRegister(mainMemory.getRegisterName(line)+mainMemory.getDestination(line));
			instruction.setJ(mainMemory.getRegisterName(line)+mainMemory.getRegister2(line));
			instruction.setK(""+mainMemory.getImm(line));
		} else if(op.startsWith("BNEZ")) {
			instruction.setDestinationRegister(mainMemory.getRegisterName(line)+mainMemory.getJump(line));
			instruction.setJ(mainMemory.getBranch(line));
		} else if(op.contains("MUL") || op.contains("DIV") || op.contains("ADD") || op.contains("SUB") || op.startsWith("L") || op.startsWith("S")){
			instruction.setDestinationRegister(mainMemory.getRegisterName(line)+mainMemory.getDestination(line));
			if(op.contains("MUL") || op.contains("DIV") || op.contains("ADD") || op.contains("SUB")) {
				instruction.setJ(mainMemory.getRegisterName(line)+mainMemory.getRegister2(line));
				instruction.setK(mainMemory.getRegisterName(line)+mainMemory.getRegister3(line));
			} else if(op.startsWith("L") || op.startsWith("S"))
				instruction.setJ(""+mainMemory.getAddressposition(line));
		}
		instructionTable.add(instruction); //add to arraylist
		return instructionTable.lastIndexOf(instruction); //value in arraylist, not the same as text file line, differs for loops
	}

	public void isOccupied() { //checks if reservation station of operation is full
		if(!reservationStations.isOccupied(fetch.peek()))
			issue.add(fetch.remove());
	}
	public boolean isWaiting(String find) { //checks if instruction is waiting for values or can start executing
		return reservationStations.isWaiting(Integer.parseInt(find.split(" ")[2]));
	}
	public int getInsts(String operation) { //returns maximum clock cycle value for each operation
		if(operation.startsWith("ADDI") || operation.startsWith("SUBI") || operation.startsWith("BNEZ"))
			return 0;
		else if(operation.contains("MUL") || operation.contains("DIV"))
			return mulcount - 1;
		else if(operation.contains("ADD") || operation.contains("SUB"))
			return addcount - 1;
		else if(operation.startsWith("L"))
			return loadcount - 1;
		else if(operation.startsWith("S"))
			return storecount - 1;
		return -1;
	}
	
	public void execution(int executing, int index, String operation) { //used in executeMethod when count reaches 0
		Instruction i = instructionTable.get(index);
		if(operation.contains("MUL")) {
			i.setResult(reservationStations.getVjmul(index) * reservationStations.getVkmul(index));
		} else if(operation.contains("DIV")){
			i.setResult(reservationStations.getVjmul(index) / reservationStations.getVkmul(index));
		} else if(operation.contains("ADD")) {
			i.setResult(reservationStations.getVjadd(index) + reservationStations.getVkadd(index));
		} else if(operation.contains("SUB")) {
			i.setResult(reservationStations.getVjadd(index) - reservationStations.getVkadd(index));
		} else if(operation.startsWith("L")) {
			i.setResult((int)mainMemory.getMemoryWithLocation(reservationStations.getAddressload(index)));
		} else if(operation.startsWith("S")) {
			i.setResult(reservationStations.getVstore(index));
		} else if(operation.startsWith("BNEZ")) {
			if(reservationStations.getVjadd(index) != 0) {
				line = mainMemory.getLabelWithString(mainMemory.getBranch(executing)); //return to label line so we can loop
			} else {
				line++; //go to next line as we don't branch
				iterations = 0; //no loop
			}
			stopFetching = false; //contine fetching
		}
	}

	public void fetchMethod() {
		 //format in queue --> operation textFileLine instructionsTableIndex
		fetch.add((String)mainMemory.getOperationsWithLocation(line) + " " + line + " " +loadInstruction(line));
		if(((String)mainMemory.getOperationsWithLocation(line)).startsWith("BNEZ"))
			stopFetching = true;
		else
			line++; //go to next line in the next clock cycle
	}
	public void issueMethod() {
		String operation = (String)issue.peek().split(" ")[0];
		int issued = Integer.parseInt((String)issue.peek().split(" ")[1]); //textFileLine
		int index = Integer.parseInt((String)issue.peek().split(" ")[2]); //instructionsTableIndex
		Instruction i = instructionTable.get(index);
		String destinationRegister = i.getDestinationRegister();
		//check if register in register file is waiting for another instruction
		String value1 = registerFile.getQ(destinationRegister);
		String value2 = registerFile.getQ(i.getJ());
		String value3 = registerFile.getQ(i.getK());
		String reg2 = "";
		String reg3 = "";
		int address = mainMemory.getAddressposition(issued);
		//we shouldn't have used these as issuing is in order
		if(registerFile.getLine(i.getJ())>index)
			value2 = "0";
		if(registerFile.getLine(i.getK())>index)
			value3 = "0";
		
		if(value2.equals("0")) { //if not waiting for another instruction, source 1
			reg2 = ""+registerFile.getContent(i.getJ()); //store its content (not register name) into reservation station
		}
		if(value3.equals("0")) { //if not waiting for another instruction, source 2
			if(operation.startsWith("ADDI") || operation.startsWith("SUBI") || operation.startsWith("BNEZ"))
				reg3 = i.getK(); //immediate value for addi and subi, branch label for bnez
			else
				reg3 = ""+registerFile.getContent(i.getK()); //store its content (not register name) into reservation station
		}
		if(operation.startsWith("S.") || operation.startsWith("BNEZ")) { //store
			value2 = value1;
			if(value2.equals("0")) { //as register is not destination we only want to take a value from it not write to it
				reg2 = ""+registerFile.getContent(destinationRegister);
			}
		}
		i.setCount(getInsts(operation)); //set count to maximum clock cycles given by the user
		reservationStations.setOccupied(operation,reg2,reg3,value2,value3,address,index); //add to reservation station
		if(!operation.startsWith("S.") && !operation.startsWith("BNEZ")) { //the rest of the operations write to registers
			registerFile.setQ(destinationRegister, reservationStations.getTagUsingLine(index), index);
		}
		i.setIssue(clock);
		execute.add(issue.remove());
	}

	public void executeMethod() {
		for(String value: execute) { //everytime, we loop on all instructions inside the execute queue
			if(!isWaiting(value)) { //start execution count if not waiting for another instruction
				int executed = Integer.parseInt(value.split(" ")[1]); //textFileLine
				int index = Integer.parseInt((String)value.split(" ")[2]); //instructionsTableIndex
				Instruction i = instructionTable.get(index);
				String operation = value.split(" ")[0];
				if(i.getCount()==getInsts(operation)) //first execution clock cycle
					i.setExecutionStart(clock);
				if(i.getCount()==0) { //last execution clock cycle
					execution(executed, index, operation);
					if(operation.startsWith("BNEZ")) {
						if(!stopFetching) {
							i.setExecutionComplete(clock);
							write.add(value);
							i.decrementCount(); //set to -1 to clear
						} //else, still waiting for another instruction in order to use register so did not execute
					} else {
						i.setExecutionComplete(clock);
						write.add(value);
						i.decrementCount(); //set to -1 to clear
					}
				} else
					i.decrementCount(); //keep decrementing every clock cycle till it reaches 0 so we can start executing
			}
		}
		for(String value: write) { //remove instructions that finished execution
			if(execute.contains(value))
				execute.remove(value);
		}
	}

	public void writeMethod() {
		int index = Integer.parseInt((String)write.peek().split(" ")[2]); //instructionsTableIndex
		Instruction i = instructionTable.get(index);
		String operation = write.peek().split(" ")[0];
		String registerWrite = i.getDestinationRegister();

		if(operation.contains("MUL") || operation.contains("DIV") || operation.contains("ADD") || operation.contains("SUB") || operation.startsWith("L")){
			reservationStations.writeWaiting(reservationStations.getTagUsingLine(index), ""+i.getResult()); //writes result to all instructions waiting
			if(reservationStations.getTagUsingLine(index)==registerFile.getQ(registerWrite)) //if destination register in register file is waiting for this instruction
				registerFile.setContent(registerWrite, i.getResult());
		} else if(operation.startsWith("S")) {
			mainMemory.setMemory(reservationStations.getAddressstore(index),i.getResult());
		}
		reservationStations.setAvailable(index); //remove from reservation station
		i.setWriteResult(clock);
		write.remove();
	}
	
	public void printQueues() {
		if(!fetch.isEmpty()) {
			System.out.println("Fetch Queue: " + fetchresult);
		}
		if(!issue.isEmpty()) {
			System.out.println("Issue Queue: " + issueresult);
		}
		if(!execute.isEmpty()) {
			System.out.println("Execute Queue: " + executeresult);
		}
		if(!write.isEmpty()) {
			System.out.println("Write Queue: " + writeresult);
		}
	}
	public void print() {
		mainMemory.toString();
		reservationStations.toString();
		registerFile.toString();
		System.out.println("----------------------Instructions Table----------------------");
		System.out.println("Iteration# | Instruction |  j   |  k   | Issue | Execution Complete | Write Result");
		for(Instruction i : instructionTable) {
			i.getOneInstruction();
		}
	}

	public static void main(String[] args) {
		String[] inputValues = new String[8];
		Scanner obj = new Scanner(System.in);
		System.out.println("Enter size of mul reservation station");
		inputValues[0] = obj.nextLine();
		System.out.println("Enter size of add reservation station");
		inputValues[1] = obj.nextLine();
		System.out.println("Enter size of load reservation station");
		inputValues[2] = obj.nextLine();
		System.out.println("Enter size of store reservation station");
		inputValues[3] = obj.nextLine();
		System.out.println("Enter number of mul cycles");
		inputValues[4] = obj.nextLine();
		System.out.println("Enter number of add cycles");
		inputValues[5] = obj.nextLine();
		System.out.println("Enter number of load cycles");
		inputValues[6] = obj.nextLine();
		System.out.println("Enter number of store cycles");
		inputValues[7]= obj.nextLine();
		System.out.println(mulspace + " " + addspace + " " + loadspace + " " + storespace);
		System.out.println(mulcount + " " + addcount + " " + loadcount + " " + storecount);
		obj.close();
//		int[] inputvalues = {2,3,3,3,8,3,2,2};
		int[] inputvalues = new int[8];
		for (int i = 0; i < 8; i++) {
        	try {
        		inputvalues[i] = Integer.parseInt(inputValues[i]);
        	} catch(NumberFormatException e) {
        		inputvalues[i] = 3;
        	}	
        }
		Main main = new Main(inputvalues);
		main.run();
	}
}
