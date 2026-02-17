package br.com.sbrwgjd.repository;

import br.com.sbrwgjd.data.dto.PersonDTO;
import br.com.sbrwgjd.model.Person;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id = :id")
    void disablePerson(@Param("id") Long id);

    @Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT ('%', :firstName,'%'))")
    Page<Person> findPeopleByName(@Param("firstName") String firstName, Pageable pageable);

}
