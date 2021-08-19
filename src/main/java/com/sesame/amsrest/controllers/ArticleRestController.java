package com.sesame.amsrest.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sesame.amsrest.Repositories.ArticleRepository;
import com.sesame.amsrest.Repositories.ProviderRepository;
import com.sesame.amsrest.entities.Article;

@RestController 
@RequestMapping({"/articles"}) 
@CrossOrigin(origins = "*")
public class ArticleRestController {

private final ArticleRepository articleRepository;
private final ProviderRepository providerRepository;

@Autowired
public ArticleRestController(ArticleRepository articleRepository,
ProviderRepository providerRepository) { 
	this.articleRepository = articleRepository; 
	this.providerRepository = providerRepository;
}

@GetMapping("/list")
public List<Article> getAllArticles() {
	return (List<Article>) articleRepository.findAll(); 
	}

@PostMapping("/add/{providerId}")
Article createArticle(@PathVariable (value = "providerId") Long providerId,@Valid @RequestBody Article article) {
return providerRepository.findById(providerId).map(provider ->{
	article.setProvider(provider);
	return articleRepository.save(article);
	}).orElseThrow(() -> new RuntimeException("ProviderId" + providerId + " not found"));
}

	@PutMapping("/update/{providerId}/{articleId}")
	public Article updateArticle(@PathVariable (value = "providerId") Long providerId,
									@PathVariable (value = "articleId") Long articleId,
									@Valid @RequestBody Article articleRequest) { 
 if(!providerRepository.existsById(providerId)) {
	 throw new RuntimeException("ProviderId " + providerId + " not found");
 }
 return articleRepository.findById(articleId).map(article -> { article.setPrice(articleRequest.getPrice());
 article.setLabel(articleRequest.getLabel());
 article.setLabel(articleRequest.getPicture());
 return articleRepository.save(article);}).orElseThrow(() -> new RuntimeException("ArticleId " +
		 articleId + "not found")); }

	@DeleteMapping("/delete/{articleId}")
	public ResponseEntity<?> deleteArticle(@PathVariable (value = "articleId") Long articleId) {
		return articleRepository.findById(articleId).map(article -> { articleRepository.delete(article);
		return ResponseEntity.ok().build();}).orElseThrow(() -> new RuntimeException("Article not found with id " + articleId));
	}
}

