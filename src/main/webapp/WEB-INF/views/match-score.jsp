<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"
    />
    <title>
        Tennis Scoreboard
    </title>
</head>

<body>
<main>
    <h1>
        Current Match
    </h1>
    <div>
        <table border="1">
            <thead>
            <tr>
                <th>Player</th>
                <th>Sets</th>
                <th>Games</th>
                <th>Points</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>${p1}</td>
                <td>${p1Sets}</td>
                <td>${p1Games}</td>
                <td>${p1Points}</td>
                <td><button>Score</button></td>
            </tr>
            <tr>
                <td>${p2}</td>
                <td>${p2Sets}</td>
                <td>${p2Games}</td>
                <td>${p2Points}</td>
                <td><button>Score</button></td>
            </tr>
            </tbody>
        </table>
    </div>
</main>
</body>

</html>