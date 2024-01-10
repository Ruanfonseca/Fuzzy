package com.Analisador.fuzzy.repository;

import com.Analisador.fuzzy.model.Retorno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface RetornoRepository extends CrudRepository<Retorno,Long> {


}
