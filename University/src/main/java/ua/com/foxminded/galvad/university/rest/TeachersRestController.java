package ua.com.foxminded.galvad.university.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.TeacherService;

@RestController
@RequestMapping("/api/teachers")
@Tag(name = "Teachers REST Controller", description = "It is used for making restful web services for Teachers")
public class TeachersRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeachersRestController.class);

	private static final String PATH_ID = "/{id}";
	private static final String NOT_FOUND_ERROR = "Teacher is not found";
	public final TeacherService teacherService;

	@Autowired
	public TeachersRestController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@Operation(summary = "Retrieve a Teacher by ID", description = "It's used for retrieving a Teacher by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Teacher is not found") })
	@GetMapping(PATH_ID)
	public ResponseEntity<TeacherDTO> retrieve(
			@PathVariable @Parameter(description = "ID of the required Teacher") Integer id) {
		TeacherDTO teacherDTO;
		try {
			teacherDTO = teacherService.retrieve(id);
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while retrieving DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		}
		return new ResponseEntity<>(addLinks(teacherDTO), HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@Operation(summary = "Update a Teacher", description = "It's used for updating existing Teacher with specific ID by new data")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Teacher is not found"),
			@ApiResponse(responseCode = "500", description = "Teacher wasn't updated") })
	@PutMapping(PATH_ID)
	public ResponseEntity<TeacherDTO> update(
			@PathVariable @Parameter(description = "ID of the initial Teacher") Integer id,
			@RequestBody @Parameter(description = "An updated version of Teacher with the initial ID") TeacherDTO updatedDTO) {
		TeacherDTO teacherDTO;
		try {
			teacherDTO = teacherService.update(teacherService.retrieve(id), updatedDTO);
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while updating DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while updating DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.INTERNAL_SERVER_ERROR, "Teacher wasn't updated");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Teacher wasn't updated", ex);
		}
		return new ResponseEntity<>(teacherDTO, HttpStatus.OK);
	}

	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@Operation(summary = "Get list of Teachers", description = "It's used for retrieving a list of all the added Teachers")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "None of Teachers is found") })
	@GetMapping()
	public ResponseEntity<List<TeacherDTO>> findAll() {
		List<TeacherDTO> result = new ArrayList<>();
		try {
			teacherService.findAll().stream().forEach(s -> result.add(addLinks(s)));
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while retrieving a list of TeacherDTOs, HttpStatus={}, ErrorMessage={}",
					HttpStatus.NOT_FOUND, "None of Teachers is found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "None of Teachers is found", e);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@Operation(summary = "Create a Teacher", description = "It's used for creating a new Teacher")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "500", description = "Teacher wasn't added") })
	@PostMapping()
	public ResponseEntity<TeacherDTO> create(
			@RequestBody @Parameter(description = "A new Teacher to create") TeacherDTO newDTO) {
		TeacherDTO teacherDTO;
		try {
			teacherDTO = teacherService.create(newDTO);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while adding a TeacherDTO to DB, HttpStatus={}, ErrorMessage={}",
					HttpStatus.INTERNAL_SERVER_ERROR, "Teacher wasn't added");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Teacher wasn't added", ex);
		}
		return new ResponseEntity<>(teacherDTO, HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@Operation(summary = "Delete a Teacher by ID", description = "It's used for deleting a Teacher with a specific ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Teacher is not found"),
			@ApiResponse(responseCode = "500", description = "Teacher wasn't deleted") })
	@DeleteMapping(PATH_ID)
	public ResponseEntity<Void> delete(
			@PathVariable @Parameter(description = "ID of the required Teacher to delete") Integer id) {
		try {
			teacherService.delete(teacherService.retrieve(id));
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while deleting DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while deleting DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.INTERNAL_SERVER_ERROR, "Teacher wasn't deleted");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Teacher wasn't deleted", ex);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private TeacherDTO addLinks(TeacherDTO teacherDTO) {
		LOGGER.trace("Going to add links for TeacherDTO with id={}", teacherDTO.getId());
		teacherDTO.add(linkTo(methodOn(TeachersRestController.class).retrieve(teacherDTO.getId())).withSelfRel());
		teacherDTO.add(linkTo(methodOn(TeachersRestController.class).update(teacherDTO.getId(), teacherDTO))
				.withRel("update"));
		teacherDTO.add(linkTo(methodOn(TeachersRestController.class).delete(teacherDTO.getId())).withRel("delete"));
		teacherDTO.add(linkTo(methodOn(TeachersRestController.class).findAll()).withRel("teachers"));
		LOGGER.trace("The links for TeacherDTO with id={} were added successfully", teacherDTO.getId());
		return teacherDTO;
	}

}