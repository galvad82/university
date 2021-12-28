package ua.com.foxminded.galvad.university.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.repository.ClassroomRepository;
import ua.com.foxminded.galvad.university.repository.LessonRepository;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

	private static final String NAME = "NAME";
	private static final String NEWNAME = "NEWNAME";

	@Mock
	private ClassroomRepository mockClassroomRepository;

	@Mock
	private LessonRepository mockLessonRepository;

	@Mock
	private ModelMapper mockModelMapper;

	@InjectMocks
	private ClassroomService classroomService;

	@Test
	void testCreate() {
		ClassroomDTO DTO = new ClassroomDTO();
		DTO.setName(NAME);
		when(mockModelMapper.map(DTO, Classroom.class)).thenReturn(new Classroom(1, NAME));
		classroomService.create(DTO);
		verify(mockClassroomRepository, times(1)).save(any(Classroom.class));
	}

	@Test
	void testRetrieve() {
		ClassroomDTO DTO = new ClassroomDTO();
		DTO.setName(NAME);
		Classroom classroom = new Classroom(1, NAME);
		when(mockClassroomRepository.findByName(NAME)).thenReturn(classroom);
		when(mockModelMapper.map(classroom, ClassroomDTO.class)).thenReturn(DTO);
		classroomService.retrieve(NAME);
		verify(mockClassroomRepository, times(1)).findByName(NAME);
	}

	@Test
	void testUpdate() {
		ClassroomDTO oldDTO = new ClassroomDTO();
		oldDTO.setName(NAME);
		ClassroomDTO newDTO = new ClassroomDTO();
		newDTO.setName(NEWNAME);
		Classroom oldEntity = new Classroom(1, NAME);
		when(mockModelMapper.map(oldDTO, Classroom.class)).thenReturn(oldEntity);
		when(mockModelMapper.map(newDTO, Classroom.class)).thenReturn(new Classroom(2, NEWNAME));
		when(mockClassroomRepository.findByName(NAME)).thenReturn(oldEntity);
		classroomService.update(oldDTO, newDTO);
		verify(mockClassroomRepository, times(1)).save(any(Classroom.class));
	}

	@Test
	void testDelete() {
		ClassroomDTO DTO = new ClassroomDTO();
		DTO.setName(NAME);
		Classroom entity = new Classroom(1, NAME);
		when(mockModelMapper.map(DTO, Classroom.class)).thenReturn(entity);
		when(mockClassroomRepository.findByName(NAME)).thenReturn(entity);
		classroomService.delete(DTO);
		verify(mockClassroomRepository, times(1)).delete(any(Classroom.class));

	}

	@Test
	void testFindAll() {
		ClassroomDTO DTO = new ClassroomDTO();
		DTO.setName(NAME);
		Classroom classroom = new Classroom(1, NAME);
		List<Classroom> listOfClassrooms = new ArrayList<>();
		listOfClassrooms.add(classroom);
		when(mockClassroomRepository.findAll()).thenReturn(listOfClassrooms);
		when(mockModelMapper.map(classroom, ClassroomDTO.class)).thenReturn(DTO);
		classroomService.findAll();
		verify(mockClassroomRepository, times(1)).findAll();
	}
}
