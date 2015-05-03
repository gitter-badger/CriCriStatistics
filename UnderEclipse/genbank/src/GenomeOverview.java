import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class GenomeOverview
{

    private static IMediatorGUI mediatorGUI = MediatorGUI.getInstance();
    
    GenomeOverview() {
        if (Files.exists(FileSystems.getDefault().getPath("genome_overview.txt")) == false) {
            try {
                URL website = new URL("http://www.ncbi.nlm.nih.gov/genomes/Genome2BE/genome2srv.cgi?action=download&orgn=&report=overview&king=All&group=All&subgroup=All");
                File target = new File("genome_overview.txt");
                Files.copy(website.openStream(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    List<Genome> getGenomeList() throws IOException {
        List<Genome> genomeList = new ArrayList<Genome>();

        for (final String line: Files.readAllLines(FileSystems.getDefault().getPath("genome_overview.txt"), Charset.defaultCharset()))
        {
            if (line.charAt(0) != '#')
            {
                String[] split = line.split("\t");

                Genome g = new Genome();
                g.setOrganism(split[0]);
                g.setKingdom(split[1]);
                g.setGroup(split[2]);
                g.setSubGroup(split[3]);
                g.setSize(split[4]);
                g.setChrs(split[5]);

                genomeList.add(g);
            }
        }
        mediatorGUI.setProgressBar(genomeList.size());
        return genomeList;
    }

}
