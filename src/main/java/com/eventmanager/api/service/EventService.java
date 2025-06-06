package com.eventmanager.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.eventmanager.api.domain.coupon.Coupon;
import com.eventmanager.api.domain.event.Event;
import com.eventmanager.api.domain.event.EventDetailsDTO;
import com.eventmanager.api.domain.event.EventRequestDTO;
import com.eventmanager.api.domain.event.EventResponseDTO;
import com.eventmanager.api.repositories.EventRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Value("${aws.bucketName}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private EventRepository repository;

    @Autowired
    private AddresService addresService;

    @Autowired
    private CouponService couponService;

    public Event createEvent(EventRequestDTO data){
        String imgUrl = null;

        if(data.image() != null){
            imgUrl = this.uploadImg(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());

        repository.save(newEvent);

        if (!data.remote()){
            this.addresService.createAddres(data, newEvent);
        }

        return newEvent;
    }

    public List<EventResponseDTO> getUpcomingEvents  (int page, int size){
        // PAGINAÇÃO DOS EVENTS
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = this.repository.findUpcomingEvents(new Date(), pageable);
        return eventPage.map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getAddres() != null ? event.getAddres().getCity() : "",
                        event.getAddres() != null ? event.getAddres().getUf() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl()))
                .stream().toList();
    }

    public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String uf, Date startDate, Date endDate) {

        title = (title != null) ? title : "";
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : new Date();
        endDate = (endDate != null)
                ? endDate
                : Date.from(LocalDate.now().plusYears(10).atStartOfDay(ZoneId.systemDefault()).toInstant());

        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = this.repository.findFilteredEvents(title, city, uf, startDate, endDate, pageable);
        return eventsPage.map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getAddres() != null ? event.getAddres().getCity() : "",
                        event.getAddres() != null ? event.getAddres().getUf() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl()))
                .stream().toList();
    }

    private String uploadImg(MultipartFile multipartFile){
        // Gerar um nome unico para o filename
        String filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            File file = this.convertMultipartToFile(multipartFile);
            s3Client.putObject(bucketName, filename, file);
            file.delete();
            return s3Client.getUrl(bucketName, filename).toString();
        }catch (Exception e){
            System.err.println("Erro ao subir arquivo. Detalhes:");
            e.printStackTrace();
            return "";
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    public EventDetailsDTO getEventDetails(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        List<Coupon> coupons = couponService.consultCoupons(eventId, new Date());

        List<EventDetailsDTO.CouponDTO> couponDTOS = coupons.stream()
                .map(coupon -> new EventDetailsDTO.CouponDTO(
                        coupon.getCode(),
                        coupon.getDiscount(),
                        coupon.getValid())).collect(Collectors.toList());

        return new EventDetailsDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddres() != null ? event.getAddres().getCity() : "",
                event.getAddres() != null ? event.getAddres().getUf() : "",
                event.getImgUrl(),
                event.getEventUrl(),
                couponDTOS
        );

    }
}
