import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class NorthPanel extends JPanel implements ActionListener {
	private JButton btnSetMainPanel;
	private JButton btnSetPersonalPanel;
	private JButton btnSetGroupPanel;
	private JButton btnSetCreateGroupPanel;
	private ImageIcon homeIcon;
	private ImageIcon personalIcon;
	private ImageIcon groupIcon;
	private ImageIcon createIcon;

	private int ImageSizeX = 130;
	private int ImageSizeY = 130;

	public NorthPanel() {
		btnSetMainPanel = new JButton();
		setLayout(new GridLayout(1, 0, 0, 0));
		btnSetMainPanel.setBackground(Color.WHITE);
		btnSetMainPanel.setBorder(null);
		btnSetMainPanel.setToolTipText("일정 확인");
		add(btnSetMainPanel);

		btnSetPersonalPanel = new JButton();
		btnSetPersonalPanel.setBackground(Color.WHITE);
		btnSetPersonalPanel.setBorder(null);
		btnSetPersonalPanel.setToolTipText("개인 일정 설정");
		add(btnSetPersonalPanel);

		btnSetGroupPanel = new JButton();
		btnSetGroupPanel.setBackground(Color.WHITE);
		btnSetGroupPanel.setBorder(null);
		btnSetGroupPanel.setToolTipText("그룹 일정 맺기");
		add(btnSetGroupPanel);

		btnSetCreateGroupPanel = new JButton();
		btnSetCreateGroupPanel.setBackground(Color.WHITE);
		btnSetCreateGroupPanel.setBorder(null);
		btnSetCreateGroupPanel.setToolTipText("새 그룹 만들기");
		add(btnSetCreateGroupPanel);

		btnSetMainPanel.addActionListener(this);
		btnSetPersonalPanel.addActionListener(this);
		btnSetGroupPanel.addActionListener(this);
		btnSetCreateGroupPanel.addActionListener(this);

		initImage();
	}

	private void initImage() {
		homeIcon = ImageTransFormer.transformImage("ProjectImageIcon/home.png", ImageSizeX, ImageSizeY);
		btnSetMainPanel
				.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/homePush.png", ImageSizeX, ImageSizeY));
		personalIcon = ImageTransFormer.transformImage("ProjectImageIcon/personal.png", ImageSizeX, ImageSizeY);
		btnSetPersonalPanel.setIcon(personalIcon);
		groupIcon = ImageTransFormer.transformImage("ProjectImageIcon/manageGroup.png", ImageSizeX, ImageSizeY);
		btnSetGroupPanel.setIcon(groupIcon);
		createIcon = ImageTransFormer.transformImage("ProjectImageIcon/createNewGroup.png", ImageSizeX, ImageSizeY);
		btnSetCreateGroupPanel.setIcon(createIcon);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSetMainPanel) {
			setPanel(MainFrame.MAIN);
		}
		if (e.getSource() == btnSetPersonalPanel) {
			setPanel(MainFrame.MYSCHEDULE);
		}
		if (e.getSource() == btnSetGroupPanel) {
			setPanel(MainFrame.GROUPMANAGE);
		}
		if (e.getSource() == btnSetCreateGroupPanel) {
			setPanel(MainFrame.CREATEGROUP);
		}
		MainFrame.getInstance().getInitialData();
		MainFrame.getInstance().slideDown();
	}

	public void setPanel(int state) {
		btnSetMainPanel.setIcon(homeIcon);
		btnSetPersonalPanel.setIcon(personalIcon);
		btnSetGroupPanel.setIcon(groupIcon);
		btnSetCreateGroupPanel.setIcon(createIcon);
		switch (state) {
		case MainFrame.MAIN:
			MainFrame.getInstance().switchingPanel(MainFrame.MAIN);
			btnSetMainPanel
					.setIcon(ImageTransFormer.transformImage("ProjectImageIcon/homePush.png", ImageSizeX, ImageSizeY));
			break;
		case MainFrame.MYSCHEDULE:
			MainFrame.getInstance().switchingPanel(MainFrame.MYSCHEDULE);
			btnSetPersonalPanel.setIcon(
					ImageTransFormer.transformImage("ProjectImageIcon/personalPush.png", ImageSizeX, ImageSizeY));
			break;
		case MainFrame.GROUPMANAGE:
			MainFrame.getInstance().switchingPanel(MainFrame.GROUPMANAGE);
			btnSetGroupPanel.setIcon(
					ImageTransFormer.transformImage("ProjectImageIcon/manageGroupPush.png", ImageSizeX, ImageSizeY));
			break;
		case MainFrame.CREATEGROUP:
			MainFrame.getInstance().switchingPanel(MainFrame.CREATEGROUP);
			btnSetCreateGroupPanel.setIcon(
					ImageTransFormer.transformImage("ProjectImageIcon/createNewGroupPush.png", ImageSizeX, ImageSizeY));
			break;
		}
	}

}
