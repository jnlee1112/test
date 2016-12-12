
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class MyCalendar extends JPanel implements ActionListener {

	class MouseClick extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				JFrame f = new JFrame(year + "년" + month + "월" + day + "일");
				PersnalSchedulePanel psp = new PersnalSchedulePanel();
				f.setSize(psp.getWidth(), psp.getHeight()+30);
				f.add(psp);
				f.setLocationRelativeTo(null);
				f.setVisible(true);
			}
		}
	}

	String[] days = { "일", "월", "화", "수", "목", "금", "토" };
	int year, month, day, todays, memoday = 0;
	Font f;
	Calendar today;
	Calendar cal;
	JButton btnBefore2, btnAfter2; // befor2 작년 // after2 내년
	JButton btnBefore, btnAfter;
	JButton[] calBtn = new JButton[49];
	JLabel time;
	JPanel panNorth;
	JPanel panCenter;
	JTextField txtMonth, txtYear;
	// 글자를 넣을수있는 텍스트 필드 년 월 메모부분
	JTextArea txtWrite;
	BorderLayout bLayout = new BorderLayout();

	public MyCalendar() {

		today = Calendar.getInstance(); // 디폴트의 타임 존 및 로케일을 사용해 달력을 가져옵니다.
		cal = new GregorianCalendar();

		year = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH) + 1;// 1월의 값이 0

		panNorth = new JPanel();
		panNorth.add(btnBefore2 = new JButton(" ↓ "));
		panNorth.add(btnBefore = new JButton(" ← "));

		panNorth.add(txtYear = new JTextField(year + "년"));
		panNorth.add(txtMonth = new JTextField(month + "월"));

		f = new Font("Sherif", Font.BOLD, 18); // 년가 월을 표시하는 텍스트 필드의 글자 속성
		txtYear.setFont(f);
		txtMonth.setFont(f);

		txtYear.setEnabled(false); // 년과 월을 선택 비활성화하여 숫자 수정을 불가피한다.
		txtMonth.setEnabled(false);

		panNorth.add(btnAfter = new JButton(" → "));
		panNorth.add(btnAfter2 = new JButton(" ↑ "));

		add(panNorth, "North");

		panCenter = new JPanel(new GridLayout(7, 7));// 격자나,눈금형태의 배치관리자
		f = new Font("Sherif", Font.BOLD, 12);

		btnAfter.addActionListener(this);
		btnAfter2.addActionListener(this);
		btnBefore.addActionListener(this);
		btnBefore2.addActionListener(this);

		gridInit();
		calSet();
		hideInit();
		add(panCenter, "Center");

		setBounds(200, 200, 450, 400); // (x,y,가로,세로) 프레임창의 위치
		setVisible(true);

	}// end constuctor

	public void calSet() {

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, (month - 1));
		cal.set(Calendar.DATE, 1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		/*
		 * get 및 set 를 위한 필드치로, 요일을 나타냅니다. 이 필드의
		 * 값은,SUNDAY,MONDAY,TUESDAY,WEDNESDAY ,THURSDAY,FRIDAY, 및 SATURDAY 가
		 * 됩니다. get()메소드의 의해 요일이 숫자로 반환
		 */

		int j = 0;
		int hopping = 0;
		calBtn[0].setForeground(new Color(255, 0, 0));// 일요일 "일" RGB의 색 넣는다.
		calBtn[6].setForeground(new Color(0, 0, 255));// 토요일 "토"

		for (int i = cal.getFirstDayOfWeek(); i < dayOfWeek; i++) {
			j++;
		}
		/*
		 * 일요일부터 그달의 첫시작 요일까지 빈칸으로 셋팅하기 위해
		 */
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
			if (memoday == 1) { // memo가 저장된날은 짙은핑크색으로
				calBtn[i + 6 + hopping].setForeground(new Color(255, 0, 255));
			} else {
				calBtn[i + 6 + hopping].setForeground(new Color(0, 0, 0));
				if ((i + hopping - 1) % 7 == 0) {// 일요일
					calBtn[i + 6 + hopping].setForeground(new Color(255, 0, 0));
				}
				if ((i + hopping) % 7 == 0) {// 토요일
					calBtn[i + 6 + hopping].setForeground(new Color(0, 0, 255));
				}
			}

			/*
			 * 요일을 찍은 다음부터 계산해야 하니 요일을 찍은 버튼의 갯수를 더하고 인덱스가 0부터 시작이니 -1을 해준 값으로
			 * 연산을 해주고 버튼의 색깔을 변경해준다.
			 */
			calBtn[i + 6 + hopping].setText((i) + "");
		} // for

	}// end Calset()

	public void actionPerformed(ActionEvent cook) { // 액션 누르는걸cook 눌렀을때
		if (cook.getSource() == btnBefore) { // 이전달로 가기위한 소스부
			this.panCenter.removeAll();
			calInput(-1); // 달을 하나 빼준다
			gridInit();
			panelInit();
			calSet();
			hideInit();
			this.txtYear.setText(year + "년");
			this.txtMonth.setText(month + "월");
		} else if (cook.getSource() == btnAfter) { // 다음 달로 가기위한 소스부
			this.panCenter.removeAll();
			calInput(1); // 달을 하나 더해준다.
			gridInit();
			panelInit();
			calSet();
			hideInit();
			this.txtYear.setText(year + "년");
			this.txtMonth.setText(month + "월");
		} else if (cook.getSource() == btnBefore2) { // 전년 으로 가기위한 소스부
			this.panCenter.removeAll();
			calInput(-12); // 12개월을 빼준다.
			gridInit();
			panelInit();
			calSet();
			hideInit();
			this.txtYear.setText(year + "년");
			this.txtMonth.setText(month + "월");
		} else if (cook.getSource() == btnAfter2) { // 내년으 로 가기위한 소스부
			this.panCenter.removeAll();
			calInput(12); // 12개월을 더해준다.
			gridInit();
			panelInit();
			calSet();
			hideInit();
			this.txtYear.setText(year + "년");
			this.txtMonth.setText(month + "월");
		} else if (Integer.parseInt(cook.getActionCommand()) >= 1 && Integer.parseInt(cook.getActionCommand()) <= 31) {
			day = Integer.parseInt(cook.getActionCommand());
			// 버튼의 밸류 즉 1,2,3.... 문자를 정수형으로 변환하여 클릭한 날짜를 바꿔준다.
			System.out.println(day);
			calSet();
		}
	}// end actionperformed()

	public void hideInit() {
		for (int i = 0; i < calBtn.length; i++) {
			if ((calBtn[i].getText()).equals(""))
				calBtn[i].setEnabled(false);
			// 일이 찍히지 않은 나머지 버튼을 비활성화 시킨다.
		} // end for
	}// end hideInit()
		// public void separate(){

	public void gridInit() {
		// jPanel3에 버튼 붙이기
		for (int i = 0; i < days.length; i++)
			panCenter.add(calBtn[i] = new JButton(days[i]));

		for (int i = days.length; i < 49; i++) {
			panCenter.add(calBtn[i] = new JButton(""));
			calBtn[i].addMouseListener(new MouseClick());
			calBtn[i].addActionListener(this);
		}
	}// end gridInit()

	public void panelInit() {
		GridLayout gridLayout1 = new GridLayout(7, 7);
		panCenter.setLayout(gridLayout1);
	}// end panelInit()

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

	}// end calInput()

}
