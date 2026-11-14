package grades.usecase;

import grades.domain.科目;
import grades.domain.科目リポジトリ;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** ユースケース「科目一覧を取得する」（ビジネスロジック層）。画面で科目を選ぶときに使う。読み取りだけ。 */
@Service
public class 科目一覧を取得する {

    private final 科目リポジトリ 科目リポジトリ;

    public 科目一覧を取得する(科目リポジトリ 科目リポジトリ) {
        this.科目リポジトリ = 科目リポジトリ;
    }

    @Transactional(readOnly = true)                           // 読み取りだけのトランザクション
    public List<科目> 実行する() {
        return 科目リポジトリ.すべて探す();
    }
}
