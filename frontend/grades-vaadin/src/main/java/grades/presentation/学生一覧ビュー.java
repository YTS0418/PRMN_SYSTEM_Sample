package grades.presentation;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import grades.domain.学生;
import grades.domain.学科;
import grades.usecase.学生一覧を取得する;
import grades.usecase.学科一覧を取得する;
import jakarta.annotation.security.RolesAllowed;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** 学生一覧の画面（フロントエンド編 07 章）。行を選ぶと、その学生の成績一覧へ移動する。 */
@Route("students")
@PageTitle("学生一覧")
@RolesAllowed("TEACHER")
public class 学生一覧ビュー extends VerticalLayout {

    public 学生一覧ビュー(学生一覧を取得する 学生一覧を取得する, 学科一覧を取得する 学科一覧を取得する) {
        // 学科ID → 学科名（一覧に、学科名を表示するため）
        Map<Integer, String> 学科名 = 学科一覧を取得する.実行する().stream()
                .collect(Collectors.toMap(学科::学科ID, 学科::学科名));

        var 一覧 = new Grid<学生>();
        一覧.addColumn(学生::学生ID).setHeader("学生番号").setSortable(true).setAutoWidth(true);
        一覧.addColumn(学生::氏名).setHeader("氏名").setSortable(true);
        一覧.addColumn(s -> 学科名.getOrDefault(s.学科ID(), "")).setHeader("所属学科");
        一覧.addColumn(学生::入学年度).setHeader("入学年度").setSortable(true).setAutoWidth(true);

        // 件数が限られているので、全件を渡す方法（07 章 5 節）
        var データビュー = 一覧.setItems(学生一覧を取得する.実行する());

        var 検索 = new TextField("氏名で絞り込む");
        検索.setValueChangeMode(ValueChangeMode.LAZY);        // 入力が止まってから反応する（06 章）
        検索.addValueChangeListener(e -> データビュー.setFilter(
                s -> s.氏名().toLowerCase().contains(e.getValue().toLowerCase())));

        一覧.addSelectionListener(e -> e.getFirstSelectedItem().ifPresent(s ->
                getUI().ifPresent(ui -> ui.navigate("students/" + s.学生ID() + "/transcript"))));

        setSizeFull();
        add(new H2("学生一覧"), 検索, 一覧);
        expand(一覧);
    }
}
