package com.webhub.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class LogService {
	
	private static final String LOG_FILE_PATH = "logs.txt";

    public void log(String section) {
        String logEntry = String.format("%s - Section: %s%n", LocalDateTime.now(), section);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
