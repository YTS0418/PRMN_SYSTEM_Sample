package grades.presentation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import grades.domain.学科;
import grades.usecase.学生を登録する;
import grades.usecase.学科一覧を取得する;
import jakarta.annotation.security.RolesAllowed;

/**
 * 学生の登録画面（フロントエンド編 05・08・09 章）。
 * 画面のチェック（Binder）は使いやすさのため。業務のルールの本体は Domain（学生）にある。
 */
@Route("students/register")
@PageTitle("学生の登録")
@RolesAllowed("TEACHER")
public class 学生登録ビュー extends VerticalLayout {

    private final 学生を登録する 学生を登録する;
    private final Binder<学生登録フォーム> binder = new Binder<>(学生登録フォーム.class);

    public 学生登録ビュー(学生を登録する 学生を登録する, 学科一覧を取得する 学科一覧を取得する) {
        this.学生を登録する = 学生を登録する;

        var 氏名 = new TextField("氏名");
        氏名.setPlaceholder("例：千歳 太郎");
        氏名.setHelperText("50 文字以内");
        氏名.setRequiredIndicatorVisible(true);

        var 学科 = new Select<学科>();
        学科.setLabel("所属学科");
        学科.setItems(学科一覧を取得する.実行する());
        学科.setItemLabelGenerator(grades.domain.学科::学科名);       // 画面に出す文字を決める
        学科.setRequiredIndicatorVisible(true);

        var 入学年度 = new IntegerField("入学年度");
        入学年度.setHelperText("2000〜2100");
        入学年度.setRequiredIndicatorVisible(true);

        // 入力欄と、フォームの項目を結びつけ、画面のチェックを設定する
        binder.forField(氏名)
                .asRequired("氏名を入力してください")
                .withValidator(s -> s.length() <= 50, "氏名は50文字以内にしてください")
                .bind(学生登録フォーム::get氏名, 学生登録フォーム::set氏名);
        binder.forField(学科)
                .asRequired("所属学科を選んでください")
                .bind(学生登録フォーム::get学科, 学生登録フォーム::set学科);
        binder.forField(入学年度)
                .asRequired("入学年度を入力してください")
                .withValidator(new IntegerRangeValidator("2000〜2100 の範囲で入力してください", 2000, 2100))
                .bind(学生登録フォーム::get入学年度, 学生登録フォーム::set入学年度);

        var 登録 = new Button("登録する", e -> 登録する());
        登録.addThemeVariants(ButtonVariant.PRIMARY);              // 主ボタンは、1 画面に 1 つ

        var フォーム = new FormLayout(氏名, 学科, 入学年度);
        フォーム.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));   // 入力フォームは、1 列が基本
        フォーム.setMaxWidth("28rem");

        add(new H2("学生の登録"), フォーム, 登録);
    }

    private void 登録する() {
        var 入力 = new 学生登録フォーム();
        try {
            binder.writeBean(入力);                                  // チェックして、入力値を取り出す
        } catch (ValidationException e) {
            return;                                                 // エラーは、各入力欄に表示されている
        }
        try {
            var 学生 = 学生を登録する.実行する(入力.get氏名(), 入力.get学科().学科ID(), 入力.get入学年度());   // ユースケースを呼ぶ
            Notification.show("登録しました：" + 学生.氏名() + "（学生番号 " + 学生.学生ID() + "）")
                    .addThemeVariants(NotificationVariant.SUCCESS);
            binder.readBean(new 学生登録フォーム());                  // 続けて登録できるよう、フォームを空にする
        } catch (IllegalArgumentException e) {
            // Domain のルールに違反（画面のチェックを通っても、ここで止まることがある）
            Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.ERROR);
        }
    }
}
