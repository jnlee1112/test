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

	private LogInPanel logInPanel;
	private MainPanel mainPane;
	private CreateNewGroupPanel createNewGroupPanel;
	private GroupManagePanel groupManagePanel;
	private MySchedulePanel mySchedulePanel;
	private RegisetPanel regisetPanel;

	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public static void main(String[] args) {
		MainFrame.getInstance();
	}

	public static MainFrame getInstance() {
		return mf;
	}

	private MainFrame() { // GUI 생성
		connect();
		setTitle("Group Schedule");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setPersnalCloseOperation();
		setLayout(null);
		setResizable(false);
		initialize();
		switchingPanel(LOGIN);
		setVisible(true);
	}

	private void setPersnalCloseOperation() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int i = JOptionPane.showConfirmDialog(null, "종료 하시겠습니까?", "", JOptionPane.YES_NO_OPTION);
				if (i == 0) {
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

	private void initialize() { // 프로그램 초기화
		personalList = new ArrayList<>();
		groupList = new ArrayList<>();
		possibleDateList = new ArrayList<>();
		notFixedList = new ArrayList<>();
		// 사용할 자료구조 정의

		logInPanel = new LogInPanel();
		mainPane = new MainPanel();
		createNewGroupPanel = new CreateNewGroupPanel();
		groupManagePanel = new GroupManagePanel();
		mySchedulePanel = new MySchedulePanel();
		regisetPanel = new RegisetPanel();
		// 화면 구성

		add(logInPanel);
		add(regisetPanel);
		add(mainPane);
		add(groupManagePanel);
		add(createNewGroupPanel);
		add(mySchedulePanel);
		// 프레임에 화면 달기
	}

	public void switchingPanel(int state) { // 화면 전환 함수
		logInPanel.setVisible(false);
		regisetPanel.setVisible(false);
		mainPane.setVisible(false);
		groupManagePanel.setVisible(false);
		createNewGroupPanel.setVisible(false);
		mySchedulePanel.setVisible(false);
		switch (state) {
		case LOGIN:
			logInPanel.clearIDField();
			logInPanel.clearPWField();
			setSize(logInPanel.getWidth(), logInPanel.getHeight());
			logInPanel.setVisible(true);
			break;
		case REGISTER:
			setSize(regisetPanel.getWidth(), regisetPanel.getHeight() + 20);
			regisetPanel.setVisible(true);
			break;
		case MAIN:
			setSize(mainPane.getWidth(), mainPane.getHeight());
			mainPane.setVisible(true);
			break;
		case GROUPMANAGE:
			setSize(groupManagePanel.getWidth(), groupManagePanel.getHeight());
			groupManagePanel.setVisible(true);
			break;
		case CREATEGROUP:
			setSize(createNewGroupPanel.getWidth(), createNewGroupPanel.getHeight() + 40);
			createNewGroupPanel.setVisible(true);
			break;
		case MYSCHEDULE:
			setSize(mySchedulePanel.getWidth(), mySchedulePanel.getHeight());
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
						mainPane.updateGUI();
						break;
					case ScheduleData.GET_GROUP_SCHEDULE:
						groupList = sd.getScheduleList();
						mySchedulePanel.updateGUI();
						mainPane.updateGUI();
						break;
					case ScheduleData.GET_POSSIBLE_DATE:
						possibleDateList = sd.getScheduleList();
						mySchedulePanel.updateGUI();
						break;
					case ScheduleData.GROUP_MANAGE:
						notFixedList = sd.getScheduleList();
						groupManagePanel.updateGUI();
						break;
					case ScheduleData.CREATE_NEW_GROUP:
						createNewGroupPanel.clearField();
						switchingPanel(GROUPMANAGE);
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
			e.printStackTrace();
		}
	}

	public void getInitialData() {
		sendRequest(new ScheduleData(ScheduleData.GET_PERSONAL_SCHEDULE));
		sendRequest(new ScheduleData(ScheduleData.GET_GROUP_SCHEDULE, null, null));
		sendRequest(new ScheduleData(ScheduleData.GET_POSSIBLE_DATE, null, null));
		sendRequest(new ScheduleData(ScheduleData.GROUP_MANAGE, null, null));
	}

	public ArrayList<ScheduleData> getPersonalList() {
		return personalList;
	}

	public ArrayList<ScheduleData> getGroupList() {
		return groupList;
	}

	public ArrayList<ScheduleData> getPossibleDateList() {
		return possibleDateList;
	}

	public ArrayList<ScheduleData> getNotFixedList() {
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
}
