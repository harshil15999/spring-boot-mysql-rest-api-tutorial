package com.example.easynotes.exception;

import org.springframework.stereotype.Component;

@Component
public class Messages {
    public final String FileNotFound="File Not Found";
    public final String InvalidFileName="Invalid FileName";
    public final String SuccessFullyUploaded="File uploaded successfully";
    public final String ErrorSavingFile ="Error saving the file";
    public final String FileLimit=" File must be not empty and less than 2Gb";
}
