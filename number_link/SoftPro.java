import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.Timer;
import java.util.TimerTask;
import org.w3c.dom.*;

public class SoftPro extends JFrame implements ActionListener{

    static String XMLFileName = "NumberLink.xml";// 問題と答えが入っているXMLファイル
    String mondai[];// 問題
    String kotae[];// 答え

    Timer timer; // タイマークラス
	MyTime rest; // 経過時間クラス
	TimerUtil task; // タイマーの実行タスク

    NumberLinkPanel panel; // 画像描画パネル
    JRadioButton set,reset,flip;// 線を引く、線を消す、どちらもできる
    JButton allclear,check,next,end; // 「リセット」「チェック」「次」「終了」
    JLabel settime; // 経過時間表示ラベル
    int number = 0;// 問題番号
    Element doc; // XML ファイルの内容が入る。
	Element xMondai; // XML形式の問題(1問のみ)
    public SoftPro(int num,String xml){// SfoProのコンストラクタ
        super();
        setSize(500,500);// フレームのサイズ
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// バツボタンで閉じる
		setLocationByPlatform(true);// フレームを置く
        setLayout(new BorderLayout());// レイアウトの設定

        // XMLから問題をとってくるのは野田担当
        doc = ReadDocument.read(xml); // XMLを全て読み出しておく
		number = num;// 問題番号
        NodeList nl = doc.getElementsByTagName("title");     // タイトルタグ部分を得る
		setTitle(nl.item(0).getFirstChild().getNodeValue()); // 中身を取り出しタイトルバーにセット

        xMondai = (Element) doc.getElementsByTagName("Mondai").item(number);// Mondaiタグを得る
		mondai = getStrings(xMondai,"question"); // 問題を取り出す
		kotae  = getStrings(xMondai,"answer"); // 答えを取り出す

        int w = mondai[0].length(); // 問題の横幅 
        int h = mondai.length; // 問題の行数
        panel = new NumberLinkPanel(w,h,this); // パネルを作る
		add(panel,BorderLayout.CENTER); // パネルをセンターに追加
		panel.setMondai(mondai); // 問題をセットする

        JPanel pl1 = new JPanel(); // ラジオボタン郡の配置
		pl1.setLayout(new GridLayout(10,1));// 10行１列のレイアウト

        JLabel lb1 = new JLabel("入力モード");
        JLabel lb2 = new JLabel("------------");
        ButtonGroup rb = new ButtonGroup();// ラジオボタンの配置
        set = new JRadioButton("鉛筆");
		set.addActionListener(this);
        reset = new JRadioButton("消しゴム");
		reset.addActionListener(this);
        flip = new JRadioButton("自動",true);
		flip.addActionListener(this);

        rb.add(set);
        rb.add(reset);
        rb.add(flip);
        pl1.add(lb1);
        pl1.add(set);
        pl1.add(reset);
        pl1.add(flip);
        pl1.add(lb2);
      
        allclear = new JButton("リセット");
        allclear.addActionListener(this);
        check = new JButton("チェック");
        check.addActionListener(this);
        next = new JButton("次へ");
        next.addActionListener(this);
        end = new JButton("終了");
        end.addActionListener(this);

        pl1.add(allclear);
        pl1.add(check);
        pl1.add(next);
        pl1.add(end);

        settime = new JLabel("経過時間");
        pl1.add(settime);
        setUpTimer(); // タイマーの設定を行う

        add(pl1,BorderLayout.WEST);// 左に配置

        JPanel pl2 = new JPanel();// ルール説明パネルの配置
        pl2.setLayout(new GridLayout(1,1));// １行１列のレイアウト
        // htmlでルール説明
        JLabel RuleJabel1 = new JLabel("<html><body>ルール説明<br>"+
                                        "1. 空白マスに線を引いて、同じ数字どうしをつなげていく<br>"+
                                        "2. 線を交差させたり、枝分かれさせたりしてはいけない<br>"+
                                        "3. 一マスには線は一本しか引けません<br>"+
                                        "4. 線は縦横に引き、斜めには引けません<br>"+
                                        "5. 線は全てのマスを通るようにしないといけません</body></html>");
        RuleJabel1.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));// フォントの指定
        LineBorder border = new LineBorder(Color.black, 1, true);// ルール説明の枠線
        RuleJabel1.setBorder(border);// 枠線を追加
        pl2.add(RuleJabel1);// ルールラベルの追加
        add(pl2,BorderLayout.SOUTH);// 下に配置
    }
    public static void main(String[]args){
        SoftPro frame = new SoftPro(0,XMLFileName);// 問題番号0から表示
        frame.setVisible(true);
    }
    public void actionPerformed(ActionEvent e){// ボタンが押されたときの処理
        Object obj = e.getSource();// 押されたボタンのオブジェクトを取得
        if(obj instanceof JRadioButton) {// ラジオボタンか
			JRadioButton btn = (JRadioButton)obj;// オブジェクトをJRadioButtonにキャスト
			if(btn == set)   { panel.setMode(NumberLinkPanel.SETMODE);   }// 線を描くのだけモード
			if(btn == reset) { panel.setMode(NumberLinkPanel.RESETMODE); }// 線を消すだけのモード
			if(btn == flip)  { panel.setMode(NumberLinkPanel.FLIPMODE);  }// どちらもできるモード
		}
        if(obj instanceof JButton) {// Jボタンか
            JButton b = (JButton)obj;// オブジェクトをJButtonにキャスト
            if(b == end){// 終了ボタンが押されたとき
                int optione = JOptionPane.showConfirmDialog(this,"終了しますか？",
                                "終了", JOptionPane.YES_NO_OPTION);// ダイアログの表示
                if(optione == JOptionPane.YES_OPTION){// 「はい」が押された時
                    System.exit(0);// システム終了
                }else{
                    // 何もしない
                }
            }else if(b == allclear){// リセットボタンが押されたとき
                int ret = JOptionPane.showConfirmDialog(this,"盤面を初期化します。", 
                                "リセット", JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.ERROR_MESSAGE);// ダイアログの表示
                if(ret==JOptionPane.OK_OPTION) {// 「はい」が押された時
                    panel.lineClear();// 描いた線を全て消す
                }
            }else if(b == next){// 次へボタンが押されたとき
                NodeList mn = doc.getElementsByTagName("Mondai"); // 全ての問題を取り出しておく
                int optionn = JOptionPane.showConfirmDialog(this,"次の問題に行きますか？",
                                 "選択", JOptionPane.YES_NO_OPTION);// ダイアログの表示
                if(optionn == JOptionPane.YES_OPTION){// 「はい」が押された時
                    goNext(number,mn);
                }else{
                    // 何もしない
                }
            }else if(b == check){// チェックボタンが押されたとき
                String[] s = panel.getCharenge();// 線の文字列を取得
                if(checkAnswer(kotae,s)){// 用意した答えと比較
                    stop();// 時間停止
                    JOptionPane.showMessageDialog(this,"おめでとうございます!", 
                            "正解!", JOptionPane.INFORMATION_MESSAGE);// 正解の時のダイアログ
                }else{
                    JOptionPane.showMessageDialog(this,"残念!", 
                            "不正解!", JOptionPane.INFORMATION_MESSAGE);// 不正解の時のダイアログ
                }
            }
        }
    }
    // 次の問題へ移行する関数
    public void goNext(int number,NodeList mn){
                    number++;// 次の問題番号に
                    if(number >= mn.getLength()) { // 問題がなくなったら最初に戻す
                        number = 0;
                    }
                    SoftPro nextmondai = new SoftPro(number,XMLFileName); // 新しい問題用のフレームを作る
                    nextmondai.setVisible(true); // 新しいフレームを可視化
                    dispose(); // 現在のフレームを消滅させる
    }
    // 答えと解答の比較
    boolean checkAnswer(String[] a, String[] b) {
		if(a.length != b.length) { // 配列の大きさが違うとき
			return false; // 違ったら、偽を返す
		}
		for(int i = 0; i < a.length; i++) { // １行ごとに比較する
			if(! a[i].equals(b[i])) {// 解答と答えが同じ時
                return false;       // 違ったら、偽を返す
			}
		}
		return true;    // 全ての行が一致した。
	}
    // 問題や答えの文字列の並びを得る
    String[] getStrings(Element mondai,String tag) {
		String s[];// 空配列
		NodeList nl = mondai.getElementsByTagName(tag);//  questionタグかanswerタグ部分を得る
		s = new String[nl.getLength()];// 配列の長さの指定
		for(int i = 0; i < nl.getLength() ;i++) {
			try {
				s[i] = nl.item(i).getFirstChild().getNodeValue(); // 取ってきた文字を配列に入れる
			} catch(Exception e) {// 例外処理
				s[i] = ""; // 空文字を入れる
			}
		}
		return s;// 配列を返す
	}
    //制限時間に関するクラス、関数は西田担当
    public void setUpTimer() {
		timer = new Timer();// Timerクラスのインスタンスの生成
		rest = new MyTime();// MyTimeクラスのインスタンスの生成
		start();// タイマー開始
	}
    public void start() {// タイマー開始
    	if(task == null) task = new TimerUtil(rest,settime);// TimerUtilクラスのインスタンス
        timer.schedule(task, 0, 1000); // 1秒おきにrunメソッド実行
    }
    public void stop() { // タイマーの終了
        task.cancel(); // 時間停止
        task = null; 
    }

}
class TimerUtil extends TimerTask {
	MyTime time; // 管理する時間
	JLabel label; // 時間表示用ラベル
	public TimerUtil(MyTime t,JLabel l) {
		time = t;
		label = l;
	}
	@Override
	public void run() {// 1秒おきに実行するメソッド
		time.countUp(); // カウントアップ
		int second = time.getSecond();
        int minutes = time.getMinutes();
		if(minutes < 10){
            if(second < 10){
                label.setText("経過時間 0"+minutes+":0"+second);
		    }else{
			    label.setText("経過時間 0"+minutes+":"+second);
		    }
        }else{
            if(second < 10){
                label.setText("経過時間 "+minutes+":0"+second);
		    }else{
			    label.setText("経過時間 "+minutes+":"+second);
		    }
        }
	}
}
class MyTime { //残り時間クラス
	int minutes; // 分数
	int second; // 秒数
	MyTime() {this(0);} // 引数なしコンストラクタ
	MyTime(int t) { second = t; } // コンストラクタ
	public void countUp() { // 1秒ごとに増やす
		if(second >= 0) {
			second++;
		}
		if(second == 60){
			second=0;
			minutes++;
		}
	}
	int getSecond() {
		return second; //秒数を得る
	} 
	int getMinutes(){
		return minutes;// 分数を得る。
	}
}

