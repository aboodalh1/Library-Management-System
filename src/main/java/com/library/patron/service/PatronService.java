package com.library.patron.service;

import com.library.patron.model.Patron;
import com.library.patron.repository.PatronRepository;
import com.library.patron.request.PatronDTO;
import com.library.patron.response.PatronInfoResponse;
import com.library.utils.exceptions.NotFoundException;
import com.library.utils.exceptions.RequestNotValidException;
import com.library.utils.mapper.ClassMapper;
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
        if(!patronRepository.existsById(id)){
            throw new NotFoundException("There is no patron with id = "+id);
        }
        return patronRepository.findById(id).orElse(null);
    }

    public Patron addPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @Transactional
    public PatronInfoResponse updatePatron(Long id, PatronDTO updatedPatron) {
        if (patronRepository.existsById(id)) {
            Patron patron = patronRepository.findById(id).orElse(null);
            Patron existedPhoneNumber = patronRepository.findByPhoneNumberAndIdIsNot(updatedPatron.getPhoneNumber(), id).orElse(null);
            if (existedPhoneNumber != null) {
                throw new RequestNotValidException("phone number already taken");
            }
            patron.setFirstName(updatedPatron.getFirstName());
            patron.setLastName(updatedPatron.getLastName());
            patron.setAddress(updatedPatron.getAddress());
            patron.setPhoneNumber(patron.getPhoneNumber());
            patronRepository.saveAndFlush(patron);
            return ClassMapper.INSTANCE.entityToDto(patron);

        }
        throw new RequestNotValidException("There is no patron with id = "+id);
    }

    public boolean deletePatron(Long id) {
        if (!patronRepository.existsById(id)) return false;
        else
            patronRepository.deleteById(id);
        return true;
    }
}
