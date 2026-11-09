package grades.domain;

/**
 * 学生。
 * id は、まだ保存していない学生では null（保存したときに採番される）。
 */
public record Student(Integer id, String name, int departmentId, int enrolledYear) {

    public Student {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("名前を入力してください");
        }
        if (enrolledYear < 2000) {
            throw new IllegalArgumentException("入学年度は2000以上にしてください: " + enrolledYear);
        }
    }

    /** record は変更できないので、id を付けた新しい Student を返す。 */
    public Student withId(int newId) {
        return new Student(newId, name, departmentId, enrolledYear);
    }
}
