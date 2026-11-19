package grades.dataaccess.memory;

import grades.domain.履修;
import grades.domain.履修リポジトリ;
import grades.domain.履修一覧;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 履修をメモリに保存する実装。
 * 「同じ（学生, 科目, 年度）は 1 件だけ」というルールを、ここでコードで守っている。
 * データベースでは、このルールを UNIQUE 制約が守る（データベース編 06 章）。
 */
public class メモリ履修リポジトリ implements 履修リポジトリ {

    private record キー(int 学生ID, int 科目ID, int 年度) { }

    private final Map<キー, 履修> 保存先 = new HashMap<>();

    @Override
    public void 保存する(履修 履修) {
        キー キー = new キー(履修.学生ID(), 履修.科目ID(), 履修.年度());
        保存先.put(キー, 履修);        // 同じ組み合わせがあれば上書きされる
    }

    @Override
    public 履修一覧 学生IDで探す(int 学生ID) {
        List<履修> 見つかった履修 = new ArrayList<>();
        for (履修 履修 : 保存先.values()) {
            if (履修.学生ID() == 学生ID) {
                見つかった履修.add(履修);
            }
        }
        // 科目ID、年度の昇順に並べる
        見つかった履修.sort(Comparator.comparingInt(履修::科目ID).thenComparingInt(履修::年度));
        return new 履修一覧(見つかった履修);
    }
}
