package com.techelevator.tenmo.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    File file;

    public Logger(File file){
        this.file = file;
    }

    public String dateAndTime(){
        LocalDateTime timestamp = LocalDateTime.now();

        DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
        return timestamp.format(targetFormat);
    }

    public void write(String message){

        String dateAndTime = dateAndTime();

        try{
            PrintWriter writer = null;

            if (file.exists()){

                writer = new PrintWriter(new FileOutputStream(file, true));
            } else{
                writer = new PrintWriter(file);
            }

            writer.append(dateAndTime + " | " + message + "\n");
            writer.flush();
            writer.close();

        }catch (FileNotFoundException e){
            System.out.println("File can't be accessed");
        }
    }
}
