# Sentiment Analyzer App

This is an application instance of the sentiment classifier I've been working on (see https://github.com/benjamintli/twitter_sentiment_classifier_keras).
It runs a simple post request to the endpoint hosted on heroku. This is done using a library called RetroFit, developed by Square and converted using the GSON converter developed by Google.
The app will grab a user's input and post it to the endpoint. It'll return back an accuracy value between 0 and 1, and a general polarity (positive or negative). The user is notified of this through an alertDialog
