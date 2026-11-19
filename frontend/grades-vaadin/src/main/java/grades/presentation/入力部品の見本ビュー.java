package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import static grades.presentation.見本.見本;

/**
 * 入力コンポーネントの見本（フロントエンド編 05 章）。資料の画像を撮るための画面。メニューには出さない。
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの。
 */
@Route("demo/input")
@PageTitle("入力部品の見本")
@RolesAllowed("TEACHER")
public class 入力部品の見本ビュー extends VerticalLayout {

    public 入力部品の見本ビュー() {
        add(new H2("入力部品の見本"));
        var 並び = new FlexLayout();
        並び.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        並び.add(
                見本("input-text-all", テキスト_すべて()),
                見本("input-text-basic", テキスト基本()),
                見本("input-text-placeholder", テキスト_プレースホルダー()),
                見本("input-text-helper", テキスト_ヘルパー()),
                見本("input-text-required", テキスト_必須()),
                見本("input-text-error", テキスト_エラー()),
                見本("input-text-readonly", テキスト_読み取り専用()),
                見本("input-text-disabled", テキスト_無効()),
                見本("input-text-area", テキストエリア()),
                見本("input-email", メール()),
                見本("input-number", 小数()),
                見本("input-checkbox", チェックボックス()),
                見本("input-multi-combo", 複数コンボ()),
                見本("input-integer", 整数()),
                見本("input-password", パスワード()),
                見本("input-date", 日付()),
                見本("input-select", セレクト()),
                見本("input-combo", コンボ()),
                見本("input-radio", ラジオ()),
                見本("input-checkbox-group", チェックボックス群()));
        add(並び);
    }

    private Component テキスト_すべて() {
        // snippet: input-text-all
        var 氏名 = new TextField("氏名");
        氏名.setPlaceholder("例：千歳 太郎");
        氏名.setHelperText("50 文字以内");
        氏名.setRequiredIndicatorVisible(true);
        // end snippet
        return 氏名;
    }

    private Component テキスト基本() {
        // snippet: input-text-basic
        var 氏名 = new TextField("氏名");
        // end snippet
        return 氏名;
    }

    private Component テキスト_プレースホルダー() {
        // snippet: input-text-placeholder
        var 氏名 = new TextField("氏名");
        氏名.setPlaceholder("例：千歳 太郎");
        // end snippet
        return 氏名;
    }

    private Component テキスト_ヘルパー() {
        // snippet: input-text-helper
        var 氏名 = new TextField("氏名");
        氏名.setHelperText("50 文字以内");
        // end snippet
        return 氏名;
    }

    private Component テキスト_必須() {
        // snippet: input-text-required
        var 氏名 = new TextField("氏名");
        氏名.setRequiredIndicatorVisible(true);
        // end snippet
        return 氏名;
    }

    private Component テキスト_エラー() {
        // snippet: input-text-error
        var 氏名 = new TextField("氏名");
        氏名.setInvalid(true);
        氏名.setErrorMessage("氏名を入力してください");
        // end snippet
        return 氏名;
    }

    private Component テキスト_読み取り専用() {
        // snippet: input-text-readonly
        var 学生番号 = new TextField("学生番号");
        学生番号.setValue("20240001");
        学生番号.setReadOnly(true);
        // end snippet
        return 学生番号;
    }

    private Component テキスト_無効() {
        // snippet: input-text-disabled
        var 備考 = new TextField("備考");
        備考.setEnabled(false);
        // end snippet
        return 備考;
    }

    private Component 整数() {
        // snippet: input-integer
        var 点数 = new IntegerField("点数");
        点数.setHelperText("0〜100 の整数");
        点数.setMin(0);
        点数.setMax(100);
        点数.setStepButtonsVisible(true);
        // end snippet
        return 点数;
    }

    private Component パスワード() {
        // snippet: input-password
        var パスワード = new PasswordField("パスワード");
        パスワード.setValue("grades-sample");
        // end snippet
        return パスワード;
    }

    private Component 日付() {
        // snippet: input-date
        var 受講日 = new DatePicker("受講日");
        // end snippet
        return 受講日;
    }

    private Component セレクト() {
        // snippet: input-select
        var 学科 = new Select<String>();
        学科.setLabel("所属学科");
        学科.setItems("情報システム学科", "電子工学科", "応用化学科");
        学科.setValue("電子工学科");
        // end snippet
        return 学科;
    }

    private Component コンボ() {
        // snippet: input-combo
        var 科目 = new ComboBox<String>("科目");
        科目.setItems("プログラミング基礎", "データベース", "線形代数");
        科目.setValue("データベース");
        // end snippet
        return 科目;
    }

    private Component ラジオ() {
        // snippet: input-radio
        var 状態 = new RadioButtonGroup<String>("履修の状態");
        状態.setItems("履修中", "評価済み");
        状態.setValue("履修中");
        // end snippet
        return 状態;
    }

    private Component チェックボックス群() {
        // snippet: input-checkbox-group
        var 列 = new CheckboxGroup<String>("表示する列");
        列.setItems("科目名", "単位", "年度", "点数");
        列.select("科目名", "点数");
        // end snippet
        return 列;
    }

    private Component テキストエリア() {
        // snippet: input-text-area
        var 備考 = new TextArea("備考");
        備考.setValue("遠隔授業で受講。\n課題は、期限内に提出済み。");
        備考.setMinHeight("120px");
        // end snippet
        return 備考;
    }

    private Component メール() {
        // snippet: input-email
        var 連絡先 = new EmailField("連絡先のメールアドレス");
        連絡先.setValue("taro@example.com");
        // end snippet
        return 連絡先;
    }

    private Component 小数() {
        // snippet: input-number
        var 平均点 = new NumberField("平均点");
        平均点.setValue(91.5);
        平均点.setReadOnly(true);
        // end snippet
        return 平均点;
    }

    private Component チェックボックス() {
        // snippet: input-checkbox
        var 履修中 = new Checkbox("履修中として登録する");
        履修中.setValue(true);
        // end snippet
        return 履修中;
    }

    private Component 複数コンボ() {
        // snippet: input-multi-combo
        var 科目 = new MultiSelectComboBox<String>("科目（複数）");
        科目.setItems("プログラミング基礎", "データベース", "線形代数");
        科目.setWidth("360px");
        科目.select("プログラミング基礎", "データベース");
        // end snippet
        return 科目;
    }
}
