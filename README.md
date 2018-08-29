# OlearisWeather
GoogleMap-Retrofit-OpenWeatherMap-Demo

Android sample application using Retrofit (http://square.github.io/retrofit/) to do GoogleMap & OpenWeatherMap API calls.

The application consists of 3 screens:
- Places (by default empty, at the bottom Add place button - to add a new one
place / locality),
- Map - map for selecting coordinates (by default, the marker captures your
current location, with the Ok and Cancel buttons to confirm the new
coordinates),
- Weather - in the place selected on the map (default is empty, name
place / locality appears after its selection on the screen with the map, at the bottom
Check weather button - to request current weather).
The principle of the application:
The user enters the application and enters the Places screen.
The screen is Places (the first time it is empty).
To add a new point, the user clicks on Add place and goes to the screen
Map, clicks on the map, removes the past marker, selects a new point and clicks OK.
Then it returns to the Places screen, in which the selected place / populated
the item has already been saved.
On the Places screen, the user clicks on the saved location. The screen is displayed.
Weather.
The Weather screen - contains a list of saved weather results
the first run is empty).
By clicking on the Check weather button, a request is made to https://openweathermap.org
about the current weather in the place, with the given coordinates. Weather data are displayed on the
screen as a list item. Each subsequent click adds data to the database, and
in the list on the screen forming a weather history in the specified place.
