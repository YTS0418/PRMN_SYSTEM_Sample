package grades;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot の起動クラス。画面のテーマの指定も、ここに書く（フロントエンド編 13 章）。
 * テーマは、@StyleSheet で指定する。指定しないと、部品の種類（成功・エラーの色など）が、見た目に反映されない。
 */
@SpringBootApplication
@StyleSheet(Lumo.STYLESHEET)                          // テーマを、明示的に指定する（フロントエンド編 13 章）
@StyleSheet("styles.css")                             // 自分の CSS（テーマの後に書く）
public class 成績管理アプリ implements AppShellConfigurator {

	public static void main(String[] args) {
		if (System.getenv("DB_PASSWORD") == null) {          // 設定し忘れを、起動のときに分かる言葉で知らせる
			System.err.println("環境変数 DB_PASSWORD が設定されていません。README の「パスワードの渡し方」を見てください。");
			return;
		}
		SpringApplication.run(成績管理アプリ.class, args);
	}

}
