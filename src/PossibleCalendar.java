import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.FlowLayout;
import javax.swing.JLabel;

class PossibleCalendar extends JPanel implements ActionListener {

	private String[] days = { "일", "월", "화", "수", "목", "금", "토" };
	private int year, month, day, todays;
	private Font f;
	private Calendar today;
	private Calendar cal;
	private JButton btnLastYear, btnNextYear;
	private JButton btnLastMonth, btnNextMonth;
	private JButton[] calBtn = new JButton[49];
	private JButton cancleBtn;
	private JButton okBtn;
	private JPanel panCenter;
	private JTextField txtMonth, txtYear;
	private int btnSizeX = 40;
	private int btnSizeY = 30;
	private ArrayList<ScheduleData> possibleDateList;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;

	public PossibleCalendar() {
		setSize(500, 500);
		setLayout(null);
		possibleDateList = new ArrayList<>();
		today = Calendar.getInstance();
		cal = new GregorianCalendar();
		year = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH) + 1;
		panCenter = new JPanel(new GridLayout(7, 7));
		panCenter.setBounds(0, 68, 500, 300);
		f = new Font("Sherif", Font.BOLD, 18);

		okBtn = new JButton();
		okBtn.setBounds(356, 376, 60, 60);
		okBtn.setToolTipText("개인 일정 저장");
		okBtn.addActionListener(this);
		cancleBtn = new JButton();
		cancleBtn.setBounds(428, 376, 60, 60);
		cancleBtn.addActionListener(this);
		add(okBtn);
		add(cancleBtn);

		gridInit();
		calSet();
		hideInit();
		add(panCenter);
		setButton();

		JPanel panel = new JPanel();
		panel.setBounds(52, 20, 396, 48);
		add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnLastYear = new JButton();
		panel.add(btnLastYear);
		btnLastYear.addActionListener(this);
		btnLastYear.setBorder(null);
		btnLastMonth = new JButton();
		btnLastMonth.setLocation(171, 9);
		panel.add(btnLastMonth);
		txtYear = new JTextField(year + "년");
		panel.add(txtYear);
		txtYear.setFont(f);
		txtYear.setBorder(null);
		txtYear.setEnabled(false);
		txtYear.setBackground(null);
		txtMonth = new JTextField(month + "월");
		panel.add(txtMonth);
		txtMonth.setFont(f);
		txtMonth.setEnabled(false);
		txtMonth.setBorder(null);
		txtMonth.setBackground(null);

		btnNextMonth = new JButton();
		panel.add(btnNextMonth);
		btnNextMonth.addActionListener(this);
		btnLastMonth.addActionListener(this);
		btnLastMonth.setBorder(null);
		btnNextMonth.setBorder(null);
		btnNextYear = new JButton();
		panel.add(btnNextYear);
		btnNextYear.addActionListener(this);
		btnNextYear.setBorder(null);

		okBtn.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/saveIcon.png", 60, 60));
		cancleBtn.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/cancleIcon.png", 60, 60));
		btnLastMonth.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/leftI.png", btnSizeX, btnSizeY));
		btnNextMonth.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/rightI.png", btnSizeX, btnSizeY));
		btnLastYear.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/doubleLeftI.png", btnSizeX, btnSizeY));
		btnNextYear.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/doubleRightI.png", btnSizeX, btnSizeY));

		label = new JLabel(ImageTransFormer.transformImage("ProjectImageIcon/possibleDate.png", 100, 45));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(new Color(128, 255, 255));
		label.setFont(new Font("HY견고딕", Font.BOLD, 15));
		label.setBounds(182, 387, 90, 40);
		add(label);

		label_1 = new JLabel(ImageTransFormer.transformImage("ProjectImageIcon/groupSchedule.png", 100, 45));
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setForeground(new Color(128, 255, 128));
		label_1.setFont(new Font("HY견고딕", Font.BOLD, 15));
		label_1.setBounds(97, 387, 90, 40);
		add(label_1);

