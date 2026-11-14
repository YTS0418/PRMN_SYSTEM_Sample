package grades.usecase;

import java.util.List;

/**
 * 成績一覧（1 人の学生の、科目ごとの成績と平均点）。
 * 平均点は、点数のある履修だけの平均。求め方は 履修一覧 が知っている。
 */
public record 成績一覧(String 氏名, List<成績明細> 明細リスト, double 平均点) { }
