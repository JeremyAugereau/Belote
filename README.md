# Belote
## Introduction
This project has as goal to create an AI for a game. We decided to create a game based on "Belote" with a lot of simplifications. 

## Rules
Just like classic belote, our game is based on a system of tricks: during each trick
each player plays a card, and at the end of the trick, the player who played the highest card wins the points and has the hand for the next trick. This continues until the players have no more cards to play.
cards to play. At the end of a game the player with the most points wins the game.  

Each player has three types of cards: _hand_ cards, _table_ cards and _secret_ cards.
When a player has to play, he can choose to play a card from his hand or from his table. 
At the beginning of the game, each player has as many cards in his table as he has secret cards, and both players have as many cards as each other, distributed in the same way. The table cards are face up for all to see. The secret cards are face down, hidden from everyone and one secret card
is placed underneath each table card. Each time a table card is played, the corresponding secret 
card (if any) underneath is revealed and takes its place. The cards
 in the hand are not known by the player in question


## How to use
### Parameters 

Run the program with two parameters :
- `nbIteration` the number of iteration made by the bot during the training
- `player` "p1" if you want to start , "p2" if you want the AI to start

### AI loading
If the AI as not been trained already for this number of iterations, it will train and save this result in order not have to train each time.

### Playing
Before you play all the informations about the current round are displayed in the console. This includes the previous card played during the current round if you are second, and the card(s) in the enemy _table_ .

Each round the possible cards you can play are displayed. Enter value the starting from zero to select the card you want to play.