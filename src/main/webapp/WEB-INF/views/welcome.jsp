<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="./tennis-scoreboard/css/welcome.css">
    <title>Tennis Scoreboard</title>
</head>
<body>
<video autoplay muted loop class="video-background">
        <source src="./tennis-scoreboard/video/tennis-match.mp4" type="video/mp4">
        Your browser does not support the video tag.
</video>
<main>
    <h1>Welcome to the Match Portal</h1>
    <div>
        <a href="${newMatchUrl}"><button>Start a new match</button></a>
        <a href="${matchesUrl}"><button>Show results</button></a>
    </div>
</main>
</body>
</html>