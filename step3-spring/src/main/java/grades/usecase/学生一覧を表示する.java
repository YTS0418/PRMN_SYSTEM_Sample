package grades.usecase;

import grades.domain.学生;
import grades.domain.学生リポジトリ;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** ユースケース「学生一覧を表示する」（ビジネスロジック層）。画面で学生を選ぶときに使う。読み取りだけ。 */
@Service
public class 学生一覧を表示する {

    private final 学生リポジトリ 学生リポジトリ;

    public 学生一覧を表示する(学生リポジトリ 学生リポジトリ) {
        this.学生リポジトリ = 学生リポジトリ;
    }

    @Transactional(readOnly = true)                           // 読み取りだけのトランザクション
    public List<学生> 実行する() {
        return 学生リポジトリ.すべて探す();
    }
}
