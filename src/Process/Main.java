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

	public void setInput(int[] values) {
		mulspace = values[0];
		addspace = values[1];
		loadspace = values[2];
		storespace = values[3];
		mulcount = values[4];
		addcount = values[5];
		loadcount = values[6];
		storecount = values[7];
	}
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
	
	public void run() {
		while(runOne()) {
			System.out.println("************************************************************");
		}
	}
	public boolean runOne() {
		if(clock==1000) {
			System.out.println("Timed out");
			return false;
		}
		if(!fetch.isEmpty() || !issue.isEmpty() || !execute.isEmpty() || !write.isEmpty() || clock == 0) {
			System.out.println("********************** Clock Cycle: " + clock + " **********************");
			if(line < mainMemory.getOperations().length) {
				if(!mainMemory.getLabel(line) && !stopFetching) {
					fetchMethod();
				} else if(mainMemory.getLabel(line)) {
					iterations++;
					loadInstruction(line);
					line++;
					fetchMethod();
				}
			}
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
			printQueues();
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
			print();
			clock++;
			return true;
		}
		clock-=clock;
		return false;
	}

	public int loadInstruction(int line) {
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
		instructionTable.add(instruction);
		return instructionTable.lastIndexOf(instruction);
	}

	public void isOccupied() {
		if(!reservationStations.isOccupied(fetch.peek()))
			issue.add(fetch.remove());
	}
	public boolean isWaiting(String find) {
		return reservationStations.isWaiting(Integer.parseInt(find.split(" ")[2]));
	}
	public int getInsts(String operation) {
		if(operation.startsWith("ADDI") || operation.startsWith("SUBI") || operation.startsWith("BNEZ"))
			return 1;
		else if(operation.contains("MUL") || operation.contains("DIV"))
			return mulcount;
		else if(operation.contains("ADD") || operation.contains("SUB"))
			return addcount;
		else if(operation.startsWith("L"))
			return loadcount;
		else if(operation.startsWith("S"))
			return storecount;
		return -1;
	}
	
	public void execution(int executing, int index, String operation) {
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
			if(registerFile.getQ(i.getDestinationRegister()).equals("0")) {
				if(registerFile.getContent(i.getDestinationRegister()) != 0) {
					line = mainMemory.getLabelWithString(mainMemory.getBranch(executing));
				} else {
					line++;
					iterations = 0;
				}
				stopFetching = false;
			}
		}
	}

	public void fetchMethod() {
		fetch.add((String)mainMemory.getOperationsWithLocation(line) + " " + line + " " +loadInstruction(line));
		if(((String)mainMemory.getOperationsWithLocation(line)).startsWith("BNEZ"))
			stopFetching = true;
		else
			line++;
	}
	public void issueMethod() {
		String operation = (String)issue.peek().split(" ")[0];
		int issued = Integer.parseInt((String)issue.peek().split(" ")[1]);
		int index = Integer.parseInt((String)issue.peek().split(" ")[2]);
		Instruction i = instructionTable.get(index);
		String destinationRegister = i.getDestinationRegister();
		String value1 = registerFile.getQ(destinationRegister);
		String value2 = registerFile.getQ(i.getJ());
		String value3 = registerFile.getQ(i.getK());
		String reg2 = "";
		String reg3 = "";
		int address = mainMemory.getAddressposition(issued);
		if(registerFile.getLine(i.getJ())>index)
			value2 = "0";
		if(registerFile.getLine(i.getK())>index)
			value3 = "0";
		if(value2.equals("0")) {
			reg2 = ""+registerFile.getContent(i.getJ());
		}
		if(value3.equals("0")) {
			if(operation.startsWith("ADDI") || operation.startsWith("SUBI") || operation.startsWith("BNEZ"))
				reg3 = i.getK();
			else
				reg3 = ""+registerFile.getContent(i.getK());
		}
		if(operation.startsWith("S.")) {
			value2 = value1;
			if(value2.equals("0")) {
				reg2 = ""+registerFile.getContent(destinationRegister);
			}
		}
		i.setCount(getInsts(operation));
		reservationStations.setOccupied(operation,reg2,reg3,value2,value3,address,index);
		if(!operation.startsWith("S.") && !operation.startsWith("BNEZ")) {
			registerFile.setQ(destinationRegister, reservationStations.getTagUsingLine(index), index);
		}
		i.setIssue(clock);
		execute.add(issue.remove());
	}

	public void executeMethod() {
		for(String value: execute) {
			if(!isWaiting(value)) {
				int executed = Integer.parseInt(value.split(" ")[1]);
				int index = Integer.parseInt((String)value.split(" ")[2]);
				Instruction i = instructionTable.get(index);
				String operation = value.split(" ")[0];
				if(i.getCount()==getInsts(operation))
					i.setExecutionStart(clock);
				if(i.getCount()==0) {
					execution(executed, index, operation);
					if(operation.startsWith("BNEZ")) {
						if(!stopFetching) {
							i.setExecutionComplete(clock);
							write.add(value);
							i.decrementCount();
						}
					} else {
						i.setExecutionComplete(clock);
						write.add(value);
						i.decrementCount();
					}
				} else
					i.decrementCount();
			}
		}
		for(String value: write) {
			if(execute.contains(value))
				execute.remove(value);
		}
	}

	public void writeMethod() {
		int index = Integer.parseInt((String)write.peek().split(" ")[2]);
		Instruction i = instructionTable.get(index);
		String operation = write.peek().split(" ")[0];
		String registerWrite = i.getDestinationRegister();

		if(operation.contains("MUL") || operation.contains("DIV") || operation.contains("ADD") || operation.contains("SUB") || operation.startsWith("L")){
			reservationStations.writeWaiting(reservationStations.getTagUsingLine(index), ""+i.getResult());
			if(reservationStations.getTagUsingLine(index)==registerFile.getQ(registerWrite))
				registerFile.setContent(registerWrite, i.getResult());
		} else if(operation.startsWith("S")) {
			mainMemory.setMemory(reservationStations.getAddressstore(index),i.getResult());
		}
		reservationStations.setAvailable(index);
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
