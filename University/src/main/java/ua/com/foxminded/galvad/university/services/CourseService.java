package ua.com.foxminded.galvad.university.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.CourseDAO;
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Teacher;

@Service
public class CourseService {

	private ModelMapper modelMapper = new ModelMapper();
	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private TeacherDAO teacherDAO;

	public void create(CourseDTO courseDTO) {
		courseDAO.create(convertToEntity(courseDTO));
	}

	public CourseDTO retrieve(Integer id) {
		return convertToDTO(courseDAO.retrieve(id));
	}

	public void update(CourseDTO oldDTO, CourseDTO newDTO) {
		courseDAO.update(convertToEntity(oldDTO, newDTO));
	}

	public void delete(CourseDTO courseDTO) {
		courseDAO.delete(convertToEntity(courseDTO));
	}

	public List<CourseDTO> findAll() {
		return courseDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public Course setId(Course course) {
		course.setId(courseDAO.getId(course));
		return course;
	}

	private CourseDTO convertToDTO(Course entity) {
		CourseDTO courseDTO = modelMapper.map(entity, CourseDTO.class);
		courseDTO.setTeacher(modelMapper.map(teacherDAO.retrieve(entity.getTeacher().getId()), TeacherDTO.class));
		return courseDTO;
	}

	protected Course convertToEntity(CourseDTO courseDTO) {
		Course entity = modelMapper.map(courseDTO, Course.class);
		Teacher teacher = entity.getTeacher();
		teacher.setId(teacherDAO.getId(teacher));
		entity.setTeacher(teacher);
		entity.setId(courseDAO.getId(entity));
		return entity;
	}

	private Course convertToEntity(CourseDTO oldDTO, CourseDTO newDTO) {
		Course entity = modelMapper.map(newDTO, Course.class);
		Teacher teacher = entity.getTeacher();
		teacher.setId(teacherDAO.getId(teacher));
		entity.setTeacher(teacher);
		entity.setId(courseDAO.getId(convertToEntity(oldDTO)));
		return entity;
	}
}
