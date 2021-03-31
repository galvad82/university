package ua.com.foxminded.galvad.university.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.ClassroomDAO;
import ua.com.foxminded.galvad.university.dao.impl.CourseDAO;
import ua.com.foxminded.galvad.university.dao.impl.GroupDAO;
import ua.com.foxminded.galvad.university.dao.impl.LessonDAO;
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Lesson;

@Service
public class LessonService {

	private ModelMapper modelMapper = new ModelMapper();
	@Autowired
	private CourseService courseService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private ClassroomService classroomService;
	@Autowired
	private ClassroomDAO classroomDAO;
	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private GroupDAO groupDAO;
	@Autowired
	private TeacherDAO teacherDAO;
	@Autowired
	private LessonDAO lessonDAO;

	public void create(LessonDTO lessonDTO) {
		lessonDAO.create(convertToEntity(lessonDTO));
	}

	public LessonDTO retrieve(Integer id) {
		return convertToDTO(lessonDAO.retrieve(id));
	}

	public void update(LessonDTO oldDTO, LessonDTO newDTO) {
		lessonDAO.update(convertToEntity(oldDTO, newDTO));
	}

	public void delete(LessonDTO lessonDTO) {
		lessonDAO.delete(convertToEntity(lessonDTO));
	}

	public List<LessonDTO> findAll() {
		return lessonDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public void deleteByClassroom(ClassroomDTO classroomDTO) {
		lessonDAO.deleteByClassroomID(classroomService.convertToEntity(classroomDTO).getId());
	}

	public void deleteByCourse(CourseDTO courseDTO) {
		lessonDAO.deleteByCourseID(courseService.convertToEntity(courseDTO).getId());
	}

	public void deleteByGroup(GroupDTO groupDTO) {
		lessonDAO.deleteByGroupID(groupService.convertToEntity(groupDTO).getId());
	}

	private LessonDTO convertToDTO(Lesson entity) {
		entity.setClassroom(classroomDAO.retrieve(entity.getClassroom().getId()));
		Course course = courseDAO.retrieve(entity.getCourse().getId());
		course.setTeacher(teacherDAO.retrieve(course.getTeacher().getId()));
		entity.setCourse(course);
		entity.setGroup(groupDAO.retrieve(entity.getGroup().getId()));
		return modelMapper.map(entity, LessonDTO.class);
	}

	private Lesson convertToEntity(LessonDTO lessonDTO) {
		Lesson entity = new Lesson(groupService.convertToEntity(lessonDTO.getGroup()),
				courseService.convertToEntity(lessonDTO.getCourse()),
				classroomService.convertToEntity(lessonDTO.getClassroom()), lessonDTO.getStartTime(),
				lessonDTO.getDuration());
		entity.setId(lessonDAO.getId(entity));
		return entity;
	}
	
	private Lesson convertToEntity(LessonDTO oldDTO, LessonDTO newDTO) {
		Lesson entity = new Lesson(groupService.convertToEntity(newDTO.getGroup()),
				courseService.convertToEntity(newDTO.getCourse()),
				classroomService.convertToEntity(newDTO.getClassroom()), newDTO.getStartTime(),
				newDTO.getDuration());
		entity.setId(lessonDAO.getId(convertToEntity(oldDTO)));
		return entity;
	}
}
