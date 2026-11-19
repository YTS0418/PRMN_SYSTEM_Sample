package grades.presentation;

import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/** ログインとアクセス制御の設定（フロントエンド編 10 章）。 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity                                    // ユースケースの @PreAuthorize を有効にする（12 章）
class セキュリティ設定 {

    @Bean
    SecurityFilterChain フィルタ連鎖(HttpSecurity http) throws Exception {
        // Vaadin に必要な設定（画面の通信、静的ファイル、ログアウトなど）をまとめて取り込み、ログイン画面を指定する
        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> configurer.loginView(ログインビュー.class));
        return http.build();
    }

    /** パスワードのハッシュ化の方式。既定は BCrypt（11 章）。 */
    @Bean
    PasswordEncoder パスワードエンコーダ() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
