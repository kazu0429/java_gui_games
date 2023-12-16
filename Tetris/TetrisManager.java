import java.awt.*;
import java.util.List;
import java.util.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TetrisManager extends Thread {

    private TetrisField tf;
    private final int BLOCK_SIZE = 20;   // ブロックサイズ
    private final int FIELD_WIDTH = 12;  // フィールドの横幅
    private final int FIELD_HEIGHT = 21; // フィールドの縦幅
    private int[][] FILED = new int[FIELD_HEIGHT][FIELD_WIDTH]; // フィールド
    private int jx, iy; // フィールド描画用の変数

    private Mino mino;  // ミノクラスのインスタンス
    private int minoNo; // 生成するミノの番号

    private boolean overFlag;  // ゲームオーバーか
    private StartScreen ss;    // スタート画面クラスのインスタンス
    private int level;         // レベル:上がるほどにミノの落下スピードが速くなる
    private int score;         // スコア
    private long minoNum;      // ミノを生成した数
    
    private Path filePath = Paths.get("HighScore.txt"); // ハイスコア保存ファイル
    private String fileNmae = "HighScore.txt";
    private Charset charset = Charset.forName("UTF-8"); //文字コード
    private List<String> highScore = new ArrayList<>(); // ハイスコア書き込み用
    private int highScoreText; // ハイスコア表示用
    private int tmpScore;      // ハイスコア読み込み用



    // コンストラクタ
    public TetrisManager(TetrisField tf){
        this.tf = tf;
        ss = new StartScreen(this);
        // ハイスコアを読み込む
        Path file = Paths.get(fileNmae);
        try{
            tmpScore = Integer.parseInt(Files.readString(file).replaceAll("\n", ""));
        }catch(IOException e){
            e.printStackTrace();
        }
        initGame();
    }
    
    // ゲームの初期化
    public void initGame(){
        // フィールドの初期化
        for(int i = 0; i<FIELD_HEIGHT; i++){
            for(int j = 0; j<FIELD_WIDTH; j++){
                if(j == 0 || j == FIELD_WIDTH-1 || i == FIELD_HEIGHT-1){
                    FILED[i][j] = 9; // 壁と床
                }else{
                    FILED[i][j] = 0; // 移動可能場所
                }
            }
        }
        overFlag = false;
        level = 1;
        score = 0;
        minoNum  = 1;
        createMino(); // ミノ生成
        highScoreText = tmpScore;  // ゲーム生成時にハイスコアの更新
    }

    // ミノを生成する
    public void createMino(){
        int cnt = 0; 
        /* 上から3段以内にミノがなければ全ての種類からミノを選ぶ*/ 
        for(int i = 0; i<3;i++){
            for(int j = 1; j<FILED[i].length-1;j++){
                if(FILED[i][j] != 0){
                    cnt++;
                }
            }
        }
        if(cnt == 0)minoNo = (int)(Math.random()*7);
        else minoNo = (int)(Math.random()*3);
        mino = new Mino(this, minoNo);
    }

    // ゲームの実行
    public void run(){
        while(true){
            while(mino.movableMino()){
                int time = 501-level; // 最初は落下時間が0.5秒スタート
                wait(time); // 落下のための待機時間
                tf.repaint();  // 再描画
            }
            FILED = mino.upDateFiled();        // フィールド更新
            if(!gameOver()){
                if(minoNum % 10 == 0) level++; // ミノが10個生成されるたびにlevelup
                score += mino.scoreUp();       // 揃っていたらscoreUp
                createMino();                  // 新しいミノの生成
                minoNum++;                     
            }else{
                higtScoreWite();               // ハイスコアの書き込み
            }
        }
    }

    // ファイルのハイスコアを書き込む
    public void higtScoreWite(){
        if(score > highScoreText){
            highScore = new ArrayList<>(Arrays.asList(String.valueOf(score)));
            try {
                Files.write(filePath,highScore,charset, 
                        StandardOpenOption.TRUNCATE_EXISTING); //新規書き込み
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 落下時間用
    public void wait(int time){
        try{
            sleep(time);
        }catch(InterruptedException e){}
    }

    // ミノの描画やスコア、メッセージを表示
    public void draw(Graphics g){
        if(!ss.gameStart()){
            ss.draw(g);
            return;
        }
        g.setColor(Color.white);
        g.drawString("HigtScore:"+(highScoreText),300,20);
        g.drawString("Level:"+level,300,40);
        g.drawString("Score:"+(score),300,60);
        g.drawString("ESCAPEキー：最初から",0,460);
        if(mino == null) return;
        mino.draw(g);
        fieldGridDraw(g);
        if(overFlag){
            drawOverMsg(g);
        }
    }

    // フィールドを描画する関数
    public void fieldGridDraw(Graphics g){
        if(!ss.gameStart()) return; // スタートしていなければ何もしない

        // フィールド配列の要素ごとに描画を分ける
        for(int i = 0; i<FIELD_HEIGHT; i++){
            for(int j = 0; j < FIELD_WIDTH;j++){
                jx = j*20+20;
                iy = i*20+20;
                switch(FILED[i][j]){
                    case 9: g.setColor(Color.black);g.drawRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            g.setColor(Color.white);g.fillRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            break;
                    case 0: g.setColor(Color.white);g.drawRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE); 
                            break;
                    case 1: g.setColor(Color.white);g.drawRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            g.setColor(Color.red);g.fillRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            break;
                    case 2: g.setColor(Color.white);g.drawRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            g.setColor(Color.blue);g.fillRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            break;
                    case 3: g.setColor(Color.white);g.drawRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            g.setColor(Color.green);g.fillRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            break;
                    case 4: g.setColor(Color.white);g.drawRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            g.setColor(Color.yellow);g.fillRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            break;
                    case 5: g.setColor(Color.white);g.drawRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            g.setColor(Color.cyan);g.fillRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            break;
                    case 6: g.setColor(Color.white);g.drawRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            g.setColor(Color.magenta);g.fillRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            break;
                    case 7: g.setColor(Color.white);g.drawRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            g.setColor(Color.orange);g.fillRect(jx, iy, BLOCK_SIZE, BLOCK_SIZE);
                            break;
                }
            }
        }
    }

    // ゲームオーバ時の表示用
    public void drawOverMsg(Graphics g){
        g.setColor(Color.white);
        g.fillRect(80, 165, 200, 50);
        g.setColor(Color.red);
        g.setFont(new Font("", Font.PLAIN, 30));
        g.drawString("GAMEOVER", 100, 200);
        g.fillRect(60, 310, 240, 70);
        g.setColor(Color.black);
        g.setFont(new Font("", Font.PLAIN, 20));
        g.drawString("あなたの得点は"+(score)+"pt",75,340);
        if(score > highScoreText){
            g.setFont(new Font("", Font.PLAIN, 20));
            g.drawString("ハイスコア！",120,370);
            tmpScore = score;
        }
    }

    // フィールドの上段にブロックがあればゲームオーバー
    public boolean gameOver(){
        for(int i = 0; i<2;i++){
            for(int j = 1; j < FILED[i].length-1;j++){
                if(FILED[i][j] != 0){
                    overFlag = true;
                    return true;
                }
            }
        }
        return false;
    }

    public int[][] getFILED(){
        return FILED;
    }

    public TetrisField getTF(){
        return tf;
    }
    
    // フィールド状況をコンソールから確認する関数
    public void showField(){
        for(int i = 0; i<FIELD_HEIGHT; i++){
            for(int j = 0; j<FIELD_WIDTH;j++){
                System.out.print(FILED[i][j] + " ");
            }
            System.out.println();
        }
    }
}
