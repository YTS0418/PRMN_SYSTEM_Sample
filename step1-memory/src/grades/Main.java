package grades;

import grades.dataaccess.memory.メモリ学生リポジトリ;
import grades.dataaccess.memory.メモリ履修リポジトリ;
import grades.dataaccess.memory.メモリ科目リポジトリ;
import grades.domain.学生リポジトリ;
import grades.domain.履修リポジトリ;
import grades.domain.科目;
import grades.domain.科目リポジトリ;
import grades.presentation.コンソール画面;
import grades.usecase.学生を登録する;
import grades.usecase.学生一覧を表示する;
import grades.usecase.成績を登録する;
import grades.usecase.成績一覧を表示する;
import grades.usecase.科目一覧を表示する;

import java.util.List;

/**
 * 部品をつないで起動する（全部の層を知っている唯一のクラス）。
 * 保存先を変えたいときは、ここの new メモリ… を差し替えるだけ（step2 で実際にやる）。
 * クラス名 Main とメソッド名 main は、Java が起動するときの決まりなので、英語のままにしている。
 */
public class Main {

    public static void main(String[] args) {
        // 1. 保存先を決めて作る
        学生リポジトリ 学生リポジトリ = new メモリ学生リポジトリ();
        科目リポジトリ 科目リポジトリ = new メモリ科目リポジトリ(List.of(
                new 科目(1, "プログラミング基礎", 2),
                new 科目(2, "データベース", 2),
                new 科目(3, "線形代数", 4),
                new 科目(4, "電気回路", 2),
                new 科目(5, "有機化学", 2)));
        履修リポジトリ 履修リポジトリ = new メモリ履修リポジトリ();

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
