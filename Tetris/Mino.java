import java.awt.*;
import java.awt.event.*;


public class Mino implements KeyListener{

    private int [][] FILED; // 盤面
    private int FW, FH; // 盤面の横幅と縦幅
    private int[][][] mino; // 特定のミノ用
    private int sx = 20, sy = 20; // 移動幅
    private int nx, ny; // 現在のミノの描画位置
    private int w = 20, h = 20; // ブロックサイズ
    private int x = 5, y = 0 ; // フィールドのスタート位置、ミノの位置管理
    private int rot = 0; // 回転の状態
    private int next_x = 0, next_y = 0,next_rot = 0; // 次の行動を判定する変数
    private int score; // スコア

    // [ミノの型][回転状態][行列][要素]
    private int minos[][][][] = {
        {
            // O型
            {
                {1,1},
                {1,1}
            }
        },{
            // T型
            {
                {0,2,0},
                {2,2,2},
                {0,0,0}
            },{
                {0,2,0},
                {0,2,2},
                {0,2,0}
            },{
                {0,0,0},
                {2,2,2},
                {0,2,0}
            },{
                {0,2,0},
                {2,2,0},
                {0,2,0}
            }
        },{ // Z型
            { 
                {5,5,0},
                {0,5,5},
                {0,0,0}
            },{
                {0,5,0},
                {5,5,0},
                {5,0,0}
            }
        },{ // s型
            {
                {0,6,6},
                {6,6,0},
                {0,0,0}
            },{
                {0,6,0},
                {0,6,6},
                {0,0,6}
            }
        },{ // I型
            {
                {0,0,0,0},
                {3,3,3,3},
                {0,0,0,0},
                {0,0,0,0}
            },{
                {0,0,3,0},
                {0,0,3,0},
                {0,0,3,0},
                {0,0,3,0}
            }
        },{ // J型
            {
                {0,0,0,0},
                {0,4,0,0},
                {0,4,4,4},
                {0,0,0,0}
            },{
                {0,0,0,0},
                {0,4,4,0},
                {0,4,0,0},
                {0,4,0,0} 
            },{
                {0,0,0,0},
                {4,4,4,0},
                {0,0,4,0},
                {0,0,0,0} 
            },{
                {0,0,4,0},
                {0,0,4,0},
                {0,4,4,0},
                {0,0,0,0} 
            }
        },{ // L型
            {
                {0,0,0,0},
                {0,7,7,7},
                {0,7,0,0},
                {0,0,0,0}
            },{
                {0,0,0,0},
                {0,7,7,0},
                {0,0,7,0},
                {0,0,7,0}
            },{
                {0,0,0,0},
                {0,0,7,0},
                {7,7,7,0},
                {0,0,0,0}
            },{
                {0,7,0,0},
                {0,7,0,0},
                {0,7,7,0},
                {0,0,0,0}
            }
        }
    };

    public Mino(TetrisManager tm, int number){
        mino = minos[number]; // number番目のミノ生成
        tm.getTF().addKeyListener(this); // キーリスナー登録
        FILED = tm.getFILED(); // フィールド取得
        FH = FILED.length; 
        FW = FILED[0].length;
        score = 0;
    }

    // ミノの描画
    public void draw(Graphics g){
        for(int i = 0; i<mino[rot].length;i++){
            for(int j = 0; j<mino[rot][i].length; j++){
                nx = j*w+sx+sx*x;
                ny = i*h+sy*y;
                // 要素ごとに色を変える
                switch(mino[rot][i][j]){
                    case 0:break;
                    case 1:g.setColor(Color.red);g.fillRect(nx, ny, w, h);break;
                    case 2:g.setColor(Color.blue);g.fillRect(nx, ny, w, h);break;
                    case 3:g.setColor(Color.green);g.fillRect(nx, ny, w, h);break;
                    case 4:g.setColor(Color.yellow);g.fillRect(nx, ny, w, h);break;
                    case 5:g.setColor(Color.cyan);g.fillRect(nx, ny, w, h);break;
                    case 6:g.setColor(Color.magenta);g.fillRect(nx, ny, w, h);break;
                    case 7:g.setColor(Color.orange);g.fillRect(nx, ny, w, h);break;
                }
            }
        }
    }

