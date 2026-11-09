package grades.infrastructure.memory;

import grades.domain.Enrollment;
import grades.domain.EnrollmentRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 履修をメモリに保存する実装。
 * 「同じ（学生, 科目, 年度）は 1 件だけ」というルールを、ここでコードで守っている。
 * データベースでは、このルールを UNIQUE 制約が守る（データベース編 06 章）。
 */
public class InMemoryEnrollmentRepository implements EnrollmentRepository {

    private record Key(int studentId, int courseId, int academicYear) { }

    private final Map<Key, Enrollment> data = new HashMap<>();

    @Override
    public void save(Enrollment enrollment) {
        Key key = new Key(enrollment.studentId(), enrollment.courseId(), enrollment.academicYear());
        data.put(key, enrollment);     // 同じ組み合わせがあれば上書きされる
    }

    @Override
    public List<Enrollment> findByStudentId(int studentId) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : data.values()) {
            if (e.studentId() == studentId) {
                result.add(e);
            }
        }
        result.sort(Comparator.comparingInt(Enrollment::courseId)
                .thenComparingInt(Enrollment::academicYear));
        return result;
    }
}
