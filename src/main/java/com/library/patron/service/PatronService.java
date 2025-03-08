package com.library.patron.service;

import com.library.patron.model.Patron;
import com.library.patron.repository.PatronRepository;
import com.library.patron.request.PatronDTO;
import com.library.patron.response.PatronInfoResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatronService {

    private final PatronRepository patronRepository;

    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    public Patron getPatronById(Long id) {
        return patronRepository.findById(id).orElse(null);
    }

    public Patron addPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @Transactional
    public Patron updatePatron(Long id, Patron updatedPatron) {
        if (patronRepository.existsById(id)) {
            updatedPatron.setId(id);
            return patronRepository.save(updatedPatron);
        }
        return null;
    }

    public boolean deletePatron(Long id) {
        if (!patronRepository.existsById(id)) return false;
        else
            patronRepository.deleteById(id);
        return true;
    }
}
