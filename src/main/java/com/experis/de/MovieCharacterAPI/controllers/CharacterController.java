package com.experis.de.MovieCharacterAPI.controllers;

import com.experis.de.MovieCharacterAPI.dto.CharacterMessage;
import com.experis.de.MovieCharacterAPI.services.MovieCharacterService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/characters")
public class CharacterController {

    /**
     * Using service here
     */
    private final MovieCharacterService movieCharacterService;

    @Autowired
    public CharacterController(MovieCharacterService movieCharacterService) {
        this.movieCharacterService = movieCharacterService;
    }

    /**
     * Get all characters
     */
    @Operation(summary = "Get all characters, no filter")
    @ApiResponses(value = {
            //Is there anybody out there?
            @ApiResponse(responseCode = "200", description = "List of characters",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CharacterMessage.class)) }),
            //Nope
            @ApiResponse(responseCode = "404", description = "No character was found.",
                    content = @Content) })

     @GetMapping()
    public ResponseEntity<List<CharacterMessage>> getAllCharacters(){
        List<CharacterMessage> characterList;
        HttpStatus status;

        characterList = movieCharacterService.getAllCharacters();
        status = (characterList != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(characterList, status);
    }

    /**
     * Get just one
     */
    @Operation(summary = "Get single character by ID")
    @ApiResponses(value = {
            //Here he/she/it comes
            @ApiResponse(responseCode = "200", description = "I found someone",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CharacterMessage.class)) }),
            //Nope
            @ApiResponse(responseCode = "404", description = "Nobody found with that ID",
                    content = @Content) })

    @GetMapping("/{id}")
    public ResponseEntity<CharacterMessage> getCharacter(@PathVariable Long id){
        CharacterMessage character;
        HttpStatus status;

        character = movieCharacterService.getCharacter(id);
        status = (character != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(character, status);
    }

    /**
     * Create new character
     */
    @Operation(summary = "Create character by input data, movieID is optional")
    @ApiResponses(value = {
            //Success
            @ApiResponse(responseCode = "200", description = "A new star is born",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CharacterMessage.class)) }),
            //Wrong DNA code used
            @ApiResponse(responseCode = "404", description = "Something went wrong here",
                    content = @Content) })

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CharacterMessage> createCharacter(@RequestBody CharacterMessage characterMessage) {
        HttpStatus status;

        characterMessage = movieCharacterService.createOrUpdateCharacter(characterMessage);
        status = (characterMessage != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(characterMessage, status);
    }

    /**
     * Change existing character
     */
    @Operation(summary = "Update character by input data, movieID is optional")
    @ApiResponses(value = {
            //Done
            @ApiResponse(responseCode = "200", description = "Character updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CharacterMessage.class)) }),
            //Nope
            @ApiResponse(responseCode = "404", description = "Character could not be updated",
                    content = @Content) })

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CharacterMessage> updateCharacter(@RequestBody CharacterMessage characterMessage) {
        HttpStatus status;

        characterMessage = movieCharacterService.createOrUpdateCharacter(characterMessage);
        status = (characterMessage != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(characterMessage, status);
    }

    /**
     * Delete character and all references
     */
    @Operation(summary = "Delete character and remove references")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exterminated"),
            @ApiResponse(responseCode = "404", description = "Character could not be deleted")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCharacter(@PathVariable Long id) {

        if (movieCharacterService.deleteCharacter(id))
        {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

}
