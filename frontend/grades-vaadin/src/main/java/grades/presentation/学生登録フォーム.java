package grades.presentation;

import grades.domain.学科;

/** 学生の登録フォームの入力値（Binder 用。値を変更できる入力専用のクラス。フロントエンド編 08 章）。 */
public class 学生登録フォーム {

    private String 氏名;
    private 学科 学科;
    private Integer 入学年度;

    public String get氏名() { return 氏名; }
    public void set氏名(String 氏名) { this.氏名 = 氏名; }
    public 学科 get学科() { return 学科; }
    public void set学科(学科 学科) { this.学科 = 学科; }
    public Integer get入学年度() { return 入学年度; }
    public void set入学年度(Integer 入学年度) { this.入学年度 = 入学年度; }
}
