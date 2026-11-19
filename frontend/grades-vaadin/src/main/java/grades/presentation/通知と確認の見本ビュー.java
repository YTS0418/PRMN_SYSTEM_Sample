package grades.presentation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

/**
 * 通知（Notification）と確認ダイアログ（ConfirmDialog）の見本（フロントエンド編 05 章）。
 * 業務の機能ではなく、資料のコードを、そのまま動かして、表示を確かめるための画面。メニューには出さない。
 *
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの
 * （資料との一致は、Lecture の images/frontend/capture/check_snippets.py で確かめる）。
 */
@Route("demo/feedback")
@PageTitle("通知と確認の見本")
@RolesAllowed("TEACHER")
public class 通知と確認の見本ビュー extends VerticalLayout {

    public 通知と確認の見本ビュー() {
        add(new H2("通知と確認の見本"));

        add(new H3("通知（Notification）の種類"));
        add(行(
                ボタン("通知（種類なし）", e -> {
                    // snippet: notification-default
                    Notification.show("学生を登録しました");
                    // end snippet
                }),
                ボタン("通知（情報）", e -> {
                    // snippet: notification-info
                    Notification.show("成績を更新しました")
                            .addThemeVariants(NotificationVariant.INFO);
                    // end snippet
                }),
                ボタン("通知（成功）", e -> {
                    // snippet: notification-success
                    Notification.show("学生を登録しました")
                            .addThemeVariants(NotificationVariant.SUCCESS);
                    // end snippet
                }),
                ボタン("通知（警告）", e -> {
                    // snippet: notification-warning
                    Notification.show("点数が未入力です。履修中として登録します")
                            .addThemeVariants(NotificationVariant.WARNING);
                    // end snippet
                }),
                ボタン("通知（エラー）", e -> {
                    // snippet: notification-error
                    Notification.show("点数は0〜100にしてください: 150")
                            .addThemeVariants(NotificationVariant.ERROR);
                    // end snippet
                })));

        add(new H3("通知の位置と表示時間"));
        add(行(
                ボタン("通知（上・中央）", e -> {
                    // snippet: notification-position
                    Notification.show("登録しました", 5000, Notification.Position.TOP_CENTER);
                    // end snippet
                }),
                ボタン("通知（画面の中央）", e -> {
                    // snippet: notification-middle
                    Notification.show("登録に失敗しました", 0, Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.ERROR);
                    // end snippet
                })));

        add(new H3("確認ダイアログ（ConfirmDialog）"));
        add(行(
                ボタン("確認ダイアログ（基本）", e -> {
                    // snippet: confirm-dialog-basic
                    var 確認 = new ConfirmDialog();
                    確認.setHeader("成績を保存します");
                    確認.setText("入力した内容で、成績を保存します。");
                    確認.setCancelable(true);
                    確認.setCancelText("キャンセル");
                    確認.setConfirmText("保存する");
                    確認.open();
                    // end snippet
                }),
                ボタン("確認ダイアログ（削除）", e -> {
                    // snippet: confirm-dialog-danger
                    var 確認 = new ConfirmDialog();
                    確認.setHeader("成績を削除します");
                    確認.setText("この操作は、取り消せません。");
                    確認.setCancelable(true);
                    確認.setCancelText("キャンセル");
                    確認.setConfirmText("削除する");
                    確認.setConfirmButtonTheme("error primary");
                    確認.open();
                    // end snippet
                })));

        add(new H3("ダイアログ（Dialog）"));
        add(行(
                ボタン("ダイアログ（基本）", e -> {
                    // snippet: dialog-basic
                    var 点数 = new IntegerField("点数");
                    var ダイアログ = new Dialog();
                    ダイアログ.setHeaderTitle("点数の修正");
                    ダイアログ.add(点数);
                    ダイアログ.getFooter().add(
                            new Button("キャンセル", e2 -> ダイアログ.close()),
                            new Button("保存", e2 -> ダイアログ.close()));
                    ダイアログ.open();
                    // end snippet
                })));
    }

    private static Button ボタン(String 文字, com.vaadin.flow.component.ComponentEventListener<com.vaadin.flow.component.ClickEvent<Button>> 処理) {
        return new Button(文字, 処理);
    }

    private static FlexLayout 行(Button... ボタン) {
        var 行 = new FlexLayout(ボタン);
        行.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        行.getStyle().set("gap", "0.5rem");
        return 行;
    }
}
