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

	private MainFrame mf;
	private JButton groupManageBtn;
	private JButton myScheduleBtn;

	public MainPanel(MainFrame mf) {
		this.mf = mf;
		setSize(width, height);
		setLayout(new BorderLayout());

		MyCalendar centerPanel = new MyCalendar();
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
		add(southPanel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == myScheduleBtn) { // �� ���� ���� ��ư
			mf.switchingPanel(MainFrame.MYSCHEDULE);
		}
		if (e.getSource() == groupManageBtn) { // �׷� ���� ��ư
			mf.switchingPanel(MainFrame.GROUPMANAGE);
		}
	}
}
