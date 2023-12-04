package Memory;

//import java.util.Random;

public class RegisterFile {
	String[] tag;
	String[] q;
	int[] content;
	int[] changed;
	
	public RegisterFile() {
		setTag();
		setQ();
		generateContent();
		changed = new int[64];
	}
	
	public void generateContent() {
		content = new int[64];
		//Random random = new Random();
		//int rand;
		for(int i = 0; i < 64; i++) {
			if(i==32)
				content[i] = 0;
			else {
				//rand = random.nextInt(51);
				//content[i] = rand;
				content[i] = 1;
			}
		}
	}
	
	public String[] getTag() {
		return tag;
	}
	public void setTag() {
		tag = new String[64];
		for(int i = 0; i < 32; i++) {
			tag[i] = "F"+i;
		}
		for(int i = 32; i < 64; i++) {
			tag[i] = "R"+(i-32);
		}
	}
	public String getQ(int n) {
		return q[n];
	}
	public void setQ() {
		int n = 64;
		q = new String[n];
		for(int i = 0; i < n; i++) {
			q[i] = "0";
		}
	}
	public void setQusingTag(int n, String value) {
		q[n] = value;
	}
	public int getContent(int n) {
		return content[n];
	}
	public int getContentWithTag(String n) {
		for(int i=0;i<32;i++) {
			if(n.equals("F"+i))
				return content[i];
			if(n.equals("R"+i))
				return content[i+32];
		}
		return -1;
	}
	public void setContent() {
		int n = 64;
		content = new int[n];
		for(int i = 0; i < n; i++) {
			content[i] = 0;
		}
	}
	public void setContentusingTag(int n, int val) {
		content[n] = val;
		q[n] = "0";
		changed[n] = 1;
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
