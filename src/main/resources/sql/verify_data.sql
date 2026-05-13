-- 验证班级数据
SELECT '===== CLASZ TABLE =====' AS info;
SELECT id, name, room, begin_date, end_date, master_id, subject FROM clazz ORDER BY id;

-- 验证学员数据
SELECT '===== STUDENT TABLE =====' AS info;
SELECT id, name, no, gender, phone, degree, clazz_id, violation_count, violation_score FROM student ORDER BY id;

-- 验证联表查询: 班级 + 班主任姓名
SELECT '===== CLASZ + MASTER =====' AS info;
SELECT c.id, c.name AS className, c.room, c.begin_date, c.end_date, e.name AS masterName, c.subject
FROM clazz c LEFT JOIN emp e ON c.master_id = e.id
ORDER BY c.id;

-- 验证联表查询: 学员 + 班级名称
SELECT '===== STUDENT + CLASZ =====' AS info;
SELECT s.id, s.name, s.no, s.gender, s.degree, c.name AS clazzName
FROM student s LEFT JOIN clazz c ON s.clazz_id = c.id
ORDER BY s.id;
