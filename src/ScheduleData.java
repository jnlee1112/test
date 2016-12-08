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

	private int state;
	private String grName;
	private String place;
	private ArrayList<Integer> memberList;
	private ArrayList<Date> dateList;
	private Date date;
	private String title;
	private boolean agree;

	public ScheduleData(int state, String grName, String place, ArrayList<Integer> memberList) {
		this.state = state;
		this.grName = grName;
		this.place = place;
		this.memberList = memberList;
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

	public ArrayList<Integer> getMemberList() {
		return memberList;
	}

	public void setMemberList(ArrayList<Integer> memberList) {
		this.memberList = memberList;
	}

	public ArrayList<Date> getDateList() {
		return dateList;
	}

	public void setDateList(ArrayList<Date> dateList) {
		this.dateList = dateList;
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

}
