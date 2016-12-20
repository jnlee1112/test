import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GroupManagePanel extends JPanel implements ActionListener {

	private int width = 500;
	private int height = 500;

	private ArrayList<ScheduleData> list;

	private int index;
	private JButton createNewGroupBtn;
	private JButton backToMainBtn;
	private JPanel centerPanel;
	private JTextArea textArea;
	private JButton leftBtn;
	private JButton rightBtn;
	private JButton xBtn;
	private JScrollPane scrollPane;
	private JButton oBtn;

	public GroupManagePanel() {
		list = new ArrayList<>();
		setSize(width, height);
		setLayout(new BorderLayout());

		centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setSize(width, 500);
		JPanel southPanel = new JPanel();
		southPanel.setPreferredSize(new Dimension(width, 100));
		southPanel.setBackground(Color.WHITE);

		leftBtn = new JButton();
		leftBtn.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/left.png", 50, 50));

		leftBtn.setForeground(new Color(0, 0, 0));
		leftBtn.setBackground(new Color(0, 0, 51));
		leftBtn.setBounds(12, 174, 48, 58);
		leftBtn.setBorderPainted(false);
		leftBtn.setFocusPainted(false);
		leftBtn.setContentAreaFilled(false);

		centerPanel.add(leftBtn);
		leftBtn.addActionListener(this);

		rightBtn = new JButton();
		rightBtn.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/right.png", 50, 50));
		rightBtn.setBorderPainted(false);
		rightBtn.setFocusPainted(false);
		rightBtn.setContentAreaFilled(false);
		rightBtn.setBounds(415, 174, 62, 58);
		rightBtn.setOpaque(false);
		centerPanel.add(rightBtn);
		rightBtn.addActionListener(this);

		createNewGroupBtn = new JButton("새그룹 추가");
		createNewGroupBtn.addActionListener(this);
		backToMainBtn = new JButton("이전 화면");
		backToMainBtn.addActionListener(this);

		add(centerPanel);
		centerPanel.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(94, 97, 300, 209);
		scrollPane.setBorder(null);
		centerPanel.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
		scrollPane.setViewportView(textArea);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(15, 31, 46));
		panel.setBounds(0, 320, 500, 122);
		centerPanel.add(panel);
		panel.setLayout(null);

		oBtn = new JButton("");
		oBtn.setBorderPainted(false);
		oBtn.setFocusPainted(false);
		oBtn.setContentAreaFilled(false);
		oBtn.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/okBtn.png", 80, 80));
		oBtn.setBounds(93, 21, 119, 71);
		oBtn.addActionListener(this);
		panel.add(oBtn);

		xBtn = new JButton();
		xBtn.setBounds(281, 10, 97, 88);
		xBtn.setBorderPainted(false);
		xBtn.setFocusPainted(false);
		xBtn.setContentAreaFilled(false);
		xBtn.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/cancelBtn.png", 80, 80));
		xBtn.addActionListener(this);
		panel.add(xBtn);

	}

	public void updateTextArea() {
		if (list.isEmpty()) {
			textArea.setText("");
			return;
		}
		textArea.setText("");
		String groupName = "<그룹명>" + list.get(index).getGrName() + "\n\r";
		String groupDate = "<날짜>" + list.get(index).getDate() + "\n\r";
		String groupMember = "<멤버>  " + list.get(index).getMemberNameL().size() + " 명\n\r";
		for (String mn : list.get(index).getMemberNameL()) {
			groupMember += mn + "\n\r";
		}
		textArea.append(groupName);
		textArea.append(groupDate);
		textArea.append(groupMember);
	}

	public void actionPerformed(ActionEvent e) {
		if (list.isEmpty()) {
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
			int i = list.get(index).getGrno();
			MainFrame.getInstance().sendRequest(new ScheduleData(ScheduleData.AGREE, true, i));
			if (JOptionPane.showConfirmDialog(null, "장소 베팅을 하시겠습니까?", null,
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				doBetting(i);
			} else {
				MainFrame.getInstance()
						.sendRequest(new ScheduleData(ScheduleData.BET, null, -1, list.get(index).getGrno()));
			}
			MainFrame.getInstance().getInitialData();
		}
		if (e.getSource() == xBtn) {
			MainFrame.getInstance().sendRequest(new ScheduleData(ScheduleData.AGREE, false, list.get(index).getGrno()));
			MainFrame.getInstance().getInitialData();
			JOptionPane.showMessageDialog(null, "삭제되었습니다.");
		}
	}

	private void doBetting(int i) {
		JFrame f = new JFrame();
		f.getContentPane().setBackground(new Color(128, 255, 128));
		f.setSize(340, 130);
		f.setLayout(null);
		JLabel lbl1 = new JLabel("장소");
		lbl1.setBounds(20, 0, 50, 50);
		JTextField jtf1 = new JTextField(15);
		jtf1.setBounds(60, 10, 120, 30);
		JLabel lbl2 = new JLabel("금액");
		lbl2.setBounds(20, 40, 50, 50);
		JTextField jtf2;
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
		DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
		decimalFormat.setGroupingUsed(false);
		jtf2 = new JFormattedTextField(decimalFormat);
		jtf2.setColumns(15); // whatever size you wish to set
		jtf2.setBounds(60, 50, 120, 30);
		JButton btn = new JButton("SEND");
		btn.addActionListener(e -> {
			try{
				MainFrame.getInstance().sendRequest(new ScheduleData(ScheduleData.BET, jtf1.getText(),
						Integer.parseInt(jtf2.getText()), i));
				f.dispose();
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(null, "금액은 숫자로 입력하세요");
			}
		});
		btn.setBounds(200, 20, 100, 50);
		f.add(lbl1);
		f.add(jtf1);
		f.add(lbl2);
		f.add(jtf2);
		f.add(btn);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public void updateGUI() {
		list = MainFrame.getInstance().getNotFixedList();
		index = 0;
		updateTextArea();
	}
}
