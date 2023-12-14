package Memory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Memory {
	int memorySize = 500;
	Object[] Memory = new Object[memorySize];
	int[] changed = new int[memorySize];
	Object[] read = new Object[100];
	String[] operations;
	int[] destination;
	int[] addressposition;
	int[] register2;
	int[] register3;
	String[] registerName;
	int[] imm;
	int[] jump;
	String[] label;
	String[] branch;
	public static int count = 0;

	public Memory(String fileName) {
		try {
			//reads text file
			FileReader file = new FileReader(fileName);
			BufferedReader myReader = new BufferedReader(file);
			String line;

			while ((line = myReader.readLine()) != null) {
				read[count] = line;
				count++;
			}
			//both at the end
			generateMemory();
			setOperations();
			
			file.close();
			myReader.close();

		} catch (IOException e) {
			System.out.println("An error occurred.");
		}
	}
	
	//getters for GUI
	public int[] getChanged() {
		return changed;
	}
	public Object[] getMemory() {
		return Memory;
	}
	public String[] getOperations() {
		return operations;
	}
	
	//getters and setters for Main
	
	public Object getMemoryWithLocation(int i) { //used in execution for load ONLY
		return Memory[i];
	}
	public void setMemory(int x, Object y) { //used in write for store ONLY
		Memory[x] = y;
	}
	
	public Object getReadWithLocation(int i) { //not used
		return read[i];
	}
	public void setRead(int x, Object y) { //not used
		read[x] = y;
	}
	
	//getters for values taken from text file to help in Main
	
	public int getDestination(int n) {
		return destination[n]; //destination register
	}
	public int getAddressposition(int n) {
		return addressposition[n]; //address for load and store
	}
	public int getRegister2(int n) {
		return register2[n]; //source 1 for mul/div/add/sub
	}
	public int getRegister3(int n) {
		return register3[n]; //source 2 for mul/div/add/sub (not addi or subi)
	}
	public String getRegisterName(int n) {
		return registerName[n]; //if register is F or R
	}
	public int getImm(int n) {
		return imm[n]; //source 2 for addi and subi
	}
	public int getJump(int n) {
		return jump[n]; //register in BNEZ
	}
	public String getBranch(int n) {
		return branch[n]; //branch name (label) in BNEZ
	}
	//label that is written in a line alone in text file
	public boolean getLabel(int n) { //checks if line in text file is a label
		if(label[n]==null)
			return false;
		return true;
	}
	public int getLabelWithString(String branch) { //returns line of label in text file that BNEZ should branch to
		for(int i=0;i<label.length;i++) {
			if(label[i]==null)
				continue;
			else if(label[i].equals(branch))
				return i;
		}
		return -1;
	}
	public String getOperationsWithLocation(int i) { //used in Main to get operation in loadInstruction using line in text file
		return operations[i];
	}
	public void generateMemory() { //all values in memory are set to 5
		for(int i = 0; i < memorySize; i++) {
			Memory[i] = 5;
		}
	}
	public void setChanged(int index) {
		changed[index] = 1; //if value is accessed because of a load or a store, changed is set to 1 to only print those
	}
	public void setOperations() { //sets all arrays to values from the read array that stores data from text file
		operations = new String[count];
		destination = new int[count];
		addressposition = new int[count];
		register2 = new int[count];
		register3 = new int[count];
		registerName = new String[count];
		imm = new int[count];
		jump = new int[count];
		label = new String[count];
		branch = new String[count];
		for(int i=0;i<count;i++) {
			destination[i] = -1;
			addressposition[i] = -1;
			register2[i] = -1;
			register3[i] = -1;
			imm[i] = -1;
			jump[i] = -1;
			registerName[i] = "";
		}
		for(int i=0;i<count;i++) {
			operations[i] = (((String)read[i]).split(" "))[0];
			if(operations[i].startsWith("L.") || operations[i].startsWith("S.")) {
				String result1;
				String result2;
				result1 = (((String)read[i]).split(" "))[1];
				result2 = (((String)read[i]).split(","))[1];
				for(int j = 0;j<32;j++) {
					if(result1.contains("F"+j)) {
						destination[i] = j;
						registerName[i] = "F";
					}
					if(result1.contains("R"+j)) {
						destination[i] = j;
						registerName[i] = "R";
					}
				}
				addressposition[i] = Integer.parseInt(result2);
			} else if(operations[i].equals("ADDI") || operations[i].equals("SUBI")) {
				String result1;
				String result2;
				String result3;
				result1 = ((((String)read[i]).split(" "))[1]).split(",")[0];
				result2 = (((String)read[i]).split(","))[1];
				result3 = (((String)read[i]).split(","))[2];
				for(int j = 0;j<32;j++) {
					if(result1.equals("R"+j)) {
						destination[i] = j;
						registerName[i] = "R";
					}
					if(result2.equals("R"+j))
						register2[i] = j;
				}
				imm[i]=Integer.parseInt(result3);
			} else if(operations[i].contains("ADD") || operations[i].contains("SUB") || operations[i].contains("MUL") || operations[i].contains("DIV")) {
				String result1;
				String result2;
				String result3;
				result1 = ((((String)read[i]).split(" "))[1]).split(",")[0];
				result2 = (((String)read[i]).split(","))[1];
				result3 = (((String)read[i]).split(","))[2];
				for(int j = 0;j<32;j++) {
					if(result1.equals("F"+j)) {
						destination[i] = j;
						registerName[i] = "F";
					}
					if(result2.equals("F"+j))
						register2[i] = j;
					if(result3.equals("F"+j))
						register3[i] = j;
					if(result1.equals("R"+j)) {
						destination[i] = j;
						registerName[i] = "R";
					}
					if(result2.equals("R"+j))
						register2[i] = j;
					if(result3.equals("R"+j))
						register3[i] = j;
				}
			} else if(operations[i].equals("BNEZ")) {
				String result;
				result = ((((String)read[i]).split(" "))[1]).split(",")[0];
				for(int j = 0;j<32;j++) {
					if(result.equals("F"+j)) {
						jump[i] = j;
						registerName[i] = "F";
					}
					if(result.equals("R"+j)) {
						jump[i] = j;
						registerName[i] = "R";
					}
				}
				branch[i] = (((String)read[i]).split(","))[1];
			} else {
				label[i] = (String)read[i];
			}
		}
		for(int i=0;i<addressposition.length;i++) {
			if(addressposition[i]!=-1)
				setChanged(addressposition[i]); //if address is used by load or store, to be printed
		}
	}

	public String toString() {
		System.out.println("------------------------Memory------------------------");
		for(int i=0;i<Memory.length;i++) {
			if(changed[i]==1)
				System.out.println(i + ": " + Memory[i]);
//			else
//				System.out.println(i + ": " + Memory[i] + "   not changed");
		}
//		System.out.println("---------------------Instructions---------------------");
//		for(int i=0;i<read.length;i++) {
//			if(read[i]!=null)
//				System.out.println(i + ": " + read[i]);
//		}
//		System.out.println("---------------------Operations---------------------");
//		for(int i=0;i<operations.length;i++) {
//			System.out.println(i + ": " + operations[i]);
//		}
//		System.out.println("------------------Effective Address------------------");
//		for(int i=0;i<addressposition.length;i++) {
//			if(addressposition[i]!=-1)
//				System.out.println(i + ": " + addressposition[i]);
//		}
//		System.out.println("---------------------Destination---------------------");
//		for(int i=0;i<destination.length;i++) {
//			if(destination[i]!=-1)
//				System.out.println(i + ": " + destination[i]);
//		}
//		System.out.println("---------------------Register 2---------------------");
//		for(int i=0;i<register2.length;i++) {
//			if(register2[i]!=-1)
//				System.out.println(i + ": " + register2[i]);
//		}
//		System.out.println("---------------------Register 3---------------------");
//		for(int i=0;i<register3.length;i++) {
//			if(register3[i]!=-1)
//				System.out.println(i + ": " + register3[i]);
//		}
//		System.out.println("---------------------Immediate---------------------");
//		for(int i=0;i<imm.length;i++) {
//			if(imm[i]!=-1)
//				System.out.println(i + ": " + imm[i]);
//		}
//		System.out.println("------------------------Jump------------------------");
//		for(int i=0;i<jump.length;i++) {
//			if(jump[i]!=-1)
//				System.out.println(i + ": " + jump[i]);
//		}
//		System.out.println("-----------------------Branch-----------------------");
//		for(int i=0;i<branch.length;i++) {
//			if(branch[i]!=null)
//				System.out.println(i + ": " + branch[i]);
//		}
//		System.out.println("-----------------------Label-----------------------");
//		for(int i=0;i<label.length;i++) {
//			if(label[i]!=null)
//				System.out.println(i + ": " + label[i]);
//		}
		return "";
	}
}