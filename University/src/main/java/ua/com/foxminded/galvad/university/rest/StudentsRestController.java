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

import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentsRestController {

	public final StudentService studentService;
	private static final String NOT_FOUND_ERROR = "Student is not found";

	@Autowired
	public StudentsRestController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<StudentDTO> retrieve(@PathVariable Integer id) {
		StudentDTO studentDTO;
		try {
			studentDTO = studentService.retrieve(id);
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		}
		return new ResponseEntity<>(addLinks(studentDTO), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StudentDTO> update(@PathVariable Integer id, @RequestBody StudentDTO updatedDTO) {
		StudentDTO studentDTO;
		try {
			studentDTO = studentService.update(studentService.retrieve(id), updatedDTO);
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Student wasn't updated", ex);
		}
		return new ResponseEntity<>(studentDTO, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<StudentDTO>> findAll() {
		List<StudentDTO> result = new ArrayList<>();
		try {
			studentService.findAll().stream().forEach(s -> result.add(addLinks(s)));
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "None of Students is not found", e);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<StudentDTO> create(@RequestBody StudentDTO newDTO) {
		StudentDTO studentDTO;
		try {
			studentDTO = studentService.create(newDTO);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Student wasn't added", ex);
		}
		return new ResponseEntity<>(studentDTO, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		try {
			studentService.delete(studentService.retrieve(id));
		} catch (DataNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Student wasn't deleted", ex);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	public StudentDTO addLinks(StudentDTO studentDTO) {
		studentDTO.add(linkTo(methodOn(StudentsRestController.class).retrieve(studentDTO.getId())).withSelfRel());
		studentDTO.add(linkTo(methodOn(StudentsRestController.class).update(studentDTO.getId(), studentDTO))
				.withRel("update"));
		studentDTO.add(linkTo(methodOn(StudentsRestController.class).delete(studentDTO.getId())).withRel("delete"));
		studentDTO.add(linkTo(methodOn(StudentsRestController.class).findAll()).withRel("students"));
		return studentDTO;
	}

}