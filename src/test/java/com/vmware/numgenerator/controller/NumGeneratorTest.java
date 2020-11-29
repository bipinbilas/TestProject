package com.vmware.numgenerator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.numgenerator.models.Request;
import com.vmware.numgenerator.models.Result;
import com.vmware.numgenerator.models.Task;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.vmware.numgenerator.models.Status.SUCCESS;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NumGeneratorTest {
    @Autowired
    protected MockMvc mvc;

    @Test
    @SneakyThrows
    public void numGenerateTest(){
        Request requestModel = new Request();
        requestModel.setGoal("10");
        requestModel.setStep("2");
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(requestModel);

        //Generate Numbers
        String generateResponse = mvc.perform(post("/api/generate").contentType(APPLICATION_JSON).content(content)).andExpect(status().isOk()).andExpect(jsonPath(
                "$.task", notNullValue())).andReturn().getResponse().getContentAsString();
        Task task = fromResponse(generateResponse, Task.class);

        //Get num list
        String numListResponse = mvc.perform(get("/api/tasks/"+task.getTask()+"?action=get_numlist"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Result actualResult = fromResponse(numListResponse, Result.class);

        Result expectedResult = new Result();
        expectedResult.setResult("10,8,6,4,2,0");

        assertEquals(expectedResult.getResult(), actualResult.getResult());

        String statusResponse = mvc.perform(get("/api/tasks/"+task.getTask()+"/status")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Result statusResult = fromResponse(statusResponse, Result.class);
        assertEquals(SUCCESS.getValue(), statusResult.getResult());

    }


    @Test
    @SneakyThrows
    public void numBulkGenerateTest(){
        List<Request> requestList = new ArrayList<>();
        Request requestModel1 = new Request();
        requestModel1.setGoal("10");
        requestModel1.setStep("2");
        requestList.add(requestModel1);

        Request requestModel2 = new Request();
        requestModel2.setGoal("10");
        requestModel2.setStep("3");
        requestList.add(requestModel2);

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(requestList);

        //Generate bulk numbers
        String generateResponse = mvc.perform(post("/api/bulkGenerate").contentType(APPLICATION_JSON).content(content)).andExpect(status().isOk()).andExpect(jsonPath(
                "$.task", notNullValue())).andReturn().getResponse().getContentAsString();
        Task task = fromResponse(generateResponse, Task.class);

        //Get num list
        mvc.perform(get("/api/tasks/"+task.getTask()+"?action=get_numlist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", notNullValue()));
    }

    @SneakyThrows
    public static <T> T fromResponse(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
