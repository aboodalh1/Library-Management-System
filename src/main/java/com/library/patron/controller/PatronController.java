package com.library.patron.controller;
import com.library.patron.model.Patron;
import com.library.patron.request.PatronDTO;
import com.library.patron.service.PatronService;
import com.library.utils.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {
    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping
    public ApiResponse<List<Patron>> getAllPatrons() {

        return new ApiResponse<>(true,200, patronService.getAllPatrons());
    }

    @GetMapping("/{id}")
    public ApiResponse<Patron> getPatronById(@PathVariable Long id) {

        return new ApiResponse<>(true,200,patronService.getPatronById(id));
    }
    @PostMapping("/add_patron")
    public ResponseEntity<ApiResponse<Patron>> addPatron(@Valid @RequestBody PatronDTO patronDTO) {
        Patron patron = new Patron();

        patron.setFirstName(patronDTO.getFirstName());
        patron.setLastName(patronDTO.getLastName());
        patron.setAddress(patronDTO.getAddress());
        patron.setPhoneNumber(patronDTO.getPhoneNumber());

        Patron savedPatron = patronService.addPatron(patron);

        return ResponseEntity.ok(new ApiResponse<>(true,200,patronService.addPatron(savedPatron)));
    }
    @PutMapping("/{id}")
    public ApiResponse<Patron> updatePatron(@PathVariable Long id, @RequestBody Patron patron) {
        return new ApiResponse<>(true,200,patronService.updatePatron(id, patron));
    }
    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePatron(@PathVariable Long id)
    {
        boolean isDeleted = patronService.deletePatron(id);
        if(isDeleted) return new ApiResponse<>(true,200,"Patron deleted Successfully");
        return new ApiResponse<>(false,404,"There is no patron with id: "+id);
    }

}
