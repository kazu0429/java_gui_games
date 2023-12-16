import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

// import jp.fit.jc.it.BorderView;
// import jp.fit.jc.it.PPanel2;
// import jp.fit.jc.it.StringView;

public class Sample2606 extends JFrame {

	static String sample[][] = {{"1","","","","3"},{"","","2","",""},{"","","","3",""},{"","","","1",""},{"2","","","",""}};

	public Sample2606(String title) {
		super(title); // いつもの設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setLayout(new BorderLayout());
		setSize(550,550);

		Sample2606Panel panel = new Sample2606Panel(sample.length,sample.length,this); // パネルを作って配置
		add(panel,BorderLayout.CENTER);

		panel.setSample(sample); // サンプル文字をセット
	}
	// メインルーチン
	public static void main(String args[]) {
		Sample2606 frame = new Sample2606("文字列の表示");
		frame.setVisible(true);
	}
}

class Sample2606Panel extends PPanel2 {
	static Color[] myColor = {Color.pink,Color.yellow,Color.magenta,Color.cyan};
	int nColor = 0;

	StringView sview; // 文字列ビュー、あとでアクセスするのでフィールドにしておく

	Sample2606Panel(int x, int y, JFrame f) {
		super(x,y,f);
		setUnitSize(75,75);
		sview = new StringView(this,StringView.SMALL_NW); // NW(左上)に小さな文字を表示する文字列ビュー
		addPView(sview);
		addPView(new BorderView(this, BorderView.FULL_BORDER,Color.gray)); // 枠線ビュー
	}

	public void setSample(String s[][]) {
		sview.setStringByArray(s);
	}
}
