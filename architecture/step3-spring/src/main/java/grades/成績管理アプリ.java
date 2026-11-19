package grades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Spring Boot の起動クラス。部品の組み立て（step1・2 の Main の仕事）は、Spring が行う。 */
@SpringBootApplication
public class 成績管理アプリ {

	public static void main(String[] args) {
		if (System.getenv("DB_PASSWORD") == null) {          // 設定し忘れを、起動のときに分かる言葉で知らせる
			System.err.println("環境変数 DB_PASSWORD が設定されていません。README の「パスワードの渡し方」を見てください。");
			return;
		}
		SpringApplication.run(成績管理アプリ.class, args);
	}

}
