package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.signals.local.ValueSignal;
import jakarta.annotation.security.RolesAllowed;

import static grades.presentation.見本.見本;

/**
 * 発展的な機能の見本（フロントエンド編 A4）。資料の画像を撮るための画面。メニューには出さない。
 * 「// snippet: 名前」と「// end snippet」の間が、資料に載せているコードと同じもの。
 */
@Route("demo/advanced")
@PageTitle("発展的な機能の見本")
@RolesAllowed("TEACHER")
public class 発展的な機能の見本ビュー extends VerticalLayout {

    public 発展的な機能の見本ビュー() {
        add(new H2("発展的な機能の見本"));
        var 並び = new FlexLayout();
        並び.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        並び.add(
                見本("advanced-upload-default", アップロード_既定()),
                見本("advanced-upload-japanese", アップロード_日本語()),
                見本("advanced-signal", シグナル()));
        add(並び);
    }

    private Component アップロード_既定() {
        // snippet: advanced-upload-default
        var アップロード = new Upload();
        アップロード.setAcceptedFileTypes(".csv");
        アップロード.setMaxFiles(1);
        // end snippet
        アップロード.setWidth("420px");
        return アップロード;
    }

    private Component アップロード_日本語() {
        // snippet: advanced-upload-japanese
        var アップロード = new Upload();
        アップロード.setAcceptedFileTypes(".csv");
        アップロード.setMaxFiles(1);
        アップロード.setI18n(new UploadI18N()
                .setDropFiles(new UploadI18N.DropFiles()
                        .setOne("成績の CSV ファイルを、ここにドロップ")
                        .setMany("ファイルを、ここにドロップ"))
                .setAddFiles(new UploadI18N.AddFiles()
                        .setOne("ファイルを選ぶ")
                        .setMany("ファイルを選ぶ")));
        // end snippet
        アップロード.setWidth("420px");
        return アップロード;
    }

    private Component シグナル() {
        // snippet: advanced-signal
        var 件数 = new ValueSignal<>(3);                              // 値を持つ Signal
        var ラベル = new Span();
        ラベル.bindText(件数.map(n -> "登録した件数：" + n));            // 値に応じて、文字が自動で決まる
        // end snippet
        return ラベル;
    }
}
