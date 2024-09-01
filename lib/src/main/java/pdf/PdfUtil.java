package pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;
import model.ExchangeRate;

public class PdfUtil {

    public static void writePdf(List<ExchangeRate> exchangeRates, String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            float yOffset = 700; // 초기 Y 오프셋

            // 타이틀 출력
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 15);
                contentStream.newLineAtOffset(100, yOffset);
                contentStream.showText("Exchange Rates Report");
                contentStream.endText();
                yOffset -= 40;
            }

            // 환율 데이터 출력
            for (ExchangeRate rate : exchangeRates) {
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {

                	// 통화 정보와 날짜 정보를 같은 줄에 표시
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.setNonStrokingColor(0.0f, 0.0f, 1.0f); // 파란색
                    contentStream.newLineAtOffset(100, yOffset);
                    contentStream.showText("Currency: " + rate.getCurrency());

                    // 날짜 정보를 같은 줄에 다른 폰트로 표시
                    contentStream.setFont(PDType1Font.COURIER, 10);
                    contentStream.setNonStrokingColor(0.0f, 0.5f, 0.0f); // 녹색
                    contentStream.newLineAtOffset(200, 0); // X 오프셋을 조정하여 같은 줄에 배치
                    contentStream.showText(" | Date: " + rate.getDate());
                    contentStream.endText();

                    yOffset -= 20;

                    // 환율 정보 (Helvetica 폰트, 빨간색)
                    contentStream.setNonStrokingColor(1.0f, 0.0f, 0.0f); // 빨간색
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.newLineAtOffset(100, yOffset);
                    contentStream.showText("Rate: " + rate.getRate().toString());
                    contentStream.endText();

                    yOffset -= 40;

                    // 페이지에 출력할 공간이 부족하면 새 페이지 추가
                    if (yOffset < 100) {
                        page = new PDPage();
                        document.addPage(page);
                        yOffset = 700;
                    }
                }
            }

            document.save(filePath);
            System.out.println("PDF 파일이 생성되었습니다: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}