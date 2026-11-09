package grades.presentation;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/** 例外を HTTP の応答に変える（Presentation 層の仕事）。Domain / Application は HTTP を知らない。 */
@RestControllerAdvice                                              // すべての Controller に共通の例外処理
class ApiExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)                // 学生・科目が見つからない
    ResponseEntity<String> notFound(NoSuchElementException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)              // Domain の検査に違反（点数の範囲など）
    ResponseEntity<String> badRequest(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    /**
     * データベースの制約違反（存在しない学科の指定など。一意制約違反の DuplicateKeyException も含む）。
     * 例外のメッセージには SQL 文やテーブル名が含まれるので、そのまま返さず、短い固定の文に置き換える。
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<String> conflict(DataIntegrityViolationException e) {
        return ResponseEntity.status(409).body("登録できませんでした。入力内容（学科の指定など）を確認してください");
    }
}
