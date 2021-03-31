package ua.com.foxminded.galvad.university.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.model.Teacher;

@Service
public class TeacherService {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private TeacherDAO teacherDAO;

	public void create(TeacherDTO teacherDTO) {
		teacherDAO.create(convertToEntity(teacherDTO));
	}

	public TeacherDTO retrieve(Integer id) {
		return convertToDTO(teacherDAO.retrieve(id));
	}

	public void update(TeacherDTO oldDTO, TeacherDTO newDTO) {
		teacherDAO.update(convertToEntity(oldDTO, newDTO));
	}

	public void delete(TeacherDTO teacherDTO) {
		teacherDAO.delete(convertToEntity(teacherDTO));
	}

	public List<TeacherDTO> findAll() {
		return teacherDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private TeacherDTO convertToDTO(Teacher entity) {
		return modelMapper.map(entity, TeacherDTO.class);
	}

	private Teacher convertToEntity(TeacherDTO teacherDTO) {
		Teacher entity = modelMapper.map(teacherDTO, Teacher.class);
		entity.setId(teacherDAO.getId(entity));
		return entity;
	}

	private Teacher convertToEntity(TeacherDTO oldDTO, TeacherDTO newDTO) {
		Teacher entity = modelMapper.map(newDTO, Teacher.class);
		entity.setId(teacherDAO.getId(convertToEntity(oldDTO)));
		return entity;
	}
}
