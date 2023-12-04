package Process;

import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;
import Memory.*;

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
	Memory mainMemory;
	Queue<String> fetch = new LinkedList<String>();
	Queue<String> issue = new LinkedList<String>();
	Queue<String> execute = new LinkedList<String>();
	Queue<String> write = new LinkedList<String>();
	int[] insts;
	int[] result;
	ReservationStations reservationStations;
	RegisterFile registerFile;
	InstructionsTable instructionsTable;
	
	public Main() {
		clock = 0;
		line = 0;
		mainMemory = new Memory("Instructions");
		instructionsTable = new InstructionsTable(mainMemory.getOperations().length);
		insts = new int[mainMemory.getOperations().length];
		result = new int[mainMemory.getOperations().length];
		//reservationStations = new ReservationStations(mulspace,addspace,loadspace,storespace,mulcount,addcount,loadcount,storecount);
		reservationStations = new ReservationStations();
		registerFile = new RegisterFile();
		loadInstructions();
	}
	
	public void run() {
		instructionsTable.setInstructions(mainMemory.getOperations());
		while(!fetch.isEmpty() || !issue.isEmpty() || !execute.isEmpty() || !write.isEmpty() || clock == 0) {
			System.out.println("******************* Clock Cycle: " + clock + " *******************");
			if(line < mainMemory.getOperations().length) {
				if(mainMemory.getLabel(line) == null)
					fetch.add((String)mainMemory.getOperationsWithLocation(line) + " " + line);
				else
					instructionsTable.incrementIteration(line);
				line++;
			}
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
				if(!isOccupied()) {
					issue.add(fetch.remove());
				}
			}
			mainMemory.toString();
			reservationStations.toString();
			registerFile.toString();
			instructionsTable.toString();
			clock++;
			//System.out.println("******************************************************");
		}
	}
	
	public void loadInstructions() {
		for(int i=0;i<mainMemory.getOperations().length;i++) {
			if(mainMemory.getOperationsWithLocation(i).startsWith("MUL") || mainMemory.getOperationsWithLocation(i).startsWith("DIV")){
				instructionsTable.setDestinationRegister(i, "F"+mainMemory.getRegister1(i));
				instructionsTable.setJ(i, "F"+mainMemory.getRegister2(i));
				instructionsTable.setK(i, "F"+mainMemory.getRegister3(i));
			} else if(mainMemory.getOperationsWithLocation(i).startsWith("ADD") || mainMemory.getOperationsWithLocation(i).startsWith("SUB")) {
				if(mainMemory.getOperationsWithLocation(i).startsWith("ADDI") || mainMemory.getOperationsWithLocation(i).startsWith("SUBI")) {
					instructionsTable.setDestinationRegister(i, "R"+mainMemory.getRegister1(i));
					instructionsTable.setJ(i, "R"+mainMemory.getRegister2(i));
					instructionsTable.setK(i, ""+mainMemory.getImm(i));
				} else if(mainMemory.getOperationsWithLocation(i).startsWith("ADD")) {
					instructionsTable.setDestinationRegister(i, "F"+mainMemory.getRegister1(i));
					instructionsTable.setJ(i, "F"+mainMemory.getRegister2(i));
					instructionsTable.setK(i, "F"+mainMemory.getRegister3(i));
				} else {
					instructionsTable.setDestinationRegister(i, "F"+mainMemory.getRegister1(i));
					instructionsTable.setJ(i, "F"+mainMemory.getRegister3(i));
					instructionsTable.setK(i, "F"+mainMemory.getRegister2(i));
				}
			} else if(mainMemory.getOperationsWithLocation(i).startsWith("L")) {
				instructionsTable.setDestinationRegister(i, "F"+mainMemory.getDestination(i));
				instructionsTable.setJ(i, ""+mainMemory.getAddressposition(i));
			} else if(mainMemory.getOperationsWithLocation(i).startsWith("S")) {
				instructionsTable.setDestinationRegister(i, "F"+mainMemory.getDestination(i));
				instructionsTable.setJ(i, ""+mainMemory.getAddressposition(i));
			}
			insts[i] = -1;
		}
	}
	
	public boolean isOccupied() {
		String find = fetch.peek();
		if(find.startsWith("MUL") || find.startsWith("DIV")){
			return reservationStations.isOccupiedMul();
		} else if(find.startsWith("ADD") || find.startsWith("SUB")) {
			return reservationStations.isOccupiedAdd();
		} else if(find.startsWith("L")) {
			return reservationStations.isOccupiedLoad();
		} else if(find.startsWith("S")) {
			return reservationStations.isOccupiedStore();
		}
		return true;
	}
	
	public boolean isWaiting(String find) {
		int index = Integer.parseInt(find.split(" ")[1]);
		if(find.startsWith("MUL") || find.startsWith("DIV")){
			if(reservationStations.getQjmul(index).equals("0") && reservationStations.getQkmul(index).equals("0")) {
				return false;
			} else
				return true;
		} else if(find.startsWith("ADD") || find.startsWith("SUB")) {
			if(reservationStations.getQjadd(index).equals("0") && reservationStations.getQkadd(index).equals("0")) {
				return false;
			} else
				return true;
		} else if(find.startsWith("S")) {
			if(reservationStations.getQstore(index).equals("0")) {
				return false;
			} else
				return true;
		} else if(find.startsWith("L")) {
			
		}
		return false;
	}
	
	public void issueMethod() {
		String operation = (String)issue.peek().split(" ")[0];
		int issued = Integer.parseInt((String)issue.peek().split(" ")[1]);
		if(operation.startsWith("MUL") || operation.startsWith("DIV")){
			String value1 = registerFile.getQ(mainMemory.getRegister1(issued));
			String value2 = registerFile.getQ(mainMemory.getRegister2(issued));
			String value3 = registerFile.getQ(mainMemory.getRegister3(issued));
			String reg2 = "";
			String reg3 = "";
			if(value1.equals("0")) {
				registerFile.setQusingTag(mainMemory.getRegister1(issued), reservationStations.searchRegister(mainMemory.getRegister1(issued)));
			}
			if(value2.equals("0")) {
				reg2 = "F"+mainMemory.getRegister2(issued);
			}
			if(value3.equals("0")) {
				reg3 = "F"+mainMemory.getRegister3(issued);
			}
			reservationStations.setOccupiedMul(operation,reg2,reg3,value2,value3,-1,issued);
			insts[issued] = reservationStations.getMaxmul();
		} else if(operation.startsWith("ADD") || operation.startsWith("SUB")) {
			if(operation.startsWith("ADDI") || operation.startsWith("SUBI")) {
				String value1 = registerFile.getQ(mainMemory.getRegister1(issued)+32);
				String value2 = registerFile.getQ(mainMemory.getRegister2(issued)+32);
				String value3 = ""+mainMemory.getImm(issued);
				String reg2 = "";
				if(value1.equals("0")) {
					registerFile.setQusingTag(mainMemory.getRegister1(issued), reservationStations.searchRegister(mainMemory.getRegister1(issued)));
				}
				if(value2.equals("0")) {
					reg2 = "R"+(mainMemory.getRegister2(issued));
				}
				reservationStations.setOccupiedAdd(operation,reg2,value3,value2,"0",-1,issued);
				if(operation.startsWith("ADDI"))
					insts[issued] = 1;
				else
					insts[issued] = reservationStations.getMaxadd();
			} else {
				String value1 = registerFile.getQ(mainMemory.getRegister1(issued));
				String value2 = registerFile.getQ(mainMemory.getRegister2(issued));
				String value3 = registerFile.getQ(mainMemory.getRegister3(issued));
				String reg2 = "";
				String reg3 = "";
				if(value1.equals("0")) {
					registerFile.setQusingTag(mainMemory.getRegister1(issued), reservationStations.searchRegister(mainMemory.getRegister1(issued)));
				}
				if(value2.equals("0")) {
					reg2 = "F"+mainMemory.getRegister2(issued);
				}
				if(value3.equals("0")) {
					reg3 = "F"+mainMemory.getRegister3(issued);
				}
				reservationStations.setOccupiedAdd(operation,reg2,reg3,value2,value3,-1,issued);
				insts[issued] = reservationStations.getMaxadd();
			}
		} else if(operation.startsWith("L")) {
			String value = registerFile.getQ(mainMemory.getDestination(issued));
			if(value.equals("0")) {
				registerFile.setQusingTag(mainMemory.getDestination(issued), reservationStations.searchRegister(mainMemory.getDestination(issued)));
			}
			reservationStations.setOccupiedLoad(mainMemory.getAddressposition(issued),issued);
			insts[issued] = reservationStations.getMaxload();
		} else if(operation.startsWith("S")) {
			String value = registerFile.getQ(mainMemory.getDestination(issued));
			String reg = "";
			if(value.equals("0")) {
				registerFile.setQusingTag(mainMemory.getDestination(issued), reservationStations.searchRegister(mainMemory.getDestination(issued)));
				reg = "F"+mainMemory.getDestination(issued);
			}
			reservationStations.setOccupiedStore(mainMemory.getAddressposition(issued),reg,value, issued);
			insts[issued] = reservationStations.getMaxstore();
		} else if(operation.startsWith("BENZ")){
			insts[issued] = 1;
		}
		instructionsTable.setIssue(issued, clock);
		execute.add(issue.remove());
	}
	
	public void executeMethod() {
		for(String value: execute) {
			if(!isWaiting(value)) {
				int executed = Integer.parseInt(value.split(" ")[1]);
				String operation = value.split(" ")[0];
				if(operation.startsWith("MUL") || operation.startsWith("DIV")){
					if(insts[executed]==reservationStations.getMaxmul())
						instructionsTable.setExecutionStart(executed, clock);
				} else if(operation.startsWith("ADD") || operation.startsWith("SUB")) {
					if(insts[executed]==reservationStations.getMaxadd())
						instructionsTable.setExecutionStart(executed, clock);
					else if(operation.startsWith("ADDI") && insts[executed]==1)
						instructionsTable.setExecutionStart(executed, clock);
				} else if(operation.startsWith("L")) {
					if(insts[executed]==reservationStations.getMaxload())
						instructionsTable.setExecutionStart(executed, clock);
				} else if(operation.startsWith("S")) {
					if(insts[executed]==reservationStations.getMaxstore())
						instructionsTable.setExecutionStart(executed, clock);
				} else if(operation.startsWith("BNEZ")) {
					if(insts[executed]==1)
						instructionsTable.setExecutionStart(executed, clock);
				}
				if(insts[executed]==0) {
					if(operation.startsWith("MUL")) {
						result[executed] = registerFile.getContent(mainMemory.getRegister2(executed)) * registerFile.getContent(mainMemory.getRegister3(executed));
					} else if(operation.startsWith("DIV")){
						result[executed] = registerFile.getContent(mainMemory.getRegister2(executed)) / registerFile.getContent(mainMemory.getRegister3(executed));
					} else if(operation.startsWith("ADDI")) {
						result[executed] = registerFile.getContent(mainMemory.getRegister2(executed)+32) + mainMemory.getImm(executed);
					} else if(operation.startsWith("SUBI")) {
						result[executed] = registerFile.getContent(mainMemory.getRegister2(executed)+32) - mainMemory.getImm(executed);
					} else if(operation.startsWith("ADD")) {
						result[executed] = registerFile.getContent(mainMemory.getRegister2(executed)) + registerFile.getContent(mainMemory.getRegister3(executed));
					} else if(operation.startsWith("SUB")) {
						result[executed] = registerFile.getContent(mainMemory.getRegister3(executed)) - registerFile.getContent(mainMemory.getRegister3(executed));
					} else if(operation.startsWith("L")) {
						result[executed] = (int)mainMemory.getMemoryWithLocation(reservationStations.getAddressload(executed));
					} else if(operation.startsWith("S")) {
						result[executed] = registerFile.getContentWithTag(reservationStations.getVstore(executed));
					} else if(operation.startsWith("BNEZ")) {
						line = 0;
					}
					instructionsTable.setExecutionComplete(executed, clock);
					insts[executed]=-2;
				} else {
					if(insts[executed]!=0) {
						insts[executed]--;
					}
				}
			}
		}
		for(int i=0;i<insts.length;i++) {
			if(insts[i]==-2) {
				execute.remove(mainMemory.getOperationsWithLocation(i) + " " + i);
				write.add(mainMemory.getOperationsWithLocation(i) + " " + i);
				insts[i]=-1;
			}
		}
	}
	
	public void writeMethod() { //FIFO
		int written = Integer.parseInt((String)write.peek().split(" ")[1]);
		String operation = (String)write.peek().split(" ")[0];
		if(operation.startsWith("MUL") || operation.startsWith("DIV")){
			if(registerFile.getQ(written).equals("M" + reservationStations.getTagUsingLinemul(written)) || registerFile.getQ(written).equals("0")) {
				registerFile.setContentusingTag(mainMemory.getRegister1(written), result[written]);
				reservationStations.writeWaiting(registerFile.getQ(mainMemory.getRegister1(written)), mainMemory.getRegister1(written));
				reservationStations.setAvailableMul(written);
			}
		} else if(operation.startsWith("ADD") || operation.startsWith("SUB")) {
			if(operation.startsWith("ADDI") || operation.startsWith("SUBI")) {
				if(registerFile.getQ(written).equals("A" + reservationStations.getTagUsingLineadd(written)) || registerFile.getQ(written).equals("0")) {
					registerFile.setContentusingTag(mainMemory.getRegister1(written)+32, result[written]);
					reservationStations.writeWaiting(registerFile.getQ(mainMemory.getRegister1(written)), mainMemory.getRegister1(written));
					reservationStations.setAvailableAdd(written);
				}
			} else {
				if(registerFile.getQ(written).equals("A" + reservationStations.getTagUsingLineadd(written)) || registerFile.getQ(written).equals("0")) {
					registerFile.setContentusingTag(mainMemory.getRegister1(written), result[written]);
					reservationStations.writeWaiting(registerFile.getQ(mainMemory.getRegister1(written)), mainMemory.getRegister1(written));
					reservationStations.setAvailableAdd(written);
				}
			}
		} else if(operation.startsWith("L")) {
			if(registerFile.getQ(written).equals("L" + reservationStations.getTagUsingLineload(written)) || registerFile.getQ(written).equals("0")) {
				registerFile.setContentusingTag(mainMemory.getDestination(written), result[written]);
				reservationStations.writeWaiting(registerFile.getQ(mainMemory.getDestination(written)), mainMemory.getDestination(written));
				reservationStations.setAvailableLoad(written);
			}
		} else if(operation.startsWith("S")) {
			mainMemory.setMemory(reservationStations.getAddressstore(written),result[written]);
			reservationStations.setAvailableStore(written);
			
		}
		instructionsTable.setWriteResult(written, clock);
		write.remove();
	}
	
	public static void main(String[] args) {
//		Scanner obj = new Scanner(System.in);
//		System.out.println("Enter size of mul reservation station");
//		mulspace = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of add reservation station");
//		addspace = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of load reservation station");
//		loadspace = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of store reservation station");
//		storespace = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of mul cycles");
//		mulcount = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of add cycles");
//		addcount = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of load cycles");
//		loadcount = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of store cycles");
//		storecount= Integer.parseInt(obj.nextLine());
//		System.out.println(mulspace + " " + addspace + " " + loadspace + " " + storespace);
//		System.out.println(mulcount + " " + addcount + " " + loadcount + " " + storecount);
//		obj.close();
		Main main = new Main();
		main.run();
	}
}
