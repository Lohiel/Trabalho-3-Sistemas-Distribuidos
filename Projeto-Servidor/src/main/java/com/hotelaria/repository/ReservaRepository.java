package com.hotelaria.repository;

import com.hotelaria.model.Reserva;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReservaRepository {

    private final Map<Long, Reserva> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Reserva> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<Reserva> findById(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(storage.get(id));
    }

    public Reserva save(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva não pode ser nula");
        }
        if (reserva.getId() == null) {
            reserva.setId(idGenerator.getAndIncrement());
        }
        storage.put(reserva.getId(), reserva);
        return reserva;
    }

    public void clear() {
        storage.clear();
        idGenerator.set(1);
    }
}
