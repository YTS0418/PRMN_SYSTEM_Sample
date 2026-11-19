package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

import static grades.presentation.見本.見本;

/**
 * Grid の見本（フロントエンド編 07 章）。資料の画像を撮るための画面。メニューには出さない。
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの。
 */
@Route("demo/grid")
@PageTitle("Grid の見本")
@RolesAllowed("TEACHER")
public class Gridの見本ビュー extends VerticalLayout {

    /** Grid の 1 行になるデータ。 */
    record 学生行(int 学生番号, String 氏名, String 学科, int 入学年度) { }

    /** 成績の 1 行（点数が null のときは、履修中）。 */
    record 成績行(String 科目名, int 年度, Integer 点数, boolean 合格) { }

    public Gridの見本ビュー() {
        add(new H2("Grid の見本"));
        var 並び = new FlexLayout();
        並び.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        並び.add(
                見本("grid-basic", 基本()),
                見本("grid-sortable", 並べ替え()),
                見本("grid-component-column", 部品の列()),
                見本("grid-selection-multi", 複数選択()));
        add(並び);
    }

    private Component 基本() {
        // snippet: grid-basic
        var 一覧 = new Grid<学生行>();
        一覧.addColumn(学生行::学生番号).setHeader("学生番号");
        一覧.addColumn(学生行::氏名).setHeader("氏名");
        一覧.addColumn(学生行::学科).setHeader("所属学科");
        一覧.setItems(
                new 学生行(1, "Taro", "情報システム学科", 2024),
                new 学生行(2, "Hanako", "情報システム学科", 2024),
                new 学生行(3, "Jiro", "電子工学科", 2024));
        一覧.setAllRowsVisible(true);
        // end snippet
        一覧.setWidth("560px");
        return 一覧;
    }

    private Component 並べ替え() {
        // snippet: grid-sortable
        var 一覧 = new Grid<学生行>();
        一覧.addColumn(学生行::学生番号).setHeader("学生番号").setSortable(true);
        var 氏名の列 = 一覧.addColumn(学生行::氏名).setHeader("氏名").setSortable(true);
        一覧.addColumn(学生行::入学年度).setHeader("入学年度").setSortable(true);
        一覧.setItems(
                new 学生行(1, "Taro", "情報システム学科", 2024),
                new 学生行(2, "Hanako", "情報システム学科", 2024),
                new 学生行(3, "Jiro", "電子工学科", 2024));
        一覧.sort(List.of(new GridSortOrder<>(氏名の列, SortDirection.ASCENDING)));
        一覧.setAllRowsVisible(true);
        // end snippet
        一覧.setWidth("560px");
        return 一覧;
    }

    private Component 部品の列() {
        // snippet: grid-component-column
        var 一覧 = new Grid<成績行>();
        一覧.addColumn(成績行::科目名).setHeader("科目");
        一覧.addColumn(m -> m.点数() == null ? "履修中" : String.valueOf(m.点数())).setHeader("点数");
        一覧.addComponentColumn(m -> {
            var バッジ = new Span(m.合格() ? "合格" : "不合格");
            バッジ.getElement().getThemeList().add(m.合格() ? "badge success" : "badge error");
            return バッジ;
        }).setHeader("合否");
        一覧.setItems(
                new 成績行("プログラミング基礎", 2024, 80, true),
                new 成績行("線形代数", 2024, 55, false),
                new 成績行("データベース", 2025, null, false));
        一覧.setAllRowsVisible(true);
        // end snippet
        一覧.setWidth("560px");
        return 一覧;
    }

    private Component 複数選択() {
        // snippet: grid-selection-multi
        var 一覧 = new Grid<学生行>();
        一覧.setSelectionMode(Grid.SelectionMode.MULTI);
        一覧.addColumn(学生行::氏名).setHeader("氏名");
        一覧.addColumn(学生行::学科).setHeader("所属学科");
        var 学生 = List.of(
                new 学生行(1, "Taro", "情報システム学科", 2024),
                new 学生行(2, "Hanako", "情報システム学科", 2024),
                new 学生行(3, "Jiro", "電子工学科", 2024));
        一覧.setItems(学生);
        一覧.select(学生.get(0));
        一覧.select(学生.get(2));
        一覧.setAllRowsVisible(true);
        // end snippet
        一覧.setWidth("560px");
        return 一覧;
    }
}