// 問題パネルと線を描画するパネル
class NumberLinkPanel extends PPanel2 implements MouseMotionListener,MouseListener{
    
    public static final int FLIPMODE = 0;// 自動モード
	public static final int RESETMODE = 1;// 線を消すだけのモード
    public static final int SETMODE = 2;// 線を描くだけのモード
    int mode = FLIPMODE;// 最初は自動モード
 
    int getMode() { return mode; }// 現在のモード
	void setMode(int m) { mode = m; }// モードをセット

    Point cur; // マウスの位置
    LineView v1; // 編集用ビュー
	LineView v2; // 作業用ビュー

    CombinedView cview;// 問題用パネル
   
    class DrawInvert extends DrawItem {
    	Color fgColor,bgColor; // 文字色と背景色
		public DrawInvert(PPanel2 p,Color fg,Color bg) {
			super(p);
			fgColor = fg;
			bgColor = bg;
		}
		public void draw(Graphics g, int px, int py, char ch) {
	    	int x = getOriginX(px); // 座標値を計算する
	    	int y = getOriginY(py);
			g.setColor(bgColor); // 色をセットし
			g.fillRect(x, y, getUnitSize().x, getUnitSize().y); // 矩形に塗る
			cview.setFontColor(fgColor); // 色をセットし
			cview.drawChar(g, px, py, ch);// 枠に文字を書く
		}
    }

