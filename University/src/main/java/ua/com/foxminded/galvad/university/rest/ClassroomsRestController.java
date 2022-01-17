package ua.com.foxminded.galvad.university.rest;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.ClassroomService;
import ua.com.foxminded.galvad.university.services.LessonService;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomsRestController {

	private static final String NOT_FOUND_ERROR = "Classroom is not found";
	public final ClassroomService classroomService;
	public final LessonService lessonService;

	@Autowired
	public ClassroomsRestController(ClassroomService classroomService, LessonService lessonService) {
		this.classroomService = classroomService;
		this.lessonService = lessonService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClassroomDTO> retrieve(@PathVariable Integer id) {
		ClassroomDTO classroomDTO;
		try {
			classroomDTO = classroomService.retrieve(id);
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		}
		return new ResponseEntity<>(addLinks(classroomDTO), HttpStatus.OK);
	}

	@GetMapping("/{id}/lessons")
	public ResponseEntity<List<LessonDTO>> findAllLessonsForClassroom(@PathVariable Integer id) {
		List<LessonDTO> result = new ArrayList<>();
		try {
			lessonService.findAllLessonsForGroup(classroomService.retrieve(id).getName()).stream()
					.forEach(s -> result.add(addLinksForLessonDTO(s)));
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No lessons were found for the classroom", e);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "A list of lessons wasn't prepared",
					ex);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClassroomDTO> update(@PathVariable Integer id, @RequestBody ClassroomDTO updatedDTO) {
		ClassroomDTO classroomDTO;
		try {
			classroomDTO = classroomService.update(classroomService.retrieve(id), updatedDTO);
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Classroom wasn't updated", ex);
		}
		return new ResponseEntity<>(classroomDTO, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<ClassroomDTO>> findAll() {
		List<ClassroomDTO> result = new ArrayList<>();
		try {
			classroomService.findAll().stream().forEach(s -> result.add(addLinks(s)));
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "None of Classrooms is not found", e);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<ClassroomDTO> create(@RequestBody ClassroomDTO newDTO) {
		ClassroomDTO classroomDTO;
		try {
			classroomDTO = classroomService.create(newDTO);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Classroom wasn't added", ex);
		}
		return new ResponseEntity<>(classroomDTO, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		try {
			classroomService.delete(classroomService.retrieve(id));
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Classroom wasn't deleted", ex);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private ClassroomDTO addLinks(ClassroomDTO classroomDTO) {
		classroomDTO.add(linkTo(methodOn(ClassroomsRestController.class).retrieve(classroomDTO.getId())).withSelfRel());
		classroomDTO.add(linkTo(methodOn(ClassroomsRestController.class).update(classroomDTO.getId(), classroomDTO))
				.withRel("update"));
		classroomDTO
				.add(linkTo(methodOn(ClassroomsRestController.class).delete(classroomDTO.getId())).withRel("delete"));
		classroomDTO.add(linkTo(methodOn(ClassroomsRestController.class).findAll()).withRel("classrooms"));
		classroomDTO.add(linkTo(methodOn(ClassroomsRestController.class).findAllLessonsForClassroom(classroomDTO.getId()))
				.withRel("lessonsForClassroom"));
		return classroomDTO;
	}

	private LessonDTO addLinksForLessonDTO(LessonDTO lessonDTO) {
		lessonDTO.add(linkTo(methodOn(LessonsRestController.class).retrieve(lessonDTO.getId())).withSelfRel());
		lessonDTO.add(
				linkTo(methodOn(LessonsRestController.class).update(lessonDTO.getId(), lessonDTO)).withRel("update"));
		lessonDTO.add(linkTo(methodOn(LessonsRestController.class).delete(lessonDTO.getId())).withRel("delete"));
		lessonDTO.add(linkTo(methodOn(LessonsRestController.class).findAll()).withRel("lessons"));
		return lessonDTO;
	}
}
