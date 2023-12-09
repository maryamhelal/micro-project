package Memory;

public class ReservationStations {
	String[] tagmul;
	int[] busymul;
	String[] opmul;
	String[] vjmul;
	String[] vkmul;
	String[] qjmul;
	String[] qkmul;
	int[] addressmul;
	int[] linemul;
	
	String[] tagadd;
	int[] busyadd;
	String[] opadd;
	String[] vjadd;
	String[] vkadd;
	String[] qjadd;
	String[] qkadd;
	int[] addressadd;
	int[] lineadd;
	
	String[] tagload;
	int[] busyload;
	int[] addressload;
	int[] lineload;
	
	String[] tagstore;
	int[] busystore;
	int[] addressstore;
	String[] vstore;
	String[] qstore;
	int[] linestore;
	
	public ReservationStations(int mul, int add, int load, int store) {
		Mul(mul);
		Add(add);
		Load(load);
		Store(store);
	}
	
	public void Mul(int n) {
		tagmul = new String[n];
		busymul = new int[n];
		opmul = new String[n];
		vjmul = new String[n];
		vkmul = new String[n];
		qjmul = new String[n];
		qkmul = new String[n];
		addressmul = new int[n];
		linemul = new int[n];
		for(int i = 0; i < n; i++) {
			tagmul[i] = "M"+(i+1);
			busymul[i]=0;
			opmul[i] = "";
			vjmul[i] = "";
			vkmul[i] = "";
			qjmul[i] = "";
			qkmul[i] = "";
			addressmul[i] = -1;
			linemul[i] = -1;
		}
	}
	
	public void Add(int n) {
		tagadd = new String[n];
		busyadd = new int[n];
		opadd = new String[n];
		vjadd = new String[n];
		vkadd = new String[n];
		qjadd = new String[n];
		qkadd = new String[n];
		addressadd = new int[n];
		lineadd = new int[n];
		for(int i = 0; i < n; i++) {
			tagadd[i] = "A"+(i+1);
			busyadd[i]=0;
			opadd[i] = "";
			vjadd[i] = "";
			vkadd[i] = "";
			qjadd[i] = "";
			qkadd[i] = "";
			addressadd[i] = -1;
			lineadd[i] = -1;
		}
	}
	
	public void Load(int n) {
		tagload = new String[n];
		busyload = new int[n];
		addressload = new int[n];
		lineload = new int[n];
		for(int i = 0; i < n; i++) {
			tagload[i] = "L"+(i+1);
			busyload[i]=0;
			addressload[i] = -1;
			lineload[i] = -1;
		}
	}
	
	public void Store(int n) {
		tagstore = new String[n];
		busystore = new int[n];
		addressstore = new int[n];
		vstore = new String[n];
		qstore = new String[n];
		linestore = new int[n];
		for(int i = 0; i < n; i++) {
			tagstore[i] = "S"+(i+1);
			busystore[i]=0;
			addressstore[i] = -1;
			vstore[i] = "";
			qstore[i] = "";
			linestore[i] = -1;
		}
	}

	public int getVjmul(int line) {
		if(vjmul[getIndexUsingLine(line)].equals(""))
			return 0;
		else
			return Integer.parseInt(vjmul[getIndexUsingLine(line)]);
	}
	public int getVkmul(int line) {
		if(vkmul[getIndexUsingLine(line)].equals(""))
			return 0;
		else
			return Integer.parseInt(vkmul[getIndexUsingLine(line)]);
	}
	public String getQjmul(int line) {
		return qjmul[getIndexUsingLine(line)];
	}
	public String getQkmul(int line) {
		return qkmul[getIndexUsingLine(line)];
	}
	
	public int getVjadd(int line) {
		if(vjadd[getIndexUsingLine(line)].equals(""))
			return 0;
		else
			return Integer.parseInt(vjadd[getIndexUsingLine(line)]);
	}
	public int getVkadd(int line) {
		if(vkadd[getIndexUsingLine(line)].equals(""))
			return 0;
		else
			return Integer.parseInt(vkadd[getIndexUsingLine(line)]);
	}
	public String getQjadd(int line) {
		return qjadd[getIndexUsingLine(line)];
	}
	public String getQkadd(int line) {
		return qkadd[getIndexUsingLine(line)];
	}
	
