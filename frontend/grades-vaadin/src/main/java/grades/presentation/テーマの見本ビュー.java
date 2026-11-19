package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import static grades.presentation.見本.見本;

/**
 * テーマ・CSS の見本（フロントエンド編 13 章）。資料の画像を撮るための画面。メニューには出さない。
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの。
 */
@Route("demo/theme")
@PageTitle("テーマの見本")
@RolesAllowed("TEACHER")
public class テーマの見本ビュー extends VerticalLayout {

    public テーマの見本ビュー() {
        add(new H2("テーマの見本"));
        var 並び = new FlexLayout();
        並び.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        並び.add(
                見本("theme-class", クラス名()),
                見本("theme-variable", 変数を上書き()),
                見本("theme-dark", ダークモード()));
        add(並び);
    }

    private Component クラス名() {
        // snippet: theme-class
        var 合格 = new Span("合格");
        var 不合格 = new Span("不合格");
        不合格.addClassName("grade-failed");                 // styles.css の .grade-failed で、色を付ける
        // end snippet
        return new HorizontalLayout(合格, 不合格);
    }

    private Component 変数を上書き() {
        // snippet: theme-variable
        var 登録 = new Button("登録する");
        登録.addThemeVariants(ButtonVariant.PRIMARY);
        登録.getStyle().set("--lumo-primary-color", "#1a7f37");      // この部品だけ、主の色を緑にする
        // end snippet
        return 登録;
    }

    private Component ダークモード() {
        // snippet: theme-dark
        var 氏名 = new TextField("氏名");
        var 登録 = new Button("登録する");
        登録.addThemeVariants(ButtonVariant.PRIMARY);
        var 画面 = new VerticalLayout(氏名, 登録);
        画面.getElement().setAttribute("theme", "dark");            // この範囲だけ、ダークモード（Lumo）
        // end snippet
        画面.getStyle().set("background", "var(--lumo-base-color)").set("color", "var(--lumo-body-text-color)");
        return 画面;
    }
}
