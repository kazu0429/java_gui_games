import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

// import jp.fit.jc.it.BorderView;
// import jp.fit.jc.it.LineView;
// import jp.fit.jc.it.PPanel2;

public class Sample2605 extends JFrame implements ActionListener{

	static String sample[][] = {{"1","","","","3"},{"","","2","",""},{"","","","3",""},{"","","","1",""},{"2","","","",""}};
	JRadioButton set,reset,flip;
	JButton result;
	Sample2605Panel panel;
	public Sample2605(String title) {
		super(title); // いつもの設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setLayout(new BorderLayout());
		setSize(750,550);

		panel = new Sample2605Panel(10,10,this); // パネルを作って配置
		add(panel,BorderLayout.CENTER);

		
		// ボタンを配置
		JPanel we = new JPanel();
		we.setLayout(new GridLayout(15,1));
		JLabel l1 = new JLabel("編集モード");
		we.add(l1);
		
		ButtonGroup bg = new ButtonGroup(); // ラジオボタンのグループ
		set = new JRadioButton("セット");
		set.addActionListener(this);
		bg.add(set);
		we.add(set);

		reset = new JRadioButton("リセット");
		reset.addActionListener(this);
		bg.add(reset);
		we.add(reset);

		flip = new JRadioButton("反転",true);
		flip.addActionListener(this);
		bg.add(flip);
		we.add(flip);
		panel.setMode(Sample2605Panel.FLIPMODE);

		result = new JButton("結果を見る");
		result.addActionListener(this);
		we.add(result);

		add(we,BorderLayout.WEST);
	}
	// メインルーチン
	public static void main(String args[]) {
		Sample2605 frame = new Sample2605("線を引くサンプル");
		frame.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o instanceof JRadioButton) {
			JRadioButton btn = (JRadioButton)o;
			if(btn == set)   { panel.setMode(Sample2605Panel.SETMODE);   }
			if(btn == reset) { panel.setMode(Sample2605Panel.RESETMODE); }
			if(btn == flip)  { panel.setMode(Sample2605Panel.FLIPMODE);  }
		}
		else if(o instanceof JButton) {
			JButton btn = (JButton)o;
			if(btn == result)   {
				String s[] = panel.getCharenge();
				for(int i = 0; i <s.length; i++) System.out.println(s[i]);
			}
		}

	}
}

class Sample2605Panel extends PPanel2 implements MouseListener, MouseMotionListener{
	public static final int SETMODE = 2;
	public static final int RESETMODE = 1;
	public static final int FLIPMODE = 0;

	int mode = FLIPMODE;
	int getMode() { return mode; }
	void setMode(int m) { mode = m; }

	LineView v1; // 編集用ビュー
	LineView v2; // 作業用ビュー

	Point cur;	// マウスの現在位置

	Sample2605Panel(int x, int y, JFrame f) {
		super(x,y,f);
		addPView(new BorderView(this, BorderView.FULL_BORDER,Color.black));//枠罫線ビューを装着

		// 編集用ビューを作る
		v1 = new LineView(this);
		v1.setLineColor(Color.darkGray);
		addPView(v1);  // 編集用ビューを装着

		// 作業用ビューを作る
		v2 = new LineView(this);
		v2.setLineColor(new Color(255,0,0,128)); // 半透明の赤
		addPView(v2); // 作業用Viewを装着

		// マウスリスナ装着
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	String[] getCharenge() {
		return v1.getCharenge(); //編集用ビューを文字列化して返す
	}

	// マウスが押された
	public void mousePressed(MouseEvent e) {
		cur = getUnit(e); // マウス位置を覚えておく
	}

	// マウスが放された
	public void mouseReleased(MouseEvent e) {
//		System.out.println("モードは"+mode+"だ！");
		if(cur != null) {
			switch(mode) {
			case SETMODE:
				v1.set(v2);
				break;
			case RESETMODE:
				v1.reset(v2);
				break;
			case FLIPMODE:
				v1.flip(v2);
				break;
			}
			v2.clear();
			frame.repaint();
		}
		cur = null; // マウス位置を忘れる。

	}

	// ドラッグして移動中
	public void mouseDragged(MouseEvent e) {
		if(cur == null) { // マウス位置が設定されていないなら設定する
			cur = getUnit(e);
			return;
		}
		Point now = getUnit(e);
		if(now == null) { // 枠外なら忘れる
			if(cur != null) {
				v2.clear();
				frame.repaint();
			}
			cur=null;
			return;
		}
		if(now.equals(cur)) return; // 同じユニットなら何もしない。
		if(now.x - 1 == cur.x) { // 右に動いた
			v2.setRight(cur,!v2.getRight(cur)); // 線の有無を反転する
			v2.setLeft(now, !v2.getLeft(now)); // 線の有無を反転する
			cur = now; // 位置を更新
			frame.repaint(); // 再描画
			return;
		}
		if(now.x + 1 == cur.x) { // 左に動いた
			v2.setLeft(cur, !v2.getLeft(cur)); // 線の有無を反転する
			v2.setRight(now,!v2.getRight(now)); // 線の有無を反転する
			cur = now; // 位置を更新
			frame.repaint(); // 再描画
			return;
		}
		if(now.y - 1 == cur.y) { // 下に動いた
			v2.setDown(cur,!v2.getDown(cur)); // 線の有無を反転する
			v2.setUp(now,!v2.getUp(now)); // 線の有無を反転する
			cur = now; // 位置を更新
			frame.repaint(); // 再描画
			return;
		}
		if(now.y + 1 == cur.y) { // 上に動いた
			v2.setUp(cur, !v2.getUp(cur)); // 線の有無を反転する
			v2.setDown(now, ! v2.getDown(now)); // 線の有無を反転する
			cur = now; // 位置を更新
			frame.repaint(); // 再描画
			return;
		}
	}

	@Override // 使わないメソッドたち
	public void mouseMoved(MouseEvent e) {	}
	public void mouseClicked(MouseEvent e) {	}
	public void mouseEntered(MouseEvent e) {	}
	public void mouseExited(MouseEvent e) {	}
}