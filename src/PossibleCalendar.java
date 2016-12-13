import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PossibleCalendar extends MyCalendar implements ActionListener {

	public PossibleCalendar() {

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
		}
	}

}
