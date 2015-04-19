//#Organism/Name	Kingdom	Group	SubGroup	Size (Mb)	Chrs	Organelles	Plasmids	BioProjects
public class Genome
{
    private String organism;
    private String kingdom;
    private String group;
    private String subGroup;
    private String size;
    private String chrs;
    private int id;
    private int nbFailedCDS;
    private int nbCorrectCDS;

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public String getKingdom() {
        return kingdom;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getChrs() {
        return chrs;
    }

    public void setChrs(String chrs) {
        this.chrs = chrs;
    }
    
    public int getNbFailedCDS(){
        return this.nbFailedCDS;
    }
    
    public void setNbFailedCDS(int nbFailedCDS){
        this.nbFailedCDS = nbFailedCDS;
    }
    
    public int getNbCorrectCDS(){
        return this.nbCorrectCDS;
    }
    
    public void setNbCorrectCDS(int nbCorrectCDS){
        this.nbCorrectCDS = nbCorrectCDS;
    }

    public void setId(int genomeId){
      this.id = genomeId;
    }
    
    public int getId(){
      return this.id;
    }
}
