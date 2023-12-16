import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

// import jp.fit.jc.it.BorderLabelView;
// import jp.fit.jc.it.BorderView;
// import jp.fit.jc.it.PPanel2;

public class Sample1801 extends JFrame {
	public Sample1801(String title) {
		super(title); // いつもの設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setLayout(new BorderLayout());
		setSize(400,400);

		Sample1801Panel panel = new Sample1801Panel(9,9,this); //将棋盤のようなパネル
		add(panel,BorderLayout.CENTER);
	}

	public static void main(String args[]) {
		Sample1801 frame= new Sample1801("将棋盤のようなラベル付き罫線描画");
		frame.setVisible(true);
	}

}

class Sample1801Panel extends PPanel2 {
	static String[] yoko = {"9","8","7","6","5","4","3","2","1"};// 上ラベル
	static String[] tate = {"一","二","三","四","五","六","七","八","九"};// 右ラベル
	Sample1801Panel(int x, int y, JFrame f) {
		super(x,y,f);
		BorderLabelView blv =
				new BorderLabelView(this,BorderView.FULL_BORDER,Color.gray);
		blv.setLabels(yoko, BorderLabelView.NORTH); // 上ラベルの設定
		blv.setLabels(tate, BorderLabelView.EAST);  // 右ラベルの設定
		blv.setLabelOffset(2,10,BorderLabelView.EAST); // 右ラベルの表示位置調整

		addPView(blv);
	}
}