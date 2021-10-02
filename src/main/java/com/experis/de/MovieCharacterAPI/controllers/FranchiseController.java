package com.experis.de.MovieCharacterAPI.controllers;

import com.experis.de.MovieCharacterAPI.dto.CharacterMessage;
import com.experis.de.MovieCharacterAPI.dto.FranchiseMessage;
import com.experis.de.MovieCharacterAPI.services.MovieCharacterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/franchises")
public class FranchiseController {

    /**
     * Using service here
     */
    private final MovieCharacterService movieCharacterService;

    @Autowired
    public FranchiseController(MovieCharacterService movieCharacterService) {
        this.movieCharacterService = movieCharacterService;
    }

    /**
     * Get all franchises
     */
    @Operation(summary = "Get all franchises without filter")
    @ApiResponses(value = {
            //Found them
            @ApiResponse(responseCode = "200", description = "List of franchises",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FranchiseMessage.class)) }),
            //Nope
            @ApiResponse(responseCode = "404", description = "No franchises found",
                    content = @Content) })

    @GetMapping()
    public ResponseEntity<List<FranchiseMessage>> getAllFranchises(){
        List<FranchiseMessage> franchiseList;
        HttpStatus status;

        franchiseList = movieCharacterService.getAllFranchises();
        status = (franchiseList != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(franchiseList, status);
    }

    /**
     * Get only one franchise
     */
    @Operation(summary = "Get single franchise by ID")
    @ApiResponses(value = {
            //Yes, there is something
            @ApiResponse(responseCode = "200", description = "Found the franchise",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FranchiseMessage.class)) }),
            //Nope
            @ApiResponse(responseCode = "404", description = "Franchise not found",
                    content = @Content) })

    @GetMapping("/{id}")
    public ResponseEntity<FranchiseMessage> getFranchise(@PathVariable Long id){
        FranchiseMessage franchise;
        HttpStatus status;

        franchise = movieCharacterService.getFranchise(id);
        status = (franchise != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(franchise, status);
    }

    /**
     * Create new franchise
     */
    @Operation(summary = "Create franchise by input data")
    @ApiResponses(value = {
            //success
            @ApiResponse(responseCode = "200", description = "Franchise created, want to be the new Marvel?",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CharacterMessage.class)) }),
            //nope
            @ApiResponse(responseCode = "404", description = "Franchise could not be created",
                    content = @Content) })

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<FranchiseMessage> createFranchise(@RequestBody FranchiseMessage franchiseMessage) {
        HttpStatus status;

        franchiseMessage = movieCharacterService.createOrUpdateFranchise(franchiseMessage);
        status = (franchiseMessage != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(franchiseMessage, status);
    }

    /**
     * Update franchise
     */
    @Operation(summary = "Update franchise by input data")
    @ApiResponses(value = {
            //Worked
            @ApiResponse(responseCode = "200", description = "Franchise updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FranchiseMessage.class)) }),
            //Nope
            @ApiResponse(responseCode = "404", description = "Franchise could not be updated",
                    content = @Content) })    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<FranchiseMessage> updateFranchise(@RequestBody FranchiseMessage franchiseMessage) {
        HttpStatus status;

        franchiseMessage = movieCharacterService.createOrUpdateFranchise(franchiseMessage);
        status = (franchiseMessage != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(franchiseMessage, status);
    }

    /**
     * Update franchise
     */
    @Operation(summary = "Update movies of franchise by list of movie IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Franchise movies updated"),
            @ApiResponse(responseCode = "404", description = "Franchise movies could not be updated")
    })

    @PutMapping(path="/{id}/movies")
    public ResponseEntity<Boolean> updateFranchiseMovies(@PathVariable Long id, @RequestParam Long[] ids) {

        if (movieCharacterService.setFranchiseMovies(id, ids)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

}
