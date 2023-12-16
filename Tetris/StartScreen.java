import java.awt.*;
import java.awt.event.*;

public class StartScreen implements KeyListener{

    private boolean start; // 開始フラグ
    private TetrisManager tm;
    private String[] title = {"T","E","T","R","I","S"};
    private Color[] color = {Color.red,Color.green,Color.orange,Color.magenta,Color.cyan,Color.yellow};
    private int x = 120, y = 200; // 描画位置 

    public StartScreen(TetrisManager tm){
        this.tm = tm;
        this.tm.getTF().addKeyListener(this); // キーリスナーに登録
        start = false;
    }

    public void draw(Graphics g){
        for(int i = 0; i<title.length;i++){
            g.setColor(color[i]);
            g.setFont(new Font("", Font.PLAIN, 40));
            g.drawString(title[i], i*30+x,y);
        }
        g.setColor(Color.white);
        g.setFont(new Font("", Font.PLAIN, 30));
        g.drawString("Press SPACE key to Start", 30, 300);
    }

    // tmクラスにスタート状況を返す
    public boolean gameStart(){
        return start;
    }

    // スペースキーが押されたらスタート
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_SPACE && start==false){
            this.tm.initGame();  
            start=true;
        }
        if(key == KeyEvent.VK_ESCAPE){
            this.tm.initGame();
        }
    }

    public void keyReleased(KeyEvent e){ }
    public void keyTyped(KeyEvent e){}
}