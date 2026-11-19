package grades.presentation;

import grades.domain.学生;
import grades.domain.科目;

/** 成績の登録フォームの入力値（Binder 用）。点数が null のときは「履修中」。 */
public class 成績登録フォーム {

    private 学生 学生;
    private 科目 科目;
    private Integer 年度;
    private Integer 点数;

    public 学生 get学生() { return 学生; }
    public void set学生(学生 学生) { this.学生 = 学生; }
    public 科目 get科目() { return 科目; }
    public void set科目(科目 科目) { this.科目 = 科目; }
    public Integer get年度() { return 年度; }
    public void set年度(Integer 年度) { this.年度 = 年度; }
    public Integer get点数() { return 点数; }
    public void set点数(Integer 点数) { this.点数 = 点数; }
}
