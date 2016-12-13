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
		deleteBtn = new JButton("����");
		deleteBtn.addActionListener(this);
		deleteBtn.setVerticalAlignment(SwingConstants.BOTTOM);
		deleteBtn.setHorizontalAlignment(SwingConstants.TRAILING);
		panelEast.add(deleteBtn);
		panelCenter.add(panelEast, BorderLayout.EAST);

		JPanel panelSouth = new JPanel();
		createGroupBtn = new JButton("�׷� ���");
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
		if (arg0.getSource() == addBtn) { // ��� �߰�
			memberID = memberIDtf.getText();
			// �����ϴ� ���̵� ���� �˻�
			for (String string : idList) {
				if (string.equals(memberID)) {
					JOptionPane.showMessageDialog(null, "�̹� �߰��� ����Դϴ�.");
					return;
				}
			}
			mf.sendRequest(new MemberData(MemberData.FIND_ID, memberID, null));
			memberIDtf.setText("");
		} else if (arg0.getSource() == deleteBtn) {// ��� ����
			int index = addedList.getSelectedIndex();
			idList.remove(index);
			mnoList.remove(index);
			addedList.setListData(idList.toArray());
		} else if (arg0.getSource() == createGroupBtn) {// �׷� ���
			// ��� ����
			groupN = groupNtf.getText();
			place = placetf.getText();
			mf.sendRequest(new ScheduleData(ScheduleData.CREATE_NEW_GROUP, groupN, place, mnoList));

			// �ʱ�ȭ
			memberIDtf.setText("");
			groupNtf.setText("");
			placetf.setText("");
			idList = new ArrayList<>();
			addedList.setListData(idList.toArray());

			mf.switchingPanel(MainFrame.GROUPMANAGE);
		} else if (arg0.getSource() == cancelBtn) { // ���� ȭ��
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
