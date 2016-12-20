import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

class MainCalendar extends JPanel implements ActionListener {

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
	private SlidePanel slidePanel;
	private ScheduleViewFrame scheduleViewFrame;
	private boolean isUp;
	private boolean isViewing;

	private int btnSizeX = 40;
	private int btnSizeY = 30;

	public MainCalendar() {
		setBackground(Color.WHITE);
		setSize(500, 500);
		isUp = false;
		today = Calendar.getInstance();
		cal = new GregorianCalendar();
		year = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH) + 1;

		panNorth = new JPanel();
		panNorth.setBounds(52, 20, 396, 48);
		panNorth.setBackground(Color.WHITE);
		panNorth.add(btnLastYear = new JButton());
		panNorth.add(btnLastMonth = new JButton());
		panNorth.add(txtYear = new JTextField(year + "년"));
		panNorth.add(txtMonth = new JTextField(month + "월"));
		panNorth.add(btnNextMonth = new JButton());
		panNorth.add(btnNextYear = new JButton());

		btnLastYear.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/doubleLeft.png", btnSizeX, btnSizeY));
		btnLastMonth.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/left.png", btnSizeX, btnSizeY));
		btnNextYear.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/doubleRight.png", btnSizeX, btnSizeY));
		btnNextMonth.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/right.png", btnSizeX, btnSizeY));

		f = new Font("Sherif", Font.BOLD, 18);
		txtYear.setFont(f);
		txtYear.setBackground(Color.WHITE);
		txtYear.setBorder(null);
		txtYear.setOpaque(false);
		txtMonth.setFont(f);
		txtMonth.setBackground(Color.WHITE);
		txtMonth.setBorder(null);
		txtMonth.setOpaque(false);

		txtYear.setEnabled(false);
		txtMonth.setEnabled(false);
		setLayout(null);

		add(panNorth);

		panCenter = new JPanel(new GridLayout(7, 7));
		panCenter.setBounds(0, 68, 500, 300);
		panCenter.setBackground(Color.WHITE);

		slidePanel = new SlidePanel();
		slidePanel.setLocation(415, 400);
		// sp.setBounds();
		add(slidePanel);

		gridInit();
		calSet();
		hideInit();
		add(panCenter);
		setButton();

		btnLastMonth.addActionListener(this);
		btnLastYear.addActionListener(this);
		btnNextMonth.addActionListener(this);
		btnNextYear.addActionListener(this);

		JLabel label_1 = new JLabel(ImageTransFormer.transformImage("ProjectImageIcon/groupSchedule.png", 100, 45));
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setForeground(new Color(128, 255, 128));
		label_1.setFont(new Font("HY견고딕", Font.BOLD, 15));
		label_1.setBounds(97, 387, 90, 40);
		add(label_1);

		JLabel label_2 = new JLabel(ImageTransFormer.transformImage("ProjectImageIcon/personalSchedule.png", 100, 45));
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setForeground(new Color(255, 255, 128));
		label_2.setFont(new Font("HY견고딕", Font.BOLD, 15));
		label_2.setBounds(10, 387, 90, 40);
		add(label_2);

		btnLastMonth.setBorder(null);
		btnLastYear.setBorder(null);
		btnNextMonth.setBorder(null);
		btnNextYear.setBorder(null);
	}

	class MouseClick extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				if(scheduleViewFrame != null)
					scheduleViewFrame.dispose();
				if (MainFrame.getInstance().findGroupScheduleData(getClickDate()) != null) {
					JOptionPane.showMessageDialog(null, "넌 못지나간다.");
					return;
				}
				JFrame f = new JFrame(" 개인일정 :  " + year + "년" + month + "월" + day + "일");
				ScheduleData sd = MainFrame.getInstance().findPersonalScheduleData(getClickDate());
				f.setLocation(MainFrame.getInstance().getLocation().x + 520, MainFrame.getInstance().getLocation().y);
				PersnalSchedulePanel psp;
				if (sd == null) {
					sd = new ScheduleData(-1);
					sd.setDate(getClickDate());
					psp = new PersnalSchedulePanel(sd, f);
				} else
					psp = new PersnalSchedulePanel(sd, f);

				f.setSize(psp.getWidth(), psp.getHeight() + 30);
				f.getContentPane().add(psp);
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
			ScheduleData sd = MainFrame.getInstance().findPersonalScheduleData(getClickDate());
			if (scheduleViewFrame != null)
				scheduleViewFrame.dispose();
			if (sd != null) {
				scheduleViewFrame = new ScheduleViewFrame(sd, true);
			}
			sd = MainFrame.getInstance().findGroupScheduleData(getClickDate());
			if (sd != null) {
				scheduleViewFrame = new ScheduleViewFrame(sd, false);
			}
		}
	}

	private void setButton() {
		for (int i = 0; i < 7; i++)
			calBtn[i].setForeground(Color.white);
		calBtn[0].setForeground(new Color(255, 0, 0));// 일요일 "일" RGB의 색 넣는다.
		calBtn[6].setForeground(new Color(0, 0, 255));// 토요일 "토"
		for (int i = 7; i < calBtn.length; i++) {
			calBtn[i].addMouseListener(new MouseClick());
			calBtn[i].addActionListener(this);
		}
		calSetColor();
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
			if (MainFrame.getInstance().findPersonalScheduleData(d) != null)
				calBtn[i].setBackground(new Color(255, 255, 128));// 노랑
			else if (MainFrame.getInstance().findGroupScheduleData(d) != null)
				calBtn[i].setBackground(new Color(128, 255, 128));// 연두
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

	private void repaintCalendar(int i) {
		this.panCenter.removeAll();
		calInput(i);
		gridInit();
		calSet();
		hideInit();
		setButton();
		this.txtYear.setText(year + "년");
		this.txtMonth.setText(month + "월");
	}

	private void hideInit() { // 사용하지 않는 버튼 지우기
		for (int i = 0; i < calBtn.length; i++) {
			calBtn[i].setBackground(Color.white);
			calBtn[i].setBorder(null);
			calBtn[i].setFont(f);
			if (i < 7)
				calBtn[i].setBackground(new Color(15, 31, 46));
			if ((calBtn[i].getText()).equals(""))
				calBtn[i].setEnabled(false);
		}
	}

	private void gridInit() { // 달력 초기화 및 재 생성
		GridLayout gridLayout1 = new GridLayout(7, 7);
		panCenter.setLayout(gridLayout1);

		for (int i = 0; i < days.length; i++)
			panCenter.add(calBtn[i] = new JButton(days[i]));

		for (int i = days.length; i < 49; i++) {
			panCenter.add(calBtn[i] = new JButton(""));
		}
	}

	private void calInput(int gap) {
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

	public void updateGUI() {
		repaintCalendar(0);
		if (scheduleViewFrame != null)
			scheduleViewFrame.dispose();
	}

	public void slideUp() {
		if (!isUp) {
			for (int i = 0; i < 155; i++)
				slidePanel.setLocation(slidePanel.getLocation().x, slidePanel.getLocation().y - 1);
			isUp = true;
		} else {
			for (int i = 0; i < 155; i++)
				slidePanel.setLocation(slidePanel.getLocation().x, slidePanel.getLocation().y + 1);
			isUp = false;
		}
	}

	public void slideDown() {
		if (isUp) {
			for (int i = 0; i < 155; i++)
				slidePanel.setLocation(slidePanel.getLocation().x, slidePanel.getLocation().y + 1);
			isUp = false;
		}
	}
}
