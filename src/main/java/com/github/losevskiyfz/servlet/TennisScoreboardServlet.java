package com.github.losevskiyfz.servlet;

import com.github.losevskiyfz.cdi.ApplicationContext;
import com.github.losevskiyfz.dto.CurrentMatchDto;
import com.github.losevskiyfz.dto.TwoPlayersRequest;
import com.github.losevskiyfz.service.CurrentMatchesService;
import com.github.losevskiyfz.service.MatchService;
import com.github.losevskiyfz.validation.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import static com.github.losevskiyfz.servlet.TennisScoreboardServlet.*;

@WebServlet(urlPatterns = {WELCOME_URL, NEW_MATCH_URL, MATCH_SCORE_URL})
public class TennisScoreboardServlet extends HttpServlet {
    public static final String ROOT_URL = "/tennis-scoreboard";
    public static final String NEW_MATCH_URL = "/new-match";
    public static final String MATCH_SCORE_URL = "/match-score";
    public static final String WELCOME_URL = "/welcome";
    private static final Logger LOG = Logger.getLogger(TennisScoreboardServlet.class.getName());

    private final ApplicationContext context = ApplicationContext.getInstance();
    private final MatchService matchService = context.resolve(MatchService.class);
    private final CurrentMatchesService currentMatchesService = context.resolve(CurrentMatchesService.class);
    private final Validator validator = context.resolve(Validator.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals(ROOT_URL + WELCOME_URL)) {
            LOG.info(String.format("GET request to %s", ROOT_URL + WELCOME_URL));
            req.setAttribute("newMatchUrl", ROOT_URL + NEW_MATCH_URL);
            req.setAttribute("matchScoreUrl", ROOT_URL + MATCH_SCORE_URL);
            req.getRequestDispatcher("/WEB-INF/views/welcome.jsp").forward(req, resp);
        } else if (req.getRequestURI().equals(ROOT_URL + NEW_MATCH_URL)) {
            LOG.info(String.format("GET request to %s", ROOT_URL + NEW_MATCH_URL));
            req.setAttribute("newMatchUrl", ROOT_URL + NEW_MATCH_URL);
            req.getRequestDispatcher("/WEB-INF/views/new-match.jsp").forward(req, resp);
        } else if (
                req.getRequestURI().startsWith(ROOT_URL + MATCH_SCORE_URL) &&
                        !req.getParameter("uuid").isEmpty() &&
                        req.getParameter("uuid") != null
        ) {
            String uuid = req.getParameter("uuid");
            LOG.info(String.format("GET request to %s?uuid=%s", ROOT_URL + MATCH_SCORE_URL, uuid));
            CurrentMatchDto currentMatchDto =
                    currentMatchesService.getMatch(UUID.fromString(uuid)).orElseThrow();
            req.setAttribute("p1", currentMatchDto.getP1().getName());
            req.setAttribute("p2", currentMatchDto.getP2().getName());
            req.setAttribute("p1Games", currentMatchDto.getP1Games());
            req.setAttribute("p2Games", currentMatchDto.getP2Games());
            req.setAttribute("p1Sets", currentMatchDto.getP1Sets());
            req.setAttribute("p2Sets", currentMatchDto.getP2Sets());
            req.setAttribute("p1Points", currentMatchDto.getP1Points());
            req.setAttribute("p2Points", currentMatchDto.getP2Points());
            req.getRequestDispatcher("/WEB-INF/views/match-score.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals(ROOT_URL + NEW_MATCH_URL)) {
            String p1 = req.getParameter("player1");
            String p2 = req.getParameter("player2");
            LOG.info(String.format("POST request to %s: player1:%s, player2:%s", NEW_MATCH_URL, p1, p2));
            TwoPlayersRequest playerPair = TwoPlayersRequest.builder()
                    .player1(p1)
                    .player2(p2)
                    .build();
            validator.validate(playerPair);
            CurrentMatchDto currentMatchDto = matchService.newMatch(playerPair.getPlayer1(), playerPair.getPlayer2());
            UUID matchUuid = UUID.randomUUID();
            currentMatchesService.addMatch(matchUuid, currentMatchDto);
            resp.sendRedirect(String.format("%s?uuid=%s", ROOT_URL + MATCH_SCORE_URL, matchUuid));
        }
    }
}
