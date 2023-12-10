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
	int[] imm;
	int[] jump;
	String[] label;
	String[] branch;
	public static int count = 0;

	public Memory(String fileName) {
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader myReader = new BufferedReader(file);
			String line;

			while ((line = myReader.readLine()) != null) {
				read[count] = line;
				count++;
			}
			generateMemory();
			setOperations();
			file.close();
			myReader.close();

		} catch (IOException e) {
			System.out.println("An error occurred.");
		}
	}
	
	public int[] getChanged() {
		return changed;
	}
	public Object[] getMemory() {
		return Memory;
	}
	public String[] getOperations() {
		return operations;
	}
	
	public Object getMemoryWithLocation(int i) {
		return Memory[i];
	}
	public void setMemory(int x, Object y) {
		Memory[x] = y;
	}
	public Object getReadWithLocation(int i) {
		return read[i];
	}
	public void setRead(int x, Object y) {
		read[x] = y;
	}
	public int getDestination(int n) {
		return destination[n];
	}
	public int getAddressposition(int n) {
		return addressposition[n];
	}
	public int getRegister2(int n) {
		return register2[n];
	}
	public int getRegister3(int n) {
		return register3[n];
	}
	public int getImm(int n) {
		return imm[n];
	}
	public int getJump(int n) {
		return jump[n];
	}
	public String getBranch(int n) {
		return branch[n];
	}
	public boolean getLabel(int n) {
		if(label[n]==null)
			return false;
		return true;
	}
	public int getLabelWithString(String branch) {
		for(int i=0;i<label.length;i++) {
			if(label[i]==null)
				continue;
			else if(label[i].equals(branch))
				return i;
		}
		return -1;
	}
	public String getOperationsWithLocation(int i) {
		return operations[i];
	}
	public void generateMemory() {
		for(int i = 0; i < memorySize; i++) {
			Memory[i] = 5;
		}
	}
	public void setChanged(int index) {
		changed[index] = 1;
	}
	public void setOperations() {
		operations = new String[count];
		destination = new int[count];
		addressposition = new int[count];
		register2 = new int[count];
		register3 = new int[count];
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
		}
		for(int i=0;i<count;i++) {
			operations[i] = (((String)read[i]).split(" "))[0];
			if(operations[i].equals("L.D") || operations[i].equals("S.D")) {
				String result1;
				String result2;
				result1 = (((String)read[i]).split(" "))[1];
				result2 = (((String)read[i]).split(","))[1];
				//System.out.println("load/store: " + result1 + " " + result2);
				for(int j = 0;j<32;j++) {
					if(result1.contains("F"+j)) {
						destination[i] = j;
					}
				}
				addressposition[i] = Integer.parseInt(result2);
			} else if(operations[i].equals("ADD.D") || operations[i].equals("SUB.D") || operations[i].equals("MUL.D") || operations[i].equals("DIV.D")){
				String result1;
				String result2;
				String result3;
				result1 = ((((String)read[i]).split(" "))[1]).split(",")[0];
				result2 = (((String)read[i]).split(","))[1];
				result3 = (((String)read[i]).split(","))[2];
				//System.out.println("floating point: " + result1 + " " + result2 + " " + result3);
				for(int j = 0;j<32;j++) {
					if(result1.equals("F"+j))
						destination[i] = j;
					if(result2.equals("F"+j))
						register2[i] = j;
					if(result3.equals("F"+j))
						register3[i] = j;
				}
			} else if(operations[i].equals("ADDI") || operations[i].equals("SUBI")) {
				String result1;
				String result2;
				String result3;
				result1 = ((((String)read[i]).split(" "))[1]).split(",")[0];
				result2 = (((String)read[i]).split(","))[1];
				result3 = (((String)read[i]).split(","))[2];
				//System.out.println("integer: " + result1 + " " + result2 + " " + result3);
				for(int j = 0;j<32;j++) {
					if(result1.equals("R"+j))
						destination[i] = j;
					if(result2.equals("R"+j))
						register2[i] = j;
				}
				imm[i]=Integer.parseInt(result3);
			} else if(operations[i].equals("BNEZ")) {
				String result;
				result = ((((String)read[i]).split(" "))[1]).split(",")[0];
				//System.out.println("jump: " + result + " " + (((String)read[i]).split(","))[1]);
				for(int j = 0;j<32;j++) {
					if(result.equals("R"+j))
						jump[i] = j;
				}
				branch[i] = (((String)read[i]).split(","))[1];
			} else {
				label[i] = (String)read[i];
			}
		}
		for(int i=0;i<addressposition.length;i++) {
			if(addressposition[i]!=-1)
				setChanged(addressposition[i]);
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