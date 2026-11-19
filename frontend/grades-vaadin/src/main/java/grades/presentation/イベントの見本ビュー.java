package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import static grades.presentation.見本.見本;

/**
 * イベントの見本（フロントエンド編 06 章）。資料の画像を撮るための画面。メニューには出さない。
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの。
 */
@Route("demo/events")
@PageTitle("イベントの見本")
@RolesAllowed("TEACHER")
public class イベントの見本ビュー extends VerticalLayout {

    public イベントの見本ビュー() {
        add(new H2("イベントの見本"));
        var 並び = new FlexLayout();
        並び.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        並び.add(見本("events-value-change", 値の変更()));
        add(並び);
    }

    private Component 値の変更() {
        // snippet: events-value-change
        var 氏名 = new TextField("氏名");
        var 表示 = new Span();
        氏名.addValueChangeListener(e -> 表示.setText("入力された氏名：" + e.getValue()));
        氏名.setValue("千歳 太郎");
        // end snippet
        return new VerticalLayout(氏名, 表示);
    }
}
