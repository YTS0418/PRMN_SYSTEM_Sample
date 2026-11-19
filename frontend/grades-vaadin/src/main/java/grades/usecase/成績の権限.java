package grades.usecase;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * 「この人が、この学生の成績を見てよいか」を判断する（権限の確認）。
 * @PreAuthorize の中から、@成績の権限.本人である(authentication, #学生ID) として呼ばれる。
 * 教員は、すべての学生を見てよい（@PreAuthorize の hasRole('TEACHER') で判断する）。ここでは、学生が自分かどうかだけを見る。
 */
@Component("成績の権限")
public class 成績の権限 {

    public boolean 本人である(Authentication 認証, int 学生ID) {
        if (認証 != null && 認証.getPrincipal() instanceof ログインユーザー ユーザー) {
            return ユーザー.学生ID() != null && ユーザー.学生ID() == 学生ID;
        }
        return false;
    }
}
