package com.vmware.numgenerator.controller;


import com.vmware.numgenerator.business.NumGeneratorService;
import com.vmware.numgenerator.models.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller responsible for generating the number sequences and maintaining the status.
 */
@RestController
@RequestMapping("/api")
public class NumGeneratorController {

    @Autowired
    private NumGeneratorService service;
    /**
     * Generate number sequences.
     *
     * @param numGenerateRequest Num Generator Request
     * @return UUID as result
     */
    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestBody Request numGenerateRequest) {
        return ok(service.generate(numGenerateRequest));
    }

    /**
     * Generate bulk number sequences.
     *
     * @param requests Num Generator requests.
     * @return UUID as result
     */
    @PostMapping("/bulkGenerate")
    public ResponseEntity<?> bulkGenerate(@RequestBody List<Request> requests) {
        return ok(service.bulkGenerate(requests));
    }

    /**
     * Maintain status of the generated number sequences.
     *
     * @param taskUuid Task UUID
     * @return Status
     */
    @GetMapping("/tasks/{taskUuid}/status")
    public ResponseEntity<?> getTaskStatus(@PathVariable String taskUuid){
        return ok(service.getStatus(taskUuid));
    }

    /**
     * Get number list.
     *
     * @param taskUuid Task Uuid
     * @param action Action {get_numlist}
     * @return Get Num List
     */
    @GetMapping("/tasks/{taskUuid}")
    public ResponseEntity<?> getNumList(@PathVariable String taskUuid, @RequestParam String action) {
        return ok(service.getNumList(taskUuid));
    }
}
