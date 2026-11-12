package grades.domain;

/**
 * 保存や取得に失敗したことを表す例外。
 * JDBC の SQLException など、保存先の技術の例外を、この例外に包み直して上の層に伝える。
 * domain パッケージに置くので、どの層からも使える。
 */
public class 保存失敗例外 extends RuntimeException {

    public 保存失敗例外(String メッセージ, Throwable 原因) {
        super(メッセージ, 原因);
    }
}
