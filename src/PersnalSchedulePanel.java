

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;

public class PersnalSchedulePanel extends JPanel {

	private int width = 300;
	private int height = 400;
	private JLabel lblDate, lblTitle, lblPlace;
	private JTextField tfTitle, tfPlace;
	private JButton btnOk, btnDelete;
	private JTextField textField;
	private JTextField textField_1;

	public PersnalSchedulePanel() {
		setSize(width, height);
		setLayout(null);

		lblDate = new JLabel("New label");
		lblDate.setBackground(Color.WHITE);
		lblDate.setBounds(70, 22, 57, 15);
		add(lblDate);

		JLabel lblNewLabel_1 = new JLabel("\uC7A5\uC18C");
		lblNewLabel_1.setBounds(70, 62, 57, 15);
		add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("\uC81C\uBAA9");
		lblNewLabel_2.setBounds(70, 111, 57, 15);
		add(lblNewLabel_2);

		textField = new JTextField();
		textField.setBounds(164, 59, 116, 21);
		add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setBounds(164, 108, 116, 21);
		add(textField_1);
		textField_1.setColumns(10);

		JButton btnNewButton = new JButton("\uD655\uC778");
		btnNewButton.setBounds(70, 169, 97, 23);
		add(btnNewButton);

		JButton btnNewButton_1 = new JButton("\uC0AD\uC81C");
		btnNewButton_1.setBounds(191, 169, 97, 23);
		add(btnNewButton_1);
		lblTitle = new JLabel("����");

	}
}
