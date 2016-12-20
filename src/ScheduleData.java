import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

public class ScheduleData implements Serializable {
	public static final int PERSNAL_SCHEDULE_ADD = 1;
	public static final int PERSNAL_SCHEDULE_DELETE = 2;
	public static final int PERSNAL_SCHEDULE_UPDATE = 3;
	public static final int ADD_POSSIBLE_DATE = 4;
	public static final int GROUP_MANAGE = 5;
	public static final int CREATE_NEW_GROUP = 6;
	public static final int CREATE_FAIL = 7;
	public static final int GET_PERSONAL_SCHEDULE = 8;
	public static final int GET_GROUP_SCHEDULE = 9;
	public static final int GET_POSSIBLE_DATE = 10;
	public static final int AGREE = 11;
	public static final int BET = 12;

	private int state;
	private int grno;
	private int bet;
	private String grName;
	private String place;
	private ArrayList<Integer> memberNoList;
	private ArrayList<String> memberNameL;
	private ArrayList<ScheduleData> scheduleList;
	private Date date;
	private String title;
	private boolean agree;

	public ScheduleData(int state, ArrayList<ScheduleData> scheduleList) {
		this.state = state;
		this.scheduleList = scheduleList;
	}

	public ScheduleData(int state, String grName, String place, ArrayList<Integer> memberList) {
		this.state = state;
		this.grName = grName;
		this.place = place;
		this.memberNoList = memberList;
	}

	public ScheduleData(int state, String grName, String place, Date date, ArrayList<Integer> memberNoList) {
		this.state = state;
		this.grName = grName;
		this.place = place;
		this.date = date;
		this.memberNoList = memberNoList;
	}

	public ScheduleData(int state, String grName, String place, ArrayList<String> memberNameL, Date date, int grNo) {
		this.state = state;
		this.grName = grName;
		this.place = place;
		this.date = date;
		this.memberNameL = memberNameL;
		this.grno = grNo;
	}

	public ScheduleData(int state, String place, int bet, int grno) {
		this.state = state;
		this.place = place;
		this.bet = bet;
		this.grno = grno;
	}

	public ScheduleData(int state, String title, String place) {
		this.state = state;
		this.title = title;
		this.place = place;
	}

	public ScheduleData(int state, String title, String place, Date date) {
		this.state = state;
		this.title = title;
		this.place = place;
		this.date = date;
	}

	public ScheduleData(int state, boolean agree, int grno) {
		this.state = state;
		this.agree = agree;
		this.grno = grno;
	}

	public ScheduleData(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getGrName() {
		return grName;
	}

	public void setGrName(String grName) {
		this.grName = grName;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public ArrayList<Integer> getMemberNoList() {
		return memberNoList;
	}

	public void setMemberNoList(ArrayList<Integer> memberList) {
		this.memberNoList = memberList;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isAgree() {
		return agree;
	}

	public void setAgree(boolean agree) {
		this.agree = agree;
	}

	public ArrayList<String> getMemberNameL() {
		return memberNameL;
	}

	public void setMemberNameL(ArrayList<String> memberNameL) {
		this.memberNameL = memberNameL;
	}

	public int getGrno() {
		return grno;
	}

	public void setGrno(int grno) {
		this.grno = grno;
	}

	public ArrayList<ScheduleData> getScheduleList() {
		return scheduleList;
	}

	public int getBet() {
		return bet;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}

}