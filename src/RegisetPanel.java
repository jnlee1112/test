import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RegisetPanel extends JPanel implements ActionListener {

	private int width = 410;
	private int height = 450;

	private JTextField tfName, tfID, tfPW;
	private JButton btnOk, btnCancle;
	private JTextField tfEmail;

	public RegisetPanel() {
		setSize(width, height);

		JPanel panel1 = new JPanel();
		panel1.setBackground(Color.WHITE);
		panel1.setBounds(0, 0, 400, 123);
		panel1.setLayout(null);

		JPanel panel3 = new JPanel();
		panel3.setBackground(new Color(15, 31, 46));
		panel3.setBounds(0, 123, 400, 287);
		panel3.setLayout(null);
		setLayout(null);

		add(panel1);

		JLabel lblNewLabel_1 = new JLabel("");
		btnOk = new JButton();
		btnOk.setBorderPainted(false);
		btnOk.setFocusPainted(false);
		btnOk.setContentAreaFilled(false);
		lblNewLabel_1.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/IconImage.png", 80, 80));
		lblNewLabel_1.setBounds(153, 10, 139, 113);
		panel1.add(lblNewLabel_1);
		add(panel3);
		JLabel namelb = new JLabel("\uC774\uB984");
		namelb.setHorizontalAlignment(SwingConstants.CENTER);
		namelb.setFont(new Font("HY강B", Font.PLAIN, 14));
		namelb.setForeground(Color.WHITE);
		namelb.setBounds(99, 54, 73, 15);
		panel3.add(namelb);
		tfPW = new JTextField(10);
		tfPW.setBounds(184, 147, 116, 21);
		panel3.add(tfPW);
		JLabel idlb = new JLabel("ID");
		idlb.setHorizontalAlignment(SwingConstants.CENTER);
		idlb.setFont(new Font("HY강B", Font.PLAIN, 14));
		idlb.setForeground(Color.WHITE);
		idlb.setBounds(99, 102, 79, 15);
		panel3.add(idlb);
		tfName = new JTextField(10);
		tfName.setBounds(184, 51, 116, 21);
		panel3.add(tfName);
		tfID = new JTextField(10);
		tfID.setBounds(184, 99, 116, 21);
		panel3.add(tfID);
		JLabel lblEmail = new JLabel("E-Mail");
		lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmail.setFont(new Font("HY강B", Font.PLAIN, 14));
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setBounds(99, 198, 76, 15);
		panel3.add(lblEmail);
		JLabel passwordlb = new JLabel("비밀번호");
		passwordlb.setFont(new Font("HY강B", Font.PLAIN, 14));
		passwordlb.setHorizontalAlignment(SwingConstants.CENTER);
		passwordlb.setForeground(Color.WHITE);
		passwordlb.setBounds(99, 150, 69, 15);
		panel3.add(passwordlb);
		tfEmail = new JTextField(10);
		tfEmail.setBounds(184, 195, 116, 21);
		panel3.add(tfEmail);
		btnOk = new JButton();
		btnOk.setBorderPainted(false);
		btnOk.setFocusPainted(false);
		btnOk.setContentAreaFilled(false);
		btnOk.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/registerBtn.png", 100, 50));
		btnOk.setBackground(new Color(247, 211, 27));
		btnOk.setFont(new Font("HY강B", Font.PLAIN, 12));
		btnOk.setBounds(82, 244, 110, 23);
		panel3.add(btnOk);
		btnCancle = new JButton();
		btnCancle.setBackground(Color.GRAY);
		btnOk.setBorderPainted(false);
		btnOk.setFocusPainted(false);
		btnOk.setContentAreaFilled(false);
		btnCancle.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/cancelBtn2.png", 100, 50));
		btnCancle.setFont(new Font("HY강B", Font.PLAIN, 12));
		btnCancle.setBounds(216, 244, 102, 23);
		panel3.add(btnCancle);

		JLabel lblNewLabel = new JLabel("REGISTER");
		lblNewLabel.setForeground(new Color(247, 211, 27));
		lblNewLabel.setFont(new Font("HY강B", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(144, 10, 96, 31);
		panel3.add(lblNewLabel);
		btnCancle.addActionListener(this);
		btnOk.addActionListener(this);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOk) {
			MainFrame.getInstance().sendRequest(new MemberData(MemberData.REGISTER, tfName.getText(), tfID.getText(),
					tfPW.getText(), tfEmail.getText()));
		} else if (e.getSource() == btnCancle) {
			clearField();
			MainFrame.getInstance().switchingPanel(MainFrame.LOGIN);
		}
	}

	public void clearField() {
		tfEmail.setText("");
		tfID.setText("");
		tfName.setText("");
		tfPW.setText("");
	}

}
