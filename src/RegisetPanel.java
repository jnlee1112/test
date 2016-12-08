import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisetPanel extends JPanel implements ActionListener {

	private int width = 300;
	private int height = 240;

	private JTextField tfName, tfID, tfPW;
	private JButton btnOk, btnCancle;
	private JTextField tfEmail;

	private MainFrame mf;

	public RegisetPanel(MainFrame mf) {
		this.mf = mf;
		setSize(width, height);
		setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel1 = new JPanel();
		JLabel namelb = new JLabel("    \uC774\uB984    ");
		tfName = new JTextField(10);
		panel1.add(namelb);
		panel1.add(tfName);

		JPanel panel2 = new JPanel();
		JLabel idlb = new JLabel("       ID       ");
		tfID = new JTextField(10);
		panel2.add(idlb);
		panel2.add(tfID);

		JPanel panel3 = new JPanel();
		JLabel passwordlb = new JLabel("비밀번호");
		tfPW = new JTextField(10);
		panel3.add(passwordlb);
		panel3.add(tfPW);

		JPanel panel4 = new JPanel();
		JLabel lblEmail = new JLabel("   E-Mail   ");
		tfEmail = new JTextField(10);
		panel4.add(lblEmail);
		panel4.add(tfEmail);

		JPanel panel5 = new JPanel();
		btnOk = new JButton("확인");
		btnOk.addActionListener(this);
		btnCancle = new JButton("취소");
		btnCancle.addActionListener(this);
		panel5.add(btnOk);
		panel5.add(btnCancle);

		add(panel1);
		add(panel2);
		add(panel3);
		add(panel4);
		add(panel5);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOk) {
			mf.sendRequest(new MemberData(MemberData.REGISTER, tfName.getText(), tfID.getText(), tfPW.getText(),
					tfEmail.getText()));
		} else if (e.getSource() == btnCancle) {
			clearField();
			mf.switchingPanel(MainFrame.LOGIN);
		}
	}

	public void clearField() {
		tfEmail.setText("");
		tfID.setText("");
		tfName.setText("");
		tfPW.setText("");
	}

}
