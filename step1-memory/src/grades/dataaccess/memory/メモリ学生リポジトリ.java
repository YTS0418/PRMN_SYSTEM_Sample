package grades.dataaccess.memory;

import grades.domain.学生;
import grades.domain.学生リポジトリ;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/** 学生をメモリ（Map）に保存する実装。アプリを終了すると消える。 */
public class メモリ学生リポジトリ implements 学生リポジトリ {

    private final Map<Integer, 学生> 保存先 = new TreeMap<>();   // キーは学生ID（データベースの主キーの索引に当たる）
    private int 次の学生ID = 1;                                  // 採番（データベースの IDENTITY 列に当たる）

    /** データベースの INSERT と同じように、いつも新しい学生として追加する（渡された学生の 学生ID は使わない）。 */
    @Override
    public 学生 保存する(学生 学生) {
        学生 保存する学生 = 学生.学生IDを付ける(次の学生ID);     // 学生ID を採番する（record は変更できないため、新しい学生を作る）
        次の学生ID++;
        保存先.put(保存する学生.学生ID(), 保存する学生);
        return 保存する学生;                                    // 学生ID が付いた方を返す
    }

    @Override
    public Optional<学生> 探す(int 学生ID) {
        return Optional.ofNullable(保存先.get(学生ID));
    }

    @Override
    public List<学生> すべて探す() {
        return new ArrayList<>(保存先.values());
    }
}
