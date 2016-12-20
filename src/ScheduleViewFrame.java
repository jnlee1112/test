import java.awt.Color;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Font;

public class ScheduleViewFrame extends JFrame {
	public ScheduleViewFrame(ScheduleData sd, boolean isPersonal) {
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(255, 225, 128));

		JLabel lbl1 = new JLabel("");
		lbl1.setFont(new Font("HY강B", Font.PLAIN, 16));
		lbl1.setHorizontalAlignment(SwingConstants.LEFT);
		lbl1.setBounds(68, 32, 198, 29);
		getContentPane().add(lbl1);

		JLabel lbl2 = new JLabel("");
		lbl2.setFont(new Font("HY강B", Font.PLAIN, 16));
		lbl2.setHorizontalAlignment(SwingConstants.LEFT);
		lbl2.setBounds(68, 71, 198, 29);
		getContentPane().add(lbl2);

		JLabel lbl3 = new JLabel("");
		lbl3.setFont(new Font("HY강B", Font.PLAIN, 16));
		lbl3.setHorizontalAlignment(SwingConstants.LEFT);
		lbl3.setBounds(68, 110, 198, 29);
		getContentPane().add(lbl3);

		JTextArea lblTextArea = new JTextArea();
		lblTextArea.setFont(new Font("HY강B", Font.PLAIN, 16));
		lblTextArea.setBackground(null);
		lblTextArea.setEditable(false);
		lblTextArea.setBounds(68, 178, 198, 111);
		getContentPane().add(lblTextArea);
		setSize(350, 350);
		lbl1.setText("<날짜>  " + sd.getDate());
		setLocation(MainFrame.getInstance().getLocation().x + 520, MainFrame.getInstance().getLocation().y);
		if (isPersonal) {
			lbl3.setText("<제목>  " + sd.getTitle());
			lblTextArea.setText("<장소>\n\r");
			lblTextArea.append(sd.getPlace());
		} else {
			lbl2.setText("<그룹이름>  " + sd.getGrName());
			lbl3.setText("<장소>  " + sd.getPlace());
			lblTextArea.append("< 참여  인원 >\n\r");
			for (String str : sd.getMemberNameL()) {
				lblTextArea.append(str + "\n\r");
			}
		}
		setVisible(true);
	}
}