    // ミノを動かす
    public boolean movableMino(){
        for(int i = 0; i<mino[rot].length;i++){
            for(int j = 0; j<mino[rot][i].length; j++){
                if(mino[rot][i][j] != 0){
                    if(FILED[y+i][x+j] != 0){
                        y = (y==0)?y:--y; // 一番上ならそのまま、そうでないなら増やした分減らす
                        return false;
                    }
                }
            }
        }
        y++; // 下へ移動
        return true;
    }

    // 移動完了後にフィールドを更新
    public int[][] upDateFiled(){
        for(int i = 0; i<mino[rot].length;i++){
            for(int j = 0; j<mino[rot][i].length; j++){
                if(mino[rot][i][j] != 0){
                    FILED[y+i][x+j] = mino[rot][i][j]; // フィールドに現在のミノの位置を代入
                }
            }
        }
        removeGrid();
        return FILED;
    }
    
    // スコアを返す
    public int scoreUp(){
        return score;
    }


    // 揃っている行の判定と削除
    public void removeGrid(){
        for(int i = 0; i<FH-1;i++){
            int cnt = 0;
            for(int j = 1;j<FILED[i].length-1;j++){
                if(FILED[i][j] != 0){
                    cnt++;
                }
            }
            // 横に10個揃っていたら
            if(cnt == 10){
                // 揃っている行に0を代入
                for(int j = 1;j<FILED[i].length-1;j++){
                    FILED[i][j] = 0;
                }
                // それより上のミノを1つずつ下に移動させる
                for(int k = i-1; k>=0;k--){
                    for(int j = 0;j<FILED[k].length;j++){
                        FILED[k+1][j] = FILED[k][j];
                    }
                }
                score += 100;
            }
        }
    }

    // キーが押された時
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        switch(key){
            case KeyEvent.VK_RIGHT: x = slideto(1);break;
            case KeyEvent.VK_LEFT : x = slideto(-1);break;
            case KeyEvent.VK_A    : rot = rotateto(-1);break;
            case KeyEvent.VK_D    : rot = rotateto(1);break;
            case KeyEvent.VK_UP   : y = fastfall();break;
            case KeyEvent.VK_DOWN : y = fall();break;
        }
    }

    // 横に移動できるか
    public int slideto(int vec){
        next_x = x+vec;
        for(int i = 0; i<mino[rot].length;i++){
            for(int j = 0; j<mino[rot][i].length; j++){
                if(mino[rot][i][j] != 0){
                    // 端でないか、空きスペースかどうか
                    if(next_x+j<0||next_x+j>FW||FILED[y+i][next_x+j] != 0){
                        return x;
                    }
                }
            }
        }
        return next_x;
    }

    // 回転できるか
    public int rotateto(int vec){
        if(vec!=1) next_rot = (rot==0)?mino.length-1:rot-1; // 回転状況が一番初めなら最後に、それ以外ならひとつ戻す
        else next_rot = (rot==mino.length-1)?0:rot+1;       // 回転状況が一番最後なら最初に、それ以外はひとつ進める
        for(int i = 0; i<mino[next_rot].length;i++){
            for(int j = 0; j<mino[next_rot][i].length; j++){
                if(mino[next_rot][i][j] != 0){
                    // 回転した場所が下、端、空きスペースでないか
                    if(y>FILED.length|| x+j<0||x+j>FW||FILED[y+i][x+j] != 0){
                        return rot;
                    }
                }
            }
        }
        return next_rot;
    }

    // 落下できるところまで一気に落下する
    public int fastfall(){
        next_y = y++;
        for(int k = next_y; k<FH;k++){
            for(int i = 0; i<mino[rot].length;i++){
                for(int j = 0; j<mino[rot][i].length; j++){
                    if(mino[rot][i][j] != 0){
                        if(next_y+i>FH||FILED[next_y+i][x+j] != 0){
                            return --next_y;
                        }
                    }
                }
            }
            next_y++;
        }
        return next_y;
    }

    // 下矢印を押している間、下へ加速
    public int fall(){
        next_y = y++;
        for(int i = 0; i<mino[rot].length;i++){
                for(int j = 0; j<mino[rot][i].length; j++){
                    if(mino[rot][i][j] != 0){
                        if(next_y>FH||FILED[next_y+i][x+j] != 0){
                            return --next_y;
                        }
                    }
                }
            }
        return ++next_y;
    }

    public void keyReleased(KeyEvent ev){ }
    public void keyTyped(KeyEvent ev){ }
}
