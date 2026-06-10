package com.hotelaria.repository;

import com.hotelaria.model.Cliente;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ClienteRepository {

    private final Map<Long, Cliente> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Cliente> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<Cliente> findById(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(storage.get(id));
    }

    public Optional<Cliente> findByCpf(String cpf) {
        if (cpf == null) return Optional.empty();
        return storage.values().stream()
                .filter(c -> cpf.equals(c.getCpf()))
                .findFirst();
    }

    public Cliente save(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        if (cliente.getId() == null) {
            cliente.setId(idGenerator.getAndIncrement());
        }
        storage.put(cliente.getId(), cliente);
        return cliente;
    }

    public void clear() {
        storage.clear();
        idGenerator.set(1);
    }
}
