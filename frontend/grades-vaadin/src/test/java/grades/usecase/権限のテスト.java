package grades.usecase;

import grades.domain.学生;
import grades.domain.学生リポジトリ;
import grades.domain.履修一覧;
import grades.domain.履修リポジトリ;
import grades.domain.科目リポジトリ;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 権限のテスト（フロントエンド編 A1・12 章）。
 * 「他の学生の ID を指定して、拒否されること」を、データベースも画面も使わずに、ユースケースだけで確かめる。
 */
@SpringJUnitConfig(権限のテスト.設定.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)   // コンストラクタの引数を、自動で渡す
class 権限のテスト {

    /** テスト用の組み立て。保存先は、メモリ上のダミー。メソッドセキュリティを有効にする。 */
    @Configuration
    @EnableMethodSecurity
    static class 設定 {
        @Bean 学生リポジトリ 学生リポジトリ() {
            return new 学生リポジトリ() {
                public 学生 保存する(学生 s) { return s; }
                public Optional<学生> 探す(int id) { return Optional.of(new 学生(id, "テスト", 1, 2024)); }
                public List<学生> すべて探す() { return List.of(); }
            };
        }
        @Bean 科目リポジトリ 科目リポジトリ() {
            return new 科目リポジトリ() {
                public Optional<grades.domain.科目> 探す(int id) { return Optional.empty(); }
                public List<grades.domain.科目> すべて探す() { return List.of(); }
            };
        }
        @Bean 履修リポジトリ 履修リポジトリ() {
            return new 履修リポジトリ() {
                public void 保存する(grades.domain.履修 r) { }
                public 履修一覧 学生IDで探す(int id) { return new 履修一覧(List.of()); }
            };
        }
        @Bean 成績の権限 成績の権限() { return new 成績の権限(); }
        @Bean 成績一覧を取得する 成績一覧を取得する(学生リポジトリ a, 科目リポジトリ b, 履修リポジトリ c) {
            return new 成績一覧を取得する(a, b, c);
        }
        @Bean 学生を登録する 学生を登録する(学生リポジトリ a) { return new 学生を登録する(a); }
    }

    private final 成績一覧を取得する 成績一覧を取得する;
    private final 学生を登録する 学生を登録する;

    権限のテスト(成績一覧を取得する 成績一覧を取得する, 学生を登録する 学生を登録する) {
        this.成績一覧を取得する = 成績一覧を取得する;
        this.学生を登録する = 学生を登録する;
    }

    @AfterEach
    void 認証を消す() {
        SecurityContextHolder.clearContext();
    }

    private void ログインする(String ロール, Integer 学生ID) {
        var ユーザー = new ログインユーザー("test", "x", true, AuthorityUtils.createAuthorityList("ROLE_" + ロール), 学生ID);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(ユーザー, null, ユーザー.getAuthorities()));
    }

    @Test
    void 学生は自分の成績を見られる() {
        ログインする("STUDENT", 1);
        assertDoesNotThrow(() -> 成績一覧を取得する.実行する(1));
    }

    @Test
    void 学生は他の学生の成績を見られない() {
        ログインする("STUDENT", 1);
        assertThrows(AccessDeniedException.class, () -> 成績一覧を取得する.実行する(2));
    }

    @Test
    void 教員はすべての学生の成績を見られる() {
        ログインする("TEACHER", null);
        assertDoesNotThrow(() -> 成績一覧を取得する.実行する(2));
    }

    @Test
    void 学生は学生を登録できない() {
        ログインする("STUDENT", 1);
        assertThrows(AccessDeniedException.class, () -> 学生を登録する.実行する("悪い学生", 1, 2024));
    }

    @Test
    void ログインしていないと成績を見られない() {
        assertThrows(Exception.class, () -> 成績一覧を取得する.実行する(1));
    }
}
