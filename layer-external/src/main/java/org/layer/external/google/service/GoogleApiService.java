package org.layer.external.google.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.domain.member.entity.MemberFeedback;
import org.layer.external.google.enums.SheetType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleApiService {
    private final Sheets googleSheetService;

    @Value("${google.sheet.id}")
    private String sheetId;


    public void writeFeedback(SheetType sheetType, MemberFeedback memberFeedback, int score, String description) {
        new MemberFeedback();
        try {
            String feedbackSheet = sheetType.getSheetName() + "!" + sheetType.getColumns();
            ValueRange response = googleSheetService.spreadsheets().values().get(sheetId, feedbackSheet).execute();

            List<List<Object>> existingValues = response.getValues();
            int currentRowCount = existingValues != null ? existingValues.size() : 0;

            // 다음 행에서 데이터를 시작하도록 설정
            String range = sheetType.getSheetName() + "!A" + (currentRowCount + 1) + ":H" + (currentRowCount + 1);
            List<List<Object>> values = new ArrayList<>();
            List<Object> row = new ArrayList<>();

            row.add(memberFeedback.getMemberId() != null ? memberFeedback.getMemberId() : "");
            row.add(Optional.ofNullable(memberFeedback.getEmail()).orElse(""));
            row.add(Optional.ofNullable(memberFeedback.getMemberName()).orElse(""));
            row.add(memberFeedback.getMemberCreatedAt() != null ? memberFeedback.getMemberCreatedAt().toString() : "");
            row.add(memberFeedback.getRetrospectCount() != null ? memberFeedback.getRetrospectCount() : "");
            row.add(memberFeedback.getSpaceCount() != null ? memberFeedback.getSpaceCount() : "");
            row.add(score != 0 ? score : "");
            row.add(Optional.ofNullable(description).orElse(""));


            values.add(row);
            ValueRange body = new ValueRange().setValues(values);
            googleSheetService.spreadsheets().values().update(sheetId, range, body)
                    .setValueInputOption("RAW")
                    .execute();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
