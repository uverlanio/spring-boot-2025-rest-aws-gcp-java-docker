package br.com.sbrwgjd.repository;

import br.com.sbrwgjd.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {
}
