import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	private int width = 400;
	private int height = 500;

	private JTextField groupNtf, placetf, memberIDtf;
	private JButton addBtn, deleteBtn, createGroupBtn, cancelBtn;
	private JList addedList;
	private ArrayList<String> idList = new ArrayList<>();
	private ArrayList<Integer> mnoList = new ArrayList<>();
	private MainFrame mf;

	public CreateNewGroupPanel(MainFrame mf) {
		this.mf = mf;
		setSize(width, height);
		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(3, 1, 0, 0));
		JPanel groupPanel = new JPanel();
		JLabel groupNLabel = new JLabel("\uADF8\uB8F9\uBA85");
		groupNtf = new JTextField(10);
		groupPanel.add(groupNLabel);
		groupPanel.add(groupNtf);
		panel.add(groupPanel);
		JPanel placePanel = new JPanel();
		JLabel placeLabel = new JLabel("  \uC7A5\uC18C  ");
		placetf = new JTextField(10);
		placePanel.add(placeLabel);
		placePanel.add(placetf);
		panel.add(placePanel);
		JPanel memberPanel = new JPanel();
		JLabel memberIDLabel = new JLabel("                      \uBA64\uBC84ID");
		memberIDtf = new JTextField(10);
		memberPanel.add(memberIDLabel);
		memberPanel.add(memberIDtf);
		addBtn = new JButton("\uCD94\uAC00");
		addBtn.addActionListener(this);
		memberPanel.add(addBtn);
		panel.add(memberPanel);

		JPanel panelCenter = new JPanel();
		add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout());
		JLabel label = new JLabel("      \uCD94\uAC00\uB41C \uBA64\uBC84");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		panelCenter.add(label, BorderLayout.NORTH);
		JScrollPane scroll = new JScrollPane();
		addedList = new JList<>();
		scroll.setViewportView(addedList);
		panelCenter.add(scroll, BorderLayout.CENTER);
		JPanel panelEast = new JPanel();
		deleteBtn = new JButton("삭제");
		deleteBtn.addActionListener(this);
		deleteBtn.setVerticalAlignment(SwingConstants.BOTTOM);
		deleteBtn.setHorizontalAlignment(SwingConstants.TRAILING);
		panelEast.add(deleteBtn);
		panelCenter.add(panelEast, BorderLayout.EAST);

		JPanel panelSouth = new JPanel();
		createGroupBtn = new JButton("그룹 등록");
		createGroupBtn.addActionListener(this);
		cancelBtn = new JButton("\uC774\uC804 \uD654\uBA74");
		cancelBtn.addActionListener(this);
		panelSouth.add(createGroupBtn);
		panelSouth.add(cancelBtn);
		add(panelSouth, BorderLayout.SOUTH);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		String groupN = "";
		String place = "";
		String memberID = "";
		if (arg0.getSource() == addBtn) { // 멤버 추가
			memberID = memberIDtf.getText();
			// 존재하는 아이디 인지 검사
			for (String string : idList) {
				if (string.equals(memberID)) {
					JOptionPane.showMessageDialog(null, "이미 추가한 멤버입니다.");
					return;
				}
			}
			mf.sendRequest(new MemberData(MemberData.FIND_ID, memberID, null));
			memberIDtf.setText("");
		} else if (arg0.getSource() == deleteBtn) {// 멤버 삭제
			int index = addedList.getSelectedIndex();
			idList.remove(index);
			mnoList.remove(index);
			addedList.setListData(idList.toArray());
		} else if (arg0.getSource() == createGroupBtn) {// 그룹 등록
			// 등록 진행
			groupN = groupNtf.getText();
			place = placetf.getText();
			mf.sendRequest(new ScheduleData(ScheduleData.CREATE_NEW_GROUP, groupN, place, mnoList));

			// 초기화
			memberIDtf.setText("");
			groupNtf.setText("");
			placetf.setText("");
			idList = new ArrayList<>();
			addedList.setListData(idList.toArray());

			mf.switchingPanel(MainFrame.GROUPMANAGE);
		} else if (arg0.getSource() == cancelBtn) { // 이전 화면
			memberIDtf.setText("");
			groupNtf.setText("");
			placetf.setText("");
			idList = new ArrayList<>();
			addedList.setListData(idList.toArray());

			mf.switchingPanel(MainFrame.GROUPMANAGE);
		}
	}
	
	public void addToList(String memberID, int mno){
		idList.add(memberID);
		mnoList.add(mno);
		addedList.setListData(idList.toArray());
	}

}
