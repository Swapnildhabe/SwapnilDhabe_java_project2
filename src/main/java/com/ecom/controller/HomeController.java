package com.ecom.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.UserDetails;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;

@Controller
public class HomeController {
	@Autowired
	private	CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	 
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/")
	public String index() 
	{
		
		return"index";
	}
	
	@GetMapping("/login")
	public String login() 
	{
		return"login";
	}
	
	@GetMapping("/register")
	public String register() 
	{
		return"register";
	}
	
	@GetMapping("/products")
	public String products(Model m , @RequestParam(value= "category", defaultValue = "") String category) 
	{
		List<Category> categories = categoryService.getAllActiveCategory();
		List<Product> products = productService.getAllActiveProducts(category);
		m.addAttribute("categories", categories);
		m.addAttribute("products", products);
		m.addAttribute("paramValue", category);
		return"product";
	}
	
	@GetMapping("/product/{id}")
	public String product(@PathVariable int id,Model m) 
	{	
		Product productById = productService.getProductById(id);
		m.addAttribute("product", productById);
		
		return"view_product";
	}
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDetails user, @RequestParam("img") MultipartFile file)
	{
		String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
		user.setProfileImage(imageName);
		userService.saveUser(user);
		UserDetails saveUser = userService.saveUser(user);
		if(!ObjectUtils.isEmpty(imageName))
		{
			user.setProfileImage(imageName);
			userService.saveUser(user);
			if(!file.isEmpty())
			{
//				File saveFile = new ClassPathResource("static/img").getFile();
//				
//				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"product_img"+File.separator+image.getOriginalFilename());
////					System.out.println(path);
//				Files.copy(image.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING );
			}
		}
		
		return "redirect:/register";
	}
}