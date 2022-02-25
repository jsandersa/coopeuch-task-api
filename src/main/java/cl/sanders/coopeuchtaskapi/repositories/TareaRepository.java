package cl.sanders.coopeuchtaskapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sanders.coopeuchtaskapi.model.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

}
