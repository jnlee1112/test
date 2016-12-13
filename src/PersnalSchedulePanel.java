
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PersnalSchedulePanel extends JPanel implements ActionListener {

	private boolean isAdd;
	private Calendar calendar;
	private int width = 350;
	private int height = 300;
	private JButton btnDelete;
	private JTextField tfTitle;
	private JTextField tfPlace;
	private JButton btnOK;
	private JFrame frame;

	public PersnalSchedulePanel(boolean isAdd, Calendar calendar, JFrame frame) {
		this.isAdd = isAdd;
		this.calendar = calendar;
		this.frame = frame;
		setSize(width, height);
		setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("\uC7A5\uC18C");
		lblNewLabel_1.setBounds(53, 104, 57, 15);
		add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("\uC81C\uBAA9");
		lblNewLabel_2.setBounds(53, 55, 57, 15);
		add(lblNewLabel_2);

		tfTitle = new JTextField();
		tfTitle.setBounds(104, 104, 209, 21);
		add(tfTitle);
		tfTitle.setColumns(10);

		tfPlace = new JTextField();
		tfPlace.setBounds(104, 55, 209, 21);
		add(tfPlace);
		tfPlace.setColumns(10);

		btnOK = new JButton("\uD655\uC778");
		btnOK.setBounds(53, 169, 97, 23);
		btnOK.addActionListener(this);
		add(btnOK);

		btnDelete = new JButton("\uC0AD\uC81C");
		btnDelete.setBounds(203, 169, 97, 23);
		btnDelete.addActionListener(this);
		add(btnDelete);
	}

	public void actionPerformed(ActionEvent e) {
		long d = calendar.getTimeInMillis();
		Date date = new Date(d);
		if (e.getSource() == btnOK) {
			if (tfTitle.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "제목이 비었습니다.");
				System.out.println("1");
			} else if (tfPlace.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "장소가 비었습니다.");
				System.out.println("2");
			} else if (isAdd) {
				MainFrame.getInstance().sendRequest(new ScheduleData(ScheduleData.PERSNAL_SCHEDULE_ADD,
						tfTitle.getText(), tfPlace.getText(), date));
				System.out.println("3");
				frame.dispose();
			} else {
				MainFrame.getInstance().sendRequest(new ScheduleData(ScheduleData.PERSNAL_SCHEDULE_UPDATE,
						tfTitle.getText(), tfPlace.getText(), date));
				System.out.println("4");
				frame.dispose();
			}
		}
		if (e.getSource() == btnDelete) {
			if (!isAdd) {
				MainFrame.getInstance()
						.sendRequest(new ScheduleData(ScheduleData.PERSNAL_SCHEDULE_DELETE, null, null, date));
				frame.dispose();
			}
		}
	}
}
