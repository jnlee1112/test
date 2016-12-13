
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

class MyCalendar extends JPanel {

	String[] days = { "일", "월", "화", "수", "목", "금", "토" };
	int year, month, day, todays, memoday = 0;
	Font f;
	Calendar today;
	Calendar cal;
	JButton btnLastYear, btnNextYear;
	JButton btnLastMonth, btnNextMonth;
	JButton[] calBtn = new JButton[49];
	JPanel panNorth;
	JPanel panCenter;
	JTextField txtMonth, txtYear;

	public MyCalendar() {

		today = Calendar.getInstance();
		cal = new GregorianCalendar();

		year = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH) + 1;

		panNorth = new JPanel();
		panNorth.add(btnLastYear = new JButton(" ↓ "));
		panNorth.add(btnLastMonth = new JButton(" ← "));

		panNorth.add(txtYear = new JTextField(year + "년"));
		panNorth.add(txtMonth = new JTextField(month + "월"));

		f = new Font("Sherif", Font.BOLD, 18);
		txtYear.setFont(f);
		txtMonth.setFont(f);

		txtYear.setEnabled(false);
		txtMonth.setEnabled(false);

		panNorth.add(btnNextMonth = new JButton(" → "));
		panNorth.add(btnNextYear = new JButton(" ↑ "));

		add(panNorth, "North");

		panCenter = new JPanel(new GridLayout(7, 7));
		f = new Font("Sherif", Font.BOLD, 12);

		gridInit();
		calSet();
		hideInit();
		add(panCenter, "Center");
	}// end constuctor

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

}
