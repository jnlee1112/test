import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class PossibleCalendar extends JPanel implements ActionListener {

	private ArrayList<ScheduleData> personalScheduleSet;
	private ArrayList<ScheduleData> groupScheduleSet;

	private String[] days = { "일", "월", "화", "수", "목", "금", "토" };
	private int year, month, day, todays;
	private Font f;
	private Calendar today;
	private Calendar cal;
	private JButton btnLastYear, btnNextYear;
	private JButton btnLastMonth, btnNextMonth;
	private JButton[] calBtn = new JButton[49];
	private JPanel panNorth;
	private JPanel panCenter;
	private JTextField txtMonth, txtYear;
	private JLabel lblShceduleInfo;

	public PossibleCalendar() {
		today = Calendar.getInstance();
		cal = new GregorianCalendar();
		year = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH) + 1;

		panNorth = new JPanel();
		panNorth.setBounds(20, 10, 349, 42);
		panNorth.add(btnLastYear = new JButton(" ↓ "));
		panNorth.add(btnLastMonth = new JButton(" ← "));

		panNorth.add(txtYear = new JTextField(year + "년"));
		panNorth.add(txtMonth = new JTextField(month + "월"));

		f = new Font("Sherif", Font.BOLD, 18);
		txtYear.setFont(f);
		txtMonth.setFont(f);

		txtYear.setEnabled(false);
		txtMonth.setEnabled(false);
		setLayout(null);

		panNorth.add(btnNextMonth = new JButton(" → "));
		panNorth.add(btnNextYear = new JButton(" ↑ "));

		add(panNorth);

		panCenter = new JPanel(new GridLayout(7, 7));
		panCenter.setBounds(20, 72, 349, 181);
		f = new Font("Sherif", Font.BOLD, 12);

		gridInit();
		calSet();
		hideInit();
		add(panCenter);
		setButton();

		btnLastMonth.addActionListener(this);
		btnLastYear.addActionListener(this);
		btnNextMonth.addActionListener(this);
		btnNextYear.addActionListener(this);

		lblShceduleInfo = new JLabel("");
		lblShceduleInfo.setBounds(20, 269, 349, 31);
		add(lblShceduleInfo);
	}

	public void setButton() {
		for (int i = 7; i < calBtn.length; i++) {
			calBtn[i].addMouseListener(new MouseClick());
			calBtn[i].addActionListener(this);
		}
		calSetColor();
	}

	class MouseClick extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, year);
				c.set(Calendar.MONTH, (month - 1));
				c.set(Calendar.DATE, day);
				ScheduleData scheduleData = new ScheduleData(-1, null, null);
				Date d = new Date(c.getTimeInMillis());
				scheduleData.setDate(d);
				for (ScheduleData sd : groupScheduleSet) {
					if (sd.getDate().toString().equals(d.toString())) {
						JOptionPane.showMessageDialog(null, "넌 못지나간다.");
					}
				}
				for (ScheduleData sd : personalScheduleSet) {
					if (sd.getDate().toString().equals(d.toString()))
						scheduleData = sd;
				}
				JFrame f = new JFrame(year + "년" + month + "월" + day + "일");
				PersnalSchedulePanel psp = new PersnalSchedulePanel(scheduleData, f);
				f.setSize(psp.getWidth(), psp.getHeight() + 30);
				f.getContentPane().add(psp);
				f.setLocationRelativeTo(null);
				f.setVisible(true);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLastMonth) {
			repaintCalendar(-1);
		} else if (e.getSource() == btnNextMonth) {
			repaintCalendar(1);
		} else if (e.getSource() == btnLastYear) {
			repaintCalendar(-12);
		} else if (e.getSource() == btnNextYear) {
			repaintCalendar(12);
		} else if (Integer.parseInt(e.getActionCommand()) >= 1 && Integer.parseInt(e.getActionCommand()) <= 31) {
			day = Integer.parseInt(e.getActionCommand());
			calSet();
			Calendar c = (Calendar) cal.clone();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, (month - 1));
			c.set(Calendar.DATE, day);
			Date d = new Date(c.getTimeInMillis());
			for (ScheduleData sd : personalScheduleSet) {
				if (sd.getDate().toString().equals(d.toString())) {
					lblShceduleInfo.setText(sd.personalToString());
					return;
				}
			}
			for (ScheduleData sd : groupScheduleSet) {
				if (sd.getDate().toString().equals(d.toString())) {
					lblShceduleInfo.setText(sd.groupToString());
					return;
				}
			}
		}
		lblShceduleInfo.setText("");
	}

	public void addPersnalSchedule(ScheduleData sd) {
		personalScheduleSet.add(sd);
		repaintCalendar(0);
	}

	public void addGroupSchedule(ScheduleData sd) {
		groupScheduleSet.add(sd);
		repaintCalendar(0);
	}

	public void calSetColor() { // 일정 색칠하기
		Calendar c = (Calendar) cal.clone();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, (month - 1));
		int count = 1;
		for (int i = 7; i < calBtn.length; i++) {
			if ((calBtn[i].getText()).equals(""))
				continue;
			c.set(Calendar.DATE, count);
			Date d = new Date(c.getTimeInMillis());
			if (personalScheduleSet == null)
				personalScheduleSet = new ArrayList();
			if (groupScheduleSet == null)
				groupScheduleSet = new ArrayList<>();
			for (ScheduleData sd : personalScheduleSet) {
				if (sd.getDate().toString().equals(d.toString()))
					calBtn[i].setBackground(new Color(255, 255, 128));// 노랑
			}
			for (ScheduleData sd : groupScheduleSet) {
				if (sd.getDate().toString().equals(d.toString()))
					calBtn[i].setBackground(new Color(128, 255, 128));// 연두
			}
			count++;
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

	public void updateCalendar() {
		groupScheduleSet = new ArrayList<>();
		personalScheduleSet = new ArrayList<>();
		repaintCalendar(0);
	}
}
