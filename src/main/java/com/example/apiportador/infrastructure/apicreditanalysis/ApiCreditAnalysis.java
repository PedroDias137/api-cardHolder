package com.example.apiportador.infrastructure.apicreditanalysis;


import com.example.apiportador.infrastructure.apicreditanalysis.dto.Credit;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "apiCreditAnalysis", url = "localhost:8081/analysis")
public interface ApiCreditAnalysis {


    @GetMapping()
    List<Credit> getAnalysis(@RequestParam(value = "uuid", required = false) String uuid);

    @GetMapping("/{id}")
    Credit getAnalysiId(@PathVariable UUID id);
}
