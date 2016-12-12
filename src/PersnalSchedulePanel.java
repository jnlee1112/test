
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PersnalSchedulePanel extends JPanel {

	private int width = 350;
	private int height = 300;
	private JLabel lblTitle, lblPlace;
	private JTextField tfTitle, tfPlace;
	private JButton btnOk, btnDelete;
	private JTextField textField;
	private JTextField textField_1;

	public PersnalSchedulePanel() {
		setSize(355, 234);
		setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("\uC7A5\uC18C");
		lblNewLabel_1.setBounds(53, 104, 57, 15);
		add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("\uC81C\uBAA9");
		lblNewLabel_2.setBounds(53, 55, 57, 15);
		add(lblNewLabel_2);

		textField = new JTextField();
		textField.setBounds(104, 104, 209, 21);
		add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setBounds(104, 55, 209, 21);
		add(textField_1);
		textField_1.setColumns(10);

		JButton btnNewButton = new JButton("\uD655\uC778");
		btnNewButton.setBounds(53, 169, 97, 23);
		add(btnNewButton);

		JButton btnNewButton_1 = new JButton("\uC0AD\uC81C");
		btnNewButton_1.setBounds(203, 169, 97, 23);
		add(btnNewButton_1);
		lblTitle = new JLabel("Á¦¸ñ");

	}
}
