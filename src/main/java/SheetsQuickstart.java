import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import java.io.*;
import java.lang.management.GarbageCollectorMXBean;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.ConsoleHandler;

public class SheetsQuickstart {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    // SpreadSheet Permission for ReadWrite access.
    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS,SheetsScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    // Function for getting credentials
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    // For retrieving the data
    public static void getData(List<List<Object>> values) throws IOException {

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("Name, Major");
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                System.out.printf("Last Name : %s and First Name = %s\n",row.get(4),row.get(5));
            }
            System.out.println("Total Records = " + values.size());
        }
        System.out.println("Data fetched Successfully!");
    }

    public static void setData(String spreadsheetId,String range, Sheets service,List<List<Object>> list) throws IOException, InterruptedException {
        if (list == null || list.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("Name, Major");
            for (List row : list) {
                ValueRange appendBody = new ValueRange()
                        .setValues(Arrays.asList(
                                Arrays.asList(row.get(4),row.get(5),row.get(7))
                        ));

                // Result execution
                AppendValuesResponse appendResult = service.spreadsheets().values()
                        .append(spreadsheetId, range, appendBody)
                        .setValueInputOption("USER_ENTERED")
                        .setInsertDataOption("INSERT_ROWS")
                        .setIncludeValuesInResponse(true)
                        .execute();

                // Garbadge Collection
                appendResult = null;
                appendBody = null;
                // We do have only 60 request perminutes so adding half seconds sleep.
                Thread.sleep(500);
            }
        }

        System.out.println("Data Inserted Successfull!");
    }

    public static void main(String... args) throws IOException, GeneralSecurityException, InterruptedException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1TFTQd8klo3JoV9tNP8JC3QmzKWfMWLxhQbJrOLjSrBE";
        final String range = "congress!A2:CG";

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        // Created another object for coping data to another sheet.
        ValueRange responseAdd = service.spreadsheets().values()
                .get(spreadsheetId,"congresscoppied!A2:CG")
                .execute();

        List<List<Object>> values = response.getValues();

        // Calling Methods
        //getData(values);
        setData(spreadsheetId,"congresscoppied!A2:CG",service, values);
    }
}