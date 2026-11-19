package grades.usecase;

import grades.domain.学科;
import grades.domain.学科リポジトリ;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** ユースケース「学科一覧を取得する」。画面で所属学科を選ぶときに使う。読み取りだけ。 */
@Service
public class 学科一覧を取得する {

    private final 学科リポジトリ 学科リポジトリ;

    public 学科一覧を取得する(学科リポジトリ 学科リポジトリ) {
        this.学科リポジトリ = 学科リポジトリ;
    }

    @PreAuthorize("hasRole('TEACHER')")                       // 教員だけ
    @Transactional(readOnly = true)
    public List<学科> 実行する() {
        return 学科リポジトリ.すべて探す();
    }
}
