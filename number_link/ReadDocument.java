// XML を読み込むクラス
// eclipse を使っている場合は同じプロジェクト内に置く
import javax.xml.parsers.*; // XMLの解析に必要
import org.w3c.dom.*;       // 解析されたＸＭＬを扱う


public class ReadDocument {
	// read メソッドのみ準備している。uriはファイル名、もしくはURLでもOK
	public static Element read(String uri) {
		DocumentBuilder db;
		Document doc;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = db.parse(uri);
			return  doc.getDocumentElement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
