import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class UpdateMemberPanel extends JPanel implements ActionListener {

	private int width = 410;
	private int height = 295;

	private JTextField tfName, tfID, tfPW;
	private JButton btnOk, btnCancle;
	private JButton btnOk_1;
	private JTextField tfEmail;
	private JFrame f;

	public UpdateMemberPanel(JFrame f) {
		this.f = f;
		setSize(width, height);

		JPanel panel3 = new JPanel();
		panel3.setBackground(new Color(15, 31, 46));
		panel3.setBounds(0, 0, 410, 296);
		panel3.setLayout(null);
		setLayout(null);
		btnOk = new JButton();
		btnOk.setBorderPainted(false);
		btnOk.setFocusPainted(false);
		btnOk.setContentAreaFilled(false);
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
		btnOk_1 = new JButton();
		btnOk_1.setForeground(Color.WHITE);
		btnOk_1.setBorderPainted(false);
		btnOk_1.setFocusPainted(false);
		btnOk_1.setContentAreaFilled(false);
		btnOk_1.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/update.PNG", 100, 50));
		btnOk_1.setBackground(new Color(247, 211, 27));
		btnOk_1.setFont(new Font("HY강B", Font.PLAIN, 12));
		btnOk_1.setBounds(82, 244, 110, 23);
		panel3.add(btnOk_1);
		btnCancle = new JButton();
		btnCancle.setBackground(Color.GRAY);
		btnOk_1.setBorderPainted(false);
		btnOk_1.setFocusPainted(false);
		btnOk_1.setContentAreaFilled(false);
		btnCancle.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/cancelB.PNG", 100, 50));
		btnCancle.setFont(new Font("HY강B", Font.PLAIN, 12));
		btnCancle.setBounds(216, 244, 102, 23);
		panel3.add(btnCancle);

		JLabel lblNewLabel = new JLabel("U P D A T E");
		lblNewLabel.setForeground(new Color(247, 211, 27));
		lblNewLabel.setFont(new Font("HY강B", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(144, 10, 116, 31);
		panel3.add(lblNewLabel);
		btnCancle.addActionListener(this);
		btnOk_1.addActionListener(this);

		tfName.setText(MainFrame.getInstance().getUserName());
		tfID.setText(MainFrame.getInstance().getUserID());
		tfID.setEditable(false);
		tfPW.setText(MainFrame.getInstance().getUserPW());
		tfEmail.setText(MainFrame.getInstance().getUserEmail());

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOk_1) {
			MainFrame.getInstance().sendRequest(new MemberData(MemberData.UPDATE_MEMBER, tfName.getText(),
					tfID.getText(), tfPW.getText(), tfEmail.getText()));
			MainFrame.getInstance().setUserPW(tfPW.getText());
			clearField();
		} else if (e.getSource() == btnCancle) {
			clearField();
			MainFrame.getInstance().switchingPanel(MainFrame.MAIN);
		}
		f.dispose();
	}

	public void clearField() {
		tfEmail.setText("");
		tfID.setText("");
		tfName.setText("");
		tfPW.setText("");
	}

}