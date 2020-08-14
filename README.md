# Asset-Management-Blockchain

Technologies used for creation:<br /> Itellij idea

Concept:<br /> This is a prototype for a blockchain in which a user can buy and sell assets to a central bank which has unlimited funds and limited properties (more assets can be added by changing the source code). Peer to peer was not implemented due to lack of central server.

Working:<br />
As soon as the user runs the program, 2 options are presented to the user: Login and Register. As long as the program is running, the database of the user will not be erased and there cannot be 2 same usernames. <br />
After the user logs into the system, the user is presented with 8 options:<br />
Buy Assets: A list of assets with respective prices appears. You cannot proceed to buy an asset if you don't have sufficient balance in your wallet.<br />
Show my transcations: Loops throught the whole blockchain searching for all the users transactions using the user's public key.<br />
Validate : Let's user mine a block with difficulty 5.<br />
Add Money: Creates a transaction from bank to user.<br />
Sell Assets: Sell Assets to the bank.<br />
Balance: Shows user's wallet balance.<br />
My Assets: Shows all assets currently owned by the user

