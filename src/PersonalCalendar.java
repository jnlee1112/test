import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;

class PersonalCalendar extends MyCalendar implements ActionListener {

	private HashSet<ScheduleData> personalScheduleSet;
	private HashSet<ScheduleData> groupScheduleSet;

	public PersonalCalendar() {
		// 버튼에 리스너 달기 해야함
		btnLastMonth.addActionListener(this);
		btnLastYear.addActionListener(this);
		btnNextMonth.addActionListener(this);
		btnNextYear.addActionListener(this);
		setButton();
	}

	private void setButton() {
		for (JButton jButton : calBtn) {
			jButton.addMouseListener(new MouseClick());
		}
	}

	class MouseClick extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				JFrame f = new JFrame(year + "년" + month + "월" + day + "일");
				PersnalSchedulePanel psp = new PersnalSchedulePanel(true, cal, f);
				f.setSize(psp.getWidth(), psp.getHeight() + 30);
				f.add(psp);
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
		}
	}

	public void addPersnalSchedule(ScheduleData sd) {
		personalScheduleSet.add(sd);
		repaintCalendar(0);
		setButton();
	}

	public void addGroupSchedule(ScheduleData sd) {
		groupScheduleSet.add(sd);
		repaintCalendar(0);
		setButton();
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
			Date date = new Date(cal.getTimeInMillis());
			if (personalScheduleSet == null)
				personalScheduleSet = new HashSet<>();
			if (groupScheduleSet == null)
				groupScheduleSet = new HashSet<>();
			for (ScheduleData sd : personalScheduleSet) {
				if (sd.getDate().equals(date))
					calBtn[i + 6 + hopping].setBackground(new Color(255, 255, 128));
			}
			for (ScheduleData sd : groupScheduleSet) {
				if (sd.getDate().equals(date))
					calBtn[i + 6 + hopping].setBackground(new Color(128, 255, 0));
			}
		}
	}
}
