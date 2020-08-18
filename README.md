# Gdx_Snake_Game
Authors:  Yihui Wang, Paul Takem, Zhanyao Zhang, Micheal Okoro 
## Description
Several snake games exist, but all represents their origin from the normal “Snake game” concepts where the player manoeuvres a line which grows in length, with the line itself being a primary obstacle. The concept originated in the 1976 arcade game Blockade. Our game is based on this game and adds much more features than it. 
We create a movable snake, with food and enemies on the screen, and there are some special apples which could give the snake more strength such as speeding up, the ability to kill the enemies and being invisible to the enemies. The player uses the arrow keys to provide directions of movements. When the snake eats an apple, it increases in length, and the goal of our game is to make the snake as long as possible in a limited time. The game over when the snake meets enemies or time is up. 
## Game Interface
![](https://github.com/jameswyh/Gdx_Snake_Game/blob/master/SnakeInterface/Picture1.png)
## Design and Implementation
### Game Screen
The main class of the game. It is used to deal with the main function of the game. In this class, we use many variables to control different values, such as the state of game, the speed of the snake, the length of movement, the position and direction of the snake. Many Booleans are used to judge different conditions, such as whether the snake is dead, whether the snake has the abilities to speed up and kill enemies. The methods in this class control the whole concept of the game. These are used to check many different conditions during the game. 
The Boolean “Speedup” and “disappear” are to deal with the S-apple and P-apple. If the snake eats a S-apple, the “Speedup” will be true, and the method SpeedUp() will do the speeding up effect. So does the P-apple.
### Body part
Body part class is one of the most important class in this game, which is used to handle the length of the snake body. An array is used in this class to deal with the body part. When the snake eats an apple, a new body will be added to the array and will be showed on the screen. The UpdateBodyPosition() will always update the position of the body parts, which makes the body move following the snake head.  
### Apple
Apple class deals with the apple in the game. It includes the position and time of the apple, which decides when and where an apple should be placed. The TIME and timer decide how long the apples will appear on the screen.
### Enemy
This is used to handle the enemies in the game. It includes the position, time, and direction of the enemies, which decides when and where an enemy should be placed and which direction an enemy should move.
### UML
![](https://github.com/jameswyh/Gdx_Snake_Game/blob/master/SnakeInterface/uml1.png)
![](https://github.com/jameswyh/Gdx_Snake_Game/blob/master/SnakeInterface/uml2.png)


