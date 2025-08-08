import java.io.*;




public class Parser {
    
    
    private BufferedReader reader;
    private String currentCommand;
    private String nextLine;

    /**
     * Constructs a new Parser instance and opens the input file.
     * 
     * @param filePath the path to the VM source file.
     * @throws IOException if the file cannot be opened.
     */

     public Parser (File filePath) throws IOException{
        {
        if (filePath == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        // Handle file existence check (optional)
        if (!filePath.exists() || !filePath.isFile()) {
            throw new FileNotFoundException("The provided file does not exist or is not a valid file: " + filePath);
        }
        this.reader = new BufferedReader(new FileReader(filePath));
        this.nextLine = readNextValidLine();
        this.currentCommand = null;
    }
     }

     private String readNextValidLine() throws IOException {
        String line;
        // Keep reading lines until we find a valid command (non-empty, non-comment)
        while ((line = reader.readLine()) != null) {
        line = line.trim(); // Clean up whitespace
        if (!line.isEmpty() && !line.startsWith("//")) {
            return line;
        }
    }
    return null;
        




     }




      /**
     * Closes the parser and releases resources.
     * 
     * @throws IOException if an error occurs while closing the file.
     */
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }


    /**
     * Checks if there are any more commands to process in the file
     * 
     * @return true if there are more commands, flase otherwise
     * @throws IOException  if an error ocurrs while reading the file
     */
    public boolean hasMoreCommands() throws IOException{
        return nextLine != null;
    }


    /**
     * Gets the next command and makes it the current instruction (string)
     *
     */
    public void advance() throws IOException {
        String line;
        currentCommand = nextLine;  // Use the current line
        nextLine = null;  // Reset nextLine to null
    
        // Keep reading lines until we find a valid command
        while ((line = reader.readLine()) != null) {
            line = line.trim();  // Clean up the line by removing leading/trailing whitespace
            
            // Skip empty lines and comments
            if (!line.isEmpty() && !line.startsWith("//")) {
                nextLine = line;  // Set nextLine to the new valid command
                return;
            }
        }
    }

    /**
     * Returns the type of the current command (a string constant):
     * @return a string constant representing the command type:
     *         - "C_ARITHMETIC" for arithmetic-logical commands
     *         - "C_PUSH" for push commands
     *         - "C_POP" for pop commands
     */
    public String commandType()
    {
        String currentCommand = this.currentCommand.trim();

        if (currentCommand == null) 
        {
            throw new IllegalStateException("No current command to analyze.");
        }
        // Checking the actual command type after triming
        
        if (currentCommand.startsWith("push")) 
        {
            return "C_PUSH";    
        }
        else if (currentCommand.startsWith("pop")) 
        {
            return "C_POP";    
        }
        else return "C_ARITHMETIC";
    }
    

    /**
     * Returns the first argument of the current command;
     * In the case of C_ARITHMETIC, the command itself is returned (string)
     */
    public String arg1()
    {
        if (currentCommand ==null) 
        {
            throw new IllegalStateException("No current command to process.");  
        }

        if (commandType().equals("C_ARITHMETIC"))
         {
            return currentCommand;    
        }

        // Now we have "complex" commands, so we need to split them
        String[] partsOfCommand = currentCommand.split("\\s+");
        if (partsOfCommand.length > 1) 
        {
            return partsOfCommand[1]; 
        }
        throw new IllegalStateException("Command does not have enough arguments.");

    }

    /**
     * Returns the second argument of the current command (int);
     * Called only if the current command is C_PUSH, C-POP, C_FUNCTION, or C_CALL
     */
    public String arg2()
    {
        if (currentCommand ==null) 
        {
            throw new IllegalStateException("No current command to process.");  
        }
        String type = commandType();
        if (!(type.equals("C_PUSH")) && !(type.equals("C_POP")) && !(type.equals("C_FUNCTION")) && !(type.equals("C_CALL"))) 
        {
            throw new IllegalStateException("arg2() can only be called for C_PUSH, C_POP, C_FUNCTION, or C_CALL commands.");
        }
        String[] partsOfCommand = currentCommand.split("\\s+");
        return partsOfCommand[2];
  
    }

    public static void main(String[] args)
    {
        try
        {
            Parser parser = new Parser (new File(args[0]));

            // Testing hasMoreCommands():
            System.out.println("Testing Testing hasMoreCommands and advance:");
            while (parser.hasMoreCommands()) 
            {
                parser.advance();
                System.out.println("Current command: " + parser.currentCommand);  
                System.out.println("Command type: " + parser.commandType()); 
                System.out.println("arg1: " + parser.arg1());

                if ((parser.commandType().equals("C_PUSH")) || (parser.commandType().equals("C_POP")) || (parser.commandType().equals("C_FUNCTION")) || (parser.commandType().equals("C_CALL"))) {
                System.out.println("arg2: " + parser.arg2());
            }
 
            }
            parser.close();
            System.out.println("Mo more commands");
        }catch (IOException e)
            {
                e.printStackTrace();
            }
        



    }

    

}
