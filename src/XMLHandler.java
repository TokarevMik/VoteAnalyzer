import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class XMLHandler extends DefaultHandler {
    private static StringBuilder insertQuery = new StringBuilder();
//    private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    private static int stringBuilderCount = 0;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("voter")) {
            String name = attributes.getValue("name");
            String birthDate = attributes.getValue("birthDay");
            countVoterBuilder(name, birthDate);
            stringBuilderCount++;
        }

    } //

    @Override
    public void endElement (String uri, String localName, String qName) {
        if (qName.equals("voter")) {
            if (stringBuilderCount>=500000) {
                try {
                    DBConnection.executeMultiInsert(insertQuery);
                    insertQuery = new StringBuilder();
                    stringBuilderCount=0;
                    System.out.println("Сброс");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (qName.equals("voters")) {
            try {
                DBConnection.executeMultiInsert(insertQuery);
                System.out.println("Сброс финальный");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            insertQuery = new StringBuilder();
        }
    }

    public static void countVoterBuilder(String name, String birthDay) {
        birthDay = birthDay.replace('.', '-');
        boolean isStart = insertQuery.length() == 0;
        insertQuery.append((isStart ? "" : ",") + "('" + name + "', '" + birthDay + "', 1)");

    }
}
