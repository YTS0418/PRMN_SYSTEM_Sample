package grades.presentation;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/** ログイン画面（フロントエンド編 10 章）。 */
@Route(value = "login", autoLayout = false)               // 共通のレイアウト（メニュー）は使わない
@PageTitle("ログイン")
@AnonymousAllowed                                          // ログインしていなくても見られる
public class ログインビュー extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm ログイン = new LoginForm();

    public ログインビュー() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        ログイン.setAction("login");                        // 入力を、Spring Security の /login に送る
        ログイン.setI18n(日本語の文言());
        ログイン.setForgotPasswordButtonVisible(false);     // このサンプルには、パスワードの再設定がない
        add(new H1("成績管理システム"), ログイン);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // ログインに失敗して戻ってきたとき（URL に ?error が付く）は、エラーを表示する
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            ログイン.setError(true);
        }
    }

    /** ログイン画面の文言を、日本語にする。エラーは、ユーザー名とパスワードの、どちらが違うかを教えない（12 章）。 */
    private static LoginI18n 日本語の文言() {
        var 文言 = new LoginI18n();
        var フォーム = new LoginI18n.Form();
        フォーム.setTitle("ログイン");
        フォーム.setUsername("ユーザー名");
        フォーム.setPassword("パスワード");
        フォーム.setSubmit("ログイン");
        文言.setForm(フォーム);
        var エラー = new LoginI18n.ErrorMessage();
        エラー.setTitle("ログインできませんでした");
        エラー.setMessage("ユーザー名またはパスワードが違います。");
        文言.setErrorMessage(エラー);
        return 文言;
    }
}
