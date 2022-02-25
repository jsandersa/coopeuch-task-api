package cl.sanders.coopeuchtaskapi.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import cl.sanders.coopeuchtaskapi.model.Tarea;
import cl.sanders.coopeuchtaskapi.repositories.TareaRepository;

@RestController
@RequestMapping(value = "/api/v1/tareas")
@CrossOrigin("*")
public class TareaRestController {

	@Autowired
	private TareaRepository repository;
	
	@GetMapping()
	public List<Tarea> getAll() {
		return repository.findAll(Sort.by(Order.asc("identificador")));
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Tarea> find(@PathVariable Long id) {

		Optional<Tarea> tarea = repository.findById(id);
		if (tarea.isPresent()) {
			return new ResponseEntity<Tarea>(tarea.get(), HttpStatus.OK);
		}
		
		throw new CustomApiException(HttpStatus.NOT_FOUND, "Tarea no encontrada: " + id);
	}

	@PostMapping()
	public ResponseEntity<Tarea> save(@Valid @RequestBody Tarea tarea) {
		// FECHA DE CREACION
		tarea.setFechaCreacion(new Date());
		Tarea obj = repository.save(tarea);
		return new ResponseEntity<Tarea>(obj, HttpStatus.OK);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Tarea> save(@Valid @RequestBody Tarea tarea, @PathVariable long id) {

		Optional<Tarea> obj = repository.findById(id);

		if (!obj.isPresent())
			return ResponseEntity.notFound().build();

		tarea.setIdentificador(id);
		tarea.setFechaCreacion(obj.get().getFechaCreacion());
		
		repository.save(tarea);

		return new ResponseEntity<Tarea>(tarea, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Tarea> delete(@PathVariable Long id) {
		Optional<Tarea> tarea = repository.findById(id);

		if (!tarea.isPresent()) {
			return new ResponseEntity<Tarea>(HttpStatus.NO_CONTENT);
		}

		repository.delete(tarea.get());
		
		return new ResponseEntity<Tarea>(tarea.get(), HttpStatus.OK);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return errors;
	}
}

@SuppressWarnings("serial")
class CustomApiException extends ResponseStatusException {

	private String errorMessage;
	
	public CustomApiException(String message) {
		super(HttpStatus.BAD_REQUEST);
		errorMessage = message;
	}
	
	public CustomApiException(HttpStatus status, String message) {
		super(status);
		errorMessage = message;
	}
	
	@Override
	public String getMessage() {
		return errorMessage;
	}
}
