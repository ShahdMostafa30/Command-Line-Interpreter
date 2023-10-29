
# Java Command Line Interpreter (CLI)

This Command Line Interpreter (CLI) is a simple text-based interface for interacting with your operating system. Users can input commands through the keyboard, and the CLI will parse and execute these commands. The CLI will continue to accept user commands until the user enters "exit," at which point the CLI will terminate.

## Program Structure

The CLI program is organized into two main classes: `Parser` and `Terminal`. These classes serve distinct purposes:

1.  `Parser`: This class is responsible for parsing user input, identifying the command, and its arguments.
2.  `Terminal`: The `Terminal` class handles the execution of commands and manages the overall CLI environment.

## Supported Commands

The CLI supports the following commands:

1.  `help`: Prints the list of supported commands.
2.  `echo`: Prints the arguments passed to it.
3.  `pwd`: Prints the current working directory.
4.  `cd`: Changes the current working directory.
5.  `ls`: Lists the contents of the current directory.
6.  `ls -r`: Lists the contents of the current directory in reverse order.
7.  `cp`: Copies a file to a new location.
8.  `cp -r`: Copies a directory to a new location.
9.  `history`: Prints the last 5 commands.
10.  `mkdir`: Creates a new directory.
11.  `rmdir`: Removes an empty directory.
12.  `touch`: Creates a new file.
13.  `rm`: Removes a file.
14.  `cat`: Prints the contents of a file.
15.  `exit`: Exits the terminal.

## Usage

To use the CLI, follow these steps:

1.  Run the program.
2.  The CLI will start and display a prompt, awaiting your input.
3.  Enter commands and press Enter to execute them.
4.  The CLI will respond with the output of the executed command.
5.  Continue entering commands until you want to exit, then enter "exit."

Example:
```bash
$ ls 
File1.txt File2.txt 
$ mkdir NewDirectory 
$ cd NewDirectory 
$ pwd 
/Your/Current/Path/NewDirectory 
$ exit
```

## Dependencies

This CLI project is implemented in Java and does not require any external dependencies beyond the standard Java libraries.

## Implementation Details

The CLI is implemented in Java and uses standard libraries for file and directory operations. It parses user input, identifies the command and arguments, and executes the corresponding functions.

## Contributing

Contributions to this CLI project are welcome. Feel free to fork the repository and create pull requests for improvements or additional features.

## License

This CLI project is open-source and available under the [MIT License](./LICENSE).

## Contributors

This CLI program was developed with contributions from the following team members:

-   ***[Youssef Morad](https://github.com/YoussefMorad1)***
-   ***[Shahd Mostafa](https://github.com/ShahdMostafa30)***
-   ***[Maryam Osama](https://github.com/maryamosama33)***
-   ***[Shahd Osama](https://github.com/shahdosama10)***

Thank you for using our Command Line Interpreter!