	public int getAddressload(int line) {
		return addressload[getIndexUsingLine(line)];
	}
	
	public int getAddressstore(int line) {
		return addressstore[getIndexUsingLine(line)];
	}
	public int getVstore(int line) {
		if(vstore[getIndexUsingLine(line)].equals(""))
			return 0;
		else
			return Integer.parseInt(vstore[getIndexUsingLine(line)]);
	}
	public String getQstore(int line) {
		return qstore[getIndexUsingLine(line)];
	}
	
	public void setOccupied(String op, String vj, String vk, String qj, String qk, int address, int line) {
		int n = -1;
		if(op.startsWith("MUL") || op.startsWith("DIV")) {
			for(int i = 0;i < tagmul.length; i++) {
				if(busymul[i]==0) {
					n=i;
					break;
				}
			}
			busymul[n]=1;
			opmul[n] = op;
			vjmul[n] = vj;
			vkmul[n] = vk;
			qjmul[n] = qj;
			qkmul[n] = qk;
			addressmul[n] = address;
			linemul[n] = line;
		} else if(op.startsWith("ADD") || op.startsWith("SUB") || op.startsWith("BNEZ")){
			for(int i = 0;i < tagadd.length; i++) {
				if(busyadd[i]==0) {
					n=i;
					break;
				}
			}
			busyadd[n]=1;
			opadd[n] = op;
			vjadd[n] = vj;
			vkadd[n] = vk;
			qjadd[n] = qj;
			qkadd[n] = qk;
			addressadd[n] = address;
			lineadd[n] = line;
		} else if(op.startsWith("L")) {
			for(int i = 0;i < tagload.length; i++) {
				if(busyload[i]==0) {
					n=i;
					break;
				}
			}
			busyload[n]=1;
			addressload[n] = address;
			lineload[n] = line;
		} else if(op.startsWith("S")) {
			for(int i = 0;i < tagstore.length; i++) {
				if(busystore[i]==0) {
					n=i;
					break;
				}
			}
			busystore[n]=1;
			addressstore[n] = address;
			vstore[n] = vj;
			qstore[n] = qj;
			linestore[n] = line;
		}
	}
	
