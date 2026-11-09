package grades.domain;

import java.util.List;
import java.util.Optional;

/** 学生の保存の窓口。どう保存するかは書かない（実装は infrastructure 層）。 */
public interface StudentRepository {

    /** 保存して、id が付いた Student を返す。 */
    Student save(Student student);

    /** 見つからないときは Optional.empty()。 */
    Optional<Student> findById(int id);

    /** id の昇順。 */
    List<Student> findAll();
}
