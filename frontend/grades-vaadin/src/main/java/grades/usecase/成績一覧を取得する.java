package grades.usecase;

import grades.domain.学生;
import grades.domain.学生リポジトリ;
import grades.domain.履修;
import grades.domain.履修リポジトリ;
import grades.domain.履修一覧;
import grades.domain.科目;
import grades.domain.科目リポジトリ;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * ユースケース「成績一覧を取得する」（ビジネスロジック層）。
 * ここは「誰に、何を、どの順番で頼むか」だけを書く。
 * 合格かどうかの判断や、平均点の求め方（業務のルール）は、domain の 履修・履修一覧 が持っている。
 */
@Service
public class 成績一覧を取得する {

    private final 学生リポジトリ 学生リポジトリ;
    private final 科目リポジトリ 科目リポジトリ;
    private final 履修リポジトリ 履修リポジトリ;

    public 成績一覧を取得する(学生リポジトリ 学生リポジトリ, 科目リポジトリ 科目リポジトリ, 履修リポジトリ 履修リポジトリ) {
        this.学生リポジトリ = 学生リポジトリ;
        this.科目リポジトリ = 科目リポジトリ;
        this.履修リポジトリ = 履修リポジトリ;
    }

    @PreAuthorize("hasRole('TEACHER') or @成績の権限.本人である(authentication, #学生ID)")   // 教員、または本人の学生だけ
    @Transactional(readOnly = true)                           // 読み取りだけのトランザクション
    public 成績一覧 実行する(int 学生ID) {
        // 1. 学生を学生リポジトリで探す
        学生 対象の学生 = 学生リポジトリ.探す(学生ID)
                .orElseThrow(() -> new NoSuchElementException("学生が見つかりません: " + 学生ID));

        // 2. 履修一覧を履修リポジトリで探す
        履修一覧 履修一覧 = 履修リポジトリ.学生IDで探す(学生ID);

        // 3. 履修 1 件ごとに、科目を探して、成績明細にする
        List<成績明細> 明細リスト = new ArrayList<>();
        for (履修 履修 : 履修一覧.履修リスト()) {
            科目 科目 = 科目リポジトリ.探す(履修.科目ID())
                    .orElseThrow(() -> new NoSuchElementException("科目が見つかりません: " + 履修.科目ID()));
            明細リスト.add(new 成績明細(科目.科目名(), 科目.単位数(), 履修.年度(), 履修.点数(), 履修.合格している()));
        }

        // 4. 平均点を履修一覧で計算する
        double 平均点 = 履修一覧.平均点を計算する();

        // 5. 成績一覧を作る
        return new 成績一覧(対象の学生.氏名(), 明細リスト, 平均点);
    }
}
