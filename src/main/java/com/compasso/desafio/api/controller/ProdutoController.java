package com.compasso.desafio.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.compasso.desafio.dao.ProdutoDAO;
import com.compasso.desafio.model.Produto;
import com.compasso.desafio.model.ProdutoInput;
import com.compasso.desafio.repository.ProdutoRepository;
import com.compasso.desafio.service.CadastroProdutoService;


@RestController
@RequestMapping("/products")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CadastroProdutoService cadastroProduto;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProdutoDAO produtoDao;

	// listar produtos
	@GetMapping
	public List<Produto> listar() {
		return produtoRepository.findAll();
	}

	// search produtos
	@GetMapping("/search")
	public List<Produto> search(@RequestParam(value="q", required=false) String q,
			@RequestParam(value="min_price", required=false) BigDecimal min_price, 
			@RequestParam(value="max_price", required=false) BigDecimal max_price) {
		return produtoDao.searchProduto(q, min_price, max_price);
	}
	
	// buscar produtos
	@GetMapping("/{produtoId}")
	public ResponseEntity<Produto> buscar(@PathVariable String produtoId) {
		Optional<Produto> produto = produtoRepository.findById(produtoId);
		if (produto.isPresent()) {
			return ResponseEntity.ok(produto.get());
		}
		return ResponseEntity.notFound().build();
	}

	// adicionar produto
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Produto adicionar(@Valid @RequestBody ProdutoInput produtoInput) {
		Produto produto = toEntity(produtoInput);
		return cadastroProduto.salvar(produto);
	}

	// atualizar produto
	@PutMapping("/{produtoId}")
	public ResponseEntity<Produto> atualizar(@Valid @PathVariable String produtoId,
			@Valid @RequestBody ProdutoInput produtoInput) {

		if (!produtoRepository.existsById(produtoId)) {
			return ResponseEntity.notFound().build();
		}
		Produto produto = toEntity(produtoInput);
		produto.setId(produtoId);
		produto = cadastroProduto.atualizar(produto);
		return ResponseEntity.ok(produto);

	}

	// deletar produto
	@DeleteMapping("/{produtoId}")
	public ResponseEntity<Void> remover(@PathVariable String produtoId) {

		if (!produtoRepository.existsById(produtoId)) {
			return ResponseEntity.notFound().build();
		}
		cadastroProduto.excluir(produtoId);
		return ResponseEntity.ok().build();

	}

	private Produto toEntity(ProdutoInput produtoInput) {
		return modelMapper.map(produtoInput, Produto.class);
	}
}
