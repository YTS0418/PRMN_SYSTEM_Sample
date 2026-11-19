package grades.usecase;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * ログイン中のユーザー（Spring Security の UserDetails に、学生ID を足したもの）。
 * 「誰が」操作しているかは、presentation（画面の表示）と usecase（権限の確認）の両方が使うので、usecase に置く。
 * 学生ID は、学生のときだけ入る（教員は null）。
 */
public class ログインユーザー extends User {

    private final Integer 学生ID;

    public ログインユーザー(String ユーザー名, String パスワードのハッシュ, boolean 有効, 
                      Collection<? extends GrantedAuthority> 権限, Integer 学生ID) {
        super(ユーザー名, パスワードのハッシュ, 有効, true, true, true, 権限);
        this.学生ID = 学生ID;
    }

    /** 学生のときは、その学生ID。教員のときは null。 */
    public Integer 学生ID() {
        return 学生ID;
    }
}