	public void writeWaiting(String tagdestination, String value) {
		for(int i=0;i<qjmul.length;i++) {
			if(qjmul[i].equals(tagdestination)) {
				vjmul[i] = value;
				qjmul[i] = "0";
			}
			if(qkmul[i].equals(tagdestination)) {
				vkmul[i] = value;
				qkmul[i] = "0";
			}
		}
		for(int i=0;i<qjadd.length;i++) {
			if(qjadd[i].equals(tagdestination)) {
				vjadd[i] = value;
				qjadd[i] = "0";
			}
			if(qkadd[i].equals(tagdestination)) {
				vkadd[i] = value;
				qkadd[i] = "0";
			}
		}
		for(int i=0;i<qstore.length;i++) {
			if(qstore[i].equals(tagdestination)) {
				vstore[i] = value;
				qstore[i] = "0";
			}
		}
	}
	private int getIndexUsingLine(int n) {
		for(int i=0;i<tagmul.length;i++) {
			if(linemul[i]==n)
				return i;
		}
		for(int i=0;i<tagadd.length;i++) {
			if(lineadd[i]==n)
				return i;
		}
		for(int i=0;i<tagload.length;i++) {
			if(lineload[i]==n)
				return i;
		}
		for(int i=0;i<tagstore.length;i++) {
			if(linestore[i]==n)
				return i;
		}
		return -1;
	}
	public String getTagUsingLine(int n) {
		for(int i=0;i<tagmul.length;i++) {
			if(linemul[i]==n)
				return tagmul[i];
		}
		for(int i=0;i<tagadd.length;i++) {
			if(lineadd[i]==n)
				return tagadd[i];
		}
		for(int i=0;i<tagload.length;i++) {
			if(lineload[i]==n)
				return tagload[i];
		}
		for(int i=0;i<tagstore.length;i++) {
			if(linestore[i]==n)
				return tagstore[i];
		}
		return "";
	}
	public boolean isWaiting(int index) {
		String tag = getTagUsingLine(index);
		int i = getIndexUsingLine(index);
		if(tag.startsWith("M")){
			if(!qjmul[i].equals("0") || !qkmul[i].equals("0"))
				return true;
		} else if(tag.startsWith("A")){
			if(!qjadd[i].equals("0") || !qkadd[i].equals("0"))
				return true;
		} else if(tag.startsWith("S")){
			if(!qstore[i].equals("0"))
				return true;
		}
		return false;
	}
	public boolean isOccupied(String operation) {
		int n = -1;
		if(operation.startsWith("MUL") || operation.startsWith("DIV"))
			for(int i = 0;i < tagmul.length; i++) {
				if(busymul[i]==0) {
					n=i;
					break;
				}
			}
		else if(operation.startsWith("ADD") || operation.startsWith("SUB") || operation.startsWith("BNEZ"))
			for(int i = 0; i < tagadd.length; i++) {
				if(busyadd[i]==0) {
					n=i;
					break;
				}
			}
		else if(operation.startsWith("L"))
			for(int i = 0; i < tagload.length; i++) {
				if(busyload[i]==0) {
					n=i;
					break;
				}
			}
		else if(operation.startsWith("S"))
			for(int i = 0; i < tagstore.length; i++) {
				if(busystore[i]==0) {
					n=i;
					break;
				}
			}
		if(n==-1)
			return true;
		else
			return false;
	}
	public void setAvailable(int line) {
		String tag = getTagUsingLine(line);
		int n = getIndexUsingLine(line);
		if(tag.startsWith("M")) {
			busymul[n] = 0;
			opmul[n] = "";
			vjmul[n] = "";
			vkmul[n] = "";
			qjmul[n] = "";
			qkmul[n] = "";
			addressmul[n] = -1;
			linemul[n] = -1;
		} else if(tag.startsWith("A")) {
			busyadd[n] = 0;
			opadd[n] = "";
			vjadd[n] = "";
			vkadd[n] = "";
			qjadd[n] = "";
			qkadd[n] = "";
			addressadd[n] = -1;
			lineadd[n] = -1;
		} else if(tag.startsWith("L")) {
			busyload[n] = 0;
			addressload[n] = -1;
			lineload[n] = -1;
		} else if(tag.startsWith("S")) {
			busystore[n] = 0;
			addressstore[n] = -1;
			vstore[n] = "";
			qstore[n] = "";
			linestore[n] = -1;
		}	
	}
	
	public String toString() {
		System.out.println("--------------------------Mul--------------------------");
		System.out.println("Tag | Op |  Busy | Vj | Vq | Qj | Qk | A");
		for(int i=0;i<tagmul.length;i++) {
			System.out.print(tagmul[i] + "  | " + opmul[i] + " |  " + busymul[i] + "   | " + vjmul[i] + " | " + vkmul[i] + " | " + qjmul[i] + " | " + qkmul[i] + " | ");
			if(addressmul[i] != -1) {
				System.out.println(addressmul[i]);
			} else {
				System.out.println();
			}
		}
		System.out.println("--------------------------Add--------------------------");
		System.out.println("Tag | Op | Busy | Vj | Vq | Qj | Qk | A");
		for(int i=0;i<tagadd.length;i++) {
			System.out.print(tagadd[i] + "  | " + opadd[i] + " |  " + busyadd[i] + "   | " + vjadd[i] + " | " + vkadd[i] + " | " + qjadd[i] + " | " + qkadd[i] + " | ");
			if(addressadd[i] != -1) {
				System.out.println(addressadd[i]);
			} else {
				System.out.println();
			}
		}
		System.out.println("-------------------------Load-------------------------");
		System.out.println("Tag | Busy | Address");
		for(int i=0;i<tagload.length;i++) {
			System.out.print(tagload[i] + "  |  " + busyload[i] + "   | ");
			if(addressload[i] != -1) {
				System.out.println(addressload[i]);
			} else {
				System.out.println();
			}
		}
		System.out.println("-------------------------Store-------------------------");
		System.out.println("Tag | Busy | V | Q | Address");
		for(int i=0;i<tagstore.length;i++) {
			System.out.print(tagstore[i] + "  |  " + busystore[i] + "   | " + vstore[i] + " | " + qstore[i] + " | ");
			if(addressstore[i] != -1) {
				System.out.println(addressstore[i]);
			} else {
				System.out.println();
			}
		}
		return "";
	}
}
