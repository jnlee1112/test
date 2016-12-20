import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LogInPanel extends JPanel implements ActionListener {
	private int width = 295;
	private int height = 450;

	private JTextField tfID;
	private JPasswordField tfPW;
	private JButton btnLogIn;
	private JButton btnRegiser;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;

	public LogInPanel() {
		setSize(width, height);
		setBackground(new Color(15, 31, 46));

		btnLogIn = new JButton();
		btnLogIn.setBorderPainted(false);
		btnLogIn.setFocusPainted(false);
		btnLogIn.setContentAreaFilled(false);
		btnLogIn.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/login.png", 250, 50));
		btnLogIn.setBounds(25, 292, 240, 36);
		btnLogIn.setBackground(new Color(114, 82, 83));
		btnLogIn.setForeground(new Color(255, 255, 255));
		btnLogIn.addActionListener(this);
		setLayout(null);
		add(btnLogIn);

		btnRegiser = new JButton();
		btnRegiser.setBorderPainted(false);
		btnRegiser.setFocusPainted(false);
		btnRegiser.setContentAreaFilled(false);
		btnRegiser.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/register.png", 250, 50));
		btnRegiser.setBounds(25, 341, 240, 42);
		btnRegiser.addActionListener(this);
		add(btnRegiser);

		tfID = new JTextField();
		tfID.setBounds(74, 196, 191, 35);
		tfID.setColumns(10);
		add(tfID);

		tfPW = new JPasswordField();
		tfPW.setBounds(74, 235, 191, 33);
		tfPW.setColumns(10);
		add(tfPW);

		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 290, 130);
		add(panel);

		lblNewLabel_3 = new JLabel();
		lblNewLabel_3.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/IconImage.png", 110, 100));
		panel.add(lblNewLabel_3);

		lblNewLabel = new JLabel("L O G I N");
		lblNewLabel.setFont(new Font("HY강B", Font.BOLD, 25));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(74, 153, 136, 24);
		add(lblNewLabel);

		lblNewLabel_1 = new JLabel();
		lblNewLabel_1.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/id.png", 50, 60));
		lblNewLabel_1.setBounds(28, 196, 45, 35);
		add(lblNewLabel_1);

		lblNewLabel_2 = new JLabel();
		lblNewLabel_2.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/pw.png", 50, 60));
		lblNewLabel_2.setBounds(28, 235, 45, 33);
		add(lblNewLabel_2);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLogIn) { // 로그인 버튼
			MainFrame.getInstance()
					.sendRequest(new MemberData(MemberData.FIRST_CONNECT, tfID.getText(), tfPW.getText()));
		}
		if (e.getSource() == btnRegiser) { // 회원등록 버튼
			MainFrame.getInstance().switchingPanel(MainFrame.REGISTER);
		}
	}

	public void clearPWField() {
		tfPW.setText("");
	}

	public void clearIDField() {
		tfID.setText("");
	}
}
