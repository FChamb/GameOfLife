CS1006 Practical 2 - Game of Life

Description:
This program contains a multitude of classes which work together to create a grid object that follows the
rules and specifications of Game of Life. On top of this, assets to create a visual overlay are provided
and used to enable the user to fully interact with the game. Should the use choose, there are key bound
functions which also allow control.

Usage:
Run the following commands in order to operate game:
1. javac @files
2. java GameOfLife.java

Keyboard Inputs:
Space ( ) - play/pause
Right Arrow (->) - step one generation
Shift + Right Arrow (↑ + ->) - fast-forward skip
Left Arrow (<-) - step one generation backwards {hold to continuously move}
Up Arrow (↑) - increase speed {hold for continuous increase}
Down Arrow (↓) - decrease speed {hold for continuous decrease}
G Key (G) - toggle grid lines
R Key (R) - randomize the grid
C Key (C) - clear the grid
Ctrl + S (Ctrl + S) - save game state menu
Ctrl + O (Ctrl + O) - load game state menu
Ctrl + H (Ctrl + H) - change game rule menu
Ctrl + U (Ctrl + U) - change board size menu
Left Click Mouse - press on screen button or change dead cell alive
Right Click Mouse - change alive cell dead

Button Types:
Grey Two Left Arrows - step one generation backwards
Red Square - pause
Red Right Arrow - play
Grey Right Square + Line - step one generation
Grey Two Right Arrows - fast-forward skip
Grey Upwards Arrow