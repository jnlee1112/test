import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.channels.ShutdownChannelGroupException;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MySchedulePanel extends JPanel implements ActionListener {

	private int width = 500;
	private int height = 400;

	private JButton cancleBtn;
	private JButton okBtn;
	private PossibleCalendar centerPanel;

	public MySchedulePanel() {
		setSize(width, height);
		setLayout(new BorderLayout());

		centerPanel = new PossibleCalendar();
		centerPanel.setPreferredSize(new Dimension(700, 650));
		JPanel southPanel = new JPanel();
		southPanel.setPreferredSize(new Dimension(600, 100));
		southPanel.setBackground(Color.DARK_GRAY);

		okBtn = new JButton("확인");
		okBtn.addActionListener(this);
		cancleBtn = new JButton("취소");
		cancleBtn.addActionListener(this);
		southPanel.add(okBtn);
		southPanel.add(cancleBtn);

		add(centerPanel);
		add(southPanel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okBtn) { // 확인 버튼
			MainFrame.getInstance()
					.sendRequest(new ScheduleData(ScheduleData.ADD_POSSIBLE_DATE, centerPanel.getDateList()));
			MainFrame.getInstance().switchingPanel(MainFrame.MAIN);
		}
		if (e.getSource() == cancleBtn) { // 취소 버튼
			MainFrame.getInstance().switchingPanel(MainFrame.MAIN);
		}
	}

	public void setPossibleDate(ArrayList<Date> dateList) {
		centerPanel.setPossibleDate(dateList);
		centerPanel.repaintCalendar(0);
	}

	public void addPersnalSchedule(Date date) {
		centerPanel.addPersnalSchedule(date);
	}

	public void updateCalendar() {
		centerPanel.updateCalendar();
	}
}
