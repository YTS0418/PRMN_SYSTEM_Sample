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

    private final Map<Integer, 学生> 保存先 = new TreeMap<>();   // 学生ID の昇順
    private int 次の学生ID = 1;

    @Override
    public 学生 保存する(学生 学生) {
        学生 保存する学生 = 学生;
        if (学生.学生ID() == null) {                           // まだ学生ID がない（新しい学生）なら、採番する
            保存する学生 = 学生.学生IDを付ける(次の学生ID);
            次の学生ID++;
        }
        保存先.put(保存する学生.学生ID(), 保存する学生);
        return 保存する学生;                                    // 学生ID が付いた方を返す（record は変更できないため）
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
