package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import static grades.presentation.見本.見本;

/**
 * 入力された文字の表示の見本（フロントエンド編 12 章）。資料の画像を撮るための画面。メニューには出さない。
 * 入力は、HTML のタグを含む文字（<b>…</b>）。Span は文字としてそのまま表示し、Html は HTML として解釈する。
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの。
 */
@Route("demo/security")
@PageTitle("セキュリティの見本")
@RolesAllowed("TEACHER")
public class セキュリティの見本ビュー extends VerticalLayout {

    public セキュリティの見本ビュー() {
        add(new H2("セキュリティの見本"));
        var 並び = new FlexLayout();
        並び.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        並び.add(
                見本("security-span", 文字として表示()),
                見本("security-html", HTMLとして表示()));
        add(並び);
    }

    private Component 文字として表示() {
        // snippet: security-span
        var 氏名 = "<b>千歳</b> 太郎";                       // 利用者が入力した文字（タグを含む）
        var 表示 = new Span(氏名);                            // 文字として、そのまま表示される（安全）
        // end snippet
        return 表示;
    }

    private Component HTMLとして表示() {
        // snippet: security-html
        var 氏名 = "<b>千歳</b> 太郎";                       // 利用者が入力した文字（タグを含む）
        var 表示 = new Html("<span>" + 氏名 + "</span>");     // HTML として解釈される（危険）
        // end snippet
        return 表示;
    }
}
