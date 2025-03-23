package com.library.patron.service;

import com.library.borrowing_record.Enum.BorrowingStatus;
import com.library.borrowing_record.model.BorrowingRecord;
import com.library.patron.model.Patron;
import com.library.patron.repository.PatronRepository;
import com.library.patron.request.PatronDTO;
import com.library.patron.response.PatronResponse;
import com.library.utils.exceptions.NotFoundException;
import com.library.utils.exceptions.RequestNotValidException;
import com.library.utils.mapper.ClassMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class PatronService {

    private final PatronRepository patronRepository;

    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public List<PatronResponse> getAllPatrons() {
        return patronRepository.findAll().stream().map(ClassMapper.INSTANCE::pentityToDto).collect(Collectors.toList());
    }

    public Patron getPatronById(Long id) {
        if (!patronRepository.existsById(id)) {
            throw new NotFoundException("There is no patron with id = " + id);
        }
        return patronRepository.findById(id).orElse(null);
    }

    public PatronResponse addPatron(PatronDTO request) {
        Patron patron = ClassMapper.INSTANCE.patronDtoToEntity(request);
         patronRepository.save(patron);
         return ClassMapper.INSTANCE.pentityToDto(patron);
    }

    @Transactional
    public PatronResponse updatePatron(Long id, PatronDTO updatedPatron) {
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
            return ClassMapper.INSTANCE.pentityToDto(patron);
        }
        throw new RequestNotValidException("There is no patron with id = " + id);
    }

    public boolean deletePatron(Long id) {
        if (!patronRepository.existsById(id)) return false;
        else {
            if (patronRepository.findById(id).get().getBorrowingRecords().size() > 0) {
                for (BorrowingRecord b : patronRepository.findById(id).get().getBorrowingRecords()) {
                    if (b.getStatus() == BorrowingStatus.Borrowed) {
                        throw new RequestNotValidException("You should return the borrowed book first");
                    }
                }
            }
            patronRepository.deleteById(id);
            return true;
        }
    }
}