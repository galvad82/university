package ua.com.foxminded.galvad.university.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.LessonMapper;
import ua.com.foxminded.galvad.university.model.Lesson;

@Component
public class LessonDAO implements DAO<Integer, Lesson> {

	private JdbcTemplate jdbcTemplate;
	private LessonMapper mapper;

	@Autowired
	public void setMapper(LessonMapper mapper) {
		if (mapper!=null) {
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
		String query = "INSERT INTO lessons (group_id,course,classroom,starttime,duration) VALUES (?,?,?,?,?)";
		jdbcTemplate.update(query, lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
				lesson.getStartTime(), lesson.getDuration());
	}

	public Lesson retrieve(Integer id) {
		String query = "SELECT * FROM lessons WHERE id=" + id;
		List<Lesson> listOfLessons = jdbcTemplate.query(query, mapper);
		if (!listOfLessons.isEmpty()) {
			return listOfLessons.get(0);
		} else {
			return null;
		}
	}

	public void update(Lesson lesson) {
		String query = "UPDATE lessons SET group_id=?, course=?, classroom=?, starttime=?, duration=? WHERE id=?";
		jdbcTemplate.update(query, lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
				lesson.getStartTime(), lesson.getDuration(), lesson.getId());
	}

	public void delete(Integer id) {
		String query = "DELETE FROM lessons WHERE id=" + id;
		jdbcTemplate.execute(query);
	}

	public void delete(Lesson lesson) {
		delete(lesson.getId());
	}

	public List<Lesson> findAll() {
		String query = "SELECT * FROM lessons";
		return jdbcTemplate.query(query, mapper);
	}

	public void deleteByClassroomID(Integer id) {
		String query = "DELETE FROM lessons WHERE classroom=" + id;
		jdbcTemplate.execute(query);
	} 
	public void deleteByCourseID(Integer id) {
		String query = "DELETE FROM lessons WHERE course=" + id;
		jdbcTemplate.execute(query);
	}
	public void deleteByGroupID(Integer id) {
		String query = "DELETE FROM lessons WHERE group_id=" + id;
		jdbcTemplate.execute(query);
	}
	
	
}
