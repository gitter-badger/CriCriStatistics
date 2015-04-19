import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public class HandlerSequenceId extends HandlerGenomeId {

    boolean inLink = false;
    boolean inLinkId = false;
    private static IMediatorGUI mediatorGUI = MediatorGUI.getInstance();

    List sequenceIdList = new ArrayList<String>();

    public void startElement(String uri, String localName,String qName,
                             Attributes attributes) throws SAXException {

        if (qName.equalsIgnoreCase("LINK")) {
            inLink = true;
        }

        if (inLink == true) {
            if (qName.equalsIgnoreCase("ID")) {
                inLinkId = true;
            }
        }
    }

    public void endElement(String uri, String localName,
                           String qName) throws SAXException {

        //System.out.println("End Element :" + qName);
        if (qName.equalsIgnoreCase("LINK")) {
            inLink = false;
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {

        if (inLinkId) {
            sequenceIdList.add(new String(ch, start, length));
            mediatorGUI.updateAquisitionPanel("   _ sequenceId : " + new String(ch, start, length));
            inLinkId = false;
        }
    }

    public List<String> getSequenceIdList() { return sequenceIdList; }
}
