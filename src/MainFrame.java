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
	public static final int REGISTER = 1;
	public static final int MAIN = 2;
	public static final int GROUPMANAGE = 3;
	public static final int CREATEGROUP = 4;
	public static final int MYSCHEDULE = 5;

	private static MainFrame mf = new MainFrame();

	private LogInPanel logInPanel = new LogInPanel();
	private MainPanel mainPane = new MainPanel();
	private CreateNewGroupPanel createNewGroupPanel = new CreateNewGroupPanel();
	private GroupManagePanel groupManagePanel = new GroupManagePanel();
	private MySchedulePanel mySchedulePanel = new MySchedulePanel();
	private RegisetPanel regisetPanel = new RegisetPanel();

	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public static void main(String[] args) {
		MainFrame.getInstance();
	}

	public static MainFrame getInstance() {
		return mf;
	}

	private MainFrame() { // GUI ����
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
				int i = JOptionPane.showConfirmDialog(null, "���� �Ͻðڽ��ϱ�?", "", JOptionPane.YES_NO_OPTION);
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

	protected void finalize() throws Throwable { // Ŭ���̾�Ʈ ���� �� ���� ���� ���� �۾�
		if (oos != null)
			oos.close();
		if (ois != null)
			ois.close();
		if (socket != null)
			socket.close();
	}

	private void connect() { // ���� ����
		try {
			socket = new Socket("203.233.196.93", 8888);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			new Thread(this).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("��");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("�Ʒ�");
		}
	}

	private void initialize() { // �� ȭ�� ���
		add(logInPanel);
		add(regisetPanel);
		add(mainPane);
		add(groupManagePanel);
		add(createNewGroupPanel);
		add(mySchedulePanel);
	}

	public void switchingPanel(int state) { // ȭ�� ��ȯ �Լ�
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

	public void run() { // �������� ���� ��ȣ�� �о� ó����.
		while (true) {
			try {
				Object data = ois.readObject();
				if (data instanceof MemberData) {
					MemberData md = (MemberData) data;
					System.out.println(md.getState());
					switch (md.getState()) {
					case MemberData.LOGIN_SUCCESS:
						getInitialData();
						switchingPanel(MAIN);
						break;
					case MemberData.PW_MISSMATCH:
						JOptionPane.showMessageDialog(null, "�߸��� ��й�ȣ �Դϴ�.");
						logInPanel.clearField();
						break;
					case MemberData.ID_FOUND:
						createNewGroupPanel.addToList(md.getID(), md.getMemberNo());
						break;
					case MemberData.ID_NOTFOUND:
						JOptionPane.showMessageDialog(null, "�������� �ʴ� ���̵� �Դϴ�.");
						logInPanel.clearField();
						break;
					case MemberData.REGISTER_SUCCESS:
						regisetPanel.clearField();
						switchingPanel(LOGIN);
						break;
					case MemberData.REGISTER_FAIL:
						JOptionPane.showMessageDialog(null, "�̹� ������� ���̵� �Դϴ�.");
						break;
					}
				} else if (data instanceof ScheduleData) {
					ScheduleData sd = (ScheduleData) data;
					switch (sd.getState()) {
					case ScheduleData.GET_PERSONAL_SCHEDULE:
						System.out.println(sd.getDate());
						mainPane.addPersnalSchedule(sd);
						System.out.println(sd.getState());
						break;
					case ScheduleData.CREATE_FAIL:
						JOptionPane.showMessageDialog(null, "�⼮���� 75% ���Ͽ��� �׷� ��� ����");
						break;
					case ScheduleData.CREATE_NEW_GROUP:
						switchingPanel(GROUPMANAGE);
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

	public void getInitialData() {
		sendRequest(new ScheduleData(ScheduleData.GET_PERSONAL_SCHEDULE, null, null));
		sendRequest(new ScheduleData(ScheduleData.GET_GROUP_SCHEDULE, null, null));
	}

	public void sendRequest(Object data) {
		try {
			oos.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
