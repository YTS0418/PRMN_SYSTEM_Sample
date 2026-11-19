package grades;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot の起動クラス。画面のテーマの指定も、ここに書く（フロントエンド編 13 章）。
 * テーマを指定しないときは、Vaadin の既定のテーマが使われる。指定する場合は、@StyleSheet(…) を付ける。
 */
@SpringBootApplication
public class 成績管理アプリ implements AppShellConfigurator {

	public static void main(String[] args) {
		if (System.getenv("DB_PASSWORD") == null) {          // 設定し忘れを、起動のときに分かる言葉で知らせる
			System.err.println("環境変数 DB_PASSWORD が設定されていません。README の「パスワードの渡し方」を見てください。");
			return;
		}
		SpringApplication.run(成績管理アプリ.class, args);
	}

}
