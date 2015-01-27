import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

//TODO: Get genomes overview list (timestamp for updating once a day?week?
public class Main {

    public static void main(String argv[]) {

        try {
            GenomeCDS arabicVirus = new GenomeCDS("Arabis mosaic virus small satellite RNA");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

class GenomeCDS
{
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser;

    HandlerGenomeId handlerGenomeId = new HandlerGenomeId();
    HandlerSequenceId handlerSequenceId = new HandlerSequenceId();

    GenomeCDS(String genomeName) {
        try {
            saxParser = factory.newSAXParser();
            saxParser.parse("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=genome&term=" + genomeName,
                    handlerGenomeId);
            saxParser.parse("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/elink.fcgi?dbfrom=genome&db=nuccore&id=" + handlerGenomeId.getGenomeId(),
                    handlerSequenceId);

            for (final String sequenceId : handlerSequenceId.getSequenceIdList()) {
                try {
                    URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nuccore&rettype=gb&retmode=text&id=" + sequenceId);
                    Scanner s = new Scanner(url.openStream());
                    System.out.println(s.nextLine());
                    // read from your scanner
                } catch (IOException ex) {
                    // there was some connection problem, or the file did not exist on the server,
                    // or your URL was not in the right format.
                    // think about what to do now, and put it here.
                    ex.printStackTrace(); // for now, simply output it.
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

class HandlerGenomeId extends DefaultHandler {

    boolean inIdList = false;
    boolean inIdListId = false;
    String genomeId;

    public void startElement(String uri, String localName,String qName,
                             Attributes attributes) throws SAXException {

        //System.out.println("Start Element :" + qName);

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
            System.out.println("genomeId : " + new String(ch, start, length));
            inIdListId = false;
        }
    }

    public String getGenomeId() { return genomeId; }
}

class HandlerSequenceId extends HandlerGenomeId {

    boolean inLink = false;
    boolean inLinkId = false;

    List sequenceIdList = new ArrayList<String>();

    public void startElement(String uri, String localName,String qName,
                             Attributes attributes) throws SAXException {

        //System.out.println("Start Element :" + qName);

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
            System.out.println("sequenceId : " + new String(ch, start, length));
            inLinkId = false;
        }
    }

    public List<String> getSequenceIdList() { return sequenceIdList; }
}