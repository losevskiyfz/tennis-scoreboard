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
                Welcome to the Match Portal
            </h1>
            <div>
                <form action="${newMatchUrl}" method="post" onsubmit="return validateForm();">
                    <fieldset>
                        <legend>
                            New Match
                        </legend>
                        <label for="player1">
                            Player:
                            <input id="player1" name="player1" type="text" required>
                        </label>
                        <label for="player2">
                            Player:
                            <input id="player2" name="player2" type="text" required>
                        </label>
                    </fieldset>
                    <input type="submit">
                </form>
            </div>
        </main>
        <script src="./js/form-validation.js"></script>
    </body>
</html>