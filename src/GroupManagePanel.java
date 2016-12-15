import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;

public class GroupManagePanel extends JPanel implements ActionListener {

	private int width = 400;
	private int height = 500;

	private ArrayList<ScheduleData> list;

	private int index;
	private JButton createNewGroupBtn;
	private JButton backToMainBtn;
	private JPanel centerPanel;
	private JTextArea textArea;
	private JButton leftBtn;
	private JButton rightBtn;
	private JButton oBtn;
	private JButton xBtn;

	public GroupManagePanel() {
		list = new ArrayList<>();
		setSize(width, height);
		setLayout(new BorderLayout());

		centerPanel = new JPanel();
		centerPanel.setBackground(new Color(255, 250, 205));
		centerPanel.setSize(width, 500);
		JPanel southPanel = new JPanel();
		southPanel.setPreferredSize(new Dimension(width, 100));
		southPanel.setBackground(new Color(128, 128, 128));

		textArea = new JTextArea();
		textArea.setBounds(86, 74, 228, 191);
		centerPanel.add(textArea);

		leftBtn = new JButton("\u25C0");
		leftBtn.setForeground(new Color(0, 0, 0));
		leftBtn.setBackground(new Color(255, 255, 0));
		leftBtn.setBounds(12, 154, 62, 45);
		centerPanel.add(leftBtn);
		leftBtn.addActionListener(this);

		rightBtn = new JButton("\u25B6");
		rightBtn.setBounds(326, 154, 62, 45);
		centerPanel.add(rightBtn);
		rightBtn.addActionListener(this);

		oBtn = new JButton("O");
		oBtn.setBounds(86, 314, 97, 23);
		centerPanel.add(oBtn);
		oBtn.addActionListener(this);

		xBtn = new JButton("X");
		xBtn.setBounds(217, 314, 97, 23);
		centerPanel.add(xBtn);
		xBtn.addActionListener(this);

		createNewGroupBtn = new JButton("새그룹 추가");
		createNewGroupBtn.addActionListener(this);
		backToMainBtn = new JButton("이전 화면");
		backToMainBtn.addActionListener(this);
		southPanel.add(createNewGroupBtn);
		southPanel.add(backToMainBtn);
		add(centerPanel);
		centerPanel.setLayout(null);

		add(southPanel, BorderLayout.SOUTH);
	}

	public void updateTextArea() {
		if (list.isEmpty())
			return;
		textArea.setText("");
		String groupName = "<그룹명>" + list.get(index) + "\n";
		String groupDate = "<날짜>" + list.get(index).getDate() + "\n";
		String groupMember = "<멤버>";
		for (String mn : list.get(index).getMemberNameL()) {
			groupMember += mn + " ";
		}
		textArea.append(groupName);
		textArea.append(groupDate);
		textArea.append(groupMember);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == createNewGroupBtn) {
			MainFrame.getInstance().switchingPanel(MainFrame.CREATEGROUP);
		}
		if (e.getSource() == backToMainBtn) {
			MainFrame.getInstance().switchingPanel(MainFrame.MAIN);
		}
		if (list.size() == 0) {
			return;
		}
		if (e.getSource() == leftBtn) {
			if (index > 0) {
				index--;
				updateTextArea();
			} else {
				JOptionPane.showMessageDialog(null, "첫번째 알림입니다.");
			}
		}
		if (e.getSource() == rightBtn) {
			if (index < list.size() - 1) {
				index++;
				updateTextArea();
			} else {
				JOptionPane.showMessageDialog(null, "마지막 알림입니다.");
			}
		}
		if (e.getSource() == oBtn) {
			MainFrame.getInstance().sendRequest(new ScheduleData(ScheduleData.AGREE, true, list.get(index).getGrno()));
			MainFrame.getInstance().sendRequest(new ScheduleData(ScheduleData.GROUP_MANAGE));
		}
		if (e.getSource() == xBtn) {
			MainFrame.getInstance().sendRequest(new ScheduleData(ScheduleData.AGREE, false, list.get(index).getGrno()));
			MainFrame.getInstance().sendRequest(new ScheduleData(ScheduleData.GROUP_MANAGE));
		}
	}

	public void updateGUI() {
		list = MainFrame.getInstance().getNotFixedList();
		updateTextArea();
	}
}
