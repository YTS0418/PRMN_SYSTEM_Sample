package grades.presentation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

/**
 * 部品の見本（フロントエンド編 04・05 章の説明用）。業務の機能ではなく、資料の画像を撮るための画面。
 * メニューには出さない。URL（/demo/components）を直接開く。
 */
@Route("demo/components")
@PageTitle("部品の見本")
@RolesAllowed("TEACHER")
public class 部品見本ビュー extends VerticalLayout {

    public 部品見本ビュー() {
        add(new H2("部品の見本"));

        // 04 章：FormLayout（幅に応じて、1 列／2 列。氏名は 2 列分の幅）
        var 氏名 = new TextField("氏名");
        氏名.setPlaceholder("例：千歳 太郎");
        氏名.setHelperText("50 文字以内");
        氏名.setRequiredIndicatorVisible(true);
        var 学科 = new Select<String>();
        学科.setLabel("所属学科");
        学科.setItems(List.of("情報システム学科", "電子工学科", "応用化学科"));
        var 入学年度 = new IntegerField("入学年度");
        入学年度.setHelperText("2000〜2100");
        var フォーム = new FormLayout(氏名, 学科, 入学年度);
        フォーム.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2));
        フォーム.setColspan(氏名, 2);
        フォーム.setId("form-layout");

        // 05 章：入力コンポーネント
        var 点数 = new IntegerField("点数");
        点数.setHelperText("0〜100 の整数。履修中は空欄");
        点数.setMin(0);
        点数.setMax(100);
        点数.setStepButtonsVisible(true);
        var 日付 = new DatePicker("受講日");
        var 入力 = new HorizontalLayout(new TextField("科目名"), 点数, 日付);
        入力.setId("inputs");

        // 05 章：ボタン（主・控えめ・危険）
        var 主 = new Button("登録する");
        主.addThemeVariants(ButtonVariant.PRIMARY);
        var 控えめ = new Button("キャンセル");
        控えめ.addThemeVariants(ButtonVariant.TERTIARY);
        var 危険 = new Button("削除する");
        危険.addThemeVariants(ButtonVariant.ERROR);
        var ボタン = new HorizontalLayout(主, 控えめ, 危険);
        ボタン.setId("buttons");

        // 05 章：確認ダイアログと通知
        var 確認を開く = new Button("確認ダイアログを開く", e -> {
            var 確認 = new ConfirmDialog();
            確認.setHeader("成績を削除します");
            確認.setText("この操作は、取り消せません。");
            確認.setCancelable(true);
            確認.setCancelText("キャンセル");
            確認.setConfirmText("削除する");
            確認.setConfirmButtonTheme("error primary");
            確認.open();
        });
        確認を開く.setId("open-confirm");
        var 通知を出す = new Button("通知を出す", e ->
                Notification.show("学生を登録しました").addThemeVariants(NotificationVariant.SUCCESS));
        通知を出す.setId("show-notification");

        add(new H3("フォーム（FormLayout）"), フォーム, new H3("入力コンポーネント"), 入力,
                new H3("ボタン"), ボタン, new H3("ダイアログと通知"), new HorizontalLayout(確認を開く, 通知を出す));
    }
}