		label_2 = new JLabel(ImageTransFormer.transformImage("ProjectImageIcon/personalSchedule.png", 100, 45));
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setForeground(new Color(255, 255, 128));
		label_2.setFont(new Font("HY견고딕", Font.BOLD, 15));
		label_2.setBounds(10, 387, 90, 40);
		add(label_2);
	}

	public void setButton() {
		Calendar c = (Calendar) cal.clone();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, (month - 1));
		c.set(Calendar.DATE, day);
		int count = 1;
		loop_continue: for (int i = 7; i < calBtn.length; i++) {
			if ((calBtn[i].getText()).equals(""))
				continue;
			c.set(Calendar.DATE, count);
			Date d = new Date(c.getTimeInMillis());
			if (MainFrame.getInstance().findPersonalScheduleData(d) != null) {
				calBtn[i].setBackground(new Color(255, 255, 128));// 노랑
				count++;
				continue loop_continue;
			} else if (MainFrame.getInstance().findGroupScheduleData(d) != null) {
				calBtn[i].setBackground(new Color(128, 255, 128));// 연두
				count++;
				continue loop_continue;
			}
			for (ScheduleData sd : possibleDateList) {
				if (sd.getDate().toString().equals(d.toString())) {
					calBtn[i].setBackground(new Color(128, 255, 255));// 하늘색
					break;
				}
			}
			calBtn[i].addActionListener(this);
			count++;
		}

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okBtn) { // 확인 버튼
			MainFrame.getInstance().sendRequest(new ScheduleData(ScheduleData.ADD_POSSIBLE_DATE, possibleDateList));
			MainFrame.getInstance().getInitialData();
		} else if (e.getSource() == cancleBtn) { // 취소 버튼
			MainFrame.getInstance().getInitialData();
		} else if (e.getSource() == btnLastMonth) {
			repaintCalendar(-1);
		} else if (e.getSource() == btnNextMonth) {
			repaintCalendar(1);
		} else if (e.getSource() == btnLastYear) {
			repaintCalendar(-12);
		} else if (e.getSource() == btnNextYear) {
			repaintCalendar(12);
		} else if (Integer.parseInt(e.getActionCommand()) >= 1 && Integer.parseInt(e.getActionCommand()) <= 31) {
			day = Integer.parseInt(e.getActionCommand());
			Date d = getClickDate();
			JButton pushed = null;
			for (JButton jButton : calBtn)
				if (jButton.getText().equals(e.getActionCommand()))
					pushed = jButton;

			for (ScheduleData sd : possibleDateList) {
				if (sd.getDate().toString().equals(d.toString())) {
					pushed.setBackground(null); // 색 지우기
					possibleDateList.remove(sd);
					return;
				}
			}

			pushed.setBackground(new Color(128, 255, 255)); // 하늘 색
			ScheduleData sd = new ScheduleData(-1);
			sd.setDate(d);
			possibleDateList.add(sd);
		}
	}

	public void calSet() {
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, (month - 1));
		cal.set(Calendar.DATE, 1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int j = 0;
		int hopping = 0;
		calBtn[0].setForeground(new Color(255, 0, 0));// 일요일 "일" RGB의 색 넣는다.
		calBtn[6].setForeground(new Color(0, 0, 255));// 토요일 "토"

		for (int i = cal.getFirstDayOfWeek(); i < dayOfWeek; i++) {
			j++;
		}

		hopping = j;

		for (int kk = 0; kk < hopping; kk++) {
			calBtn[kk + 7].setText("");
		}
		for (int i = cal.getMinimum(Calendar.DAY_OF_MONTH); i <= cal.getMaximum(Calendar.DAY_OF_MONTH); i++) {
			cal.set(Calendar.DATE, i);
			if (cal.get(Calendar.MONTH) != month - 1) {
				break;
			}
			todays = i;
			calBtn[i + 6 + hopping].setForeground(new Color(0, 0, 0));
			if ((i + hopping - 1) % 7 == 0) {// 일요일
				calBtn[i + 6 + hopping].setForeground(new Color(255, 0, 0));
			}
			if ((i + hopping) % 7 == 0) {// 토요일
				calBtn[i + 6 + hopping].setForeground(new Color(0, 0, 255));
			}
			calBtn[i + 6 + hopping].setText((i) + "");
		}
	}

	public void repaintCalendar(int i) {
		this.panCenter.removeAll();
		calInput(i);
		gridInit();
		calSet();
		hideInit();
		setButton();
		this.txtYear.setText(year + "년");
		this.txtMonth.setText(month + "월");
	}

	public void hideInit() { // 사용하지 않는 버튼 지우기
		for (int i = 0; i < calBtn.length; i++) {
			calBtn[i].setBorder(null);
			calBtn[i].setFont(f);
			if (i < 7)
				calBtn[i].setBackground(new Color(187, 198, 204));
			if ((calBtn[i].getText()).equals(""))
				calBtn[i].setEnabled(false);
		}
	}

	public void gridInit() { // 달력 초기화 및 재 생성
		GridLayout gridLayout1 = new GridLayout(7, 7);
		panCenter.setLayout(gridLayout1);

		for (int i = 0; i < days.length; i++)
			panCenter.add(calBtn[i] = new JButton(days[i]));

		for (int i = days.length; i < 49; i++) {
			panCenter.add(calBtn[i] = new JButton(""));
		}
	}

	public void calInput(int gap) {
		if (gap == -1 || gap == 1) {
			month += (gap);
			if (month <= 0) {
				month = 12;
				year = year - 1;
			} else if (month >= 13) {
				month = 1;
				year = year + 1;
			}
		} else if (gap == 12) {
			year++;
		} else if (gap == -12) {
			year--;
		}
	}

	private Date getClickDate() {
		Calendar c = (Calendar) cal.clone();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, (month - 1));
		c.set(Calendar.DATE, day);
		return new Date(c.getTimeInMillis());
	}

	public void updateCalendar() {
		possibleDateList = MainFrame.getInstance().getPossibleDateList();
		repaintCalendar(0);
	}

}
