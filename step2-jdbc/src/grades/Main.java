package grades;

import grades.dataaccess.jdbc.Jdbc学生リポジトリ;
import grades.dataaccess.jdbc.Jdbc履修リポジトリ;
import grades.dataaccess.jdbc.Jdbc科目リポジトリ;
import grades.domain.学生リポジトリ;
import grades.domain.履修リポジトリ;
import grades.domain.科目リポジトリ;
import grades.presentation.コンソール画面;
import grades.usecase.学生を登録する;
import grades.usecase.学生一覧を表示する;
import grades.usecase.成績を登録する;
import grades.usecase.成績一覧を表示する;
import grades.usecase.科目一覧を表示する;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.simple.JdbcClient;

/**
 * 部品をつないで起動する。step1 からの変更は、保存先を new メモリ… から new Jdbc… に差し替えた部分だけ。
 * usecase も presentation も domain も、1 行も変えていない。
 *
 * 接続するには、環境変数 DB_PASSWORD が必要（パスワードをコードに書かない）。
 * 接続先を変えたいときだけ、DB_URL と DB_USER も指定できる。
 */
public class Main {

    public static void main(String[] args) {
        // データベースへの接続（データベース編 12 章 2 節）
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/grades_db"));
        dataSource.setUser(System.getenv().getOrDefault("DB_USER", "postgres"));
        dataSource.setPassword(System.getenv("DB_PASSWORD"));
        JdbcClient jdbcClient = JdbcClient.create(dataSource);

        // 1. 保存先を決めて作る（new メモリ… から差し替え）
        学生リポジトリ 学生リポジトリ = new Jdbc学生リポジトリ(jdbcClient);
        科目リポジトリ 科目リポジトリ = new Jdbc科目リポジトリ(jdbcClient);
        履修リポジトリ 履修リポジトリ = new Jdbc履修リポジトリ(jdbcClient);

        // 2. ユースケースに渡す
        学生を登録する 学生を登録する = new 学生を登録する(学生リポジトリ);
        成績を登録する 成績を登録する = new 成績を登録する(学生リポジトリ, 科目リポジトリ, 履修リポジトリ);
        成績一覧を表示する 成績一覧を表示する = new 成績一覧を表示する(学生リポジトリ, 科目リポジトリ, 履修リポジトリ);
        学生一覧を表示する 学生一覧を表示する = new 学生一覧を表示する(学生リポジトリ);
        科目一覧を表示する 科目一覧を表示する = new 科目一覧を表示する(科目リポジトリ);

        // 3. プレゼンテーション層に渡して起動
        new コンソール画面(学生を登録する, 成績を登録する, 成績一覧を表示する, 学生一覧を表示する, 科目一覧を表示する).開始する();
    }
}
