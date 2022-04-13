package edu.uob;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;

/** This class implements the DB server. */
public final class DBServer {

  private static final char END_OF_TRANSMISSION = 4;
  private boolean parse;
  private String curDatabase;
  private String error;
  private String output;
  private final File directory;
  final private ArrayList<DBCommand> commands = new ArrayList<>();

  public static void main(String[] args) throws IOException {
    new DBServer(Paths.get(".").toAbsolutePath().toFile()).blockingListenOn(8888);
  }

  /**
   * KEEP this signature (i.e. {@code edu.uob.DBServer(File)}) otherwise we won't be able to mark
   * your submission correctly.
   *
   * <p>You MUST use the supplied {@code databaseDirectory} and only create/modify files in that
   * directory; it is an error to access files outside that directory.
   *
   * @param databaseDirectory The directory to use for storing any persistent database files such
   *     that starting a new instance of the server with the same directory will restore all
   *     databases. You may assume *exclusive* ownership of this directory for the lifetime of this
   *     server instance.
   */
  public DBServer(File databaseDirectory) {
    initServer();
    File databaseFolder = new File(String.valueOf(databaseDirectory));
    if(!databaseFolder.exists()){
      if(!databaseFolder.mkdir()){
        System.out.println("Create file failed");
      }
    }
    this.curDatabase = "";
    this.directory = databaseDirectory;
  }

  /**
   * KEEP this signature (i.e. {@code edu.uob.DBServer.handleCommand(String)}) otherwise we won't be
   * able to mark your submission correctly.
   *
   * <p>This method handles all incoming DB commands and carry out the corresponding actions.
   */
  public String handleCommand(String command) {
    initServer();
    this.parse = checkCommandValid(command);
    if(!this.parse) return "[ERROR] It is a false command: " + command;

    commands.add(new UseCommand(command));
    commands.add(new CreateCommand(command));
    commands.add(new DropCommand(command));
    commands.add(new AlterCommand(command));
    commands.add(new InsertCommand(command));
    commands.add(new UpdateCommand(command));
    commands.add(new DeleteCommand(command));
    commands.add(new SelectCommand(command));

    for (DBCommand cmd: commands){
        if(cmd.getValidCommand()){
          try {
            cmd.exeCommand(this);
            this.output = postModify(this.output);
          }catch (IOException e){
            e.printStackTrace();
          }
          String finalOutput = postModify(this.output);
          return "[OK] Thanks for your message: " + command +"\n"+ finalOutput;
        }else{
          this.error = cmd.getParseErr();
          return "[ERROR] It is a false command: " + getError();
        }
    }
    String finalOutput = postModify(this.output);
    return "[OK] Thanks for your message: " + command +"\n"+ finalOutput;
  }

  //  === Methods below are there to facilitate server related operations. ===

  /**
   * Starts a *blocking* socket server listening for new connections. This method blocks until the
   * current thread is interrupted.
   *
   * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
   * you want to.
   *
   * @param portNumber The port to listen on.
   * @throws IOException If any IO related operation fails.
   */
  public void blockingListenOn(int portNumber) throws IOException {
    try (ServerSocket s = new ServerSocket(portNumber)) {
      System.out.println("Server listening on port " + portNumber);
      while (!Thread.interrupted()) {
        try {
          blockingHandleConnection(s);
        } catch (IOException e) {
          System.err.println("Server encountered a non-fatal IO error:");
          e.printStackTrace();
          System.err.println("Continuing...");
        }
      }
    }
  }

  /**
   * Handles an incoming connection from the socket server.
   *
   * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
   * * you want to.
   *
   * @param serverSocket The client socket to read/write from.
   * @throws IOException If any IO related operation fails.
   */
  private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
    try (Socket s = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {

      System.out.println("Connection established: " + serverSocket.getInetAddress());
      while (!Thread.interrupted()) {
        String incomingCommand = reader.readLine();
        System.out.println("Received message: " + incomingCommand);
        String result = handleCommand(incomingCommand);
        writer.write(result);
        writer.write("\n" + END_OF_TRANSMISSION + "\n");
        writer.flush();
      }
    }
  }

  private void initServer(){
    this.parse =false;
    this.output ="";
    this.error = "";
  }

  private boolean checkCommandValid(String command){
    if(command.length() >= 3){
      int bracket = 0;
      int quata = 0;
      for(int i=0;i<command.length();i++){
        if(command.charAt(i) == '\''){
          quata ++;
        }else if(command.charAt(i)=='('){
          bracket ++;
        }else if(command.charAt(i)==')'){
          bracket --;
        }
      }
      return bracket == 0 && quata % 2 == 0;
    }else{
      return false;
    }
  }

  private String postModify(String command){
    if(command.equals("")){
      return command ;
    }
    StringBuilder finalOutput = new StringBuilder();
    for (int i=0;i<command.length();i++){
      if(command.charAt(i)=='+'){
        finalOutput.append(" ");
      }else{
        finalOutput.append(command.charAt(i));
      }
    }
    finalOutput = new StringBuilder(formatOutput(finalOutput.toString()));
    return finalOutput.toString();
  }

  private String formatOutput(String finalOutput){
    String[] rows = finalOutput.split("\n");
    StringBuilder newTab = new StringBuilder();
    for (int i = 0; i < rows.length; i++) {
      String r = rows[i];
      do {
        newTab.append(r);
        newTab.append('\t');
        i++;
      } while (r.indexOf(i) < rows.length);
      newTab.append('\n');
    }
    return newTab.toString();
  }

  public String getCurDatabase(){
    return this.curDatabase;
  }
  public void setCurDatabase(String path){
    this.curDatabase = path;
  }
  public void setError(String error){
    this.error = error;
  }
  public String getError(){
    return this.error;
  }
  public void setOutput(String output){this.output = output;}

  public File getDirectory() {
    return this.directory;
  }
}
