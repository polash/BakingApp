# BakingApp
An app to view video recipes. Handling media loading, verifying user interfaces with UI tests, integrated third party libraries and provides a complete UX with home screen Widgets.

Functionality
This app fetches data from https://go.udacity.com/android-baking-app-json and perform backgroud task with Async Task to get data in a separate list of Recipe, Ingredients and Steps. Shows video recipe with ExoPlayer and android media.

By clicking on the recycleview in the main screen user will see the details of that reipe ingredients and steps. Steps and Ingredients both are in recycle view to show the details video of steps. if there is no video app will show just steps description from the JSON data. This app also has tablet mode view with beautiful implementation of fragment class.

App contains stack widget in setting in the home screen which will fetch data and shows the steps in a list for all the items that are avalibale in the JSON data. Using GSON library in the widget class it fetches the data to show in the screen widget.

