<!DOCTYPE html>
<html lang="en">
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>
            Tennis Scoreboard
        </title>
    </head>

    <body>
        <nav>
            <a href="${homeUrl}">Home</a>
            <a href="${matchesUrl}">Matches</a>
        </nav>
        <main>
            <h1>
                Matches
            </h1>
            <div>
                <form action="${matchesUrl}" method="GET">
                    <label for="nameSearch">Search by Name:</label>
                    <input type="text" id="nameSearch" name="filter_by_player_name" value="${findFilter}" />
                    <button type="submit">Search</button>
                </form>
            </div>
            <div>
                <table>
                    <thead>
                        <tr>
                            <th>Player 1</th>
                            <th>Player 2</th>
                            <th>Winner</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="match" items="${matches}">
                            <tr>
                                <td>${match.player1.name}</td>
                                <td>${match.player2.name}</td>
                                <td>${match.winner.name}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div>
                <a href="${prevPageUrl}">&lt;</a>
                <span>${curPage}</span>
                <a href="${nextPageUrl}">&gt;</a>
            </div>
            <div>
                <span>${error}</span>
            </div>
        </main>
    </body>
</html>
