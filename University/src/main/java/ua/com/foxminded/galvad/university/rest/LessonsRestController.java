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

import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.LessonService;

@RestController
@RequestMapping("/api/lessons")
public class LessonsRestController {

	private static final String NOT_FOUND_ERROR = "Lesson is not found";
	public final LessonService lessonService;

	@Autowired
	public LessonsRestController(LessonService lessonService) {
		this.lessonService = lessonService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<LessonDTO> retrieve(@PathVariable Integer id) {
		LessonDTO lessonDTO;
		try {
			lessonDTO = lessonService.retrieve(id);
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, NOT_FOUND_ERROR, ex);
		}
		return new ResponseEntity<>(addLinks(lessonDTO), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<LessonDTO> update(@PathVariable Integer id, @RequestBody LessonDTO updatedDTO) {
		LessonDTO lessonDTO;
		try {
			lessonDTO = lessonService.update(lessonService.retrieve(id), updatedDTO);
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lesson wasn't updated", ex);
		}
		return new ResponseEntity<>(lessonDTO, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<LessonDTO>> findAll() {
		List<LessonDTO> result = new ArrayList<>();
		try {
			lessonService.findAll().stream().forEach(s -> result.add(addLinks(s)));
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "None of Lessons is not found", e);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "A list of lessons wasn't prepared",
					ex);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<LessonDTO> create(@RequestBody LessonDTO newDTO) {
		LessonDTO lessonDTO;
		try {
			lessonDTO = lessonService.create(newDTO);
		} catch (DataAreNotUpdatedException | DataNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lesson wasn't added", ex);
		}
		return new ResponseEntity<>(lessonDTO, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		try {
			lessonService.delete(lessonService.retrieve(id));
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lesson wasn't deleted", ex);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private LessonDTO addLinks(LessonDTO lessonDTO) {
		lessonDTO.add(linkTo(methodOn(LessonsRestController.class).retrieve(lessonDTO.getId())).withSelfRel());
		lessonDTO.add(
				linkTo(methodOn(LessonsRestController.class).update(lessonDTO.getId(), lessonDTO)).withRel("update"));
		lessonDTO.add(linkTo(methodOn(LessonsRestController.class).delete(lessonDTO.getId())).withRel("delete"));
		lessonDTO.add(linkTo(methodOn(LessonsRestController.class).findAll()).withRel("lessons"));
		return lessonDTO;
	}
}