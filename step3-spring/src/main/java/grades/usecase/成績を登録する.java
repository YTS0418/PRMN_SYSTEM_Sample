package grades.usecase;

import grades.domain.学生リポジトリ;
import grades.domain.履修;
import grades.domain.履修リポジトリ;
import grades.domain.科目リポジトリ;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/** ユースケース「成績を登録する」（ビジネスロジック層）。 */
@Service
public class 成績を登録する {

    private final 学生リポジトリ 学生リポジトリ;
    private final 科目リポジトリ 科目リポジトリ;
    private final 履修リポジトリ 履修リポジトリ;

    public 成績を登録する(学生リポジトリ 学生リポジトリ, 科目リポジトリ 科目リポジトリ, 履修リポジトリ 履修リポジトリ) {
        this.学生リポジトリ = 学生リポジトリ;
        this.科目リポジトリ = 科目リポジトリ;
        this.履修リポジトリ = 履修リポジトリ;
    }

    /** 点数が null のときは履修中。 */
    @Transactional                                            // ユースケース 1 つ = トランザクション 1 つ
    public void 実行する(int 学生ID, int 科目ID, int 年度, Integer 点数) {
        // 1. 学生を学生リポジトリで探す（いなければ、ここで終わり）
        if (学生リポジトリ.探す(学生ID).isEmpty()) {
            throw new NoSuchElementException("学生が見つかりません: " + 学生ID);
        }
        // 2. 科目を科目リポジトリで探す（なければ、ここで終わり）
        if (科目リポジトリ.探す(科目ID).isEmpty()) {
            throw new NoSuchElementException("科目が見つかりません: " + 科目ID);
        }
        履修 登録する履修 = new 履修(学生ID, 科目ID, 年度, 点数);   // 3. 履修を作る（点数の検査は 履修 が行う）
        履修リポジトリ.保存する(登録する履修);                      // 4. 履修を履修リポジトリで保存する
    }
}
