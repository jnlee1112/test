import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SlidePanel extends JPanel implements ActionListener {
	private JButton btnSlidePanel;
	private JButton btnLogOut;
	private JButton btnInfoUpdate;
	private JButton btnOut;

	public SlidePanel() {
		setBackground(new Color(128, 0, 0));
		setLayout(null);
		setSize(80, 190);

		btnSlidePanel = new JButton();
		btnSlidePanel.setBackground(new Color(15, 31, 46));
		btnSlidePanel.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/setting.png", 30, 30));
		btnSlidePanel.setBorder(null);
		btnSlidePanel.addActionListener(e -> {
			MainFrame.getInstance().slideUp();
		});
		btnSlidePanel.setBounds(0, 0, 80, 39);
		add(btnSlidePanel);

		btnLogOut = new JButton("\uB85C\uADF8\uC544\uC6C3");
		btnLogOut.addActionListener(e -> {
			if (JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?", null, JOptionPane.YES_NO_OPTION) == 0)
				MainFrame.getInstance().logOut();
		});
		btnLogOut.setOpaque(false);
		btnLogOut.setBorder(null);
		btnLogOut.setBackground(new Color(128, 0, 0));
		btnLogOut.setForeground(new Color(255, 255, 255));
		btnLogOut.setBounds(0, 40, 80, 50);
		add(btnLogOut);

		btnInfoUpdate = new JButton("\uC815\uBCF4 \uC218\uC815");
		btnInfoUpdate.setOpaque(false);
		btnInfoUpdate.setBorder(null);
		btnInfoUpdate.setBackground(new Color(128, 0, 0));
		btnInfoUpdate.setForeground(new Color(255, 255, 255));
		btnInfoUpdate.setBounds(0, 90, 80, 50);
		btnInfoUpdate.addActionListener(this);
		add(btnInfoUpdate);

		btnOut = new JButton("\uD68C\uC6D0 \uD0C8\uD1F4");
		btnOut.setOpaque(false);
		btnOut.setBorder(null);
		btnOut.setBackground(new Color(128, 0, 0));
		btnOut.setForeground(new Color(255, 255, 255));
		btnOut.setBounds(0, 140, 80, 50);
		btnOut.addActionListener(this);
		add(btnOut);

	}

	public void actionPerformed(ActionEvent e) {
		String userID = MainFrame.getInstance().getUserID();
		String userPW = MainFrame.getInstance().getUserPW();
		boolean con = true;
		while (con) {
			String pw = JOptionPane.showInputDialog("비밀번호");
			if (pw == null) {
				con = false;
			} else if (!pw.equals(userPW)) {
				JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
			} else {
				if (e.getSource() == btnInfoUpdate) {
					JFrame f = new JFrame("정보수정");
					UpdateMemberPanel update = new UpdateMemberPanel(f);
					f.add(update);
					f.setSize(update.getWidth(), update.getHeight()+20);
					f.setResizable(false);
					f.setLocationRelativeTo(null);
					f.setVisible(true);
					// f.pack();
				} else if (e.getSource() == btnOut) {
					int op = JOptionPane.showConfirmDialog(null, "정말로 탈퇴하시겠습니까?", "탈퇴", JOptionPane.YES_NO_OPTION);
					if (op == JOptionPane.YES_OPTION) {
						MainFrame.getInstance().sendRequest(new MemberData(MemberData.DELETE_ID, userID, userPW));
						MainFrame.getInstance().switchingPanel(MainFrame.LOGIN);
						MainFrame.getInstance().slideDown();
					}
				}
				con = false;
			}
		}

	}
}
