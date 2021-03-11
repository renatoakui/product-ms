package com.compasso.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compasso.desafio.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, String>{

	
}
