package grades.infrastructure.memory;

import grades.domain.Student;
import grades.domain.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/** 学生をメモリ（Map）に保存する実装。アプリを終了すると消える。 */
public class InMemoryStudentRepository implements StudentRepository {

    private final Map<Integer, Student> data = new TreeMap<>();   // id の昇順
    private int nextId = 1;

    @Override
    public Student save(Student student) {
        Student saved = (student.id() == null) ? student.withId(nextId++) : student;
        data.put(saved.id(), saved);
        return saved;                  // id が付いた方を返す（record は変更できないため）
    }

    @Override
    public Optional<Student> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(data.values());
    }
}
