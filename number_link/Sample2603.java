import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

// import jp.fit.jc.it.BorderView;
// import jp.fit.jc.it.CombinedView;
// import jp.fit.jc.it.DrawItem;
// import jp.fit.jc.it.PPanel2;


public class Sample2603 extends JFrame {

	static String sample[] = {"白黒白黒白黒","桃黒Z","赤青黄紫藍緑","燈UB△","u  b"};

	public Sample2603(String title) {
		super(title); // いつもの設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setLayout(new BorderLayout());
		setSize(300,300);

		Sample2603Panel panel = new Sample2603Panel(6,6,this); // パネルを作って配置
		add(panel,BorderLayout.CENTER);


		panel.setSample(sample); // サンプル文字をセット
		panel.setImage(); // サンプル文字をセット
	}
	// メインルーチン
	public static void main(String args[]) {
		Sample2603 frame = new Sample2603("複合パネルの使い方");
		frame.setVisible(true);
	}
}

class Sample2603Panel extends PPanel2 {
	CombinedView cview; // 複合ビュー、あとでアクセスするのでフィールドにしておく
	Sample2603Panel(int x, int y, JFrame f) {
		super(x,y,f);
		cview = new CombinedView(this);
		addPView(cview);
		addPView(new BorderView(this, BorderView.FULL_BORDER,Color.black));
	}

	public void setSample(String s[]) {
		cview.setDrawColor('桃',Color.pink);
		cview.setDrawColor('白',Color.white);
		cview.setDrawColor('黒',Color.black);
		cview.setDrawCharByString("ABCDZ赤青黄紫藍緑");
		cview.setCharByString(s);
	}

	public void setImage() {
		Image ufo = new ImageIcon("ufo.png").getImage();
		Image bomb = new ImageIcon("bomb.png").getImage();
		cview.setDrawImage('u', ufo);
		cview.setDrawImage('b', bomb);
		cview.setDrawFixedImage('U', ufo);
		cview.setDrawFixedImage('B', bomb);

		cview.setDrawItem('△', new DrawItem(this) {

			@Override
			public 	void draw(Graphics g, int x, int y, char ch) { // 赤い三角を描画する
				g.setColor(Color.red);
				Point p = panel.getOrigin(x,y); // 三角形の頂点を計算
				int px[] = {p.x + size.x/2, p.x + size.x, p.x}, py[] = {p.y,p.y+size.y,p.y + size.y};
				g.fillPolygon(px, py, 3);
			}});
	}
}