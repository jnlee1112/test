import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageTransFormer {
	static public ImageIcon transformImage(String str, int x, int y) {
		ImageIcon originIcon = new ImageIcon(str);
		Image originImg = originIcon.getImage();
		Image changedImg = originImg.getScaledInstance(x, y, Image.SCALE_SMOOTH);

		return new ImageIcon(changedImg);
	}
}
