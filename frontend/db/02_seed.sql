-- サンプルデータ
-- 対応する資料：データベース編 09 章（基本操作）、10 章（応用）、11 章（トランザクション）
-- 先に 01_schema.sql を流してから、このファイルを流す
--   psql -U postgres -d grades_db -f db/02_seed.sql
--     -U postgres   接続するユーザー（User）の名前
--     -d grades_db  接続するデータベース（database）の名前
--     -f ファイル   実行する SQL ファイル（file）。リポジトリのルートから見たパス
--
-- 資料の実行結果の表と同じ結果になるよう、値を決めてある。値を変えると、資料の表と食い違う。
--   enrollments：15 件（点数あり 12 件、履修中 3 件）、平均 73.75
--   60 点以上 10 件、60 点未満 2 件（Taro と Jiro）

INSERT INTO departments (department_id, name) VALUES
    (1, '情報システム学科'),
    (2, '電子工学科'),
    (3, '応用化学科');

INSERT INTO teachers (teacher_id, name, department_id) VALUES
    (1, '佐藤 健',   1),
    (2, '鈴木 恵',   1),
    (3, '高橋 誠',   2),
    (4, '田中 優子', 3);

-- 学科 1（情報システム学科）は Taro、Hanako、Ken
INSERT INTO students (student_id, name, department_id, enrolled_year) VALUES
    (1, 'Taro',   1, 2024),
    (2, 'Hanako', 1, 2024),
    (3, 'Jiro',   2, 2024),
    (4, 'Sakura', 3, 2024),
    (5, 'Ken',    1, 2025),
    (6, 'Yuki',   3, 2025);

INSERT INTO courses (course_id, title, credits, teacher_id) VALUES
    (1, 'プログラミング基礎', 2, 1),
    (2, 'データベース',       2, 2),
    (3, '線形代数',           4, 3),
    (4, '電気回路',           2, 3),
    (5, '有機化学',           2, 4);

-- 履修（学生, 科目, 年度, 点数）。NULL は履修中
INSERT INTO enrollments (student_id, course_id, academic_year, score) VALUES
    -- Taro：平均 69.0（60 点未満が 1 件）
    (1, 1, 2024, 80),
    (1, 2, 2025, 72),
    (1, 3, 2024, 55),
    -- Hanako：平均 91.5。線形代数 2025 は履修中（09 章の UPDATE の例）
    (2, 1, 2024, 95),
    (2, 2, 2025, 88),
    (2, 3, 2025, NULL),
    -- Jiro：平均 58.3（60 点未満が 1 件）。電気回路 2024 は 09 章の DELETE の例
    (3, 4, 2024, 65),
    (3, 1, 2024, 60),
    (3, 3, 2025, 50),
    -- Sakura：平均 84.5
    (4, 3, 2024, 91),
    (4, 5, 2025, 78),
    -- Ken：平均 85.0（データベースは履修中）
    (5, 1, 2025, 85),
    (5, 2, 2025, NULL),
    -- Yuki：平均 66.0（電気回路は履修中）
    (6, 5, 2025, 66),
    (6, 4, 2025, NULL);

-- ID を指定して投入したので、自動採番の「次の番号」を合わせる（09 章 11 節）
SELECT setval(pg_get_serial_sequence('departments', 'department_id'), (SELECT MAX(department_id) FROM departments));
SELECT setval(pg_get_serial_sequence('teachers',    'teacher_id'),    (SELECT MAX(teacher_id)    FROM teachers));
SELECT setval(pg_get_serial_sequence('students',    'student_id'),    (SELECT MAX(student_id)    FROM students));
SELECT setval(pg_get_serial_sequence('courses',     'course_id'),     (SELECT MAX(course_id)     FROM courses));
SELECT setval(pg_get_serial_sequence('enrollments', 'enrollment_id'), (SELECT MAX(enrollment_id) FROM enrollments));

-- ログイン用のユーザー（フロントエンド編 11 章）
-- ※開発・講義用の仮のユーザーです。パスワードは、3 人とも `grades-sample`（BCrypt でハッシュ化したものを保存）。
--   本番には、絶対に持ち込まないでください。
INSERT INTO users (username, password_hash, role, student_id) VALUES
    ('teacher01', '{bcrypt}$2a$10$NsG/9Dox1rNqdp9a9alIouy/wYgpBMJU1ZMkqPbIxB2FRtCmAtwqO', 'TEACHER', NULL),
    ('student01', '{bcrypt}$2a$10$G.9GVowtCyxww1gKsAI8e.vlV84q3nDA9j.JOYaQuP4gZKWsUWbD2', 'STUDENT', 1),
    ('student02', '{bcrypt}$2a$10$hx2RIiID3/3Ki7VEq7QRruThc7.bNDB/GVCm.5pEi.Dt4iFdPTHoe', 'STUDENT', 2);
