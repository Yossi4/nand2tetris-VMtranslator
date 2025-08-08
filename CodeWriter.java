import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Thid calls generates assembly code from the parsed VM command.
public class CodeWriter {

    private PrintWriter outFile; // The file where the commands will be in assembly language.
    private int counter; // Indector of a loop.
    private File inputFile; // The file where the commands are in VM language.

    // Opens the output file/stream and gets ready yo write into it.
    public CodeWriter(File file) {
        this.counter = 0;
        try {
            outFile = new PrintWriter(file);
            this.inputFile = file;
        }

        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Writes o the output file the assembly code that implements the given arithmetic-logical command.
    public void writeArithmetic(String cmd) {
        String template = "@SP\n" +"AM=M-1\n" +"D=M\n" +"A=A-1\n";
        switch(cmd) {
            case "add":
                outFile.print(template + "M=D+M\n");
                break;
            case "sub":
                outFile.print(template + "M=M-D\n");
                break;
            case "and":
                outFile.print(template + "M=D&M\n");
                break;
            case "or":
                outFile.print(template + "M=D|M\n");
                break;
            case "gt":
                outFile.print(arithmeticLogicalTemplate("JLE"));
                counter++;
                break;
            case "lt":
                outFile.print(arithmeticLogicalTemplate("JGE"));
                counter++;
                break;
            case "eq":
                outFile.print(arithmeticLogicalTemplate("JNE"));
                counter++;
                break;
            case "not":
                outFile.print("@SP\nA=M-1\nM=!M\n");
                break;
            case "neg":
                outFile.print("D=0\n@SP\nA=M-1\nM=D-M\n");
                break;
            default:
                throw new IllegalArgumentException("Call writeArithmetic() for a non-arithmetic command");
        }
    }

    // Writes to the output file the assembly code that implements the given push or pop command.
    public void writePushPop(CommandType command, String seg, int index) {
        switch (command) {
            case C_PUSH:
                switch (seg) {
                    case "constant":
                        outFile.print("@" + index + "\n" + "D=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
                        break;
                    case "local":
                        outFile.print(pushTemplate("LCL", index, false));
                        break;
                    case "argument":
                        outFile.print(pushTemplate("ARG", index, false));
                        break;
                    case "this":
                        outFile.print(pushTemplate("THIS", index, false));
                        break;
                    case "that":
                        outFile.print(pushTemplate("THAT", index, false));
                        break;
                    case "temp":
                        outFile.print(pushTemplate("R5", index + 5, false));
                        break;
                    case "pointer":
                        if (index == 0) {
                            outFile.print(pushTemplate("THIS", index, true));
                        } else if (index == 1) {
                            outFile.print(pushTemplate("THAT", index, true));
                        }
                        break;
                    case "static":
                        outFile.print(pushTemplate(inputFile.getName() + "." + index, index, true));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown segment: " + seg);
                }
                break;
        
            case C_POP:
                switch (seg) {
                    case "local":
                        outFile.print(popTemplate("LCL", index, false));
                        break;
                    case "argument":
                        outFile.print(popTemplate("ARG", index, false));
                        break;
                    case "this":
                        outFile.print(popTemplate("THIS", index, false));
                        break;
                    case "that":
                        outFile.print(popTemplate("THAT", index, false));
                        break;
                    case "temp":
                        outFile.print(popTemplate("R5", index + 5, false));
                        break;
                    case "pointer":
                        if (index == 0) {
                            outFile.print(popTemplate("THIS", index, true));
                        } else if (index == 1) {
                            outFile.print(popTemplate("THAT", index, true));
                        }
                        break;
                    case "static":
                        outFile.print(popTemplate(inputFile.getName() + "." + index, index, true));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown segment: " + seg);
                }
                break;
        
            default:
                throw new IllegalArgumentException("Call writePushPop() for a non-pushpop command");
        }
        

    }

    // Closes the output file.
    public void close() {
        outFile.close();
    }

    // A function that returns the commands in assembly language when there is a condition and a loop.
    private String arithmeticLogicalTemplate(String type) {
        return "@SP\n" + "AM=M-1\n" + "D=M\n" + "A=A-1\n" + "D=M-D\n" + "@FALSE" + counter + "\n" + "D;" + type + "\n" + "@SP\n" +
                "A=M-1\n" + "M=-1\n" + "@CONTINUE" + counter + "\n" + "0;JMP\n" + "(FALSE" + counter + ")\n" + "@SP\n" + "A=M-1\n" +
                "M=0\n" + "(CONTINUE" + counter + ")\n";
    }

    // A function that returns the commands in assembly language when the command is a push.
    private String pushTemplate(String str, int index, boolean isDirect) {
        String stringNoPointer = "";
            if(!isDirect) {
                stringNoPointer = "@" + index + "\n" + "A=D+A\nD=M\n";
            }

            return "@" + str + "\n" + "D=M\n"+ stringNoPointer + "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
    }

    // A function that returns the commands in assembly language when the command is a pop.
    private String popTemplate(String str, int index, boolean isDirect) {
        String noPointer = "";
        if(isDirect) {
            noPointer = "D=A\n";
        }

        else {
            noPointer = "D=M\n@" + index + "\nD=D+A\n";
        }

        return "@" + str + "\n" + noPointer + "@R13\n" + "M=D\n" + "@SP\n" + "AM=M-1\n" + "D=M\n" + "@R13\n" + "A=M\n" + "M=D\n";
    }   


}


