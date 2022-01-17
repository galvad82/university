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

import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.TeacherService;

@RestController
@RequestMapping("/api/teachers")
public class TeachersRestController {

	private static final String NOT_FOUND_ERROR = "Teacher is not found";
	public final TeacherService teacherService;

	@Autowired
	public TeachersRestController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<TeacherDTO> retrieve(@PathVariable Integer id) {
		TeacherDTO teacherDTO;
		try {
			teacherDTO = teacherService.retrieve(id);
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		}
		return new ResponseEntity<>(addLinks(teacherDTO), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TeacherDTO> update(@PathVariable Integer id, @RequestBody TeacherDTO updatedDTO) {
		TeacherDTO teacherDTO;
		try {
			teacherDTO = teacherService.update(teacherService.retrieve(id), updatedDTO);
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Teacher wasn't updated", ex);
		}
		return new ResponseEntity<>(teacherDTO, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<TeacherDTO>> findAll() {
		List<TeacherDTO> result = new ArrayList<>();
		try {
			teacherService.findAll().stream().forEach(s -> result.add(addLinks(s)));
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "None of Teachers is not found", e);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<TeacherDTO> create(@RequestBody TeacherDTO newDTO) {
		TeacherDTO teacherDTO;
		try {
			teacherDTO = teacherService.create(newDTO);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Teacher wasn't added", ex);
		}
		return new ResponseEntity<>(teacherDTO, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		try {
			teacherService.delete(teacherService.retrieve(id));
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Teacher wasn't deleted", ex);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	public TeacherDTO addLinks(TeacherDTO teacherDTO) {
		teacherDTO.add(linkTo(methodOn(TeachersRestController.class).retrieve(teacherDTO.getId())).withSelfRel());
		teacherDTO.add(linkTo(methodOn(TeachersRestController.class).update(teacherDTO.getId(), teacherDTO))
				.withRel("update"));
		teacherDTO.add(linkTo(methodOn(TeachersRestController.class).delete(teacherDTO.getId())).withRel("delete"));
		teacherDTO.add(linkTo(methodOn(TeachersRestController.class).findAll()).withRel("teachers"));
		return teacherDTO;
	}

}