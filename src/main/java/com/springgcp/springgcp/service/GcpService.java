package com.springgcp.springgcp.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.cloud.storage.Storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.PostConstruct;

@Service
public class GcpService {
    private Storage storage;

    @PostConstruct
    public void init() throws IOException {
        // Load the credentials from the JSON key file
        try (FileInputStream serviceAccountStream = new FileInputStream("C:\\Users\\sai\\AppData\\Roaming\\gcloud\\application_default_credentials.json")) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);

            // Build the storage object
            this.storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }
    }

    public Storage getStorage() {
        return storage;
    }
    public String uploadFileToGCS() throws IOException {
         byte[] excelFile = createExcelFile();
       // System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "C:\\Users\\sai\\AppData\\Roaming\\gcloud\\application_default_credentials.json");
        // Upload the file to Google Cloud Storage
         return uploadObject("sample143", "gcpfilenew23.xlsx", excelFile);

    }
    private byte[] createExcelFile() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");

        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue(1);
        row1.createCell(1).setCellValue("John Doe");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }



    public String uploadObject(String bucketName, String objectName, byte[] data) {
        try {
            Blob blob = storage.create(
                    Blob.newBuilder(bucketName, objectName).build(),
                    data
            );
            return objectName;
        } catch (StorageException e) {
            handleStorageException(e);
        }
        return objectName;
    }

    private void handleStorageException(StorageException e) {
        switch (e.getCode()) {
            case 403:
                System.err.println("Access denied. Please check your permissions.");
                break;
            case 404:
                System.err.println("Bucket not found. Please check the bucket name.");
                break;
            case 429:
                System.err.println("Quota exceeded. Please check your usage and quotas.");
                break;
            case 501:
                System.err.println("API not enabled. Please enable the Cloud Storage API.");
                break;
            default:
                System.err.println("An error occurred: " + e.getMessage());
                break;
        }
    }


}
