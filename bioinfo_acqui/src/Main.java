import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


//TODO: Get genomes overview list (timestamp for updating once a day?week?
public class Main {

    public static void main(String argv[]) {

        try {
            GenomeOverview genomeOverview = new GenomeOverview();
            GenomeCDS genomeCDS = new GenomeCDS();

            for (final Genome genome: genomeOverview.getGenomeList()) {
                genomeCDS.getGenomeCDS(genome.getOrganism());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

