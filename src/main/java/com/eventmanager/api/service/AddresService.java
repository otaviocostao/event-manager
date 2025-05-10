package com.eventmanager.api.service;

import com.eventmanager.api.domain.addres.Addres;
import com.eventmanager.api.domain.event.Event;
import com.eventmanager.api.domain.event.EventRequestDTO;
import com.eventmanager.api.repositories.AddresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddresService {
    @Autowired
    private AddresRepository addresRepository;

    public Addres createAddres(EventRequestDTO data, Event event){
        Addres newAddres = new Addres();
        newAddres.setCity(data.city());
        newAddres.setUf(data.state());
        newAddres.setEvent(event);

        return addresRepository.save(newAddres);
    }
}
