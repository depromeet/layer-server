package org.layer.external.google.config;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class GoogleConfig {
    private final GoogleCredentials googleCredentials;

    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    @Value("${google.sheet.token_path}")
    private String tokenPath;

    @Value("${google.sheet.credential_path}")
    private String credentialPath;


    @Bean
    public Sheets getGoogleSheetService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        String APPLICATION_NAME = "Google Sheets API Java Quickstart";
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        GoogleClientSecrets clientSecrets = createGoogleClientSecrets();

        log.info("{}<<tokenPath", tokenPath);
        File tokensDirectory = new File(tokenPath);
        if (!tokensDirectory.exists()) {
            tokensDirectory.mkdirs();
        }  // StoredCredential 파일을 복사
        copyStoredCredentialFile(tokensDirectory);

        log.info("{} <<<tokensDirectorytokensDirectory", tokensDirectory.getAbsolutePath());

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(tokensDirectory))
                .setAccessType("offline")
                .build();
//        Resource resource = resourceLoader.getResource("classpath:tokens");
//
//
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(resource.getFile()))
//                .setAccessType("offline")
//                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private GoogleClientSecrets createGoogleClientSecrets() {
        // Details 객체 생성
        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details()
                .setClientId(googleCredentials.getInstalled().getClientId())
                .setClientSecret(googleCredentials.getInstalled().getClientSecret())
                .setAuthUri(googleCredentials.getInstalled().getAuthUri())
                .setTokenUri(googleCredentials.getInstalled().getTokenUri())
                .setRedirectUris(googleCredentials.getInstalled().getRedirectUris());

        // GoogleClientSecrets 객체 생성 및 Details 설정
        return new GoogleClientSecrets().setInstalled(details);
    }


    private void copyStoredCredentialFile(File tokensDirectory) throws IOException {
        log.info("{} tokensDirectory", tokensDirectory);
        File storedCredentialFile = new File(tokensDirectory, "StoredCredential");
        if (!storedCredentialFile.exists()) {
            Resource storedCredentialResource = new ClassPathResource(credentialPath);
            try (InputStream is = storedCredentialResource.getInputStream();
                 OutputStream os = new FileOutputStream(storedCredentialFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            }
        }
    }
}