    NumberLinkPanel(int x, int y, JFrame f) {// コンストラクタ
		super(x,y,f);
        // 編集用ビューを作る
        v1 = new LineView(this);
		v1.setLineColor(Color.darkGray);// 灰色
		addPView(v1);  // 編集用ビューを装着
		// 作業用ビューを作る
		v2 = new LineView(this);
		v2.setLineColor(new Color(255,0,0,128)); // 半透明の赤
		addPView(v2); // 作業用Viewを装着
		// マウスリスナ装着
		addMouseListener(this);
		addMouseMotionListener(this);

        cview = new CombinedView(this);// 問題パネル
        DrawItem bstr = new DrawInvert(this,Color.black,Color.white); // 反転文字クラスのインスタンスを作る
        char str_num[] = {'１','２','３','４','５','６','７','８','９'};// 表示する文字列を用意
        for(int i = 0; i<str_num.length;i++) cview.setDrawItem(str_num[i], bstr);// 枠に文字を表示

		addPView(cview);
		addPView(new BorderView(this, BorderView.FULL_BORDER,Color.black));//枠罫線ビューを装着
	}

    // 問題をセットする
    void setMondai(String[] m) {
    	cview.setCharByString(m);// cviewパネルに文字をセット
    }
    //編集用ビューを文字列化して返す
    String[] getCharenge() {
		return v1.getCharenge(); // v1パネルの描画内容を文字列にして返す
	}
    //描画されている線を消す
    public void lineClear(){
        v1.clear();// 線を全て消す
        frame.repaint();
    }
    // マウスを押したとき
    public void mousePressed(MouseEvent e) {
		cur = getUnit(e); // マウス位置を覚えておく
	}
    // マウスを離したとき
    public void mouseReleased(MouseEvent e) {
        if(cur != null) {
            switch(mode) {// モードの判定
            case SETMODE:
                v1.set(v2); // 書くだけのモードの時は線を引く
                break;
            case RESETMODE:
                    v1.reset(v2); // 消すだけのモードの時は線を消す
                break;
            case FLIPMODE:
                v1.flip(v2); // どちらもできるのモードの時はケースバイケース
                break;
            }
            v2.clear();
            frame.repaint();
        }
        cur = null; // マウス位置を忘れる
    }
    // マウスをドラッグしているとき
    public void mouseDragged(MouseEvent e) {
		if(cur == null) { // マウス位置が設定されていないなら設定する
			cur = getUnit(e);// マウス位置を覚えておく
			return;
		}
		Point now = getUnit(e);
		if(now == null) { // 枠外なら忘れる
			if(cur != null) {
				frame.repaint();// マウス位置を覚えておく
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
    // 使わないメソッド
    public void mouseMoved(MouseEvent e) {	}
	public void mouseClicked(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) {	}
}

