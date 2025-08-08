import java.io.File;
import java.io.IOException;



public class Main {
    public static void main(String[] args) {
        // Retrieve the input VM file path from args[0]
        String inputFilePath = args[0];
        File inputFile = new File(inputFilePath);

        // Create the corresponding output ASM file path
        String outputFilePath = inputFilePath.replace(".vm", ".asm");
        File outputFile = new File(outputFilePath);

        // Initialize the Parser and CodeWriter objects
        Parser parser = null;
        CodeWriter codeWriter = null;

        try {
            // Create the Parser to read the input VM file
            parser = new Parser(inputFile);
            // Create the CodeWriter to write to the output ASM file
            codeWriter = new CodeWriter(outputFile);

            // Iterate through the input file and generate assembly code
            while (parser.hasMoreCommands()) {
                parser.advance(); // Move to the next command

                // Convert string command type to CommandType enum
                CommandType commandType = CommandType.valueOf(parser.commandType().toUpperCase());

                switch (commandType) {
                    case C_ARITHMETIC:
                        // For arithmetic commands (e.g., add, sub, neg, etc.)
                        codeWriter.writeArithmetic(parser.arg1());
                        break;

                    case C_PUSH:
                    case C_POP:
                        // For push/pop commands
                        int index = Integer.parseInt(parser.arg2());
                        codeWriter.writePushPop(commandType, parser.arg1(), index);
                        break;
                }
            }

            // Close the CodeWriter once all commands are processed
            codeWriter.close();
            System.out.println("Translation completed successfully!");

        } catch (IOException e) {
            System.err.println("Error during translation: " + e.getMessage());
            System.exit(1);
        }
    }
}





