import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MySchedulePanel extends JPanel implements ActionListener {

	private int width = 1000;
	private int height = 700;

	private JButton cancleBtn;
	private JButton okBtn;

	public MySchedulePanel() {
		setSize(width, height);
		setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel();
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
			MainFrame.getInstance().switchingPanel(MainFrame.MAIN);
		}
		if (e.getSource() == cancleBtn) { // 취소 버튼
			MainFrame.getInstance().switchingPanel(MainFrame.MAIN);
		}
	}
}
