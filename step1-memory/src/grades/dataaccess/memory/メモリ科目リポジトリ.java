package grades.dataaccess.memory;

import grades.domain.科目;
import grades.domain.科目リポジトリ;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/** 科目をメモリに持つ実装。科目のデータは、作るときに渡す（Main が渡す）。 */
public class メモリ科目リポジトリ implements 科目リポジトリ {

    private final Map<Integer, 科目> 保存先 = new TreeMap<>();   // 科目ID の昇順

    public メモリ科目リポジトリ(List<科目> 科目リスト) {
        for (科目 科目 : 科目リスト) {
            保存先.put(科目.科目ID(), 科目);
        }
    }

    @Override
    public Optional<科目> 探す(int 科目ID) {
        return Optional.ofNullable(保存先.get(科目ID));
    }

    @Override
    public List<科目> すべて探す() {
        return new ArrayList<>(保存先.values());
    }
}
