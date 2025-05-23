package com.example.quizz.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.quizz.domain.Category;
import com.example.quizz.domain.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository; 

    @GetMapping("/showCategories")
    public String showCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories); 
        return "categories";
    }

    @GetMapping("/add")
    public String showAddCategoryForm() {
        return "addcategory";
    }
    
    @PostMapping
    public String createCategory(@ModelAttribute Category category) {
        categoryRepository.save(category); 
        return "redirect:/categories/showCategories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, Model model) {
        categoryRepository.deleteById(id); 

        if (categoryRepository.existsById(id)) {
            model.addAttribute("message", "Delete failed"); 
            model.addAttribute("alertType", "danger"); 
        } else {
            model.addAttribute("message", "Delete successful");
            model.addAttribute("alertType", "success"); 
        }
        return "redirect:/categories/showCategories"; 
    }
}
