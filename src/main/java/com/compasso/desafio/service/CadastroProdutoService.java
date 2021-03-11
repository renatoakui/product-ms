package com.compasso.desafio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compasso.desafio.model.Produto;
import com.compasso.desafio.repository.ProdutoRepository;

@Service
public class CadastroProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Produto salvar (Produto produto) {
		Integer abc = produto.hashCode();
		produto.setId(abc.toString());
		return produtoRepository.save(produto);
	}
	
	public Produto atualizar (Produto produto) {
		return produtoRepository.save(produto);
	}
	
	public void excluir (String produtoId) {
		produtoRepository.deleteById(produtoId);
	}
	
}
