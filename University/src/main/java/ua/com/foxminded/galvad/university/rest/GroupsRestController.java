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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.GroupService;
import ua.com.foxminded.galvad.university.services.LessonService;

@RestController
@RequestMapping("/api/groups")
public class GroupsRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupsRestController.class);

	private static final String PATH_ID = "/{id}";
	private static final String PATH_ID_LESSONS = "/{id}/lessons";
	private static final String NOT_FOUND_ERROR = "Group is not found";
	public final GroupService groupService;
	public final LessonService lessonService;

	@Autowired
	public GroupsRestController(GroupService groupService, LessonService lessonService) {
		this.groupService = groupService;
		this.lessonService = lessonService;
	}

	@GetMapping(PATH_ID)
	public ResponseEntity<GroupDTO> retrieve(@PathVariable Integer id) {
		GroupDTO groupDTO;
		try {
			groupDTO = groupService.retrieve(id);
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while retrieving DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while retrieving DTO with id={}, HttpStatus={}, ErrorMessage={}",
					id, HttpStatus.INTERNAL_SERVER_ERROR, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, NOT_FOUND_ERROR, ex);
		}
		return new ResponseEntity<>(addLinks(groupDTO), HttpStatus.OK);
	}

	@GetMapping(PATH_ID_LESSONS)
	public ResponseEntity<List<LessonDTO>> findAllLessonsForGroup(@PathVariable Integer id) {
		List<LessonDTO> result = new ArrayList<>();
		try {
			lessonService.findAllLessonsForGroup(groupService.retrieve(id).getName()).stream()
					.forEach(s -> result.add(addLinksForLessonDTO(s)));
		} catch (DataNotFoundException e) {
			LOGGER.trace(
					"DataNotFoundException while retrieving all lessons for Group DTO with id={}, HttpStatus={}, ErrorMessage={}",
					id, HttpStatus.NOT_FOUND, "No lessons were found for the group");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No lessons were found for the group", e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace(
					"DataAreNotUpdatedException while retrieving all lessons for Group DTO with id={}, HttpStatus={}, ErrorMessage={}",
					id, HttpStatus.INTERNAL_SERVER_ERROR, "A list of lessons wasn't prepared");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "A list of lessons wasn't prepared",
					ex);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping(PATH_ID)
	public ResponseEntity<GroupDTO> update(@PathVariable Integer id, @RequestBody GroupDTO updatedDTO) {
		GroupDTO groupDTO;
		try {
			groupDTO = groupService.update(groupService.retrieve(id), updatedDTO);
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while updating DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while updating DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.INTERNAL_SERVER_ERROR, "Group wasn't updated");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Group wasn't updated", ex);
		}
		return new ResponseEntity<>(groupDTO, HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<List<GroupDTO>> findAll() {
		List<GroupDTO> result = new ArrayList<>();
		try {
			groupService.findAll().stream().forEach(s -> result.add(addLinks(s)));
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while retrieving a list of GroupDTOs, HttpStatus={}, ErrorMessage={}",
					HttpStatus.NOT_FOUND, "None of Groups is found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "None of Groups is found", e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace(
					"DataAreNotUpdatedException while retrieving a list of GroupDTOs, HttpStatus={}, ErrorMessage={}",
					HttpStatus.INTERNAL_SERVER_ERROR, "A list of groups wasn't prepared");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "A list of groups wasn't prepared", ex);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<GroupDTO> create(@RequestBody GroupDTO newDTO) {
		GroupDTO groupDTO;
		try {
			groupDTO = groupService.create(newDTO);
		} catch (DataAreNotUpdatedException | DataNotFoundException ex) {
			LOGGER.trace("DataAreNotUpdatedException while adding a GroupDTO to DB, HttpStatus={}, ErrorMessage={}",
					HttpStatus.INTERNAL_SERVER_ERROR, "Group wasn't added");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Group wasn't added", ex);
		}
		return new ResponseEntity<>(groupDTO, HttpStatus.CREATED);
	}

	@DeleteMapping(PATH_ID)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		try {
			groupService.delete(groupService.retrieve(id));
		} catch (DataNotFoundException e) {
			LOGGER.trace("DataNotFoundException while deleting DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, e);
		} catch (DataAreNotUpdatedException ex) {
			LOGGER.trace("DataAreNotUpdatedException while deleting DTO with id={}, HttpStatus={}, ErrorMessage={}", id,
					HttpStatus.INTERNAL_SERVER_ERROR, "Group wasn't deleted");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Group wasn't deleted", ex);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private GroupDTO addLinks(GroupDTO groupDTO) {
		LOGGER.trace("Going to add links for GroupDTO with id={}", groupDTO.getId());
		groupDTO.add(linkTo(methodOn(GroupsRestController.class).retrieve(groupDTO.getId())).withSelfRel());
		groupDTO.add(linkTo(methodOn(GroupsRestController.class).update(groupDTO.getId(), groupDTO)).withRel("update"));
		groupDTO.add(linkTo(methodOn(GroupsRestController.class).delete(groupDTO.getId())).withRel("delete"));
		groupDTO.add(linkTo(methodOn(GroupsRestController.class).findAll()).withRel("groups"));
		groupDTO.add(linkTo(methodOn(GroupsRestController.class).findAllLessonsForGroup(groupDTO.getId()))
				.withRel("lessonsForGroup"));
		LOGGER.trace("The links for GroupDTO with id={} were added successfully", groupDTO.getId());
		List<StudentDTO> list = new ArrayList<>();
		LOGGER.trace("Going to add links for the students of GroupDTO with id={}", groupDTO.getId());
		groupDTO.getListOfStudent().stream().forEach(studentDTO -> {
			studentDTO.add(linkTo(methodOn(StudentsRestController.class).retrieve(studentDTO.getId())).withSelfRel());
			studentDTO.add(linkTo(methodOn(StudentsRestController.class).update(studentDTO.getId(), studentDTO))
					.withRel("update"));
			studentDTO.add(linkTo(methodOn(StudentsRestController.class).delete(studentDTO.getId())).withRel("delete"));
			studentDTO.add(linkTo(methodOn(StudentsRestController.class).findAll()).withRel("students"));
			list.add(studentDTO);
		});
		groupDTO.setListOfStudent(list);
		LOGGER.trace("The links for the students of GroupDTO with id={} were added successfully", groupDTO.getId());
		return groupDTO;
	}

	private LessonDTO addLinksForLessonDTO(LessonDTO lessonDTO) {
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