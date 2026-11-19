package grades.domain;

import java.util.List;
import java.util.Optional;

/** 科目の保存の窓口。このサンプルでは、読み取りだけ。 */
public interface 科目リポジトリ {

    /** 見つからないときは Optional.empty()。 */
    Optional<科目> 探す(int 科目ID);

    /** 科目ID の昇順。 */
    List<科目> すべて探す();
}
