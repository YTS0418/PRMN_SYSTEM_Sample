package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.RolesAllowed;

import static grades.presentation.見本.見本;

/**
 * リンクとメニューの見本（フロントエンド編 03 章）。資料の画像を撮るための画面。メニューには出さない。
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの。
 */
@Route("demo/navigation")
@PageTitle("ナビゲーションの見本")
@RolesAllowed("TEACHER")
public class ナビゲーションの見本ビュー extends VerticalLayout {

    public ナビゲーションの見本ビュー() {
        add(new H2("ナビゲーションの見本"));
        var 並び = new FlexLayout();
        並び.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        並び.add(
                見本("nav-router-link", リンク()),
                見本("nav-side-nav", メニュー()));
        add(並び);
    }

    private Component リンク() {
        // snippet: nav-router-link
        var 一覧へ = new RouterLink("学生一覧", 学生一覧ビュー.class);
        var 登録へ = new RouterLink("学生の登録", 学生登録ビュー.class);
        // end snippet
        var 行 = new HorizontalLayout(一覧へ, 登録へ);
        行.setPadding(false);
        return 行;
    }

    private Component メニュー() {
        // snippet: nav-side-nav
        var メニュー = new SideNav();
        メニュー.addItem(new SideNavItem("学生一覧", "students"));
        メニュー.addItem(new SideNavItem("学生の登録", "students/register"));
        メニュー.addItem(new SideNavItem("成績の登録", "enrollments/register"));
        // end snippet
        メニュー.setWidth("220px");
        return メニュー;
    }
}
