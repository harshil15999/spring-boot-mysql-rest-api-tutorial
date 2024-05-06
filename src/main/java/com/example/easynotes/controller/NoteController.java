package com.example.easynotes.controller;

import com.example.easynotes.exception.Messages;
import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.model.Note;
import com.example.easynotes.repository.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by rajeevkumarsingh on 27/06/17.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class NoteController {

    @Autowired
    NoteRepository noteRepository;


    @Autowired
    Messages messages;



    private static final long MAX_FILE_SIZE = 2L * 1024 * 1024 * 1024; // 10GB


    @GetMapping("/notes")
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @PostMapping("/notes")
    public Note createNote(@Valid @RequestBody Note note) {
        return noteRepository.save(note);
    }

    @GetMapping("/notes/{id}")
    public Note getNoteById(@PathVariable(value = "id") Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
    }

    @PutMapping("/notes/{id}")
    public Note updateNote(@PathVariable(value = "id") Long noteId,
                                           @Valid @RequestBody Note noteDetails) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        note.setTitle(noteDetails.getTitle());
        note.setContent(noteDetails.getContent());

        Note updatedNote = noteRepository.save(note);
        return updatedNote;
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        noteRepository.delete(note);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        log.info(file.getOriginalFilename());

        // #TODO : put this value in properties file
        final String uploadLocation="C:\\Users\\harsh\\PROJECTS\\src\\main\\resources\\notes";

        // 1. Validate file size (between 2000 and 2000 MB)
        if (file.isEmpty() || file.getSize() > MAX_FILE_SIZE) {
            log.info("File Size is :{}",file.getSize());
            return ResponseEntity.badRequest().body(messages.FileLimit);
        }

        // 2. Extract filename directly from MultipartFile (assuming no specific format)
        String fileName = file.getOriginalFilename();
        log.info("File Name uploaded is {}",fileName);

        // 3. Sanitize filename
        if (fileName == null || fileName.isEmpty() || fileName.contains("..")) {
            return new ResponseEntity<>(messages.InvalidFileName, HttpStatus.BAD_REQUEST);
        }

        // Perform additional processing with the sanitized filename and uploaded file content (not shown for brevity)
        Path filePath=Paths.get(uploadLocation).resolve(file.getOriginalFilename());


        // Create input stream from uploaded file
        try (InputStream inputStream = file.getInputStream();
             // Create output stream to write file directly to disk
             OutputStream outputStream = new FileOutputStream(filePath.toFile())) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return new ResponseEntity<>(messages.SuccessFullyUploaded,HttpStatus.OK);
        } catch(Exception e ){
            log.error(messages.ErrorSavingFile);
            return new ResponseEntity<>(messages.ErrorSavingFile,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
