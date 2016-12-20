
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PersnalSchedulePanel extends JPanel implements ActionListener {

	private ScheduleData sd;
	private int width = 350;
	private int height = 250;
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
		setBackground(new Color(255, 225, 96));

		JLabel lblNewLabel_1 = new JLabel("\uC7A5\uC18C");
		lblNewLabel_1.setFont(new Font("HY강B", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(53, 101, 57, 18);
		add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("\uC81C\uBAA9");
		lblNewLabel_2.setFont(new Font("HY강B", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(53, 52, 57, 18);
		add(lblNewLabel_2);

		tfTitle = new JTextField();
		tfTitle.setBounds(104, 52, 209, 21);
		add(tfTitle);
		tfTitle.setColumns(10);

		tfPlace = new JTextField();
		tfPlace.setBounds(104, 101, 209, 21);
		add(tfPlace);
		tfPlace.setColumns(10);

		btnOK = new JButton();
		btnOK.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/ok.PNG", 100, 50));
		btnOK.setBorderPainted(false);
		btnOK.setFocusPainted(false);
		btnOK.setContentAreaFilled(false);
		btnOK.setBounds(53, 169, 121, 28);
		btnOK.addActionListener(this);
		add(btnOK);

		btnDelete = new JButton();
		btnDelete.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/delete.PNG", 100, 50));
		btnDelete.setBorderPainted(false);
		btnDelete.setFocusPainted(false);
		btnDelete.setContentAreaFilled(false);
		btnDelete.setBounds(186, 169, 113, 28);
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