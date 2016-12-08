import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GroupManagePanel extends JPanel implements ActionListener {

	private int width = 400;
	private int height = 500;
	private ArrayList<MessagePanel> list = new ArrayList<>();
	private MainFrame mf;
	private JButton createNewGroupBtn;
	private JButton backToMainBtn;
	private JPanel centerPanel;

	public GroupManagePanel(MainFrame mf) {
		this.mf = mf;
		setSize(width, height);
		setLayout(new BorderLayout());

		centerPanel = new JPanel();
		centerPanel.setSize(width, 500);
		JPanel southPanel = new JPanel();
		southPanel.setPreferredSize(new Dimension(width, 100));
		southPanel.setBackground(Color.DARK_GRAY);

		createNewGroupBtn = new JButton("새그룹 추가");
		createNewGroupBtn.addActionListener(this);
		backToMainBtn = new JButton("이전 화면");
		backToMainBtn.addActionListener(this);
		southPanel.add(createNewGroupBtn);
		southPanel.add(backToMainBtn);
		add(centerPanel);
		add(southPanel, BorderLayout.SOUTH);
	}

	class MessagePanel extends JPanel implements ActionListener {
		JLabel message;
		JButton agreeBtn = new JButton(" O ");
		JButton disagreelBtn = new JButton(" X ");

		public MessagePanel(String str) {
			setPreferredSize(new Dimension(width, 40));
			message = new JLabel(str);
			agreeBtn.addActionListener(this);
			disagreelBtn.addActionListener(this);
			add(message);
			add(agreeBtn);
			add(disagreelBtn);
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == agreeBtn) {
				System.out.println("동의");
			}
			if (e.getSource() == disagreelBtn) {
				System.out.println("거부");
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == createNewGroupBtn) {
			mf.switchingPanel(MainFrame.CREATEGROUP);
		}
		if (e.getSource() == backToMainBtn) {
			mf.switchingPanel(MainFrame.MAIN);
		}
	}
}
