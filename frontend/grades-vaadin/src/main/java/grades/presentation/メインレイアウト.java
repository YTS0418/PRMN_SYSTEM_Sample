package grades.presentation;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import grades.usecase.ログインユーザー;
import jakarta.annotation.security.PermitAll;

/**
 * すべての画面の外枠（メニューと、上のバー）。フロントエンド編 03 章。
 * メニューは、ロールに応じて出し分ける（11 章）。ただし、出し分けは使いやすさのため。
 * 本当の制限は、ビューの注釈と、ユースケースの @PreAuthorize が行う（12 章）。
 */
@Layout
@PermitAll                                      // レイアウトにも、アクセスの注釈が必要（ログイン済みなら、誰でも）
public class メインレイアウト extends AppLayout {

    public メインレイアウト(AuthenticationContext 認証) {
        setPrimarySection(Section.DRAWER);

        var ユーザー = 認証.getAuthenticatedUser(ログインユーザー.class).orElse(null);

        var メニュー = new SideNav();
        if (認証.hasAnyRole("TEACHER")) {
            メニュー.addItem(new SideNavItem("学生一覧", "students"));
            メニュー.addItem(new SideNavItem("学生の登録", "students/register"));
            メニュー.addItem(new SideNavItem("成績の登録", "enrollments/register"));
        } else if (ユーザー != null && ユーザー.学生ID() != null) {
            メニュー.addItem(new SideNavItem("自分の成績", "students/" + ユーザー.学生ID() + "/transcript"));
        }
        addToDrawer(メニュー);

        var タイトル = new H1("成績管理システム");
        タイトル.getStyle().set("font-size", "var(--vaadin-font-size-xl, 1.25rem)").set("margin", "0");

        // 「誰として、ログインしているか」を、常に表示する（ユーザビリティ：システム状態の視認性）
        var ログアウト = new Button("ログアウト", e -> 認証.logout());
        ログアウト.addThemeVariants(ButtonVariant.TERTIARY);
        var 右側 = new HorizontalLayout(new Span(ユーザー == null ? "" : ユーザー.getUsername()), ログアウト);
        右側.setAlignItems(FlexComponent.Alignment.CENTER);

        var バー = new HorizontalLayout(new DrawerToggle(), タイトル);
        バー.setAlignItems(FlexComponent.Alignment.CENTER);
        バー.expand(タイトル);
        バー.setWidthFull();
        バー.add(右側);
        バー.getStyle().set("padding-right", "1rem");
        addToNavbar(バー);
    }
}
