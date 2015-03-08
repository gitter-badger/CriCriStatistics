import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlerGenomeId extends DefaultHandler {

    boolean inIdList = false;
    boolean inIdListId = false;
    String genomeId;
    private static IMediatorGUI mediatorGUI = MediatorGUI.getInstance();
    private static Settings settings = Settings.getInstance();

    public void startElement(String uri, String localName,String qName,
                             Attributes attributes) throws SAXException {

        if (qName.equalsIgnoreCase("IDLIST")) {
            inIdList = true;
        }

        if (inIdList == true) {
            if (qName.equalsIgnoreCase("ID")) {
                inIdListId = true;
            }
        }
    }

    public void endElement(String uri, String localName,
                           String qName) throws SAXException {

        //System.out.println("End Element :" + qName);
        if (qName.equalsIgnoreCase("IDLIST")) {
            inIdList = false;
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {

        if (inIdListId) {
            genomeId = new String(ch, start, length);
            mediatorGUI.updateAquisitionPanel("- genomeId : " + new String(ch, start, length),(int)Thread.currentThread().getId()%settings.getNumThreads() +1 );
            inIdListId = false;
        }
    }

    public String getGenomeId() { return genomeId; }
}
