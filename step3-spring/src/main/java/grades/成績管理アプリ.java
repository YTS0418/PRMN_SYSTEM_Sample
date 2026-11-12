package grades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Spring Boot の起動クラス。部品の組み立て（step1・2 の Main の仕事）は、Spring が行う。 */
@SpringBootApplication
public class 成績管理アプリ {

	public static void main(String[] args) {
		SpringApplication.run(成績管理アプリ.class, args);
	}

}
