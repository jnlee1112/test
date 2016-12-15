
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

	private ScheduleData sd;
	private int width = 350;
	private int height = 300;
	private JButton btnDelete;
	private JTextField tfTitle;
	private JTextField tfPlace;
	private JButton btnOK;
	private JFrame frame;

	public PersnalSchedulePanel(ScheduleData sd, JFrame frame) {
		this.sd = sd;
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
		tfTitle.setBounds(104, 52, 209, 21);
		add(tfTitle);
		tfTitle.setColumns(10);

		tfPlace = new JTextField();
		tfPlace.setBounds(104, 101, 209, 21);
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

		if (sd.getState() != -1) {
			tfPlace.setText(sd.getPlace());
			tfTitle.setText(sd.getTitle());
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOK) {
			if (tfTitle.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "제목이 비었습니다.");
				return;
			} else if (tfPlace.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "장소가 비었습니다.");
				return;
			} else if (sd.getState() == -1) {
				MainFrame.getInstance().sendRequest(new ScheduleData(ScheduleData.PERSNAL_SCHEDULE_ADD,
						tfTitle.getText(), tfPlace.getText(), sd.getDate()));
			} else {
				MainFrame.getInstance().sendRequest(new ScheduleData(ScheduleData.PERSNAL_SCHEDULE_UPDATE,
						tfTitle.getText(), tfPlace.getText(), sd.getDate()));
			}
		}
		if (e.getSource() == btnDelete) {
			if (sd.getState() != -1) {
				MainFrame.getInstance()
						.sendRequest(new ScheduleData(ScheduleData.PERSNAL_SCHEDULE_DELETE, null, null, sd.getDate()));
			}
		}
		MainFrame.getInstance().getInitialData();
		frame.dispose();
	}
}
