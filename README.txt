=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: mpig
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Network I/O
    Features Implemented: Multiplayer functionality via network communication,
        allowing players to interact and exchange information about their characters.
    Justification: Network I/O is appropriate for enabling multiplayer gameplay,
        as it facilitates communication between players over a network connection.
        In the game, it allows players to ask questions about the traits of their character
        and receive responses about potential options or the correct character guessed.
        The network protocol must be sufficiently complex to handle multiple pieces of
        information and ensure non-trivial parsing. Additionally, exceptions are handled to
        prevent crashes and ensure a smooth gaming experience.

  2. Collections and/or Maps
    Features Implemented: Utilization of lists to manage players, characters, and game state.
    Justification: Using collections provides an efficient way to manage game state,
        specifically for keeping track of which characters are still possible guesses.
        They allow for dynamic resizing, efficient lookup, and easy iteration,
        which are essential for maintaining and updating game state during gameplay.

  3. Inheritance and Subtyping
    Features Implemented: Class hierarchy for characters and questions,
        with inheritance to model different character/question types and behaviors.
        Subclasses such as HumanCharacter and PetCharacter extend the base Character class
        to incorporate additional characteristics specific to each type.
        Subclasses of Question that extend the base Question abstract class
         incorporate additional characteristics specific to each type.
    Justification: Inheritance and subtyping allow for parent objects to be implemented in
        different ways, and can be extended depending on any more specific implementations of the
        game. By extending the base class, subclasses inherit common attributes and behaviors while
        being able to define and implement their own unique features, creating a more adaptable
        game. In the game, subclasses represent different types of characters and questions,
        allowing for a flexible and extensible design. Methods are overridden,
        and the parent class is not merely a placeholder but provides critical functionality,
        ensuring a meaningful use of inheritance.

  4. JUnit Testing
    Features Implemented: JUnit tests covering game mechanics, player actions, and edge cases.
    Justification: JUnit testing ensures that the game functions as expected, identifies
        potential issues or bugs, and provides confidence in the correctness of the implementation.
        It helps ensure that the game behaves as expected under various scenarios and edge cases.

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
    GuessWhoGame: Manages the main game logic, including player actions, character selection,
                    and game state.

    GameServer: Handles network communication and facilitates multiplayer functionality.

    Player: Represents a player in the game, including their secret character.

    Character: Defines the characteristics and properties of a character in the game.
      HumanCharacter: Extends Character class with additional characteristics unique to humans.
      PetCharacter: Extends Character class with additional characteristics unique to pets.

    Question: Abstract class that defines various question classes (that extend the parent class)
                that allow different traits to be asked. Employs dynamic dispatch to ensure specific
                aspects of the question are properly handled based on the type of question.
      AgeQuestion: Extends Question abstract class to allow for asking about age.
      HairColorQuestion: Extends Question abstract class to allow for asking about hair color.
      GenderQuestion: Extends Question abstract class to allow for asking about gender.
      EyeColorQuestion: Extends Question abstract class to allow for asking about eye color.
      GlassesQuestion: Extends Question abstract class to allow for asking if the character has
                        glasses.
      HatQuestion: Extends Question abstract class to allow for asking if the character has a hat.
      SpeciesQuestion: Extends Question abstract class to allow for asking about species.
      ToyQuestion: Extends Question abstract class to allow for asking if the character has a toy.

    InstructionsWindow: Opens a window with a list of all instructions needed for the game.
                            Can be closed or left open for the duration of the game.

    LoadScreen: Opens a window that allows the user to select whether to play a human or pet game.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
    This was definitely a challenge to implement, but I eventually got it to work as intended.
    It took some time to make the server model work correctly. Specifically, ensuring that
    selecting the secret character correctly updated the other player's instance secret character.
    I initially had only one way of keeping track of characters in the game (list),
    which was causing issues, but I resolved this by assigning each player their own list of
    characters, from which they could be removed during the game.
    I also had an issue with the boolean trait guesses, which I resolved by using
    Boolean.parseBoolean(), instead of Boolean.getBoolean(), which does not do as I had thought.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
    The current design has a good separation of functionality, with each class responsible for a
    specific aspect of the game. Private state is encapsulated well, taking into account all
    IntelliJ suggestions and minimizing direct access to internal data and promoting data integrity.
    While I have put a lot of time into organizing the code and making it more efficient by
    removing methods and lines that I later realized were not necessary If given the chance,
    refactoring could focus on further optimizing organization and improving readability.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
    All images were generated by DALL-E 3 for use in this project.
    I made use of JavaDocs for this project, particularly:
        https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html
        https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html
        https://docs.oracle.com/javase/8/docs/api/java/lang/Boolean.html
        https://docs.oracle.com/javase/8/docs/api/javax/security/auth/callback/Callback.html
