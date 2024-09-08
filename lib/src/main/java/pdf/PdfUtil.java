package pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfUtil {

    public static void createMedicalOpinionPdf(String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // 폰트 로드
            PDType0Font font = PDType0Font.load(document,
                    new FileInputStream(new File("C:/Users/hanjangyeon/eclipse-workspace/sample2/lib/src/main/resources/fonts/NotoSansJP-Regular.ttf")));

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float margin = 50;
                float yOffset = page.getMediaBox().getHeight() - margin;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;

                // 타이틀
                contentStream.setFont(font, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yOffset);
                contentStream.showText("主治医意見書");
                contentStream.endText();

                // 기입일
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(page.getMediaBox().getWidth() - margin - 100, yOffset);
                contentStream.showText("記入日  平成   年   月   日");
                contentStream.endText();

                yOffset -= 40;

                // 신청자 정보 테이블 생성
                drawApplicantInfoTable(contentStream, margin, yOffset, tableWidth, font);
                
            }
            document.save("MedicalOpinionForm.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final float rowHeight = 30f;

    private static void drawApplicantInfoTable(PDPageContentStream contentStream, float x, float y, float width, PDType0Font font) throws IOException {
        float col1Width = 50f; // 첫 번째 열 너비 (신청자 항목)
        float col2Width = 200f; // 두 번째 열 너비 (후리가나, 이름, 생년월일)
        float col3Width = 50f;  // 세 번째 열 너비 (성별)
        float col4Width = width - col1Width - col2Width - col3Width; // 네 번째 열 너비 (우편번호, 주소, 연락처)
        float extendedRowHeight = rowHeight * 3; // 4행의 높이 조정 (기존의 60%)

        // 전체 테두리 그리기
        contentStream.setLineWidth(1f);

        // 가로선 그리기 (행 구분)
        contentStream.moveTo(x, y);
        contentStream.lineTo(x + width, y);
        contentStream.stroke();

        contentStream.moveTo(x, y - rowHeight * 3);
        contentStream.lineTo(x + width, y - rowHeight * 3);
        contentStream.stroke();

        // 4행 (설명문과 의사정보가 들어갈 행) 위한 가로선 그리기
        contentStream.moveTo(x, y - rowHeight * 3 - extendedRowHeight);
        contentStream.lineTo(x + width, y - rowHeight * 3 - extendedRowHeight);
        contentStream.stroke();

        // 열 테두리 그리기 (1~3행 적용)
        contentStream.moveTo(x + col1Width, y);
        contentStream.lineTo(x + col1Width, y - rowHeight * 3);  // 신청자 정보와 성별 열까지만 선 그리기
        contentStream.stroke();

        contentStream.moveTo(x + col1Width + col2Width, y);
        contentStream.lineTo(x + col1Width + col2Width, y - rowHeight * 3);  // 신청자 정보와 성별 열까지만 선 그리기
        contentStream.stroke();

        contentStream.moveTo(x + col1Width + col2Width + col3Width, y);
        contentStream.lineTo(x + col1Width + col2Width + col3Width, y - rowHeight * 3);  // 성별 열까지만 선 그리기 (4행 내부 구분선 제거)
        contentStream.stroke();

        // 4열 오른쪽 테두리 추가
        contentStream.moveTo(x + col1Width + col2Width + col3Width + col4Width, y);
        contentStream.lineTo(x + col1Width + col2Width + col3Width + col4Width, y - rowHeight * 3 - extendedRowHeight);
        contentStream.stroke();

        // 2열과 4열의 내부 구분선 추가 (1행~3행)
        for (int i = 1; i < 3; i++) {
            contentStream.moveTo(x + col1Width, y - rowHeight * i);
            contentStream.lineTo(x + col1Width + col2Width, y - rowHeight * i);
            contentStream.stroke();

            contentStream.moveTo(x + col1Width + col2Width + col3Width, y - rowHeight * i);
            contentStream.lineTo(x + col1Width + col2Width + col3Width + col4Width, y - rowHeight * i);
            contentStream.stroke();
        }

        // 4행 왼쪽 테두리 추가
        contentStream.moveTo(x, y - rowHeight * 3);
        contentStream.lineTo(x, y - rowHeight * 3 - extendedRowHeight);
        contentStream.stroke();

        // 신청자 셀 통합 (내부 선 없음)
        contentStream.addRect(x, y - rowHeight * 3, col1Width, rowHeight * 3);
        contentStream.stroke();

        // 성별 셀 통합 (내부 선 없음)
        contentStream.addRect(x + col1Width + col2Width, y - rowHeight * 3, col3Width, rowHeight * 3);
        contentStream.stroke();

        // 텍스트 삽입: 신청자 (가운데 정렬)
        contentStream.setFont(font, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(x + (col1Width / 2) - 10, y - (rowHeight * 1.5f) + 5);
        contentStream.showText("申請者");
        contentStream.endText();

        // 텍스트 삽입: 후리가나
        contentStream.beginText();
        contentStream.newLineAtOffset(x + col1Width + 5, y - rowHeight + 15);
        contentStream.showText("（ふりがな）");
        contentStream.endText();

        // 텍스트 삽입: 이름 한자
        contentStream.beginText();
        contentStream.newLineAtOffset(x + col1Width + 5, y - rowHeight * 2 + 15);
        contentStream.showText("山田 太郎");
        contentStream.endText();

        // 텍스트 삽입: 생년월일
        contentStream.beginText();
        contentStream.newLineAtOffset(x + col1Width + 5, y - rowHeight * 3 + 15);
        contentStream.showText("昭和50年1月1日 (45歳)");
        contentStream.endText();

        // 텍스트 삽입: 성별 (세로로, 가운데 정렬)
        contentStream.beginText();
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(x + col1Width + col2Width + (col3Width / 2) - 10, y - rowHeight + 5);
        contentStream.showText("男");
        contentStream.newLine();
        contentStream.showText("・");
        contentStream.newLine();
        contentStream.showText("女");
        contentStream.endText();

        // 텍스트 삽입: 우편번호
        contentStream.beginText();
        contentStream.newLineAtOffset(x + col1Width + col2Width + col3Width + 10, y - rowHeight + 15);
        contentStream.showText("〒123-4567");
        contentStream.endText();

        // 텍스트 삽입: 주소
        contentStream.beginText();
        contentStream.newLineAtOffset(x + col1Width + col2Width + col3Width + 10, y - rowHeight * 2 + 15);
        contentStream.showText("東京都中央区1-1-1");
        contentStream.endText();

        // 텍스트 삽입: 연락처 (테두리 포함)
        contentStream.beginText();
        contentStream.newLineAtOffset(x + col1Width + col2Width + col3Width + 10, y - rowHeight * 3 + 15);
        contentStream.showText("連絡先 (03-1234-5678)");
        contentStream.endText();

        // 설명 문구 및 의사 정보 삽입 (4행)
        contentStream.beginText();
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(x + 5, y - rowHeight * 3 - extendedRowHeight + 3 * rowHeight - 20);
        contentStream.showText("上記の申請者に関する意見は以下の通りです。");
        contentStream.newLine();
        contentStream.showText("主治医として、本意見書が介護サービス計画作成等に利用されることに □ 同意する  □ 同意しない");
        contentStream.newLine();
        contentStream.showText("医師氏名: ____________________");
        contentStream.newLine();
        contentStream.showText("医療機関名: ____________________        電話: (      ) __________________");
        contentStream.newLine();
        contentStream.showText("医療機関所在地: ____________________    FAX: (      ) __________________");
        contentStream.endText();
    }
}
