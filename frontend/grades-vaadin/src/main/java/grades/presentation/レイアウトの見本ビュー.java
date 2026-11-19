package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import static grades.presentation.見本.枠線;
import static grades.presentation.見本.見本;

/**
 * レイアウトの見本（フロントエンド編 04 章）。資料の画像を撮るための画面。メニューには出さない。
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの。
 * 点線の枠は、レイアウトの範囲を見せるためのもの（資料のコードには、含めない）。
 */
@Route("demo/layout")
@PageTitle("レイアウトの見本")
@RolesAllowed("TEACHER")
public class レイアウトの見本ビュー extends VerticalLayout {

    public レイアウトの見本ビュー() {
        add(new H2("レイアウトの見本"));
        var 並び = new FlexLayout();
        並び.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        並び.add(
                見本("layout-vertical", 縦()),
                見本("layout-horizontal", 横()),
                見本("layout-no-padding", 余白なし()),
                見本("layout-justify-between", 左右に離す()),
                見本("layout-justify-center", 中央に寄せる()),
                見本("layout-align-center", 縦方向の中央()),
                見本("layout-expand", 余りを使わせる()),
                見本("layout-wrap", 折り返す()),
                見本("layout-form-wide", フォーム(600)),
                見本("layout-form-narrow", フォーム(300)));
        add(並び);
    }

    private Component 縦() {
        // snippet: layout-vertical
        var 縦 = new VerticalLayout(new Span("A"), new Span("B"), new Span("C"));
        // end snippet
        return 枠線(縦);
    }

    private Component 横() {
        // snippet: layout-horizontal
        var 横 = new HorizontalLayout(new Span("A"), new Span("B"), new Span("C"));
        // end snippet
        return 枠線(横);
    }

    private Component 余白なし() {
        // snippet: layout-no-padding
        var 横 = new HorizontalLayout(new Span("A"), new Span("B"), new Span("C"));
        横.setPadding(false);
        横.setSpacing(false);
        // end snippet
        return 枠線(横);
    }

    private Component 左右に離す() {
        // snippet: layout-justify-between
        var 横 = new HorizontalLayout(new Span("A"), new Span("B"), new Span("C"));
        横.setWidth("320px");
        横.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        // end snippet
        return 枠線(横);
    }

    private Component 中央に寄せる() {
        // snippet: layout-justify-center
        var 横 = new HorizontalLayout(new Span("A"), new Span("B"), new Span("C"));
        横.setWidth("320px");
        横.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        // end snippet
        return 枠線(横);
    }

    private Component 縦方向の中央() {
        // snippet: layout-align-center
        var 横 = new HorizontalLayout(new Span("A"), new Span("B"), new Span("C"));
        横.setHeight("100px");
        横.setAlignItems(FlexComponent.Alignment.CENTER);
        // end snippet
        return 枠線(横);
    }

    private Component 余りを使わせる() {
        // snippet: layout-expand
        var 氏名 = new Span("氏名（余った幅を使う）");
        var 横 = new HorizontalLayout(new Span("A"), 氏名, new Span("C"));
        横.setWidth("320px");
        横.expand(氏名);
        // end snippet
        return 枠線(横);
    }

    private Component 折り返す() {
        // snippet: layout-wrap
        var 横 = new HorizontalLayout(new Span("科目 A"), new Span("科目 B"), new Span("科目 C"), new Span("科目 D"));
        横.setWidth("200px");
        横.setWrap(true);
        // end snippet
        return 枠線(横);
    }

    private Component フォーム(int 幅) {
        // snippet: layout-form
        var 氏名 = new TextField("氏名");
        var 学科 = new TextField("所属学科");
        var 入学年度 = new IntegerField("入学年度");
        var フォーム = new FormLayout(氏名, 学科, 入学年度);
        フォーム.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2));
        フォーム.setColspan(氏名, 2);
        // end snippet
        フォーム.setWidth(幅 + "px");
        return 枠線(フォーム);
    }
}
