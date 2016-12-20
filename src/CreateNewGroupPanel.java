import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CreateNewGroupPanel extends JPanel implements ActionListener {

	private int width = 500;
	private int height = 500;

	private JTextField groupNtf, memberIDtf;
	private JButton addBtn, deleteBtn, createGroupBtn;
	private JList addedList;
	private ArrayList<String> idList = new ArrayList<>();
	private ArrayList<Integer> mnoList = new ArrayList<>();

	public CreateNewGroupPanel() {
		setSize(width, height);
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 500, 100);
		add(panel);
		panel.setLayout(null);
		JPanel groupPanel = new JPanel();
		groupPanel.setBounds(0, 0, 500, 45);
		groupPanel.setBackground(new Color(255, 225, 96));
		JLabel groupNLabel = new JLabel("\uADF8\uB8F9\uBA85");
		groupNLabel.setFont(new Font("HY강B", Font.PLAIN, 14));
		groupNLabel.setBounds(129, 7, 57, 15);
		groupNtf = new JTextField(10);
		groupNtf.setBounds(201, 5, 116, 21);
		groupPanel.setLayout(null);
		groupPanel.add(groupNLabel);
		groupPanel.add(groupNtf);
		panel.add(groupPanel);
		JPanel memberPanel = new JPanel();
		memberPanel.setBounds(0, 42, 500, 58);
		memberPanel.setBackground(new Color(255, 225, 96));
		JLabel memberIDLabel = new JLabel("\uBA64\uBC84ID");
		memberIDLabel.setFont(new Font("HY강B", Font.PLAIN, 14));
		memberIDLabel.setBounds(130, 12, 55, 15);
		memberIDtf = new JTextField(10);
		memberIDtf.setBounds(198, 10, 116, 21);
		memberPanel.setLayout(null);
		memberPanel.add(memberIDLabel);
		memberPanel.add(memberIDtf);
		addBtn = new JButton("");
		addBtn.setBounds(333, 0, 68, 34);

		addBtn.setBorderPainted(false);
		addBtn.setFocusPainted(false);
		addBtn.setContentAreaFilled(false);
		addBtn.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/addBtn.png", 20, 20));
		addBtn.addActionListener(this);
		memberPanel.add(addBtn);
		panel.add(memberPanel);

		JPanel panelCenter = new JPanel();
		panelCenter.setBounds(0, 99, 500, 364);
		add(panelCenter);
		panelCenter.setBackground(new Color(15, 31, 46));
		panelCenter.setLayout(null);
		JLabel label = new JLabel("                      \uCD94\uAC00\uB41C \uBA64\uBC84");
		label.setFont(new Font("HY강B", Font.PLAIN, 14));
		label.setForeground(Color.WHITE);
		label.setBounds(0, 0, 500, 40);
		label.setBackground(new Color(247, 211, 27));
		label.setHorizontalAlignment(SwingConstants.LEFT);
		panelCenter.add(label);
		JScrollPane scroll = new JScrollPane();
		scroll.setBorder(null);
		scroll.setBounds(0, 39, 428, 230);
		addedList = new JList<>();
		addedList.setBorder(null);
		addedList.setBackground(Color.WHITE);
		scroll.setViewportView(addedList);
		panelCenter.add(scroll);
		JPanel panelEast = new JPanel();
		panelEast.setBorder(null);
		panelEast.setBounds(427, 39, 73, 230);
		panelEast.setBackground(Color.WHITE);
		deleteBtn = new JButton("");

		deleteBtn.setBorderPainted(false);
		deleteBtn.setFocusPainted(false);
		deleteBtn.setContentAreaFilled(false);
		deleteBtn.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/deleteBtn.png", 40, 40));
		deleteBtn.setBounds(12, 10, 55, 57);
		deleteBtn.addActionListener(this);
		panelEast.setLayout(null);
		deleteBtn.setVerticalAlignment(SwingConstants.BOTTOM);
		deleteBtn.setHorizontalAlignment(SwingConstants.TRAILING);
		panelEast.add(deleteBtn);
		panelCenter.add(panelEast);

		JPanel panelSouth = new JPanel();
		panelSouth.setBounds(0, 270, 500, 61);
		panelCenter.add(panelSouth);
		panelSouth.setBackground(new Color(15, 31, 46));
		panelSouth.setLayout(null);
		createGroupBtn = new JButton();
		createGroupBtn.setBounds(146, 10, 235, 43);
		panelSouth.add(createGroupBtn);
		createGroupBtn.setBorderPainted(false);
		createGroupBtn.setFocusPainted(false);
		createGroupBtn.setContentAreaFilled(false);
		createGroupBtn.addActionListener(this);

		createGroupBtn.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/groupAdd.png", 180, 60));
		setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		String groupN = "";
		String place = "";
		String memberID = "";
		if (arg0.getSource() == addBtn) { // 멤버 추가
			memberID = memberIDtf.getText();
			if (MainFrame.getInstance().getUserID().equals(memberID)) {
				JOptionPane.showMessageDialog(null, "본인 입니다.");
				return;
			}
			// 존재하는 아이디 인지 검사
			for (String string : idList) {
				if (string.equals(memberID)) {
					JOptionPane.showMessageDialog(null, "이미 추가한 멤버입니다.");
					return;
				}
			}
			MainFrame.getInstance().sendRequest(new MemberData(MemberData.FIND_ID, memberID, null));
			memberIDtf.setText("");
		} else if (arg0.getSource() == deleteBtn) {// 멤버 삭제
			int index = addedList.getSelectedIndex();
			idList.remove(index);
			mnoList.remove(index);
			addedList.setListData(idList.toArray());
		} else if (arg0.getSource() == createGroupBtn) {// 그룹 등록
			if (mnoList.isEmpty()) {
				JOptionPane.showMessageDialog(null, "추가한 그룹원이 없습니다.");
				return;
			}
			// 등록 진행
			groupN = groupNtf.getText();
			MainFrame.getInstance()
					.sendRequest(new ScheduleData(ScheduleData.CREATE_NEW_GROUP, groupN, place, mnoList));
			MainFrame.getInstance().getInitialData();
		}
	}

	public void addToList(String memberID, int mno) {
		idList.add(memberID);
		mnoList.add(mno);
		addedList.setListData(idList.toArray());
	}

	public void clearField() {
		memberIDtf.setText("");
		groupNtf.setText("");
		idList = new ArrayList<>();
		mnoList = new ArrayList<>();
		addedList.setListData(idList.toArray());
	}

}
