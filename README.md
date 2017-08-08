# textadventurelib
This project is a library that allows for the creation of text adventure based games. These games are broken down into game states with attached options. Each option has a collection of triggers and a single action. Initially provided triggers and actions are as follows

### Actions
* **Append Text** - Appends text to the text log and shows up in the game.
* **Completion** - Completes the current game state with some data (this is how you move from one game state to the next)
* **Execute** - Execution allows for the use of the runtime to run other applications such as a web browser.
* **Modify Player** - Modifies a player in the game. This allows for the change/addition or removal of any player specific property.
* **Script** - Runs some javascript. In this javascript player modifications and other modifications to the runtime can be written.
* **Finish** - Finishes the current game.
* **Save** - Saves the current game to a file.

### Triggers
* **Text** - Fires based off of a defined regular expression.
* **Player** - Fires based off of a players state,
* **MultiPart** - Fires when all sub-triggers fire (and trigger).
* **Script** - Fires when a executed javascript function returns true.

Additional actions and triggers can be created by implementing the `IAction` or `ITrigger` interfaces.

## Getting Started
### Prerequisites
Java 8 SDK should be installed on your local machine.
Maven should be installed on your local machine.

## Building
In order to build this simply run `mvn build` on the root folder.

## Testing
In order to test this simply run `mvn test` on the root folder.

## License
This project is licensed under the MIT License - see the LICENSE.md file for details.
