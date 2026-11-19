package grades.domain;

import java.util.List;
import java.util.Optional;

/** 学生の保存の窓口。どう保存するかは書かない（実装は dataaccess パッケージ）。 */
public interface 学生リポジトリ {

    /** 新しい学生として追加し、採番された学生ID が付いた学生を返す（渡された学生の 学生ID は使わない）。 */
    学生 保存する(学生 学生);

    /** 見つからないときは Optional.empty()。 */
    Optional<学生> 探す(int 学生ID);

    /** 学生ID の昇順。 */
    List<学生> すべて探す();
}
