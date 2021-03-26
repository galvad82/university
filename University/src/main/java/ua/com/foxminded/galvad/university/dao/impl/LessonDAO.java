package ua.com.foxminded.galvad.university.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.LessonMapper;
import ua.com.foxminded.galvad.university.model.Lesson;

@Repository
public class LessonDAO implements DAO<Integer, Lesson> {

	private JdbcTemplate jdbcTemplate;
	private LessonMapper mapper;

	private static final String CREATE = "INSERT INTO lessons (group_id,course,classroom,starttime,duration) VALUES (?,?,?,?,?)";
	private static final String RETRIEVE = "SELECT * FROM lessons WHERE id=?";
	private static final String UPDATE = "UPDATE lessons SET group_id=?, course=?, classroom=?, starttime=?, duration=? WHERE id=?";
	private static final String DELETE = "DELETE FROM lessons WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM lessons";
	private static final String DELETE_BY_CLASSROOM_ID = "DELETE FROM lessons WHERE classroom=?";
	private static final String DELETE_BY_COURSE_ID = "DELETE FROM lessons WHERE course=?";
	private static final String DELETE_BY_GROUP_ID = "DELETE FROM lessons WHERE group_id=?";

	@Autowired
	public void setMapper(LessonMapper mapper) {
		if (mapper != null) {
			this.mapper = mapper;
		} else {
			throw new IllegalArgumentException("Mapper cannot be null!");
		}
	}

	@Autowired
	public void setDataSource(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}

	public void create(Lesson lesson) {
		jdbcTemplate.update(CREATE, lesson.getGroup().getId(), lesson.getCourse().getId(),
				lesson.getClassroom().getId(), lesson.getStartTime(), lesson.getDuration());
	}

	public Lesson retrieve(Integer id) {
		try {
			return jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public void update(Lesson lesson) {
		jdbcTemplate.update(UPDATE, lesson.getGroup().getId(), lesson.getCourse().getId(),
				lesson.getClassroom().getId(), lesson.getStartTime(), lesson.getDuration(), lesson.getId());
	}

	public void delete(Integer id) {
		jdbcTemplate.update(DELETE, id);
	}

	public void delete(Lesson lesson) {
		delete(lesson.getId());
	}

	public List<Lesson> findAll() {
		return jdbcTemplate.query(FIND_ALL, mapper);
	}

	public void deleteByClassroomID(Integer id) {
		jdbcTemplate.update(DELETE_BY_CLASSROOM_ID, id);
	}

	public void deleteByCourseID(Integer id) {
		jdbcTemplate.update(DELETE_BY_COURSE_ID, id);
	}

	public void deleteByGroupID(Integer id) {
		jdbcTemplate.update(DELETE_BY_GROUP_ID, id);
	}

}
