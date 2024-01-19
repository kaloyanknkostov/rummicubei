# rummicubei
The board game Rummikub made in java for the purpose of AI training Maastricht University DACS 2 year Project.
Done by group 8: <br />
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
Pytorch has to be installed separately as it is dependent in the system. For more info
refer to the [documentation](https://pytorch.org/get-started/locally/).


## How to run

To initiate our program, the user needs to navigate to src -> main -> java -> com -> gameEngine and open the gameEngine class. After clicking the run button, the user gets a choice between single player and multiplayer modes. Single player allows for playing against our ai agents. When the user clicks the single player button, he gets transfered to the name selection page. If the textfields are not empty, the user should click the "start game" button in order to initiate the game. If the user choses multiplayer mode, he first needs to select the number of players (between 2 and 4). After submitting, the name selection page appears again. In multiplayer mode, there is an additional constraint that the nicknames of 2 players cannot be the same. After clicking "start game button", the game starts. 

## Structure

- `OurCode`: The folder containing all our code.
- `jarFile`: The folder containing jar file of our project.
- `Readme.txt`: Our readme in a txt format.
