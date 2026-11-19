package grades.presentation;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import grades.usecase.成績一覧を取得する;
import grades.usecase.成績明細;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.access.AccessDeniedException;

import java.util.NoSuchElementException;

/**
 * 成績一覧の画面（フロントエンド編 03・07・12 章）。
 * ロールだけでは、「他の学生の成績か」が分からないので、権限の確認はユースケース（@PreAuthorize）が行う。
 * ここでは、拒否されたときの表示だけを担当する。
 */
@Route("students/:studentId/transcript")
@PageTitle("成績一覧")
@RolesAllowed({"TEACHER", "STUDENT"})
public class 成績一覧ビュー extends VerticalLayout implements BeforeEnterObserver {

    private final 成績一覧を取得する 成績一覧を取得する;
    private final H2 見出し = new H2();
    private final Grid<成績明細> 一覧 = new Grid<>();
    private final Span 平均 = new Span();
    private final Span メッセージ = new Span();

    public 成績一覧ビュー(成績一覧を取得する 成績一覧を取得する) {
        this.成績一覧を取得する = 成績一覧を取得する;

        一覧.addColumn(成績明細::科目名).setHeader("科目");
        一覧.addColumn(成績明細::単位数).setHeader("単位").setAutoWidth(true);
        一覧.addColumn(成績明細::年度).setHeader("年度").setAutoWidth(true);
        // 点数が null（履修中）のときは、「履修中」と表示する
        一覧.addColumn(m -> m.点数() == null ? "履修中" : String.valueOf(m.点数())).setHeader("点数").setAutoWidth(true);
        // 状態は、色だけで示さず、文字でも示す（アクセシビリティ）
        一覧.addColumn(m -> m.点数() == null ? "—" : (m.合格() ? "合格" : "不合格")).setHeader("合否").setAutoWidth(true);

        setSizeFull();
        add(見出し, 一覧, 平均, メッセージ);
        expand(一覧);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        メッセージ.setText("");
        int 学生ID;
        try {
            学生ID = event.getRouteParameters().get("studentId").map(Integer::parseInt).orElseThrow();
        } catch (NumberFormatException | NoSuchElementException e) {
            見出し.setText("成績一覧");
            メッセージ.setText("学生番号が正しくありません。");
            一覧.setItems();
            return;
        }
        try {
            var 結果 = 成績一覧を取得する.実行する(学生ID);            // 4 層のユースケースを呼ぶ
            見出し.setText(結果.氏名() + " さんの成績");
            一覧.setItems(結果.明細リスト());
            平均.setText("平均点：" + String.format("%.1f", 結果.平均点()));
            if (結果.明細リスト().isEmpty()) {
                メッセージ.setText("まだ成績が登録されていません。");
            }
        } catch (AccessDeniedException e) {
            見出し.setText("成績一覧");
            一覧.setItems();
            平均.setText("");
            メッセージ.setText("この成績は表示できません。");           // 理由の詳細は出さない
        } catch (NoSuchElementException e) {
            見出し.setText("成績一覧");
            一覧.setItems();
            平均.setText("");
            メッセージ.setText("学生が見つかりません。");
        }
    }
}
