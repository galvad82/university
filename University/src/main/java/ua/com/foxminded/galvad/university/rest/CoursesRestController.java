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

import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.CourseService;
import ua.com.foxminded.galvad.university.services.LessonService;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Courses REST Controller", description = "It is used for making restful web services for Courses")
public class CoursesRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoursesRestController.class);

	private static final String PATH_ID = "/{id}";
	private static final String PATH_ID_LESSONS = "/{id}/lessons";
	private static final String NOT_FOUND_ERROR = "Course is not found";
	private static final String UPDATE = "update";
	private static final String DELETE = "delete";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	private static final String ROLE_USER = "ROLE_USER";

	public final CourseService courseService;
	public final LessonService lessonService;

	@Autowired
	public CoursesRestController(CourseService courseService, LessonService lessonService) {
		this.courseService = courseService;
		this.lessonService = lessonService;
	}

	@Secured({ ROLE_USER, ROLE_ADMIN })
	@Operation(summary = "Retrieve a Course by ID", description = "It's used for retrieving a Course by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Course is not found") })
	@GetMapping(PATH_ID)
	public ResponseEntity<CourseDTO> retrieve(
			@PathVariable @Parameter(description = "ID of the required Course") Integer id) {
		CourseDTO courseDTO;
		try {
			courseDTO = courseService.retrieve(id);
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while retrieving DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		}
		return new ResponseEntity<>(addLinks(courseDTO), HttpStatus.OK);
	}

	@Secured({ ROLE_USER, ROLE_ADMIN })
	@Operation(summary = "Retrieve a list of Lessons for a Course by ID", description = "It's used for retrieving a list of Lessons for a Course by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "No lessons were found for the course"),
			@ApiResponse(responseCode = "500", description = "A list of lessons wasn't prepared") })
	@GetMapping(PATH_ID_LESSONS)
	public ResponseEntity<List<LessonDTO>> findAllLessonsForCourse(
			@PathVariable @Parameter(description = "ID of the Course") Integer id) {
		List<LessonDTO> result = new ArrayList<>();
		try {
			lessonService.findAllLessonsForCourse(courseService.retrieve(id).getName()).stream()
					.forEach(s -> result.add(addLinksForLessonDTO(s)));
		} catch (DataNotFoundException e) {
			LOGGER.trace(
					"DataNotFoundException while retrieving all lessons for Course DTO with id={}, HttpStatus={}, ErrorMessage={}",
					id, HttpStatus.NOT_FOUND, "No lessons were found for the course");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No lessons were found for the course", e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace(
					"DataAreNotUpdatedException while retrieving all lessons for Course DTO with id={}, HttpStatus={}, ErrorMessage={}",
					id, HttpStatus.INTERNAL_SERVER_ERROR, "A list of lessons wasn't prepared");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "A list of lessons wasn't prepared",
					ex);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Secured(ROLE_ADMIN)
	@Operation(summary = "Update a Course", description = "It's used for updating existing Course with specific ID by new data")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Course is not found"),
			@ApiResponse(responseCode = "500", description = "Course wasn't updated") })
	@PutMapping(PATH_ID)
	public ResponseEntity<CourseDTO> update(
			@PathVariable @Parameter(description = "ID of the initial Course") Integer id,
			@RequestBody @Parameter(description = "An updated version of Course with the initial ID") CourseDTO updatedDTO) {
		CourseDTO courseDTO;
		try {
			courseDTO = courseService.update(courseService.retrieve(id), updatedDTO);
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while updating DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while updating DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.INTERNAL_SERVER_ERROR, "Course wasn't updated");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Course wasn't updated", ex);
		}
		return new ResponseEntity<>(courseDTO, HttpStatus.OK);
	}

	@Secured({ ROLE_USER, ROLE_ADMIN })
	@Operation(summary = "Get list of Courses", description = "It's used for retrieving a list of all the added Courses")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "No courses were found") })
	@GetMapping()
	public ResponseEntity<List<CourseDTO>> findAll() {
		List<CourseDTO> result = new ArrayList<>();
		try {
			courseService.findAll().stream().forEach(s -> result.add(addLinks(s)));
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while retrieving a list of CourseDTOs, HttpStatus={}, ErrorMessage={}",
					HttpStatus.NOT_FOUND, "No courses were found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No courses were found", e);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Secured(ROLE_ADMIN)
	@Operation(summary = "Create a Course", description = "It's used for creating a new Course")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "500", description = "Course wasn't added") })
	@PostMapping()
	public ResponseEntity<CourseDTO> create(
			@RequestBody @Parameter(description = "A new Course to create") CourseDTO newDTO) {
		CourseDTO courseDTO;
		try {
			courseDTO = courseService.create(newDTO);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while adding a CourseDTO to DB, HttpStatus={}, ErrorMessage={}",
					HttpStatus.INTERNAL_SERVER_ERROR, "Course wasn't added");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Course wasn't added", ex);
		}
		return new ResponseEntity<>(courseDTO, HttpStatus.CREATED);
	}

	@Secured(ROLE_ADMIN)
	@Operation(summary = "Delete a Course by ID", description = "It's used for deleting a Course with a specific ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Course is not found"),
			@ApiResponse(responseCode = "500", description = "Course wasn't deleted") })
	@DeleteMapping(PATH_ID)
	public ResponseEntity<Void> delete(
			@PathVariable @Parameter(description = "ID of the required Course to delete") Integer id) {
		try {
			courseService.delete(courseService.retrieve(id));
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while deleting DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while deleting DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.INTERNAL_SERVER_ERROR, "Course wasn't deleted");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Course wasn't deleted", ex);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private CourseDTO addLinks(CourseDTO courseDTO) {
		LOGGER.trace("Going to add links for CourseDTO with id={}", courseDTO.getId());
		courseDTO.add(linkTo(methodOn(CoursesRestController.class).retrieve(courseDTO.getId())).withSelfRel());
		courseDTO.add(
				linkTo(methodOn(CoursesRestController.class).update(courseDTO.getId(), courseDTO)).withRel(UPDATE));
		courseDTO.add(linkTo(methodOn(CoursesRestController.class).delete(courseDTO.getId())).withRel(DELETE));
		courseDTO.add(linkTo(methodOn(CoursesRestController.class).findAll()).withRel("courses"));
		courseDTO.add(linkTo(methodOn(CoursesRestController.class).findAllLessonsForCourse(courseDTO.getId()))
				.withRel("lessonsForCourse"));
		TeacherDTO teacherDTO = courseDTO.getTeacher();
		teacherDTO.add(linkTo(methodOn(TeachersRestController.class).retrieve(teacherDTO.getId())).withSelfRel());
		teacherDTO.add(
				linkTo(methodOn(TeachersRestController.class).update(teacherDTO.getId(), teacherDTO)).withRel(UPDATE));
		teacherDTO.add(linkTo(methodOn(TeachersRestController.class).delete(teacherDTO.getId())).withRel(DELETE));
		teacherDTO.add(linkTo(methodOn(TeachersRestController.class).findAll()).withRel("teachers"));
		LOGGER.trace("The links for CourseDTO with id={} were added successfully", courseDTO.getId());
		return courseDTO;
	}

	private LessonDTO addLinksForLessonDTO(LessonDTO lessonDTO) {
		LOGGER.trace("Going to add links for LessonDTO with id={}", lessonDTO.getId());
		lessonDTO.add(linkTo(methodOn(LessonsRestController.class).retrieve(lessonDTO.getId())).withSelfRel());
		lessonDTO.add(
				linkTo(methodOn(LessonsRestController.class).update(lessonDTO.getId(), lessonDTO)).withRel(UPDATE));
		lessonDTO.add(linkTo(methodOn(LessonsRestController.class).delete(lessonDTO.getId())).withRel(DELETE));
		lessonDTO.add(linkTo(methodOn(LessonsRestController.class).findAll()).withRel("lessons"));
		LOGGER.trace("The links for LessonDTO with id={} were added successfully", lessonDTO.getId());
		return lessonDTO;
	}
}
