package grades.dataaccess.jdbc;

import grades.usecase.ログインユーザー;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * ログインに使うユーザーを、users テーブルから読み取る（フロントエンド編 11 章）。
 * パスワードの照合は、ここでは行わない。ハッシュを返せば、Spring Security が、入力されたパスワードと照合する。
 */
@Service
class DBユーザー実装 implements UserDetailsService {

    private final JdbcClient jdbcClient;

    DBユーザー実装(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public UserDetails loadUserByUsername(String ユーザー名) throws UsernameNotFoundException {
        String sql = """
                SELECT username, password_hash, role, student_id, enabled
                FROM users
                WHERE username = :name
                """;
        return jdbcClient.sql(sql)
                .param("name", ユーザー名)                         // プレースホルダ（SQL インジェクションの対策）
                .query((rs, rowNum) -> new ログインユーザー(
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getBoolean("enabled"),
                        AuthorityUtils.createAuthorityList("ROLE_" + rs.getString("role")),   // "TEACHER" → ROLE_TEACHER
                        (Integer) rs.getObject("student_id")))
                .optional()
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));   // 画面には、存在の有無を出さない
    }
}
