import java.io.File;
import java.util.Scanner;


public class FileIOStep0 {

	public static void main(String[] args) {
		int line = 0;
		try {
			Scanner sc = new Scanner(new File("sample.dat"));
			while(sc.hasNext()) { // 次の行が読める。
				String l = sc.nextLine(); // 1行読む
				line ++;
				String item[] = l.split(","); // コンマで区切る
				System.out.print(line+":"+item[0]+" "); // 行番号を出力
				int tanka = Integer.parseInt(item[1]); // 2個目を数値化
				int kosu  = Integer.parseInt(item[2]); // 3個目を数値化
				System.out.println(tanka+"(円)×"+kosu+"(個)="+tanka*kosu+"円");
			}
		} catch(Exception e) {
			System.out.println("エラーが発生しました。");
			e.printStackTrace(); // エラー情報を出力
		}
	}

}
