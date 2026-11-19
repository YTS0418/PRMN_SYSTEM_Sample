package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import static grades.presentation.見本.見本;

/**
 * LoginForm の見本（フロントエンド編 10 章）。資料の画像を撮るための画面。メニューには出さない。
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの。
 */
@Route("demo/login")
@PageTitle("ログインフォームの見本")
@RolesAllowed("TEACHER")
public class ログインフォームの見本ビュー extends VerticalLayout {

    public ログインフォームの見本ビュー() {
        add(new H2("ログインフォームの見本"));
        var 並び = new FlexLayout();
        並び.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        並び.add(
                見本("login-default", 既定()),
                見本("login-japanese", 日本語()),
                見本("login-error", エラー()));
        add(並び);
    }

    private Component 既定() {
        // snippet: login-default
        var ログイン = new LoginForm();
        // end snippet
        return ログイン;
    }

    private Component 日本語() {
        // snippet: login-japanese
        var ログイン = new LoginForm();
        var 文言 = new LoginI18n();
        var フォーム = new LoginI18n.Form();
        フォーム.setTitle("ログイン");
        フォーム.setUsername("ユーザー名");
        フォーム.setPassword("パスワード");
        フォーム.setSubmit("ログイン");
        文言.setForm(フォーム);
        ログイン.setI18n(文言);
        ログイン.setForgotPasswordButtonVisible(false);
        // end snippet
        return ログイン;
    }

    private Component エラー() {
        // snippet: login-error
        var ログイン = new LoginForm();
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
        ログイン.setI18n(文言);
        ログイン.setForgotPasswordButtonVisible(false);
        ログイン.setError(true);
        // end snippet
        return ログイン;
    }
}
