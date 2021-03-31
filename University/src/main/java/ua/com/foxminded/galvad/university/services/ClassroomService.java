package ua.com.foxminded.galvad.university.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.ClassroomDAO;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.model.Classroom;

@Service
public class ClassroomService {

	private ModelMapper modelMapper;
	private ClassroomDAO classroomDAO;

	@Autowired
	public void setModelMapper(ModelMapper modelMapper) {
		if (modelMapper != null) {
			this.modelMapper = modelMapper;
		} else {
			throw new IllegalArgumentException("ModelMapper cannot be null!");
		}
	}

	@Autowired
	public void setClassroomDAO(ClassroomDAO classroomDAO) {
		if (classroomDAO != null) {
			this.classroomDAO = classroomDAO;
		} else {
			throw new IllegalArgumentException("ClassroomDAO cannot be null!");
		}
	}

	public void create(ClassroomDTO classroomDTO) {
		classroomDAO.create(convertToEntity(classroomDTO));
	}

	public ClassroomDTO retrieve(Integer id) {
		return convertToDTO(classroomDAO.retrieve(id));
	}

	public void update(ClassroomDTO oldDTO, ClassroomDTO newDTO) {
		classroomDAO.update(convertToEntity(oldDTO, newDTO));
	}

	public void delete(ClassroomDTO classroomDTO) {
		classroomDAO.delete(convertToEntity(classroomDTO));
	}

	public List<ClassroomDTO> findAll() {
		return classroomDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public Classroom setId(Classroom classroom) {
		classroom.setId(classroomDAO.getId(classroom));
		return classroom;
	}
	
	private ClassroomDTO convertToDTO(Classroom entity) {
		return modelMapper.map(entity, ClassroomDTO.class);
	}

	protected Classroom convertToEntity(ClassroomDTO classroomDTO) {
		Classroom entity = modelMapper.map(classroomDTO, Classroom.class);
		entity.setId(classroomDAO.getId(entity));
		return entity;
	}

	protected Classroom convertToEntity(ClassroomDTO oldDTO, ClassroomDTO newDTO) {
		Classroom entity = modelMapper.map(newDTO, Classroom.class);
		entity.setId(classroomDAO.getId(convertToEntity(oldDTO)));
		return entity;
	}
}
