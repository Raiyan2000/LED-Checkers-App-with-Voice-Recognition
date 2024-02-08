# LED Checkers App with Voice Recognition
![LEDCheckers_header](https://github.com/Raiyan2000/ece1145--NuclearWarElephants-YRJ-/assets/77954118/c2caaafb-a150-4f61-96bb-34f8e72fd9c4)

This project includes a 8x8 checkerboard that is made of RGB LEDs and button arrays to allow for a game
of checkers to be played. Either voice recognition or the button array allows pieces to
be moved. Each team will have their own color checker pieces. A smartphone is used
to handle voice recognition.

# Why?
The world of gaming is a dynamic and rapidly evolving field. There are important steps taken in the history of the industry from board games to electronic games which are popularized today. The first board games were created thousands of years ago. However, the recent creation of electronic games is revolutionizing the world and making great advancements. Board games such as checkerboards have remained stagnant and have slightly advanced through digitalization. Although it lets people connect remotely around the world, the features and engaging factors continue to be unchanged. The LED Checkers Game controlled with Voice Recognition project aims to create an innovative and engaging way to play the game of checkers using modern technology such as wireless voice commands, button arrays for moves, and LEDs making it more captivating to the more contemporary audience. It also aims to inclusively cater to individuals with physical impairments, enabling them to execute actions solely through their voice, eliminating the necessity for physical interaction.

# Responsibilites
I was responsible for voice recognition, app development, and Bluetooth connection for the LED checkers project.

# Checkerboard App Overview video
The video showcases the overall design of the app as well as the voice recognition functionality
[![LED-Checkers-App-with-Voice-Recognition](https://img.youtube.com/vi/CgEpL-6RNF4/0.jpg)](https://www.youtube.com/watch?v=CgEpL-6RNF4)

# Full Integration Video
This video showcases the final integration and use with the hardware/Checkerboard
[![LED-Checkers-App-with-Voice-Recognition](https://img.youtube.com/vi/_Rs1Scd7FuY/0.jpg)](https://www.youtube.com/watch?v=_Rs1Scd7FuY)
# Final Design
The latest implementation uses Android Studio and Java to implement the user interface(UI). It includes four screens: splash screen, home screen, main screen and game over screen [9].  Each screen is designed for the purpose of creating an appealing look by utilizing retro style elements but also making it simple to use. The splash screen used the “Animation” library that allowed different UI elements on the screen to be manipulated. Two animation styles were used for two parts of the screen. The top and bottom view rectangles were animated using pixel positions which made the animation come from outside the screen. The UI elements in the middle used zoom manipulation to conjure it from the center of the screen while the app loaded. Screen One in the figure shows the final look of the splash screen. This animation also required understanding of gradle build files as importing such libraries changed the dependencies and compatibility version code of the file. The rest of the screen elements were static. The main screen utilized the relative layout structure which allowed for a more stable and precise structure when downloading in mobile phones with different screen lengths. Relative layout, as the name suggests, is a grouping of UI elements that displays them with relative position to its parent. The rest of the screens used constraint layout to place the UI elements where the developer can put UI elements anywhere and must constrain them on each side. This was useful when creating screens with few elements such as the Game Over Screen shown below.

![Screenshot 2023-12-15 134059](https://github.com/Raiyan2000/LED-Checkers-App-with-Voice-Recognition/assets/77954118/be8e7c50-0941-452a-9c34-9a4b24991c6f)


The wireless communication with the app and the microcontroller was handled by the HC-05 bluetooth module. To connect with the HC-05, the user needs to press the “Get Device” and the “Connect” button to connect with the HC-05 which is in the Main Screen. The status is shown by the checkbox beside it. The HC-05 was chosen because it used a two way serial communication which was compatible with the ATmega328p microcontroller and with Android Studio. It has the Rx pin for receiving data and Tx pin for transmitting data. Each of the pins needed to be connected with the opposite Rx and Tx pins in the ATmega328p microcontroller. The figure below showcases the configuration. To connect Android Studio with the HC-05, Android Studio used a separate thread to establish and communicate the bytes of data and send it to the app’s main UI thread. Since it required a specific protocol, the thread class had to be extended to check for the buffer once when 5 bytes are sent from the microcontroller and sequentially update the main UI thread afterwards. The 5 bytes of data represented a string of 5 characters which represented 5 binary bits. The binary bits represented the current player in turn, the validity of a move, the game over state, button array in use state and the player winner state. The string was parsed and checked with priority given to each bit. For example, if the array in button use state is true or the game is over, all the other bits had to be ignored. After receiving the bits, the app had to send back a ACK string to confirm its state to the microcontroller.

![1_HC-05_Bluetooth_Module](https://github.com/Raiyan2000/LED-Checkers-App-with-Voice-Recognition/assets/77954118/3902ca2d-a851-4a4d-b54d-8a7de44d5c03)


The voice recognition model for the app uses Android’s built in voice recognizer. Although the first iterations used Google's voice recognition model, it was changed because the model was not customizable or allowed additional features. Keyphrase activation is one example that was essential to the design which could not be implemented using Google’s API. The voice recognizer uses the free form language model which proved to be the most accurate to recognize speech as the commands that are used are not in any dictionary. It is used by saying the key phrase “ready” which would make the voice recognition model to listen for 5 seconds. After the timeout, the voice recognition model will produce an output. The key phrase feature used is implemented using a custom algorithm created from scratch . The algorithm continually runs the voice recognition and resets itself every 5 seconds. When the model recognizes the key phrase, it immediately reset itself and changes a condition to true to take in input from the user for the next 5 secs. The condition is changed back to false after the output is produced so it keeps looking for the key phrase to activate again.




# Testing Results
<strong>Overall Performance:</strong>

The testing results for the checkerboard app were done both when idle and while the game was running. As mentioned before, the three key objective criteria used to analyze the performance of the app are CPU clock speed, CPU energy usage,  memory usage, CPU usage. The simplified graph above shows the overall usage using the App profiler tool.

![OveralAppUsage](https://github.com/Raiyan2000/LED-Checkers-App-with-Voice-Recognition/assets/77954118/8a312613-7515-4ce1-9e45-bdb87ac5ae24)

<strong>Voice Recognition Confidence Level:</strong>

The figure above showcases a snippet of all the confidence level of the voice recognition model using all the positions of the the board. This test was done to check if the voice recognition model had a positive correlation with the correct command and its confidence level. All 32 positions starting from (A1- A8) to (H1- H8) were checked with each position tested 10 times. The testing result confirms a positive relation. When a correct command was spoken. The confidence level was around 1.0 and the confidence level dropped to 0.6 when the command was either a word or did not follow the command pattern.

![Confidence level](https://github.com/Raiyan2000/LED-Checkers-App-with-Voice-Recognition/assets/77954118/fbbed7b7-8b15-4424-afeb-8ff1acfcfd38)

<strong>RAM Usage:</strong>

Memory performance is a critical aspect of our LED Checkers mobile app. On average, during standard gameplay, the app exhibited a low memory footprint of around 140 MB. This includes the memory required for rendering the game interface, managing game logic, and storing temporary data. Even during extended sessions, the memory usage remained consistently below 150 MB, showcasing the system's ability to handle prolonged usage without experiencing memory-related issues such as crashes or slowdowns. This was important as the voice recognition tasks took memory that stuttered the UI.

![Ram_Usage](https://github.com/Raiyan2000/LED-Checkers-App-with-Voice-Recognition/assets/77954118/7ba17a8e-8ffa-4126-ae71-6feadd9085a2)


# Impacts to Non-Technical Constraints
<strong>Global, Cultural, and Societal:</strong> There will be some difficulty in playing the game across different cultures. This is because voice recognition uses the English alphabet to make moves. Someone who is a non-English speaker will have difficulty using the voice commands. However, to circumvent this, they would be able to use the button arrays. Even though they can still play, a key function of the device is unusable.

<strong>Diversity, Equity, Inclusion:</strong> The project is more inclusive to users as opposed to a normal checkers board. The board will be more inclusive to people who are unfamiliar with some of the rules of checkers, as the board will not allow for illegal moves. Also, the capacity of the board to use voice commands allows for use by some disabled people. If a person without the use of their hands wants to play normal checkers, they will have more difficulty in moving the pieces as opposed to simply saying the desired move.

<strong>Economic:</strong> The impact of implementing these checkers boards is admittedly small, but could still impact the board game market. If the product proves to be successful, other manufacturers of physical boards may lose profits and impact the tabletop game market in a negative way for competitors.

# Documents
* [Final Project Document](https://drive.google.com/file/d/1yigWm6j0tNB7jo0eexrNAjSpP3G79IGC/view?usp=sharing)
