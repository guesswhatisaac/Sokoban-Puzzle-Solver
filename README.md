SokoBot: The Intelligent Crate Pusher
======================================
Unlocking Sokoban puzzles with an advanced A* search algorithm and intuitive Java Swing GUI.

--------------------------------------------------------------------------------

🌟 PROJECT OVERVIEW
------------------

SokoBot is a Java-based application that features an intelligent solver capable of finding optimal (or near-optimal) solutions to complex Sokoban puzzles. The project leverages the A* search algorithm, enhanced with a custom heuristic and a deadlock detection mechanism, to efficiently navigate the vast state space of Sokoban. The user interface is built with Java Swing, offering a "bot solve" mode to visualize the solver's path.


🎯 MOTIVATION
------------

The primary motivations behind developing SokoBot include:

  * Exploring Search Algorithms: To gain hands-on experience in implementing and optimizing informed search algorithms like A* in a practical problem-solving context.
  * Understanding Game AI: To delve into the challenges of designing AI for classic puzzle games, specifically addressing state representation, movement validation, and deadlock identification.
  * Java GUI Development: To apply Java Swing for creating an interactive and visually appealing graphical user interface.
  * Object-Oriented Design: To practice modular and maintainable code through a well-structured object-oriented approach.


🛠️ TECHNOLOGIES USED
-------------------

  * Java (JDK 8+): The core programming language for the entire application.
  * Java Swing: For building the graphical user interface components ('JFrame', 'JPanel', 'KeyListener', 'ActionListener', 'Timer').
  * A* Search Algorithm: The chosen pathfinding algorithm for the Sokoban solver, implemented in 'Search.java'.
  * Custom Heuristics: Used within the 'Node' class to estimate the cost from the current state to the goal.
  * Deadlock Detection: A crucial optimization implemented in 'DeadlockDetector.java' to prune unreachable or unsolvable states, significantly improving solver performance.
  * File I/O: Utilized by 'FileReader.java' to load Sokoban map configurations from text files ('.txt').
  * Image Assets: PNG files ('brick.png', 'crate.png', 'crategoal.png', 'goal.png', 'robot.png') for rendering game elements.


🚀 GETTING STARTED
-----------------

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

  * Java Development Kit (JDK) 8 or higher:
    Ensure you have Java installed. You can download it from the official Oracle website or use OpenJDK.
    To check your Java version, open a terminal or command prompt and run:

        java -version
        javac -version

### Installation

1.  Clone the repository:

        git clone https://github.com/[YOUR_USERNAME]/[YOUR_REPO].git
        cd [YOUR_REPO]

2.  Project Structure:
    The project is structured into several packages:

      * 'main': Contains the 'Driver' class, the application's entry point.
      * 'gui': Contains GUI-related classes ('GameFrame', 'GamePanel').
      * 'reader': Contains 'FileReader' and 'MapData' for map parsing.
      * 'solver': Contains the core Sokoban logic ('GameLogic', 'Node', 'Position'), the search algorithm ('Search'), the deadlock detector ('DeadlockDetector'), and the bot entry point ('SokoBot').
      * 'maps': (Expected directory) This directory should contain your '.txt' map files.

### Compiling and Running

Navigate to the root of the cloned repository in your terminal.

1.  Compile the Java source files:

        javac -d . src/main/*.java src/gui/*.java src/reader/*.java src/solver/*.java

    (Note: The '-d .' option compiles all '.java' files from the 'src' directory and places the compiled '.class' files directly into their respective package directories under the current directory. Ensure your 'src' directory structure matches the package declarations.)

2.  Run the application:
    You will need to provide a map name. Maps are expected to be in a 'maps/' directory relative to where you run the command. Create a 'maps' folder in your project root if it doesn't exist and place your '.txt' map files there (e.g., 'sample.txt').

    #### Example map ('maps/sample.txt'):

        #####
        #@$.#
        #####

    To run the bot solver:

        java main.Driver sample bot


🎮 USAGE
-------

In this mode, the SokoBot AI will attempt to find a solution to the puzzle and then animate the solution.

  * Start: Run the application with the 'bot' argument: 'java main.Driver <map_name> bot'
  * Execution: The bot will automatically start searching for a solution.
  * Animation: Once a solution is found, the bot will animate the sequence of moves.
  * Status Display: The 'GamePanel' will show the search status (e.g., "Searching...", "Solution Found!", "No Solution Found.") and the elapsed time. The 'debug.txt' file will also contain detailed logs of the solver's operations.
