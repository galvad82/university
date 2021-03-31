package ua.com.foxminded.galvad.university.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.StudentDAO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.model.Student;

@Service
public class StudentService {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private StudentDAO studentDAO;

	public void create(StudentDTO studentDTO) {
		studentDAO.create(convertToEntity(studentDTO));
	}

	public StudentDTO retrieve(Integer id) {
		return convertToDTO(studentDAO.retrieve(id));
	}

	public void update(StudentDTO oldDTO, StudentDTO newDTO) {
		studentDAO.update(convertToEntity(oldDTO, newDTO));
	}

	public void delete(StudentDTO studentDTO) {
		studentDAO.delete(convertToEntity(studentDTO));
	}

	public void delete(Integer id) {
		studentDAO.delete(id);
	}

	public List<StudentDTO> findAll() {
		return studentDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private StudentDTO convertToDTO(Student entity) {
		return modelMapper.map(entity, StudentDTO.class);
	}

	private Student convertToEntity(StudentDTO studentDTO) {
		Student entity = modelMapper.map(studentDTO, Student.class);
		entity.setId(studentDAO.getId(entity));
		return entity;
	}

	private Student convertToEntity(StudentDTO oldDTO, StudentDTO newDTO) {
		Student entity = modelMapper.map(newDTO, Student.class);
		entity.setId(studentDAO.getId(convertToEntity(oldDTO)));
		return entity;
	}
}
