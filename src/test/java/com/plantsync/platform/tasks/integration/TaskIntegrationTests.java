package com.plantsync.platform.tasks.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantsync.platform.tasks.infrastructure.persistence.jpa.repositories.TaskRepository;
import com.plantsync.platform.tasks.interfaces.rest.resources.CreateTaskResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void createTask_ShouldReturnCreatedAndPersistInDatabase() throws Exception {

        // Arrange
        CreateTaskResource resource = new CreateTaskResource(
                "Watering",
                "2025-07-03",
                1L,
                1L,
                false
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.action").value("Watering"))
                .andExpect(jsonPath("$.completed").value(false));

        // Verify database state
        assertThat(taskRepository.count()).isEqualTo(1);

        var task = taskRepository.findAll().get(0);

        assertThat(task.getAction()).isEqualTo("Watering");
    }

    @Test
    @WithMockUser
    void getAllTasks_ShouldReturnTasksList() throws Exception {

        // Arrange
        CreateTaskResource resource = new CreateTaskResource(
                "Fertilizing",
                "2025-08-03",
                1L,
                1L,
                false
        );

        // Create a task first
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].action").value("Fertilizing"));
    }
}