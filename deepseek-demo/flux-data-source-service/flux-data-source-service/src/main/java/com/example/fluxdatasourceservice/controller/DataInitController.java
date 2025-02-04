package com.example.fluxdatasourceservice.controller;

import com.example.fluxdatasourceservice.model.MyObj;
import com.example.fluxdatasourceservice.repository.MyObjRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@RestController
public class DataInitController {
    private final MyObjRepository repository;
    private static final String[] NAMES = {"Alice", "Bob", "Charlie", "Diana"};
    private static final int TOTAL = 20_000;
    private static final int PER_NAME_COUNT = TOTAL / NAMES.length;

    public DataInitController(MyObjRepository repository) {
        this.repository = repository;
    }

    @Operation(
            summary = "初始化測試數據",
            description = "插入20,000條測試數據到數據庫，分屬4個不同名稱",
            responses = {
                    @ApiResponse(responseCode = "200", description = "數據初始化成功"),
                    @ApiResponse(responseCode = "400", description = "數據已存在且未使用force參數")
            }
    )
    @PostMapping("/api/init-data")
    @Transactional
    @Async
    public CompletableFuture<ResponseEntity<String>> initializeData(@RequestParam(defaultValue = "false") boolean force) {

        if (!force && repository.count() > 0) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Data already exists. Use force=true to overwrite"));
        }

        repository.deleteAll(); // 如果强制初始化则清空数据

        IntStream.range(0, NAMES.length).parallel().forEach(i -> {
            String name = NAMES[i];
            IntStream.range(0, PER_NAME_COUNT).forEach(j -> {
                MyObj obj = new MyObj(name, generateContent(j));
                repository.save(obj);
            });
        });

        return CompletableFuture.completedFuture(ResponseEntity.ok("Inserted " + TOTAL + " records successfully"));
    }

    private String generateContent(int index) {
        return "{data: '" + RandomStringUtils.randomAlphanumeric(9500) + "'}";
    }
}