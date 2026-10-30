package escola.service;

import escola.model.Sala;
import escola.repository.SalaRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class SalaService {

    private final SalaRepository repository;

    public SalaService(SalaRepository repository) {
        this.repository = repository;
    }

    public List<Sala> listar() {
        return repository.findAll();
    }

    public Sala buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sala não encontrada"));
    }

    public Sala cadastrar(Sala sala) {

        if (repository.existsByCodigo(sala.getCodigo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Código já cadastrado");
        }

        sala.setReservada(false);

        return repository.save(sala);
    }

    public Sala reservar(Long id) {
        Sala sala = buscar(id);

        if (sala.isReservada()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Sala já reservada");
        }

        sala.setReservada(true);
        return repository.save(sala);
    }

    public Sala liberar(Long id) {
        Sala sala = buscar(id);

        if (!sala.isReservada()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Sala não está reservada");
        }

        sala.setReservada(false);
        return repository.save(sala);
    }

    public void excluir(Long id) {
        Sala sala = buscar(id);

        if (sala.isReservada()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir uma sala reservada");
        }

        repository.delete(sala);
    }
}