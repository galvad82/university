package ua.com.foxminded.galvad.university.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.StudentService;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Students REST Controller", description = "It is used for making restful web services for Students")
public class StudentsRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentsRestController.class);

	private static final String PATH_ID = "/{id}";
	private static final String NOT_FOUND_ERROR = "Student is not found";
	public final StudentService studentService;

	@Autowired
	public StudentsRestController(StudentService studentService) {
		this.studentService = studentService;
	}

	@Operation(summary = "Retrieve a Student by ID", description = "It's used for retrieving a Student by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Student is not found") })
	@GetMapping(PATH_ID)
	public ResponseEntity<StudentDTO> retrieve(
			@PathVariable @Parameter(description = "ID of the required Student") Integer id) {
		StudentDTO studentDTO;
		try {
			studentDTO = studentService.retrieve(id);
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while retrieving DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		}
		return new ResponseEntity<>(addLinks(studentDTO), HttpStatus.OK);
	}

	@Operation(summary = "Update a Student", description = "It's used for updating existing Student with specific ID by new data")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Student is not found"),
			@ApiResponse(responseCode = "500", description = "Student wasn't updated") })
	@PutMapping(PATH_ID)
	public ResponseEntity<StudentDTO> update(
			@PathVariable @Parameter(description = "ID of the initial Student") Integer id,
			@RequestBody @Parameter(description = "An updated version of Student with the initial ID") StudentDTO updatedDTO) {
		StudentDTO studentDTO;
		try {
			studentDTO = studentService.update(studentService.retrieve(id), updatedDTO);
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while updating DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while updating DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.INTERNAL_SERVER_ERROR, "Student wasn't updated");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Student wasn't updated", ex);
		}
		return new ResponseEntity<>(studentDTO, HttpStatus.OK);
	}

	@Operation(summary = "Get list of Students", description = "It's used for retrieving a list of all the added Students")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "None of Students is found") })
	@GetMapping()
	public ResponseEntity<List<StudentDTO>> findAll() {
		List<StudentDTO> result = new ArrayList<>();
		try {
			studentService.findAll().stream().forEach(s -> result.add(addLinks(s)));
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while retrieving a list of StudentDTOs, HttpStatus={}, ErrorMessage={}",
					HttpStatus.NOT_FOUND, "None of Students is found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "None of Students is found", e);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Operation(summary = "Create a Student", description = "It's used for creating a new Student")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "500", description = "Student wasn't added") })
	@PostMapping()
	public ResponseEntity<StudentDTO> create(
			@RequestBody @Parameter(description = "A new Student to create") StudentDTO newDTO) {
		StudentDTO studentDTO;
		try {
			studentDTO = studentService.create(newDTO);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while adding a StudentDTO to DB, HttpStatus={}, ErrorMessage={}",
					HttpStatus.INTERNAL_SERVER_ERROR, "Student wasn't added");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Student wasn't added", ex);
		}
		return new ResponseEntity<>(studentDTO, HttpStatus.CREATED);
	}

	@Operation(summary = "Delete a Student by ID", description = "It's used for deleting a Student with a specific ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Student is not found"),
			@ApiResponse(responseCode = "500", description = "Student wasn't deleted") })
	@DeleteMapping(PATH_ID)
	public ResponseEntity<Void> delete(
			@PathVariable @Parameter(description = "ID of the required Student to delete") Integer id) {
		try {
			studentService.delete(studentService.retrieve(id));
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while deleting DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while deleting DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.INTERNAL_SERVER_ERROR, "Student wasn't deleted");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Student wasn't deleted", ex);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private StudentDTO addLinks(StudentDTO studentDTO) {
		LOGGER.trace("Going to add links for StudentDTO with id={}", studentDTO.getId());
		studentDTO.add(linkTo(methodOn(StudentsRestController.class).retrieve(studentDTO.getId())).withSelfRel());
		studentDTO.add(linkTo(methodOn(StudentsRestController.class).update(studentDTO.getId(), studentDTO))
				.withRel("update"));
		studentDTO.add(linkTo(methodOn(StudentsRestController.class).delete(studentDTO.getId())).withRel("delete"));
		studentDTO.add(linkTo(methodOn(StudentsRestController.class).findAll()).withRel("students"));
		LOGGER.trace("The links for StudentDTO with id={} were added successfully", studentDTO.getId());
		return studentDTO;
	}

}