function validateForm() {
    const player1 = document.getElementById('player1').value;
    const player2 = document.getElementById('player2').value;

    if (!isValidPlayerName(player1, "1") || !isValidPlayerName(player2, "2")) {
        return false;
    }
    if (player1 === player2) {
        alert("Player can't play with himself");
        return false;
    }
    return true;
}

function isValidPlayerName(name, playerNum) {
    if (!name) {
        alert('Player ' + playerNum + ' name cannot be empty');
        return false;
    }
    if (name.length < 3 || name.length > 50) {
        alert('Player ' + playerNum + ' name must be 2-50 characters long');
        return false;
    }
    if (!/^[\p{L}'\- ]+$/u.test(name)) {
        alert('Player ' + playerNum + ' name may contain only letters, apostrophes, hyphens, and spaces');
        return false;
    }
    if (!/^\p{L}.*\p{L}$/u.test(name)) {
        alert('Player ' + playerNum + ' name must start and end with a letter');
        return false;
    }
    if (/.*[ '\-]{2,}.*/.test(name)) {
        alert('Player ' + playerNum + ' name may not contain consecutive non-letter characters');
        return false;
    }
    return true;
}