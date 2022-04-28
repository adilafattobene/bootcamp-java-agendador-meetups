package com.bootcamp.microservicemeetup.controller.resource;

import com.bootcamp.microservicemeetup.controller.dto.MeetupDTO;
import com.bootcamp.microservicemeetup.controller.dto.MeetupFilterDTO;
import com.bootcamp.microservicemeetup.controller.dto.RegistrationDTO;
import com.bootcamp.microservicemeetup.model.entity.Meetup;
import com.bootcamp.microservicemeetup.model.entity.Registration;
import com.bootcamp.microservicemeetup.service.MeetupService;
import com.bootcamp.microservicemeetup.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meetups")
@RequiredArgsConstructor
public class MeetupController {

    private final MeetupService meetupService;
    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Integer create(@RequestBody MeetupDTO meetupDTO) {

        Meetup entity = Meetup.builder()
                .event(meetupDTO.getEvent())
                .meetupDate(meetupDTO.getDate().toString())
                .ownerId(meetupDTO.getOwnerId())
                .build();

        entity = meetupService.save(entity);
        return entity.getId();
    }

    @GetMapping
    public Page<MeetupDTO> find(MeetupFilterDTO dto, Pageable pageRequest) {
        Page<Meetup> result = meetupService.find(dto, pageRequest);

        List<MeetupDTO> meetups = result
                .getContent()
                .stream()
                .map(entity -> {
                    List<Registration> registrations = entity.getRegistrations();

                    List<RegistrationDTO> registrationDTOS = registrations.stream()
                            .map(registration -> modelMapper.map(registration, RegistrationDTO.class))
                            .collect(Collectors.toList());

                    MeetupDTO meetupDTO = modelMapper.map(entity, MeetupDTO.class);
                    meetupDTO.setRegistrations(registrationDTOS);

                    return meetupDTO;
                }).collect(Collectors.toList());

        return new PageImpl<MeetupDTO>(meetups, pageRequest, result.getTotalElements());
    }

    // ENDPOINT - alteração do meetup
    // verificar se o meetup existe
    // - // nos testes criar um teste que o meetup existe
    // - // nos testes criar um teste que o meetup NÃO existe


    // ENDPOINT - deleção do meetup
}
