# rummicubei
This is an AI robot that can play the game of Rummikub. The game is made in Java. The robot consists of 2 parts
Monte Carlo tree search that can find and play the best move possible and a prediction of opponents tiles made by ml to
solve the problem of impartial information.

Done by group 8 DACS Maastricht 2023/2024: <br />
-Frederik Grüneberg <br />
-Jakub Suszwedyk <br />
-Kees van den Eijnden <br />
-Kasper van der Horst <br />
-Kaloyan Kostov <br />
-Konrad Paszyński <br />
-Julius Verschoof <br />

## Prerequisites
- java
- maven
### Python prerequisites

Python version: 3.10.4

Install the required packages with `pip install -r requirements.txt`.
Pytorch has to be installed separately as it is dependent on the system. For more info
refer to the [documentation](https://pytorch.org/get-started/locally/).


## How to run
Make sure Python is set up and Python prerequisites are installed properly

### JAR file
Download the jar file and launch it you will be trowed in a game selection menu where you can play against other people
on the same device or more importantly, if you choose single-player mode you will start a game vs our AI and you can try
and beat it

### Build from source
You can also download the whole source code and run it from your favorite editor.
rummicubei/src/main/java/com/gameEngine/GameEngine.java is the file you need to run to achieve the same result as the jar
file

### Bot selection
In the main method of the game you can change the bot from baseline to mcts or mcts+ml beware that mcts take some time to
generate a move