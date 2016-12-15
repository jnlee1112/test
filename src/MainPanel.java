import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements ActionListener {
	private int width = 400;
	private int height = 400;

	private JButton groupManageBtn;
	private JButton myScheduleBtn;
	private MainCalendar centerPanel;
	private JButton btnLogout;

	public MainPanel() {
		setSize(width, height);
		setLayout(new BorderLayout());

		centerPanel = new MainCalendar();
		centerPanel.setPreferredSize(new Dimension(700, 650));
		JPanel southPanel = new JPanel();
		southPanel.setPreferredSize(new Dimension(600, 70));
		southPanel.setBackground(Color.DARK_GRAY);

		myScheduleBtn = new JButton("�� ���� ����");
		myScheduleBtn.addActionListener(this);
		groupManageBtn = new JButton("�׷� ����");
		groupManageBtn.addActionListener(this);
		southPanel.add(myScheduleBtn);
		southPanel.add(groupManageBtn);

		add(centerPanel);
		
		btnLogout = new JButton("Logout");
		btnLogout.setBounds(291, 297, 97, 23);
		centerPanel.add(btnLogout);
		add(southPanel, BorderLayout.SOUTH);
		btnLogout.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == myScheduleBtn) { // �� ���� ���� ��ư
			MainFrame.getInstance().getInitialData();
			MainFrame.getInstance().switchingPanel(MainFrame.MYSCHEDULE);
		}
		if (e.getSource() == groupManageBtn) { // �׷� ���� ��ư
			MainFrame.getInstance().switchingPanel(MainFrame.GROUPMANAGE);
		}
		if (e.getSource() == btnLogout){ //�α� �ƿ� ��ư
			//MainFrame.getInstance().sendRequest(new MemberData(MemberData.DISCONNECT, null, null));
			MainFrame.getInstance().switchingPanel(MainFrame.LOGIN);
		}
	}

	public void updateGUI() {
		centerPanel.updateCalendar();
	}
}
