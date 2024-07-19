package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;

    static UserDTO manager;
    static ProjectDTO project;
    static String token;

    @BeforeAll
    static void setUp() {

        token = "Bearer " + "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJOY0xZTVZpUDBkN2NxVUFuS2VvYmo4aEFBQjBhSEJQRGtKQVJ0TV9zV1U4In0.eyJleHAiOjE3MjEzNzgyMTEsImlhdCI6MTcyMTM2MDIxMSwianRpIjoiMDJmYTdkOTUtOWUyNy00NzBiLTg0ZjQtYjZjNTY1MTc1MDhmIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL2N5ZGVvLWRldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIyNzNkNmFmZS05ZjIwLTRlYWQtOWQ0Mi0wODBhODEzZDIxOWIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0aWNrZXRpbmctYXBwIiwic2Vzc2lvbl9zdGF0ZSI6ImE4OGJiMTRkLTViY2EtNDY0NC1hMjA0LTYyMjQ1ZGMwMDNkMyIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImRlZmF1bHQtcm9sZXMtY3lkZW8tZGV2Il19LCJyZXNvdXJjZV9hY2Nlc3MiOnsidGlja2V0aW5nLWFwcCI6eyJyb2xlcyI6WyJNYW5hZ2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwic2lkIjoiYTg4YmIxNGQtNWJjYS00NjQ0LWEyMDQtNjIyNDVkYzAwM2QzIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInByZWZlcnJlZF91c2VybmFtZSI6Im96enkifQ.uGR_M-g0fkHTXsZKRMXb0kpT5or1HSQ9N2Ei9i6O9mBptYeiS4hrCUBsoM8lPU20ZwtJEZ60fIVaGf-wyjNUn2G6XqgKloq3UyWnjPpKyp_gnxXlpiNfPoPNPtCA9edYg1XYGaOBtOTV_eh7vVeCl5H8wZx3A84_4w5Jn45zoYHicTGfqIo2sDZRM7oEQMCWcccgGsWXuLBFQGxgNfZob9TcHBThwCXNwXRt56V7MerzDVztyBDRLOB-Q_t0GUMU11PdgkYShNVvymlOoTAkiLopYuzDv5B8i5zd6ZJsOpdfmy9x7jQwlkJz3UCQ_DEDvVYssBuBJYvnc82oS3m3CA";
        manager = new UserDTO(
                2L,
                "",
                "",
                "ozzy",
                "abc1",
                "abc1",
                true,
                "",
                new RoleDTO(2L, "Manager"),
                Gender.MALE);

        project = new ProjectDTO(
                "API Project",
                "PR001",
                manager,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "Some details",
                Status.OPEN
        );
    }

    @Test
    void givenNoToken_getProjects_Test() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenToken_getProjects_Test() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].projectCode").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.firstName").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.firstName").isNotEmpty())
                .andExpect(jsonPath("$.data[0].assignedManager.firstName").isString())
                .andExpect(jsonPath("$.data[0].assignedManager.firstName").value("Harold"));

    }
}