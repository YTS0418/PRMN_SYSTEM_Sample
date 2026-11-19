package grades.presentation;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import grades.usecase.ログインユーザー;
import jakarta.annotation.security.PermitAll;

/** トップページ（/）。ロールに応じて、最初の画面へ移動させる。 */
@Route("")
@PermitAll
public class ホームビュー extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticationContext 認証;

    public ホームビュー(AuthenticationContext 認証) {
        this.認証 = 認証;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (認証.hasAnyRole("TEACHER")) {
            event.forwardTo("students");                                  // 教員：学生一覧へ
            return;
        }
        認証を確認(event);
    }

    private void 認証を確認(BeforeEnterEvent event) {
        var ユーザー = 認証.getAuthenticatedUser(ログインユーザー.class).orElse(null);
        if (ユーザー != null && ユーザー.学生ID() != null) {
            event.forwardTo("students/" + ユーザー.学生ID() + "/transcript");   // 学生：自分の成績へ
        }
    }
}
