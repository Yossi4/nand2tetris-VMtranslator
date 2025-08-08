# Nand2Tetris — VM Translator (Java)

Java implementation of the VM Translator from the [Nand2Tetris](https://www.nand2tetris.org/) course.  
Translates `.vm` virtual machine code files into Hack assembly code `.asm`.

## Features
- Parses VM commands including arithmetic, memory access, program flow, and function calls
- Supports multiple `.vm` files translation
- Produces valid Hack assembly code compatible with the Hack CPU emulator
- Includes a Makefile and a `VMtranslator` shell script for easy execution

## Build & Run

### Using Makefile & VMtranslator script (recommended)
```bash
# Compile the project
make

# Run the translator
./VMtranslator path/to/YourFile.vm

