

//A class to defined various debug flags
//TODO: use JCommander to do the commad line argument parsing
//TODO: Use dependencies injection instead of hard coupling

public class DebugOption
{
  private boolean GBLogging;  
  private boolean sequenceLogging;  

  public DebugOption(){
    this.GBLogging = false;
    this.sequenceLogging = false;
  }
  
  public void parseInputCommand(String args[]){
    int i = 0;
    int argc = args.length;
    
    for (i = 0; i < args.length; i++){

      if( args[i].equals("-GBLogging") ) {
        this.GBLogging = true;
      }
      else if( args[i].equals("-sequenceLogging") ){
        this.sequenceLogging = true;
      }

    }


  }

  public boolean isGBLoggingActivated(){
    return GBLogging;
  }

  public boolean isSequenceLoggingActivated(){
    return sequenceLogging;
  }


}
