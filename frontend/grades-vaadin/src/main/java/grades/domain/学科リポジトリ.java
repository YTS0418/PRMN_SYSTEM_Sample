package grades.domain;

import java.util.List;

/** 学科の保存の窓口。このサンプルでは、読み取りだけ。 */
public interface 学科リポジトリ {
    /** 学科ID の昇順。 */
    List<学科> すべて探す();
}
