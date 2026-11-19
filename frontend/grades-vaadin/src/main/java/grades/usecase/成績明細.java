package grades.usecase;

/** 成績一覧の 1 科目分の行。点数が null のときは履修中。 */
public record 成績明細(String 科目名, int 単位数, int 年度, Integer 点数, boolean 合格) { }
