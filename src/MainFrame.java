import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame implements Runnable {

	public static final int LOGIN = 0;
	public static final int REGISERT = 1;
	public static final int MAIN = 2;
	public static final int GROUPMANAGE = 3;
	public static final int CREATEGROUP = 4;
	public static final int MYSCHEDULE = 5;

	private LogInPanel logInPanel = new LogInPanel(this);
	private MainPanel mainPane = new MainPanel(this);
	private CreateNewGroupPanel createNewGroupPanel = new CreateNewGroupPanel(this);
	private GroupManagePanel groupManagePanel = new GroupManagePanel(this);
	private MySchedulePanel mySchedulePanel = new MySchedulePanel(this);
	private RegisetPanel regisetPanel = new RegisetPanel(this);

	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public static void main(String[] args) {
		new MainFrame();
	}

	public MainFrame() { // GUI 생성
		setTitle("Group Schedule");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setPersnalCloseOperation();
		setLayout(null);
		setResizable(false);
		initialize();
		switchingPanel(LOGIN);
		setVisible(true);
		connect();
	}

	private void setPersnalCloseOperation() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int i = JOptionPane.showConfirmDialog(null, "종료 하시겠습니까?", "", JOptionPane.YES_NO_OPTION);
				if (i == 0) {
					try {
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
			socket = new Socket("203.233.196.196", 8888);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("위");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("아래");
		}
	}

	private void initialize() { // 각 화면 등록
		add(logInPanel);
		add(regisetPanel);
		add(mainPane);
		add(groupManagePanel);
		add(createNewGroupPanel);
		add(mySchedulePanel);
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
			setSize(logInPanel.getWidth(), logInPanel.getHeight());
			logInPanel.setVisible(true);
			break;
		case REGISERT:
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
				Object data = ois.readUnshared();
				if (data instanceof MemberData) {

				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendRequest(MemberData data) {
		try {
			oos.writeUnshared(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
