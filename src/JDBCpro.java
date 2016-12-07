
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.swing.*;

class JDBCpro extends JFrame implements ActionListener, MouseListener {

	class MouseClick extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {

			}

		}
	}

	String[] days = { "��", "��", "ȭ", "��", "��", "��", "��" };
	int year, month, day, todays, memoday = 0;
	Font f;
	Calendar today;
	Calendar cal;
	JButton btnBefore2, btnAfter2; // befor2 �۳� // after2 ����
	JButton btnBefore, btnAfter;
	JButton btnAdd, btnDel, btnUpdate;
	JButton[] calBtn = new JButton[49];
	JLabel time;
	JPanel panSouth;
	JPanel panNorth;
	JPanel panCenter;
	JTextField txtMonth, txtYear;
	// ���ڸ� �������ִ� �ؽ�Ʈ �ʵ� �� �� �޸�κ�
	JTextArea txtWrite;
	BorderLayout bLayout = new BorderLayout();

	public JDBCpro() {

		today = Calendar.getInstance(); // ����Ʈ�� Ÿ�� �� �� �������� ����� �޷��� �����ɴϴ�.
		cal = new GregorianCalendar();
		/*
		 * GregorianCalendar ��,Calendar �� ���� ���� Ŭ�����̸�, ������ ��κ��� �������� ���Ǵ� ǥ������
		 * �޷� �ý����� �����մϴ�.
		 */
		year = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH) + 1;// 1���� ���� 0

		panNorth = new JPanel();
		panNorth.add(btnBefore2 = new JButton(" �� "));
		panNorth.add(btnBefore = new JButton(" �� "));

		panNorth.add(txtYear = new JTextField(year + "��"));
		panNorth.add(txtMonth = new JTextField(month + "��"));

		f = new Font("Sherif", Font.BOLD, 18); // �Ⱑ ���� ǥ���ϴ� �ؽ�Ʈ �ʵ��� ���� �Ӽ�
		txtYear.setFont(f);
		txtMonth.setFont(f);

		txtYear.setEnabled(false); // ��� ���� ���� ��Ȱ��ȭ�Ͽ� ���� ������ �Ұ����Ѵ�.
		txtMonth.setEnabled(false);

		panNorth.add(btnAfter = new JButton(" �� "));
		panNorth.add(btnAfter2 = new JButton(" �� "));

		add(panNorth, "North");

		/*
		 * jdbcpro��� ū�� ���� ���̾ƿ��� ��,��,��,������ ������ �г��� �ϳ� �ϳ� �÷� ���� �����̴�. ���κ��� ���� ��ǰ��
		 * �ϳ� �ϳ� �����ǵ�.....
		 */

		// �̳��� �޷¿� ���� �ش��ϴ� �κ�

		panCenter = new JPanel(new GridLayout(7, 7));// ���ڳ�,���������� ��ġ������
		f = new Font("Sherif", Font.BOLD, 12);

		gridInit();
		calSet();
		hideInit();
		add(panCenter, "Center");

		//////////////////////////////////
		panSouth = new JPanel();
		// panS_West = new JPanel();
		// panSouth.add(panS_West);
		// panSouth.add(panS_East);

		panSouth.add(btnAdd = new JButton("�޸��߰�"));
		panSouth.add(btnDel = new JButton("�޸����"));
		panSouth.add(btnUpdate = new JButton("�޸����"));
		panSouth.add(txtWrite = new JTextArea());

		txtWrite.setPreferredSize(new Dimension(150, 28));
		// �޸� �Է¹��� �ؽ�Ʈ �ڽ��� ���� 150 ���� 28 ����
		add(panSouth, "South");

		// ��ư�� ���� �ൿ���� �����Ѵ�.
		btnBefore.addActionListener(this);
		btnAfter.addActionListener(this);
		btnBefore2.addActionListener(this);
		btnAfter2.addActionListener(this);
		btnAdd.addActionListener(this); // �޸��߰�
		btnDel.addActionListener(this); // �޸����
		btnUpdate.addActionListener(this); // �޸����

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �ݴ±��
		setTitle("7�� ������Ʈ �����췯");
		setBounds(200, 200, 450, 400); // (x,y,����,����) ������â�� ��ġ
		setVisible(true);

	}// end constuctor

	public void calSet() {

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, (month - 1));
		cal.set(Calendar.DATE, 1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		/*
		 * get �� set �� ���� �ʵ�ġ��, ������ ��Ÿ���ϴ�. �� �ʵ���
		 * ����,SUNDAY,MONDAY,TUESDAY,WEDNESDAY ,THURSDAY,FRIDAY, �� SATURDAY ��
		 * �˴ϴ�. get()�޼ҵ��� ���� ������ ���ڷ� ��ȯ
		 */

		int j = 0;
		int hopping = 0;
		calBtn[0].setForeground(new Color(255, 0, 0));// �Ͽ��� "��" RGB�� �� �ִ´�.
		calBtn[6].setForeground(new Color(0, 0, 255));// ����� "��"

		for (int i = cal.getFirstDayOfWeek(); i < dayOfWeek; i++) {
			j++;
		}
		/*
		 * �Ͽ��Ϻ��� �״��� ù���� ���ϱ��� ��ĭ���� �����ϱ� ����
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
			if (memoday == 1) { // memo�� ����ȳ��� £����ũ������
				calBtn[i + 6 + hopping].setForeground(new Color(255, 0, 255));
			} else {
				calBtn[i + 6 + hopping].setForeground(new Color(0, 0, 0));
				if ((i + hopping - 1) % 7 == 0) {// �Ͽ���
					calBtn[i + 6 + hopping].setForeground(new Color(255, 0, 0));
				}
				if ((i + hopping) % 7 == 0) {// �����
					calBtn[i + 6 + hopping].setForeground(new Color(0, 0, 255));
				}
			}

			/*
			 * ������ ���� �������� ����ؾ� �ϴ� ������ ���� ��ư�� ������ ���ϰ� �ε����� 0���� �����̴� -1�� ���� ������
			 * ������ ���ְ� ��ư�� ������ �������ش�.
			 */
			calBtn[i + 6 + hopping].setText((i) + "");
		} // for

	}// end Calset()

	public void actionPerformed(ActionEvent cook) { // �׼� �����°�cook ��������
		if (cook.getSource() == btnBefore) { // �����޷� �������� �ҽ���
			this.panCenter.removeAll();
			calInput(-1); // ���� �ϳ� ���ش�
			gridInit();
			panelInit();
			calSet();
			hideInit();
			this.txtYear.setText(year + "��");
			this.txtMonth.setText(month + "��");
		} else if (cook.getSource() == btnAfter) { // ���� �޷� �������� �ҽ���
			this.panCenter.removeAll();
			calInput(1); // ���� �ϳ� �����ش�.
			gridInit();
			panelInit();
			calSet();
			hideInit();
			this.txtYear.setText(year + "��");
			this.txtMonth.setText(month + "��");
		} else if (cook.getSource() == btnBefore2) { // ���� ���� �������� �ҽ���
			this.panCenter.removeAll();
			calInput(-12); // 12������ ���ش�.
			gridInit();
			panelInit();
			calSet();
			hideInit();
			this.txtYear.setText(year + "��");
			this.txtMonth.setText(month + "��");
		} else if (cook.getSource() == btnAfter2) { // ������ �� �������� �ҽ���
			this.panCenter.removeAll();
			calInput(12); // 12������ �����ش�.
			gridInit();
			panelInit();
			calSet();
			hideInit();
			this.txtYear.setText(year + "��");
			this.txtMonth.setText(month + "��");
		} else if (cook.getSource() == btnAdd) {
			calSet();
			txtWrite.setText("");

		} else if (cook.getSource() == btnDel) {
			calSet();
			txtWrite.setText("");
		} else if (cook.getSource() == btnUpdate) {
			calSet();
			txtWrite.setText("");
		}

		else if (Integer.parseInt(cook.getActionCommand()) >= 1 && Integer.parseInt(cook.getActionCommand()) <= 31) {
			day = Integer.parseInt(cook.getActionCommand());
			// ��ư�� ��� �� 1,2,3.... ���ڸ� ���������� ��ȯ�Ͽ� Ŭ���� ��¥�� �ٲ��ش�.
			System.out.println(day);
			calSet();
		}
	}// end actionperformed()

	public void hideInit() {
		for (int i = 0; i < calBtn.length; i++) {
			if ((calBtn[i].getText()).equals(""))
				calBtn[i].setEnabled(false);
			// ���� ������ ���� ������ ��ư�� ��Ȱ��ȭ ��Ų��.
		} // end for
	}// end hideInit()
		// public void separate(){

	public void gridInit() {
		// jPanel3�� ��ư ���̱�
		for (int i = 0; i < days.length; i++)
			panCenter.add(calBtn[i] = new JButton(days[i]));

		for (int i = days.length; i < 49; i++) {
			panCenter.add(calBtn[i] = new JButton(""));
			calBtn[i].addMouseListener(this);
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

	public static void main(String[] args) {
		JDBCpro jdbc = new JDBCpro();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getClickCount() >= 2) {
			System.out.println("double clicked");
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}