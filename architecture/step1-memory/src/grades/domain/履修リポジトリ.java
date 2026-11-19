package grades.domain;

/** 履修の保存の窓口。 */
public interface 履修リポジトリ {

    /** 同じ（学生, 科目, 年度）がすでにあれば、点数を上書きする。 */
    void 保存する(履修 履修);

    /** 指定した学生の履修。科目ID、年度の昇順。 */
    履修一覧 学生IDで探す(int 学生ID);
}
