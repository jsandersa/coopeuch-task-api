package cl.sanders.coopeuchtaskapi;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import cl.sanders.coopeuchtaskapi.controller.TareaRestController;
import cl.sanders.coopeuchtaskapi.model.Tarea;
import cl.sanders.coopeuchtaskapi.repositories.TareaRepository;

@RunWith(SpringRunner.class) 
@WebMvcTest
@AutoConfigureMockMvc
class CoopeuchTaskApiApplicationTest {

    @MockBean
    private TareaRepository tareaRepository;
    
    @Autowired
    TareaRestController tareaController;

    @Autowired
    private MockMvc mockMvc;

	@Test
    public void saveValidTarea() throws Exception {
		Tarea newTarea = new Tarea();
		newTarea.setDescripcion("Una tarea Ok!");

		Mockito.when(tareaRepository.save(Mockito.any(Tarea.class))).thenReturn(newTarea);

        String tarea = "{\"descripcion\": \"Una tarea Ok!\", \"vigente\" : true}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tareas")
          .content(tarea)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.descripcion", Is.is("Una tarea Ok!")))
          .andExpect(MockMvcResultMatchers.content()
             .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void saveInvalidTarea() throws Exception {
        String tarea = "{\"descripcion\": \"\", \"vigente\" : false}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tareas")
          .content(tarea)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.descripcion", Is.is("Campo requiere un valor no vacio.")))
          .andExpect(MockMvcResultMatchers.content()
            .contentType(MediaType.APPLICATION_JSON));
     }


	@Test
    public void findTareaById_Existing() throws Exception {
		Tarea newTarea = new Tarea();
		newTarea.setDescripcion("Una tarea Ok!");

		Mockito.when(tareaRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(newTarea));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tareas/1"))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.descripcion", Is.is("Una tarea Ok!")))
          .andExpect(MockMvcResultMatchers.content()
             .contentType(MediaType.APPLICATION_JSON));
    }

	@Test
    public void findTareaById_NotFound() throws Exception {
		Mockito.when(tareaRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tareas/1"))
          .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

	@Test
    public void findAllTareas_Empty() throws Exception {
		Mockito.when(tareaRepository.findAll(Mockito.any(Sort.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tareas"))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Is.is(0)))
          .andExpect(MockMvcResultMatchers.content()
       		.contentType(MediaType.APPLICATION_JSON));
    }

	@Test
    public void findAllTareas_NotEmpty() throws Exception {
		Tarea unaTarea = new Tarea();
		unaTarea.setIdentificador(1L);
		unaTarea.setDescripcion("Hola");
		unaTarea.setVigente(true);
		unaTarea.setFechaCreacion(new Date());

		List<Tarea> found = Collections.singletonList(unaTarea);
		Mockito.when(tareaRepository.findAll(Mockito.any(Sort.class))).thenReturn(found);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tareas"))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Is.is(1)))
          .andExpect(MockMvcResultMatchers.jsonPath("$[0].descripcion", Is.is("Hola")))
          .andExpect(MockMvcResultMatchers.jsonPath("$[0].identificador", Is.is(1)))
          .andExpect(MockMvcResultMatchers.content()
       		.contentType(MediaType.APPLICATION_JSON));
    }

}
