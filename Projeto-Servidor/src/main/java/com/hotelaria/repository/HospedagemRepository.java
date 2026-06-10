package com.hotelaria.repository;

import com.hotelaria.model.Hospedagem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HospedagemRepository {

    private final Map<Long, Hospedagem> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Hospedagem> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<Hospedagem> findById(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(storage.get(id));
    }

    public Hospedagem save(Hospedagem hospedagem) {
        if (hospedagem == null) {
            throw new IllegalArgumentException("Hospedagem não pode ser nula");
        }
        if (hospedagem.getId() == null) {
            hospedagem.setId(idGenerator.getAndIncrement());
        }
        storage.put(hospedagem.getId(), hospedagem);
        return hospedagem;
    }

    public void clear() {
        storage.clear();
        idGenerator.set(1);
    }
}
