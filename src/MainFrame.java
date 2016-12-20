import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame implements Runnable {

	public static final int LOGIN = 0;
	public static final int REGISTER = 1;
	public static final int MAIN = 2;
	public static final int GROUPMANAGE = 3;
	public static final int CREATEGROUP = 4;
	public static final int MYSCHEDULE = 5;

	private static MainFrame mf = new MainFrame();

	private ArrayList<ScheduleData> personalList;
	private ArrayList<ScheduleData> groupList;
	private ArrayList<ScheduleData> possibleDateList;
	private ArrayList<ScheduleData> notFixedList;

	private String userID;
	private String userPW;
	private String userName;
	private String userEmail;

	private LogInPanel logInPanel;
	private MainCalendar mainCalendar;
	private CreateNewGroupPanel createNewGroupPanel;
	private GroupManagePanel groupManagePanel;
	private PossibleCalendar mySchedulePanel;
	private RegisetPanel regisetPanel;

	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private NorthPanel northPanel;
	private JPanel centerPanel;

	public static MainFrame getInstance() {
		return mf;
	}

	private MainFrame() { // GUI 생성
		connect();
		setTitle("Group Schedule");
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("ProjectImageIcon/IconImage.png");
		setIconImage(img);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setPersnalCloseOperation();
		setResizable(false);
		setVisible(true);
	}

	private void setPersnalCloseOperation() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (JOptionPane.showConfirmDialog(null, "종료 하시겠습니까?", "", JOptionPane.YES_NO_OPTION) == 0) {
					try {
						sendRequest(new MemberData(MemberData.DISCONNECT, null, null));
						finalize();
						System.exit(0);
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	protected void finalize() throws Throwable { // 클라이언트 종료 시 소켓 연결 해제 작업
		if (oos != null)
			oos.close();
		if (ois != null)
			ois.close();
		if (socket != null)
			socket.close();
	}

	private void connect() { // 서버 연결
		try {
			socket = new Socket("localhost", 8888);// 203.233.196.93
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			new Thread(this).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("위");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("아래");
		}
	}

	public void initialize() { // 프로그램 초기화
		personalList = new ArrayList<>();
		groupList = new ArrayList<>();
		possibleDateList = new ArrayList<>();
		notFixedList = new ArrayList<>();
		// 사용할 자료구조 정의

		logInPanel = new LogInPanel();
		createNewGroupPanel = new CreateNewGroupPanel();
		mainCalendar = new MainCalendar();
		groupManagePanel = new GroupManagePanel();
		mySchedulePanel = new PossibleCalendar();
		regisetPanel = new RegisetPanel();
		northPanel = new NorthPanel();
		centerPanel = new JPanel();
		// 각 화면 생성하기

		centerPanel.setBackground(Color.WHITE);
		centerPanel.setLayout(new BorderLayout());

		centerPanel.add(logInPanel, BorderLayout.CENTER);
		centerPanel.add(regisetPanel, BorderLayout.CENTER);
		centerPanel.add(mainCalendar, BorderLayout.CENTER);
		centerPanel.add(groupManagePanel, BorderLayout.CENTER);
		centerPanel.add(createNewGroupPanel, BorderLayout.CENTER);
		centerPanel.add(mySchedulePanel, BorderLayout.CENTER);

		getContentPane().add(centerPanel, BorderLayout.CENTER);
		getContentPane().add(northPanel, BorderLayout.NORTH);
		// 프레임에 화면 달기

		switchingPanel(LOGIN);
	}

	public void switchingPanel(int state) { // 화면 전환 함수
		logInPanel.setVisible(false);
		regisetPanel.setVisible(false);
		mainCalendar.setVisible(false);
		groupManagePanel.setVisible(false);
		createNewGroupPanel.setVisible(false);
		mySchedulePanel.setVisible(false);
		northPanel.setVisible(true);
		setSize(500, 590);
		switch (state) {
		case LOGIN:
			northPanel.setVisible(false);
			setSize(logInPanel.getWidth(), logInPanel.getHeight());
			logInPanel.clearIDField();
			logInPanel.clearPWField();
			logInPanel.setVisible(true);
			break;
		case REGISTER:
			northPanel.setVisible(false);
			setSize(regisetPanel.getWidth(), regisetPanel.getHeight());
			regisetPanel.setVisible(true);
			break;
		case MAIN:
			mainCalendar.setVisible(true);
			break;
		case GROUPMANAGE:
			groupManagePanel.setVisible(true);
			break;
		case CREATEGROUP:
			createNewGroupPanel.setVisible(true);
			break;
		case MYSCHEDULE:
			mySchedulePanel.setVisible(true);
			break;
		}
		setLocationRelativeTo(null);
	}

	public void run() { // 서버에서 오는 신호를 읽어 처리함.
		while (true) {
			try {
				Object data = ois.readObject();
				if (data instanceof MemberData) {
					MemberData md = (MemberData) data;
					switch (md.getState()) {
					case MemberData.LOGIN_SUCCESS:
						setTitle("Group Schedule  (" + md.getID() + ")");
						userID = md.getID();
						userPW = md.getPW();
						userName = md.getName();
						userEmail = md.getEmail();
						getInitialData();
						switchingPanel(MAIN);
						break;
					case MemberData.PW_MISSMATCH:
						JOptionPane.showMessageDialog(null, "잘못된 비밀번호 입니다.");
						logInPanel.clearPWField();
						break;
					case MemberData.ID_FOUND:
						createNewGroupPanel.addToList(md.getID(), md.getMemberNo());
						break;
					case MemberData.ID_NOTFOUND:
						JOptionPane.showMessageDialog(null, "존재하지 않는 아이디 입니다.");
						logInPanel.clearIDField();
						logInPanel.clearPWField();
						break;
					case MemberData.REGISTER_SUCCESS:
						regisetPanel.clearField();
						switchingPanel(LOGIN);
						break;
					case MemberData.REGISTER_FAIL:
						JOptionPane.showMessageDialog(null, "이미 사용중인 아이디 입니다.");
						break;
					}
				} else if (data instanceof ScheduleData) {
					ScheduleData sd = (ScheduleData) data;
					switch (sd.getState()) {
					case ScheduleData.GET_PERSONAL_SCHEDULE:
						personalList = sd.getScheduleList();
						mainCalendar.updateGUI();
						break;
					case ScheduleData.GET_GROUP_SCHEDULE:
						groupList = sd.getScheduleList();
						mySchedulePanel.updateCalendar();
						mainCalendar.updateGUI();
						break;
					case ScheduleData.GET_POSSIBLE_DATE:
						possibleDateList = sd.getScheduleList();
						mySchedulePanel.updateCalendar();
						break;
					case ScheduleData.GROUP_MANAGE:
						notFixedList = sd.getScheduleList();
						groupManagePanel.updateGUI();
						break;
					case ScheduleData.CREATE_NEW_GROUP:
						createNewGroupPanel.clearField();
						JOptionPane.showMessageDialog(null, "그룹 만들기 성공!");
						break;
					case ScheduleData.CREATE_FAIL:
						JOptionPane.showMessageDialog(null, "출석률이 75% 이하여서 그룹 등록 실패");
						break;
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public void sendRequest(Object data) {
		try {
			oos.writeObject(data);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "서버가 열려있지 않습니다.");
		}
	}

	public void getInitialData() {
		sendRequest(new ScheduleData(ScheduleData.GET_PERSONAL_SCHEDULE));
		sendRequest(new ScheduleData(ScheduleData.GET_GROUP_SCHEDULE, null, null));
		sendRequest(new ScheduleData(ScheduleData.GET_POSSIBLE_DATE, null, null));
		sendRequest(new ScheduleData(ScheduleData.GROUP_MANAGE, null, null));
	}

	public ArrayList<ScheduleData> getPersonalList() {
		if (personalList == null)
			return personalList = new ArrayList<>();
		return personalList;
	}

	public ArrayList<ScheduleData> getGroupList() {
		if (groupList == null)
			return groupList = new ArrayList<>();
		return groupList;
	}

	public ArrayList<ScheduleData> getPossibleDateList() {
		if (possibleDateList == null)
			return possibleDateList = new ArrayList<>();
		return possibleDateList;
	}

	public ArrayList<ScheduleData> getNotFixedList() {
		if (notFixedList == null)
			return notFixedList = new ArrayList<>();
		return notFixedList;
	}

	public ScheduleData findPersonalScheduleData(Date d) {
		for (ScheduleData sd : getPersonalList())
			if (sd.getDate().toString().equals(d.toString()))
				return sd;
		return null;
	}

	public ScheduleData findGroupScheduleData(Date d) {
		for (ScheduleData sd : getGroupList())
			if (sd.getDate().toString().equals(d.toString()))
				return sd;
		return null;
	}

	public void slideUp() {
		mainCalendar.slideUp();
	}

	public void slideDown() {
		mainCalendar.slideDown();
	}

	public void logOut() {
		switchingPanel(LOGIN);
		slideDown();
	}

	public String getUserID() {
		return userID;
	}

	public String getUserPW() {
		return userPW;
	}

	public void setUserPW(String userPW) {
		this.userPW = userPW;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

}
