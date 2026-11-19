package grades.presentation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

/** 見本の画面で使う、小さな道具（資料のコードには、含めない）。 */
final class 見本 {

    private 見本() { }

    /** 見本の部品を、id つきの入れ物に入れる。資料の画像は、この id で切り抜いて撮る。 */
    static Div 見本(String id, Component... 部品) {
        var 入れ物 = new Div(部品);
        入れ物.setId(id);
        入れ物.addClassName("sample-item");                     // 撮影のとき、ほかの見本を隠すための印
        入れ物.getStyle().set("display", "inline-block").set("align-self", "flex-start")
                .set("margin", "0.25rem 1rem 0.25rem 0");
        return 入れ物;
    }

    /** レイアウトの範囲が見えるよう、点線の枠を付ける（余白の効果を、見せるため）。 */
    static <T extends Component> T 枠線(T 部品) {
        部品.getElement().getStyle().set("outline", "1px dashed #8a8a8a");
        return 部品;
    }
}
