package grades.infrastructure.memory;

import grades.domain.Course;
import grades.domain.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/** 科目をメモリに持つ実装。科目のデータは、作るときに渡す（Main が渡す）。 */
public class InMemoryCourseRepository implements CourseRepository {

    private final Map<Integer, Course> data = new TreeMap<>();

    public InMemoryCourseRepository(List<Course> courses) {
        for (Course course : courses) {
            data.put(course.id(), course);
        }
    }

    @Override
    public Optional<Course> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(data.values());
    }
}
