package Memory;

public class RegisterFile {
	String[] tag;
	String[] q;
	int[] content;
	
	public RegisterFile() {
		setTag();
		setQ();
		setContent();
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
	public int[] getContent() {
		return content;
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
	}
	
	public String toString() {
		System.out.println("---------------------Register File---------------------");
		System.out.println("Tag | Qi | Content");
		for(int i=0;i<tag.length;i++) {
			if(i==0)
				System.out.println("-------Floating-point-------");
			if(i==32)
				System.out.println("-----------Integer-----------");
			System.out.println(tag[i] + "  | " + q[i] + " | " + content[i]);
		}
		return "";
	}
}
