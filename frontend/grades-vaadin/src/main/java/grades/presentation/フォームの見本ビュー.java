package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import static grades.presentation.見本.見本;

/**
 * Binder の入力チェックの見本（フロントエンド編 08 章）。資料の画像を撮るための画面。メニューには出さない。
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの。
 */
@Route("demo/form")
@PageTitle("フォームの見本")
@RolesAllowed("TEACHER")
public class フォームの見本ビュー extends VerticalLayout {

    public フォームの見本ビュー() {
        add(new H2("フォームの見本"));
        var 並び = new FlexLayout();
        並び.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        並び.add(
                見本("form-required", 必須()),
                見本("form-range", 範囲()),
                見本("form-status-label", 状態ラベル()));
        add(並び);
    }

    private Component 必須() {
        // snippet: form-required
        var 氏名 = new TextField("氏名");
        var binder = new Binder<学生登録フォーム>(学生登録フォーム.class);
        binder.forField(氏名)
                .asRequired("氏名を入力してください")
                .bind(学生登録フォーム::get氏名, 学生登録フォーム::set氏名);
        binder.validate();
        // end snippet
        return 氏名;
    }

    private Component 範囲() {
        // snippet: form-range
        var 入学年度 = new IntegerField("入学年度");
        入学年度.setValue(1999);
        var binder = new Binder<学生登録フォーム>(学生登録フォーム.class);
        binder.forField(入学年度)
                .withValidator(new IntegerRangeValidator("2000〜2100 の範囲で入力してください", 2000, 2100))
                .bind(学生登録フォーム::get入学年度, 学生登録フォーム::set入学年度);
        binder.validate();
        // end snippet
        return 入学年度;
    }

    private Component 状態ラベル() {
        // snippet: form-status-label
        var 氏名 = new TextField("氏名");
        var 状態 = new Span();
        var binder = new Binder<学生登録フォーム>(学生登録フォーム.class);
        binder.forField(氏名)
                .asRequired("氏名を入力してください")
                .bind(学生登録フォーム::get氏名, 学生登録フォーム::set氏名);
        binder.setStatusLabel(状態);
        binder.validate();
        // end snippet
        状態.getStyle().set("color", "var(--lumo-error-text-color, #d00)");
        var 入れ物 = new VerticalLayout(氏名, 状態);
        入れ物.setPadding(false);
        return 入れ物;
    }
}
