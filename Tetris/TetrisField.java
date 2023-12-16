import java.awt.*;
import javax.swing.JPanel;

public class TetrisField extends JPanel{

    private TetrisManager tm;

    public TetrisField(){
        setBackground(Color.black);
        setFocusable(true);
    }

    public void setTM(TetrisManager tm){
        this.tm = tm;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        tm.draw(g); // ゲーム画面の描画
    }
}