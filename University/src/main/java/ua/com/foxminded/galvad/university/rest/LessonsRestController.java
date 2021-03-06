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
import static ua.com.foxminded.galvad.university.rest.RestConstants.ROLE_ADMIN;
import static ua.com.foxminded.galvad.university.rest.RestConstants.ROLE_USER;

import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.LessonService;

@RestController
@RequestMapping("/api/lessons")
@Tag(name = "Lessons REST Controller", description = "It is used for making restful web services for Lessons")
public class LessonsRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LessonsRestController.class);

	private static final String PATH_ID = "/{id}";
	private static final String NOT_FOUND_ERROR = "Lesson is not found";

	public final LessonService lessonService;

	@Autowired
	public LessonsRestController(LessonService lessonService) {
		this.lessonService = lessonService;
	}

	@Secured({ ROLE_USER, ROLE_ADMIN })
	@Operation(summary = "Retrieve a Lesson by ID", description = "It's used for retrieving a Lesson by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Lesson is not found"),
			@ApiResponse(responseCode = "500", description = "Lesson is not found") })
	@GetMapping(PATH_ID)
	public ResponseEntity<LessonDTO> retrieve(
			@PathVariable @Parameter(description = "ID of the required Lesson") Integer id) {
		LessonDTO lessonDTO;
		try {
			lessonDTO = lessonService.retrieve(id);
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while retrieving DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while retrieving DTO with id={}, HttpStatus={}, ErrorMessage={}",
					id, HttpStatus.INTERNAL_SERVER_ERROR, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, NOT_FOUND_ERROR, ex);
		}
		return new ResponseEntity<>(addLinks(lessonDTO), HttpStatus.OK);
	}

	@Secured(ROLE_ADMIN)
	@Operation(summary = "Update a Lesson", description = "It's used for updating existing Lesson with specific ID by new data")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Lesson is not found"),
			@ApiResponse(responseCode = "500", description = "Lesson wasn't updated") })
	@PutMapping(PATH_ID)
	public ResponseEntity<LessonDTO> update(
			@PathVariable @Parameter(description = "ID of the initial Lesson") Integer id,
			@RequestBody @Parameter(description = "An updated version of Lesson with the initial ID") LessonDTO updatedDTO) {
		LessonDTO lessonDTO;
		try {
			lessonDTO = lessonService.update(lessonService.retrieve(id), updatedDTO);
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while updating DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while updating DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.INTERNAL_SERVER_ERROR, "Lesson wasn't updated");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lesson wasn't updated", ex);
		}
		return new ResponseEntity<>(lessonDTO, HttpStatus.OK);
	}

	@Secured({ ROLE_USER, ROLE_ADMIN })
	@Operation(summary = "Get list of Lessons", description = "It's used for retrieving a list of all the added Lessons")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "None of Classrooms is found"),
			@ApiResponse(responseCode = "500", description = "A list of lessons wasn't prepared") })
	@GetMapping()
	public ResponseEntity<List<LessonDTO>> findAll() {
		List<LessonDTO> result = new ArrayList<>();
		try {
			lessonService.findAll().stream().forEach(s -> result.add(addLinks(s)));
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while retrieving a list of LessonDTOs, HttpStatus={}, ErrorMessage={}",
					HttpStatus.NOT_FOUND, "None of Lessons is found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "None of Lessons is found", e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace(
					"DataAreNotUpdatedException while retrieving a list of LessonDTOs, HttpStatus={}, ErrorMessage={}",
					HttpStatus.INTERNAL_SERVER_ERROR, "A list of lessons wasn't prepared");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "A list of lessons wasn't prepared",
					ex);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Secured(ROLE_ADMIN)
	@Operation(summary = "Create a Lesson", description = "It's used for creating a new Lesson")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "500", description = "Lesson wasn't added") })
	@PostMapping()
	public ResponseEntity<LessonDTO> create(
			@RequestBody @Parameter(description = "A new Lesson to create") LessonDTO newDTO) {
		LessonDTO lessonDTO;
		try {
			lessonDTO = lessonService.create(newDTO);
		} catch (DataAreNotUpdatedException | DataNotFoundException ex) {
			LOGGER.trace("Exception while adding a ClassroomDTO to DB, HttpStatus={}, ErrorMessage={}",
					HttpStatus.INTERNAL_SERVER_ERROR, "Lesson wasn't added");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lesson wasn't added", ex);
		}
		return new ResponseEntity<>(lessonDTO, HttpStatus.CREATED);
	}

	@Secured(ROLE_ADMIN)
	@Operation(summary = "Delete a Lesson by ID", description = "It's used for deleting a Lesson with a specific ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Lesson is not found"),
			@ApiResponse(responseCode = "500", description = "Lesson wasn't deleted") })
	@DeleteMapping(PATH_ID)
	public ResponseEntity<Void> delete(
			@PathVariable @Parameter(description = "ID of the required Lesson to delete") Integer id) {
		try {
			lessonService.delete(lessonService.retrieve(id));
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while deleting DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while deleting DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.INTERNAL_SERVER_ERROR, "Lesson wasn't deleted");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lesson wasn't deleted", ex);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private LessonDTO addLinks(LessonDTO lessonDTO) {
		LOGGER.trace("Going to add links for LessonDTO with id={}", lessonDTO.getId());
		lessonDTO.add(linkTo(methodOn(LessonsRestController.class).retrieve(lessonDTO.getId())).withSelfRel());
		lessonDTO.add(
				linkTo(methodOn(LessonsRestController.class).update(lessonDTO.getId(), lessonDTO)).withRel("update"));
		lessonDTO.add(linkTo(methodOn(LessonsRestController.class).delete(lessonDTO.getId())).withRel("delete"));
		lessonDTO.add(linkTo(methodOn(LessonsRestController.class).findAll()).withRel("lessons"));
		LOGGER.trace("The links for LessonDTO with id={} were added successfully", lessonDTO.getId());
		return lessonDTO;
	}
}