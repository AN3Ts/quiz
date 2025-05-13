package com.example.quizz.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.quizz.domain.Category;
import com.example.quizz.domain.CategoryRepository;
import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoriesApiControllerTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        categoryRepository.deleteAll();
        quizRepository.deleteAll();
    }

    @Test
    void saveThreeCategoriesReturnSizeOfCategoriesList() throws Exception {
        Category category1 = new Category();
        category1.setName("Test Category 1");
        category1.setDescription("Test Description 1");

        Category category2 = new Category();
        category2.setName("Test Category 2");
        category2.setDescription("Test Description 2");

        Category category3 = new Category();
        category3.setName("Test Category 3");
        category3.setDescription("Test Description 3");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        mockMvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$", hasSize(3))));

    }

    @Test
    void getQuizzesByCategoryIdReturnsEmptyListWhenCategoryDoesNotHaveQuizzes() throws Exception {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Description");
        Category savedCategory = categoryRepository.save(category);
        Long categoryId = savedCategory.getId();

        mockMvc.perform(get("/api/categories/" + categoryId + "/quizzes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    void getQuizzesByCategoryIdReturnsSizeOfQuizzesList() throws Exception {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Description");
        Category savedCategory = categoryRepository.save(category);
        Long categoryId = savedCategory.getId();

        Quiz quiz1 = new Quiz();
        quiz1.setName("Quiz 1");
        quiz1.setDescription("Quiz1 Description");
        quiz1.setPublished(true);
        quiz1.setCategory(savedCategory);

        Quiz quiz2 = new Quiz();
        quiz2.setName("Quiz 2");
        quiz2.setDescription("Quiz2 Description");
        quiz2.setPublished(true);
        quiz2.setCategory(savedCategory);

        Quiz quiz3 = new Quiz();
        quiz3.setName("Quiz 3");
        quiz3.setDescription("Quiz3 Description");
        quiz3.setPublished(true);
        quiz3.setCategory(savedCategory);

        quizRepository.saveAll(List.of(quiz1, quiz2, quiz3));

        mockMvc.perform(get("/api/categories/" + categoryId + "/quizzes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void getQuizzesByCategoryIdWithOneUnpublishQuizReturnSizeOfQuizzedList() throws Exception {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Description");
        Category savedCategory = categoryRepository.save(category);
        Long categoryId = savedCategory.getId();

        Quiz quiz1 = new Quiz();
        quiz1.setName("Quiz 1");
        quiz1.setDescription("Quiz1 Description");
        quiz1.setPublished(true);
        quiz1.setCategory(savedCategory);

        Quiz quiz2 = new Quiz();
        quiz2.setName("Quiz 2");
        quiz2.setDescription("Quiz2 Description");
        quiz2.setPublished(false);
        quiz2.setCategory(savedCategory);

        Quiz quiz3 = new Quiz();
        quiz3.setName("Quiz 3");
        quiz3.setDescription("Quiz3 Description");
        quiz3.setPublished(true);
        quiz3.setCategory(savedCategory);

        quizRepository.saveAll(List.of(quiz1, quiz2, quiz3));

        mockMvc.perform(get("/api/categories/" + categoryId + "/quizzes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
