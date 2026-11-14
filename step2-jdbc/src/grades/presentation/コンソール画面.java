package grades.presentation;

import grades.domain.保存失敗例外;
import grades.domain.学生;
import grades.usecase.成績一覧;
import grades.usecase.成績明細;
import grades.domain.科目;
import grades.usecase.学生を登録する;
import grades.usecase.学生一覧を表示する;
import grades.usecase.成績を登録する;
import grades.usecase.成績一覧を表示する;
import grades.usecase.科目一覧を表示する;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * コンソールの入出力を担当するクラス（プレゼンテーション層）。
 * 文字列を数値に変換してユースケースを呼び、結果を表示するだけ。
 * 業務のルール（点数の範囲など）の検査は書かない。違反は、例外のメッセージを表示する。
 */
public class コンソール画面 {

    private final 学生を登録する 学生を登録する;
    private final 成績を登録する 成績を登録する;
    private final 成績一覧を表示する 成績一覧を表示する;
    private final 学生一覧を表示する 学生一覧を表示する;
    private final 科目一覧を表示する 科目一覧を表示する;
    private final Scanner 入力 = new Scanner(System.in);

    public コンソール画面(学生を登録する 学生を登録する,
                   成績を登録する 成績を登録する,
                   成績一覧を表示する 成績一覧を表示する,
                   学生一覧を表示する 学生一覧を表示する,
                   科目一覧を表示する 科目一覧を表示する) {
        this.学生を登録する = 学生を登録する;
        this.成績を登録する = 成績を登録する;
        this.成績一覧を表示する = 成績一覧を表示する;
        this.学生一覧を表示する = 学生一覧を表示する;
        this.科目一覧を表示する = 科目一覧を表示する;
    }

    /** 入力が終わった（Ctrl+D など）ことを、メニューのループに伝える。 */
    private static class 入力終了 extends RuntimeException { }

    public void 開始する() {
        System.out.println("=== 成績管理システム ===");
        try {
            while (true) {
                System.out.println();
                System.out.println("1) 学生を登録する");
                System.out.println("2) 成績を登録する");
                System.out.println("3) 成績一覧を表示する");
                System.out.println("0) 終了");
                String 選んだ番号 = 一行読む("選んでください > ");
                try {
                    switch (選んだ番号) {
                        case "1" -> 学生登録の画面();
                        case "2" -> 成績登録の画面();
                        case "3" -> 成績一覧の画面();
                        case "0" -> {
                            System.out.println("終了します");
                            return;
                        }
                        default -> System.out.println("0〜3 から選んでください");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("数字で入力してください");          // 入力形式のエラー：ここの仕事
                } catch (IllegalArgumentException | NoSuchElementException e) {
                    System.out.println("エラー: " + e.getMessage());       // domain / usecase が投げた例外
                } catch (保存失敗例外 e) {
                    System.out.println("エラー: " + e.getMessage());       // 保存先（dataaccess）で失敗した
                }
            }
        } catch (入力終了 e) {
            System.out.println();
            System.out.println("入力が終わったので終了します");
        }
    }

    private void 学生登録の画面() {
        String 氏名 = 一行読む("名前 > ");
        int 学科ID = Integer.parseInt(一行読む("学科 ID（1:情報システム 2:電子工学 3:応用化学）> "));
        int 入学年度 = Integer.parseInt(一行読む("入学年度（例：2025）> "));
        学生 登録した学生 = 学生を登録する.実行する(氏名, 学科ID, 入学年度);
        System.out.println("登録しました: 学生 ID = " + 登録した学生.学生ID() + "（" + 登録した学生.氏名() + "）");
    }

    private void 成績登録の画面() {
        学生一覧を出す();
        int 学生ID = Integer.parseInt(一行読む("学生 ID > "));
        科目一覧を出す();
        int 科目ID = Integer.parseInt(一行読む("科目 ID > "));
        int 年度 = Integer.parseInt(一行読む("履修した年度（例：2025）> "));
        String 点数の入力 = 一行読む("点数（履修中ならそのまま Enter）> ");
        Integer 点数 = null;                                   // 何も入力しなければ null（履修中）
        if (!点数の入力.isBlank()) {
            点数 = Integer.parseInt(点数の入力);                // 文字列 → 数値に変換
        }
        成績を登録する.実行する(学生ID, 科目ID, 年度, 点数);      // ユースケースを呼ぶだけ
        System.out.println("登録しました");
    }

    private void 成績一覧の画面() {
        学生一覧を出す();
        int 学生ID = Integer.parseInt(一行読む("学生 ID > "));
        成績一覧 成績一覧 = 成績一覧を表示する.実行する(学生ID);
        System.out.println();
        System.out.println("【" + 成績一覧.氏名() + " さんの成績】");
        for (成績明細 明細 : 成績一覧.明細リスト()) {
            System.out.printf("  %d年度  %s（%d単位）  %s%n",
                    明細.年度(), 明細.科目名(), 明細.単位数(), 点数の表示(明細));
        }
        System.out.printf("平均点（点数のある科目のみ）: %.1f%n", 成績一覧.平均点());
    }

    /** 点数をどう見せるかは、表示の仕事。合格かどうかの判断は、domain が済ませている。 */
    private String 点数の表示(成績明細 明細) {
        if (明細.点数() == null) {
            return "履修中";
        }
        if (明細.合格()) {
            return 明細.点数() + "点（合格）";
        }
        return 明細.点数() + "点（不合格）";
    }

    private void 学生一覧を出す() {
        System.out.println("-- 学生 --");
        for (学生 学生 : 学生一覧を表示する.実行する()) {
            System.out.println("  " + 学生.学生ID() + ": " + 学生.氏名());
        }
    }

    private void 科目一覧を出す() {
        System.out.println("-- 科目 --");
        for (科目 科目 : 科目一覧を表示する.実行する()) {
            System.out.println("  " + 科目.科目ID() + ": " + 科目.科目名());
        }
    }

    private String 一行読む(String 案内) {
        System.out.print(案内);
        if (!入力.hasNextLine()) {
            throw new 入力終了();
        }
        return 入力.nextLine().trim();
    }
}
