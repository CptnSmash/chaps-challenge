# chaps-challenge
Created for the group project component of SWEN225

This game was created by team Synthetic39 as our group project submission for SWEN225 at victoria University of Wellington, 2021.
The code here does not represent our final submission - rather, it contains some changes I have made after the fact. Most of these changes are 
limited to the Domain module, but there are some edits to the Persistency module as well.

Part of the point of the project was to teach encapsulation, and as such each team member was responsible for a different module of the overall code. 
I was responsible for the Domain module, so that's what I will discuss here.

The Domain module is responsible for the logic of the game; the rules, the "pieces" and how they interact. This kind of systems building is my passion
with regards to writing code. I'm quite pleased with how my contribution turned out, but there is one major change I would make: adding a Factory to 
handle instantiating new GameObjects. Currently, the other modules that need to utililse parts of the Domain must do so by directly invoking the 
relevant constructor, negatively impacting encapsulation. Utilising the Factory parrten could help shield the Domain, and make creating GameObjects
easier for the writers of other modules.

Credits:  
App module by Nina Wong  
Domain module by Michael Dasan  
Persistency module by Sam Milburn  
Renderer module by Sam Lockhart  
Art by Sam Lockhart
