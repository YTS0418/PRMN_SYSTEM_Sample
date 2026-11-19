package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import static grades.presentation.見本.見本;

/**
 * 文字・アイコンなどの表示部品の見本（フロントエンド編 05 章）。資料の画像を撮るための画面。メニューには出さない。
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの。
 */
@Route("demo/display")
@PageTitle("表示部品の見本")
@RolesAllowed("TEACHER")
public class 表示部品の見本ビュー extends VerticalLayout {

    public 表示部品の見本ビュー() {
        add(new H2("表示部品の見本"));
        var 並び = new FlexLayout();
        並び.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        並び.add(
                見本("display-headings", 見出し()),
                見本("display-paragraph", 段落()),
                見本("display-icon", アイコン()),
                見本("display-avatar", アバター()),
                見本("display-progress", 進み具合()));
        add(並び);
    }

    private Component 見出し() {
        // snippet: display-headings
        var 見出し = new VerticalLayout(new H1("成績管理システム"), new H2("学生一覧"), new H3("所属学科ごとの人数"));
        // end snippet
        return 見出し;
    }

    private Component 段落() {
        // snippet: display-paragraph
        var 説明 = new Paragraph("履修の登録は、年度ごとに行います。点数が空欄のものは、履修中です。");
        var 注記 = new Span("※ 平均点は、点数のある履修だけで計算します。");
        // end snippet
        return new VerticalLayout(説明, 注記);
    }

    private Component アイコン() {
        // snippet: display-icon
        var 追加 = VaadinIcon.PLUS.create();
        var 警告 = VaadinIcon.WARNING.create();
        var 利用者 = VaadinIcon.USER.create();
        // end snippet
        return new HorizontalLayout(追加, 警告, 利用者);
    }

    private Component アバター() {
        // snippet: display-avatar
        var 太郎 = new Avatar("千歳 太郎");
        var 花子 = new Avatar("千歳 花子");
        // end snippet
        return new HorizontalLayout(太郎, 花子);
    }

    private Component 進み具合() {
        // snippet: display-progress
        var 進み具合 = new ProgressBar();
        進み具合.setValue(0.6);
        進み具合.setWidth("240px");
        // end snippet
        return 進み具合;
    }
}
