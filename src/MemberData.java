import java.io.Serializable;

public class MemberData implements Serializable {

	public static final int FIRST_CONNECT = 0;
	public static final int LOGIN_SUCCESS = 1;
	public static final int ID_NOTFOUND = 2;
	public static final int ID_FOUND = 3;
	public static final int PW_MISSMATCH = 4;
	public static final int REGISTER = 5;
	public static final int REGISTER_SUCCESS = 6;
	public static final int REGISTER_FAIL = 7;
	public static final int DISCONNECT = 8;
	public static final int FIND_ID = 9;

	private int state;
	private int memberNo;
	private String name;
	private String ID;
	private String PW;
	private String email;

	public MemberData(int state, String ID, String PW) {
		this.state = state;
		this.ID = ID;
		this.PW = PW;
	}

	public MemberData(int state, String name, String ID, String PW, String email) {
		this.state = state;
		this.name = name;
		this.ID = ID;
		this.PW = PW;
		this.email = email;
	}
	
	public MemberData(int state, int memberNo, String ID) {
		this.state = state;
		this.memberNo = memberNo;
		this.ID = ID;
	}
	
	
	public int getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPW() {
		return PW;
	}

	public void setPW(String pW) {
		PW = pW;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
