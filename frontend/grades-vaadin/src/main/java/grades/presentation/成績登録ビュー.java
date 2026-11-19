package grades.presentation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import grades.domain.学生;
import grades.domain.科目;
import grades.usecase.学生一覧を取得する;
import grades.usecase.成績を登録する;
import grades.usecase.科目一覧を取得する;
import jakarta.annotation.security.RolesAllowed;

import java.util.NoSuchElementException;

/**
 * 成績の登録画面（フロントエンド編 09 章 7 節）。
 * 教員は、続けて何人も登録する。登録のあと、学生と年度を残し、科目と点数だけを入れ直せるようにする。
 */
@Route("enrollments/register")
@PageTitle("成績の登録")
@RolesAllowed("TEACHER")
public class 成績登録ビュー extends VerticalLayout {

    private final 成績を登録する 成績を登録する;
    private final Binder<成績登録フォーム> binder = new Binder<>(成績登録フォーム.class);
    private final ComboBox<科目> 科目 = new ComboBox<>("科目");
    private final IntegerField 点数 = new IntegerField("点数");

    public 成績登録ビュー(成績を登録する 成績を登録する, 学生一覧を取得する 学生一覧を取得する, 科目一覧を取得する 科目一覧を取得する) {
        this.成績を登録する = 成績を登録する;

        var 学生 = new ComboBox<学生>("学生");
        学生.setItems(学生一覧を取得する.実行する());               // 学生が多いので、絞り込める ComboBox
        学生.setItemLabelGenerator(s -> s.学生ID() + " " + s.氏名());

        科目.setItems(科目一覧を取得する.実行する());
        科目.setItemLabelGenerator(c -> c.科目名() + "（" + c.単位数() + " 単位）");

        var 年度 = new IntegerField("年度");
        年度.setValue(java.time.Year.now().getValue());

        点数.setHelperText("0〜100。履修中は、空欄のまま");
        // 範囲（0〜100）は、画面では制限しない。業務のルールの本体である Domain（履修）の検査に任せ、そのメッセージを表示する

        binder.forField(学生).asRequired("学生を選んでください").bind(成績登録フォーム::get学生, 成績登録フォーム::set学生);
        binder.forField(科目).asRequired("科目を選んでください").bind(成績登録フォーム::get科目, 成績登録フォーム::set科目);
        binder.forField(年度).asRequired("年度を入力してください").bind(成績登録フォーム::get年度, 成績登録フォーム::set年度);
        binder.forField(点数).bind(成績登録フォーム::get点数, 成績登録フォーム::set点数);

        var 登録 = new Button("登録する", e -> 登録する());
        登録.addThemeVariants(ButtonVariant.PRIMARY);

        var フォーム = new FormLayout(学生, 科目, 年度, 点数);
        フォーム.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        フォーム.setMaxWidth("28rem");

        add(new H2("成績の登録"), フォーム, 登録);
    }

    private void 登録する() {
        var 入力 = new 成績登録フォーム();
        try {
            binder.writeBean(入力);
        } catch (ValidationException e) {
            return;
        }
        try {
            成績を登録する.実行する(入力.get学生().学生ID(), 入力.get科目().科目ID(), 入力.get年度(), 入力.get点数());
            Notification.show("登録しました：" + 入力.get学生().氏名() + " / " + 入力.get科目().科目名())
                    .addThemeVariants(NotificationVariant.SUCCESS);
            科目.clear();                                       // 学生と年度は残し、科目と点数だけを入れ直す
            点数.clear();
        } catch (IllegalArgumentException | NoSuchElementException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.ERROR);
        }
    }
}
