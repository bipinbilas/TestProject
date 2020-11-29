package com.vmware.numgenerator.business;

import com.vmware.numgenerator.models.Request;
import com.vmware.numgenerator.models.Result;
import com.vmware.numgenerator.models.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static com.vmware.numgenerator.models.Status.*;

/**
 * Service class to handle number sequence.
 */
@Service
public class NumGeneratorService {

    public static Map<String, Result> NUM_SEQ_MAP = new ConcurrentHashMap<>();
    public static Map<String, String> NUM_SEQ_STATUS = new ConcurrentHashMap<>();

    /**
     * Generate task to generate the number sequences.
     *
     * @param request Request
     * @return Task
     */
    public Task generate(Request request) {
        String uuid = UUID.randomUUID().toString();
        Task task = new Task();
        task.setTask(uuid);
        CompletableFuture.runAsync(() -> {
            try {
                NUM_SEQ_STATUS.put(uuid, IN_PROGRESS.getValue());
                List<String> numList = generateNumSequence(request);
                Result result = new Result();
                result.setResult(String.join(",", numList));
                NUM_SEQ_MAP.put(uuid, result);
                NUM_SEQ_STATUS.put(uuid, SUCCESS.getValue());
            } catch (Exception e) {
                NUM_SEQ_STATUS.put(uuid, ERROR.getValue());
            }
        });
        return task;
    }

    /**
     * Bulk generator class to generate number sequences in bulk.
     *
     * @param requests List of requests
     * @return Task
     */
    public Task bulkGenerate(List<Request> requests) {
        String uuid = UUID.randomUUID().toString();
        Task task = new Task();
        task.setTask(uuid);
        CompletableFuture.runAsync(() -> {
            try {
                List<String> numlists = new ArrayList<>();
                NUM_SEQ_STATUS.put(uuid, IN_PROGRESS.getValue());
                for (Request request : requests) {
                    List<String> numList = generateNumSequence(request);
                    numlists.add(numList.toString());
                }
                Result result = new Result();
                result.setResult(numlists.toString());
                NUM_SEQ_MAP.put(uuid, result);
                NUM_SEQ_STATUS.put(uuid, SUCCESS.getValue());
            } catch (Exception e) {
                NUM_SEQ_STATUS.put(uuid, ERROR.getValue());
            }
        });
        return task;
    }

    /**
     * Get number sequence list.
     *
     * @param uuid uuid
     * @return Result
     */
    public Result getNumList(String uuid) {
        return NUM_SEQ_MAP.get(uuid);
    }

    /**
     * Get num generator status.
     *
     * @param uuid UUID
     * @return Result
     */
    public Result getStatus(String uuid) {
        String status = NUM_SEQ_STATUS.get(uuid);
        Result result = new Result();
        result.setResult(status);
        return result;
    }

    /**
     * Number Sequence generator.
     * @param request Request
     * @return List of num sequences
     */
    private List<String> generateNumSequence(Request request) {
        Integer goal = Integer.parseInt(request.getGoal());
        Integer step = Integer.parseInt(request.getStep());
        List<String> numList = new ArrayList<>();
        numList.add(String.valueOf(goal));
        while (goal > 0) {
            goal = goal - step;
            if (goal < 0) {
                numList.add("0");
                continue;
            }
            numList.add(String.valueOf(goal));
        }
        return numList;
    }
}
