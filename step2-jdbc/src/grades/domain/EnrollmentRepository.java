package grades.domain;

import java.util.List;

/** 履修の保存の窓口。 */
public interface EnrollmentRepository {

    /** 同じ（学生, 科目, 年度）がすでにあれば、点数を上書きする。 */
    void save(Enrollment enrollment);

    /** 指定した学生の履修。科目 id、年度の昇順。 */
    List<Enrollment> findByStudentId(int studentId);
}
