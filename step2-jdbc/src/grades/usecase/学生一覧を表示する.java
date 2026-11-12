package grades.usecase;

import grades.domain.学生;
import grades.domain.学生リポジトリ;

import java.util.List;

/** ユースケース「学生一覧を表示する」（ビジネスロジック層）。画面で学生を選ぶときに使う。読み取りだけ。 */
public class 学生一覧を表示する {

    private final 学生リポジトリ 学生リポジトリ;

    public 学生一覧を表示する(学生リポジトリ 学生リポジトリ) {
        this.学生リポジトリ = 学生リポジトリ;
    }

    public List<学生> 実行する() {
        return 学生リポジトリ.すべて探す();
    }
}
