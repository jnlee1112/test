import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LogInPanel extends JPanel implements ActionListener {
	private int width = 290;
	private int height = 450;

	private MainFrame mf;
	private JTextField tfID;
	private JTextField tfPW;
	private JButton btnLogIn;
	private JButton btnRegiser;

	public LogInPanel(MainFrame mf) {
		this.mf = mf;
		setSize(width, height);
		setLayout(null);

		btnLogIn = new JButton("\uB85C\uADF8\uC778");

		btnLogIn.setBounds(24, 280, 237, 36);
		btnLogIn.setBackground(new Color(114, 82, 83));
		btnLogIn.setForeground(new Color(255, 255, 255));
		btnLogIn.addActionListener(this);
		add(btnLogIn);

		btnRegiser = new JButton("\uD68C\uC6D0\uAC00\uC785");
		btnRegiser.setBounds(28, 340, 232, 42);
		btnRegiser.addActionListener(this);
		add(btnRegiser);

		tfID = new JTextField();
		tfID.setBounds(24, 195, 237, 35);
		tfID.setColumns(10);
		add(tfID);

		tfPW = new JTextField();
		tfPW.setBounds(24, 234, 236, 33);
		tfPW.setColumns(10);
		add(tfPW);

		ImageIcon ii = new ImageIcon("kakaotwitbook.png");
		JLabel lblLogInTheme = new JLabel(ii);
		lblLogInTheme.setBounds(0, -11, 284, 523);
		add(lblLogInTheme);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLogIn) { // 로그인 버튼
			mf.sendRequest(new MemberData(MemberData.FIRST_CONNECT, tfID.getText(), tfPW.getText()));
		}
		if (e.getSource() == btnRegiser) { // 회원등록 버튼
			mf.switchingPanel(MainFrame.REGISERT);
		}
	}

	public void clearField() {
		tfPW.setText("");
	}

}
