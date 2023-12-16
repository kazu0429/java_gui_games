import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

//import jp.fit.jc.it.CharacterView;
//import jp.fit.jc.it.PPanel2;



public class Sample2601 extends JFrame implements ActionListener {

	static String sample[] = {"This is","a sample","★★○●◎△"};
	JButton large,middle,small;

	Sample2601Panel panel;
	public Sample2601(String title) {
		super(title); // いつもの設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setLayout(new BorderLayout());
		setSize(600,500);

		panel = new Sample2601Panel(6,6,this); // パネルを作って配置
		add(panel,BorderLayout.CENTER);
		panel.setCharByString(sample); // サンプル文字をセット

		// 枠の大きさを変えてみるボタンたち。
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(10,1));

		large = new JButton("大");
		large.addActionListener(this);
		p.add(large);

		middle = new JButton("中");
		middle.addActionListener(this);
		p.add(middle);

		small = new JButton("小");
		small.addActionListener(this);
		p.add(small);
		add(p,BorderLayout.WEST);
	}
	// メインルーチン
	public static void main(String args[]) {
		Sample2601 frame = new Sample2601("文字出す＋罫線を出す＋枠サイズ変える");
		frame.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		if(b == large) {
			panel.setUnitSize(64, 64);
			repaint();
		} else if(b == middle) {
			panel.setUnitSize(32, 32);
			repaint();
		} else if(b == small) {
			panel.setUnitSize(16, 16);
			repaint();
		}
	}
}

class Sample2601Panel extends PPanel2 {
	CharacterView cview; // 文字を表示するビュー、あとでアクセスするのでフィールドにしておく
	Sample2601Panel(int x, int y, JFrame f) {
		super(x,y,f);
		cview = new CharacterView(this);
		addPView(cview);
		addPView(new BorderView(this, BorderView.FULL_BORDER,Color.black));
	}

	public void setCharByString(String s[]) {
		cview.setCharByString(s); // 文字をセットする。
	}

}