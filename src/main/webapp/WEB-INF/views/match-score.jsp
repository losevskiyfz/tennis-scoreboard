<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>
            Tennis Scoreboard
        </title>
        <link rel="stylesheet" href="./css/match-score.css" />
    </head>
    <body>
        <nav>
            <a href="${homeUrl}">Home</a>
            <a href="${matchesUrl}">Matches</a>
        </nav>
        <main>
            <h1>
                Current Match
            </h1>
            <div>
                <table>
                    <thead>
                        <tr>
                            <th>Player</th>
                            <th>Sets</th>
                            <th>Games</th>
                            <th>Scores</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>${match.p1Name}</td>
                            <td>${match.p1Sets}</td>
                            <td>${match.p1Games}</td>
                            <td>${match.p1Scores}</td>
                            <td>
                                <form method="POST" action="/tennis-scoreboard/match-score?uuid=${matchUuid}">
                                    <input type="hidden" name="playerNumber" value="1" />
                                    <button type="submit">Score</button>
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <td>${match.p2Name}</td>
                            <td>${match.p2Sets}</td>
                            <td>${match.p2Games}</td>
                            <td>${match.p2Scores}</td>
                            <td>
                                <form method="POST" action="/tennis-scoreboard/match-score?uuid=${matchUuid}">
                                    <input type="hidden" name="playerNumber" value="2" />
                                    <button type="submit">Score</button>
                                </form>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div>
                <span>${error}</span>
            </div>
        </main>
    </body>
</html>
