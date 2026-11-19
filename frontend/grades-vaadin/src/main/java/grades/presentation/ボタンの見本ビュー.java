package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import static grades.presentation.見本.見本;

/**
 * ボタンの見本（フロントエンド編 05 章）。資料の画像を撮るための画面。メニューには出さない。
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの。
 */
@Route("demo/button")
@PageTitle("ボタンの見本")
@RolesAllowed("TEACHER")
public class ボタンの見本ビュー extends VerticalLayout {

    public ボタンの見本ビュー() {
        add(new H2("ボタンの見本"));
        var 並び = new FlexLayout();
        並び.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        並び.add(
                見本("button-default", 通常()),
                見本("button-primary", 主()),
                見本("button-tertiary", 控えめ()),
                見本("button-error", 危険()),
                見本("button-success", 成功()),
                見本("button-warning", 警告()),
                見本("button-small", 小()),
                見本("button-large", 大()),
                見本("button-icon", アイコン()),
                見本("button-disabled", 無効()));
        add(並び);
    }

    private Component 通常() {
        // snippet: button-default
        var 登録 = new Button("登録する");
        // end snippet
        return 登録;
    }

    private Component 主() {
        // snippet: button-primary
        var 登録 = new Button("登録する");
        登録.addThemeVariants(ButtonVariant.PRIMARY);
        // end snippet
        return 登録;
    }

    private Component 控えめ() {
        // snippet: button-tertiary
        var キャンセル = new Button("キャンセル");
        キャンセル.addThemeVariants(ButtonVariant.TERTIARY);
        // end snippet
        return キャンセル;
    }

    private Component 危険() {
        // snippet: button-error
        var 削除 = new Button("削除する");
        削除.addThemeVariants(ButtonVariant.ERROR);
        // end snippet
        return 削除;
    }

    private Component 成功() {
        // snippet: button-success
        var 確定 = new Button("確定する");
        確定.addThemeVariants(ButtonVariant.SUCCESS);
        // end snippet
        return 確定;
    }

    private Component 警告() {
        // snippet: button-warning
        var 再計算 = new Button("再計算する");
        再計算.addThemeVariants(ButtonVariant.WARNING);
        // end snippet
        return 再計算;
    }

    private Component 小() {
        // snippet: button-small
        var 詳細 = new Button("詳細");
        詳細.addThemeVariants(ButtonVariant.SMALL);
        // end snippet
        return 詳細;
    }

    private Component 大() {
        // snippet: button-large
        var 開始 = new Button("開始する");
        開始.addThemeVariants(ButtonVariant.LARGE);
        // end snippet
        return 開始;
    }

    private Component アイコン() {
        // snippet: button-icon
        var 追加 = new Button("追加", VaadinIcon.PLUS.create());
        // end snippet
        return 追加;
    }

    private Component 無効() {
        // snippet: button-disabled
        var 登録 = new Button("登録する");
        登録.setEnabled(false);
        // end snippet
        return 登録;
    }
}
