package grades.usecase;

import grades.domain.科目;
import grades.domain.科目リポジトリ;

import java.util.List;

/** ユースケース「科目一覧を表示する」（ビジネスロジック層）。画面で科目を選ぶときに使う。読み取りだけ。 */
public class 科目一覧を表示する {

    private final 科目リポジトリ 科目リポジトリ;

    public 科目一覧を表示する(科目リポジトリ 科目リポジトリ) {
        this.科目リポジトリ = 科目リポジトリ;
    }

    public List<科目> 実行する() {
        return 科目リポジトリ.すべて探す();
    }
}
