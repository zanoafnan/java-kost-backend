package com.kost.kostapi.service;

import com.kost.kostapi.dto.kost.CreateKostRequest;
import com.kost.kostapi.dto.kost.KostResponse;
import com.kost.kostapi.dto.kost.SearchKostRequest;
import com.kost.kostapi.dto.kost.UpdateKostRequest;
import com.kost.kostapi.entity.Kost;
import com.kost.kostapi.entity.User;
import com.kost.kostapi.repository.KostRepository;
import com.kost.kostapi.specification.KostSpecification;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class KostService {

        private final KostRepository kostRepository;

        private Kost getOwnedKost(
                        Long id,
                        User owner) {

                Kost kost = kostRepository
                                .findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Kost not found"));
                if (!kost.getOwner().getId().equals(owner.getId())) {
                        throw new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "You do not own this kost.");
                }

                return kost;
        }

        public KostResponse create(
                        User owner,
                        CreateKostRequest request) {

                Kost kost = Kost.builder()
                                .name(request.name())
                                .description(request.description())
                                .location(request.location())
                                .price(request.price())
                                .owner(owner)
                                .build();

                return KostMapper.toResponse(
                                kostRepository.save(kost));
        }

        public Page<KostResponse> ownerKosts(
                        User owner,
                        int page,
                        int size) {

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                Sort.by("id").descending());

                return kostRepository
                                .findByOwner(owner, pageable)
                                .map(KostMapper::toResponse);
        }

        public KostResponse findById(
                        Long id) {

                Kost kost = kostRepository
                                .findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Kost not found"));

                return KostMapper.toResponse(kost);
        }

        public KostResponse update(
                        User owner,
                        Long id,
                        UpdateKostRequest request) {

                Kost kost = getOwnedKost(
                                id,
                                owner);

                if (request.name() != null) {
                        kost.setName(request.name());
                }

                if (request.description() != null) {
                        kost.setDescription(request.description());
                }

                if (request.location() != null) {
                        kost.setLocation(request.location());
                }

                if (request.price() != null) {
                        kost.setPrice(request.price());
                }

                return KostMapper.toResponse(
                                kostRepository.save(kost));
        }

        public void delete(
                        User owner,
                        Long id) {

                Kost kost = getOwnedKost(
                                id,
                                owner);

                kostRepository.delete(kost);
        }

        public Page<KostResponse> search(
                        SearchKostRequest request,
                        int page,
                        int size) {

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                Sort.by(
                                                "desc".equalsIgnoreCase(request.sort())
                                                                ? Sort.Direction.DESC
                                                                : Sort.Direction.ASC,
                                                "price"));

                return kostRepository
                                .findAll(
                                                KostSpecification.search(request),
                                                pageable)
                                .map(KostMapper::toResponse);
        }
}