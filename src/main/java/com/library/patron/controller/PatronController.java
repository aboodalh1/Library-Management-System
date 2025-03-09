package com.library.patron.controller;
import com.library.patron.model.Patron;
import com.library.patron.request.PatronDTO;
import com.library.patron.service.PatronService;
import com.library.utils.response.MyAPIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
@Tag(name = "Patron", description = "Manage patrons")
public class PatronController {
    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @Operation(
            summary = "Get All Patrons"
    )
    @GetMapping
    public MyAPIResponse<List<Patron>> getAllPatrons() {

        return new MyAPIResponse<>(true,200, patronService.getAllPatrons());
    }
    @Operation(
            summary = "Get a specific Patron",
            description = "Get a specific Patron by its Id"
    )
    @GetMapping("/{id}")
    public MyAPIResponse<Patron> getPatronById(@PathVariable Long id) {

        return new MyAPIResponse<>(true,200,patronService.getPatronById(id));
    }
    @Operation(
            summary = "Add a new Patron"
    )
    @PostMapping("/add_patron")
    public ResponseEntity<MyAPIResponse<Patron>> addPatron(@Valid @RequestBody PatronDTO patronDTO) {
        Patron patron = new Patron();

        patron.setFirstName(patronDTO.getFirstName());
        patron.setLastName(patronDTO.getLastName());
        patron.setAddress(patronDTO.getAddress());
        patron.setPhoneNumber(patronDTO.getPhoneNumber());

        Patron savedPatron = patronService.addPatron(patron);

        return ResponseEntity.ok(new MyAPIResponse<>(true,200,patronService.addPatron(savedPatron)));
    }
    @Operation(
            summary = "Edit Patron",
            description = "Edit Patron by its Id"
    )
    @PutMapping("/{id}")
    public MyAPIResponse<Patron> updatePatron(@PathVariable Long id, @RequestBody Patron patron) {
        return new MyAPIResponse<>(true,200,patronService.updatePatron(id, patron));
    }
    @Operation(
            summary = "Delete Patron",
            description = "Delete Patron by its Id"
    )
    @DeleteMapping("/{id}")
    public MyAPIResponse<String> deletePatron(@PathVariable Long id)
    {
        boolean isDeleted = patronService.deletePatron(id);
        if(isDeleted) return new MyAPIResponse<>(true,200,"Patron deleted Successfully");
        return new MyAPIResponse<>(false,404,"There is no patron with id: "+id);
    }

}
