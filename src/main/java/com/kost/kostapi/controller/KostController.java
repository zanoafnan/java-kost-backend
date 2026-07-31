package com.kost.kostapi.controller;

import com.kost.kostapi.dto.kost.CreateKostRequest;
import com.kost.kostapi.dto.kost.KostResponse;
import com.kost.kostapi.dto.kost.SearchKostRequest;
import com.kost.kostapi.dto.kost.UpdateKostRequest;
import com.kost.kostapi.entity.User;
import com.kost.kostapi.service.KostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kosts")
@RequiredArgsConstructor
public class KostController {

        private final KostService kostService;

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public KostResponse create(
                        @AuthenticationPrincipal User user,
                        @Valid @RequestBody CreateKostRequest request) {
                return kostService.create(
                                user,
                                request);
        }

        @GetMapping("/owner")
        public Page<KostResponse> ownerKosts(
                        @AuthenticationPrincipal User user,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                return kostService.ownerKosts(
                                user,
                                page,
                                size);
        }

        @GetMapping
        public Page<KostResponse> search(

                        @RequestParam(required = false) String name,
                        @RequestParam(required = false) String location,
                        @RequestParam(required = false) java.math.BigDecimal minPrice,
                        @RequestParam(required = false) java.math.BigDecimal maxPrice,
                        @RequestParam(defaultValue = "asc") String sort,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                return kostService.search(
                                new SearchKostRequest(
                                                name,
                                                location,
                                                minPrice,
                                                maxPrice,
                                                sort),
                                page,
                                size);
        }

        @GetMapping("/{id}")
        public KostResponse detail(
                        @PathVariable Long id) {
                return kostService.findById(id);
        }

        @PutMapping("/{id}")
        public KostResponse update(
                        @AuthenticationPrincipal User user,
                        @PathVariable Long id,
                        @Valid @RequestBody UpdateKostRequest request) {
                return kostService.update(user, id, request);
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void delete(
                        @AuthenticationPrincipal User user,
                        @PathVariable Long id) {
                kostService.delete(user, id);
        }
}