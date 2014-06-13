Update on 6/12/14:

 - Fixed a couple of bugs:
   - app was crashing when Home button was pressed
   - preset tip percentage and number of people were not loaded from the file correctly
 - Changed IDs of the different views to adhere to the naming convention
 - Cleaned up the layout by editing the activity XML directly
 - Added custom font
 - Generated new GIF using LiceCap

/!\ Note: Couldn't get images to work within TextView properly, as described in https://github.com/thecodepath/android_guides/wiki/Working-with-the-TextView#displaying-images-within-a-textview. Not sure if they can be scaled to fit the text area.



This is an Android application to calculate tip for a given transaction amount, tip percentage, and number of users to split the tip with.

Time spent: 6 hours in total

Completed User Stories:

 - Required: User is displayed the tip of specified percentage for specified entered amount
 - Required: User enters the total amount of the transaction
 - Required: User can select between tip amounts (i.e 10%, 15%, 20%)
 - Required: Upon selecting tip amount, formatted tip value is displayed
 - Optional: User changes the total amount and updated tip is reflected automatically
 - Optional: User can select custom tip percentage if desired
 - Optional: User can select how many ways to split the tip
 - Optional: User can edit preset tip percentages and have them persist across launches

/!\ Note: The preset amount is being persisted to the file correctly but when the app if loaded, somehow the NumberPickers for tip and users are not taking the values from the file.

 - Optional: Experiment with trying input widgets to replace the buttons and/or textviews


Walkthrough of all user stories:

![Video Walkthrough](TipCalculator.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/). 
