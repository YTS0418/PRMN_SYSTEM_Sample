package grades.domain;

/**
 * 保存や取得に失敗したことを表す例外。
 * JDBC の SQLException など、保存先の技術の例外を、この例外に包み直して上の層に伝える。
 * Domain 層に置くので、どの層からも使える。
 */
public class RepositoryException extends RuntimeException {

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
