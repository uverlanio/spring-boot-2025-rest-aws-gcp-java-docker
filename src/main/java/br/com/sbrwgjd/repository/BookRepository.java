package br.com.sbrwgjd.repository;

import br.com.sbrwgjd.model.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {

    @Query("SELECT b FROM Books b WHERE b.title LIKE LOWER(CONCAT ('%', :title,'%'))")
    Page<Books> findBookByTitle(@Param("title") String title, Pageable pageable);
}
