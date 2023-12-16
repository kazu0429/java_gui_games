import javax.swing.*;

public class Tetris extends JFrame{

    public Tetris(){
        super("Tetris");  // タイトル
        setSize(400,500); // 画面サイズ
        setLocation(300,100); // 画面設置位置
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 閉じる用
        TetrisField tf = new TetrisField(); 
        TetrisManager tm = new TetrisManager(tf);
        tf.setTM(tm);
        add(tf);
        setVisible(true);
        tm.start(); // ゲーム開始
    }
    public static void main(String[] args){
        new Tetris();
    }
}
