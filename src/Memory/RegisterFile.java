package Memory;

//import java.util.Random;

public class RegisterFile {
	String[] tag;
	String[] q;
	int[] content;
	int[] changed;
	
	public RegisterFile() {
		int n = 64;
		tag = new String[n];
		q = new String[n];
		content = new int[n];
		changed = new int[n];
		for(int i=0;i<n;i++) {
			q[i] = "0";
			content[i] = 1;
			if(i<32) {
				tag[i] = "F"+i;
			} else {
				tag[i] = "R"+(i-32);
				if(i==32)
					content[i] = 0;
			}
		}
	}

	public String getQ(String register) {
		for(int i=0;i<32;i++) {
			if(tag[i].equals(register))
				return q[i];
			if(tag[i+32].equals(register))
				return q[i+32];
		}
		return "0";
	}
	public void setQ(String register, String value) {
		for(int i=0;i<32;i++) {
			if(tag[i].equals(register))
				q[i] = value;
			if(tag[i+32].equals(register) && i!=0)
				q[i+32] = value;
		}
	}
	public int getContent(String register) {
		for(int i=0;i<32;i++) {
			if(tag[i].equals(register))
				return content[i];
			if(tag[i+32].equals(register))
				return content[i+32];
		}
		return -1;
	}
	public void setContent(String register, int val) {
		for(int i=0;i<32;i++) {
			if(tag[i].equals(register)) {
				content[i] = val;
				q[i] = "0";
				changed[i] = 1;
			}
			if(tag[i+32].equals(register) && i!=0) {
				content[i+32] = val;
				q[i+32] = "0";
				changed[i+32] = 1;
			}
		}
	}
	
	public String toString() {
		System.out.println("---------------------Register File---------------------");
		System.out.println("Tag | Qi | Content | Changed");
		for(int i=0;i<tag.length;i++) {
			if(i==0)
				System.out.println("-------Floating-point-------");
			if(i==32)
				System.out.println("-----------Integer-----------");
			System.out.print(tag[i] + "  | " + q[i] + " | " + content[i] + " | ");
			if(changed[i]==0)
				System.out.println();
			else
				System.out.println("changed");
		}
		return "";
	}
}
