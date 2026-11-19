package grades.usecase;

import grades.domain.学生;
import grades.domain.学生リポジトリ;

/**
 * ユースケース「学生を登録する」（ビジネスロジック層）。
 * ユースケース 1 つにつき、クラスを 1 つ作る。手順は「実行する」に、上から順に並べる。
 * 業務のルール（名前が空でないか など）は domain、表示は presentation、保存は dataaccess の仕事。
 */
public class 学生を登録する {

    private final 学生リポジトリ 学生リポジトリ;

    // 必要な部品は、コンストラクタの引数で受け取る（型はインタフェース）
    public 学生を登録する(学生リポジトリ 学生リポジトリ) {
        this.学生リポジトリ = 学生リポジトリ;
    }

    public 学生 実行する(String 氏名, int 学科ID, int 入学年度) {
        学生 登録する学生 = new 学生(null, 氏名, 学科ID, 入学年度);   // 1. 学生を作る（検査は 学生 が行う）
        return 学生リポジトリ.保存する(登録する学生);                // 2. 学生を学生リポジトリで保存する
    }
}
