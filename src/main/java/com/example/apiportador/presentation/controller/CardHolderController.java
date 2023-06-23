package com.example.apiportador.presentation.controller;


import com.example.apiportador.applicationservice.cardholderservice.CreateCardHolderService;
import com.example.apiportador.presentation.controller.request.CardHolderRequest;
import com.example.apiportador.presentation.controller.response.CardHolderResponse;
import com.example.apiportador.presentation.exception.ApiDownException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/card-holders")
@RequiredArgsConstructor
public class CardHolderController {

    private final CreateCardHolderService createCardHolderService;

    @PostMapping
    public ResponseEntity<CardHolderResponse> create(@RequestBody CardHolderRequest cardHolderRequest) throws ApiDownException {
        return ResponseEntity.created(null).body(createCardHolderService.create(cardHolderRequest));
    }

}
