package grades.domain;

import java.util.List;
import java.util.Optional;

/** 科目の保存の窓口。このサンプルでは、読み取りだけ。 */
public interface CourseRepository {

    Optional<Course> findById(int id);

    /** id の昇順。 */
    List<Course> findAll();
}
