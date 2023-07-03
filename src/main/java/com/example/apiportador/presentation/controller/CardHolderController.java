package com.example.apiportador.presentation.controller;


import com.example.apiportador.applicationservice.cardholderservice.CreateCardHolderService;
import com.example.apiportador.applicationservice.cardholderservice.SearchCardHolderService;
import com.example.apiportador.applicationservice.cardservice.CreateCardService;
import com.example.apiportador.applicationservice.cardservice.SearchCardService;
import com.example.apiportador.applicationservice.cardservice.UpdateCardService;
import com.example.apiportador.presentation.controller.request.CardHolderRequest;
import com.example.apiportador.presentation.controller.request.CardRequest;
import com.example.apiportador.presentation.controller.response.CardHolderResponse;
import com.example.apiportador.presentation.controller.response.CardResponse;
import com.example.apiportador.presentation.exception.ApiDownException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/card-holders")
@RequiredArgsConstructor
public class CardHolderController {


    private final CreateCardHolderService createCardHolderService;
    private final SearchCardHolderService searchCardHolderService;
    private final CreateCardService createCardService;
    private final SearchCardService searchCardService;
    private final UpdateCardService updateCardService;

    @PostMapping
    public ResponseEntity<CardHolderResponse> create(@RequestBody CardHolderRequest cardHolderRequest) throws ApiDownException {
        return ResponseEntity.created(null).body(createCardHolderService.create(cardHolderRequest));
    }

    @GetMapping
    public ResponseEntity<List<CardHolderResponse>> listAll(@RequestParam(value = "status", required = false) String status) {
        return ResponseEntity.ok(searchCardHolderService.find(status));
    }

    @GetMapping("{id}")
    public ResponseEntity<CardHolderResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(searchCardHolderService.findById(id));
    }

    @PostMapping("{cardHolderId}/cards")
    public ResponseEntity<CardResponse> createCard(@PathVariable String cardHolderId, @RequestBody CardRequest cardRequest) {

        return ResponseEntity.created(null).body(createCardService.create(cardHolderId, cardRequest));
    }

    @GetMapping("{cardHolderId}/cards")
    public ResponseEntity<List<CardResponse>> find(@PathVariable String cardHolderId, @RequestBody CardRequest cardRequest) {

        final List<CardResponse> cardResponses = searchCardService.findAll(cardHolderId);

        return cardResponses.isEmpty() ? ResponseEntity.status(204).body(cardResponses) : ResponseEntity.ok(cardResponses);
    }

    @GetMapping("{cardHolderId}/cards/{cardId}")
    public ResponseEntity<CardResponse> findById(@PathVariable String cardHolderId, @PathVariable String cardId) {

        return ResponseEntity.ok(searchCardService.findById(cardHolderId, cardId));
    }

    @PatchMapping("{cardHolderId}/cards/{cardId}")
    public ResponseEntity<CardResponse> UpdateCardLimit(@PathVariable String cardHolderId, @PathVariable String cardId, @RequestBody CardRequest cardRequest) {

        return ResponseEntity.ok(updateCardService.updateCardLimit(cardHolderId, cardId, cardRequest));
    }
}
