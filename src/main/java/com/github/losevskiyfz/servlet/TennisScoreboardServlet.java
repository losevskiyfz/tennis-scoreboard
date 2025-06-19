package com.github.losevskiyfz.servlet;

import com.github.losevskiyfz.cdi.ApplicationContext;
import com.github.losevskiyfz.dto.*;
import com.github.losevskiyfz.entity.Match;
import com.github.losevskiyfz.exception.BadPlayerNameException;
import com.github.losevskiyfz.exception.GetMatchException;
import com.github.losevskiyfz.exception.PostScoreException;
import com.github.losevskiyfz.exception.WrongPlayerNumberException;
import com.github.losevskiyfz.mapper.MatchMapper;
import com.github.losevskiyfz.service.MatchScoreCalculationService;
import com.github.losevskiyfz.service.OngoingMatchesService;
import com.github.losevskiyfz.service.MatchesPersistenceService;
import com.github.losevskiyfz.validation.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static com.github.losevskiyfz.servlet.TennisScoreboardServlet.*;

@WebServlet(urlPatterns = {WELCOME_URL, NEW_MATCH_URL, MATCH_SCORE_URL, ENDED_MATCHES_URL})
public class TennisScoreboardServlet extends HttpServlet {
    public static final String ROOT_URL = "/tennis-scoreboard";
    public static final String NEW_MATCH_URL = "/new-match";
    public static final String MATCH_SCORE_URL = "/match-score";
    public static final String ENDED_MATCHES_URL = "/matches";
    public static final String WELCOME_URL = "/welcome";
    private static final Logger LOG = Logger.getLogger(TennisScoreboardServlet.class.getName());
    private static final int PAGE_SIZE = 5;

    private final MatchMapper matchMapper = MatchMapper.INSTANCE;
    private final ApplicationContext context = ApplicationContext.getInstance();
    private final OngoingMatchesService ongoingMatchesService = context.resolve(OngoingMatchesService.class);
    private final Validator validator = context.resolve(Validator.class);
    private final MatchesPersistenceService matchesPersistenceService = context.resolve(MatchesPersistenceService.class);
    private final MatchScoreCalculationService matchScoreCalculationService = context.resolve(MatchScoreCalculationService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals(ROOT_URL + WELCOME_URL)) {
            handleGetWelcomePage(req, resp);
        } else if (req.getRequestURI().equals(ROOT_URL + NEW_MATCH_URL)) {
            handleGetNewMatch(req, resp);
        } else if (req.getRequestURI().startsWith(ROOT_URL + MATCH_SCORE_URL)) {
            handleGetMatchScore(req, resp);
        } else if (req.getRequestURI().startsWith(ROOT_URL + ENDED_MATCHES_URL)) {
            handleGetEndedMatchesUrl(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (req.getRequestURI().equals(ROOT_URL + NEW_MATCH_URL)) {
            handlePostNewMatchUrl(req, resp);
        } else if (req.getRequestURI().startsWith(ROOT_URL + MATCH_SCORE_URL)) {
            handlePostMatchScore(req, resp);
        }
    }

    private void handleGetWelcomePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info(String.format("GET request to %s", ROOT_URL + WELCOME_URL));
        req.setAttribute("newMatchUrl", ROOT_URL + NEW_MATCH_URL);
        req.setAttribute("matchesUrl", ROOT_URL + ENDED_MATCHES_URL);
        req.setAttribute("homeUrl", ROOT_URL + WELCOME_URL);
        req.getRequestDispatcher("/WEB-INF/views/welcome.jsp").forward(req, resp);
    }

    private void handleGetNewMatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info(String.format("GET request to %s", ROOT_URL + NEW_MATCH_URL));
        req.setAttribute("newMatchUrl", ROOT_URL + NEW_MATCH_URL);
        req.setAttribute("matchesUrl", ROOT_URL + ENDED_MATCHES_URL);
        req.setAttribute("homeUrl", ROOT_URL + WELCOME_URL);
        req.getRequestDispatcher("/WEB-INF/views/new-match.jsp").forward(req, resp);
    }

    private void handleGetMatchScore(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuidParam = req.getParameter("uuid");
        LOG.info(String.format("GET request to %s with uuid param: %s", ROOT_URL + MATCH_SCORE_URL, uuidParam));
        UuidRequest uuidRequest = UuidRequest.builder().uuid(uuidParam).build();
        validator.validate(uuidRequest);
        CurrentMatch match = ongoingMatchesService.get(UUID.fromString(uuidRequest.getUuid()))
                .orElseThrow(() -> new GetMatchException("Match is not found in running matches"));
        req.setAttribute("match", matchMapper.toMatchScoreModel(match));
        req.setAttribute("matchUuid", uuidRequest.getUuid());
        req.setAttribute("matchesUrl", ROOT_URL + ENDED_MATCHES_URL);
        req.setAttribute("homeUrl", ROOT_URL + WELCOME_URL);
        req.getRequestDispatcher("/WEB-INF/views/match-score.jsp").forward(req, resp);
    }

    private void handleGetEndedMatchesUrl(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageNumber = req.getParameter("page");
        String filterByPlayerName = req.getParameter("filter_by_player_name");
        MatchesRequest matchesRequest = MatchesRequest.builder()
                .filterByPlayer(filterByPlayerName == null || filterByPlayerName.isEmpty() ? "" : filterByPlayerName)
                .page(pageNumber == null || pageNumber.isEmpty() ? "1" : pageNumber)
                .build();
        validator.validate(matchesRequest);
        int curPage = Integer.parseInt(matchesRequest.getPage());
        String prevPage = curPage - 1 == 0 ? "1" : String.valueOf(curPage - 1);
        List<Match> matches = matchesPersistenceService.findByNamePaged(curPage, PAGE_SIZE, matchesRequest.getFilterByPlayer());
        req.setAttribute("matches", matches);
        req.setAttribute("matchesUrl", ROOT_URL + ENDED_MATCHES_URL);
        req.setAttribute("homeUrl", ROOT_URL + WELCOME_URL);
        req.setAttribute("findFilter", matchesRequest.getFilterByPlayer());
        req.setAttribute("prevPageUrl", String.format(ROOT_URL + ENDED_MATCHES_URL + "?page=%s&filter_by_player_name=%s", prevPage, matchesRequest.getFilterByPlayer()));
        req.setAttribute("curPage", curPage);
        req.setAttribute("nextPageUrl", String.format(ROOT_URL + ENDED_MATCHES_URL + "?page=%s&filter_by_player_name=%s", curPage + 1, matchesRequest.getFilterByPlayer()));
        req.getRequestDispatcher("/WEB-INF/views/matches.jsp").forward(req, resp);
    }

    private void handlePostNewMatchUrl(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            String p1 = req.getParameter("player1");
            String p2 = req.getParameter("player2");
            LOG.info(String.format("POST request to %s: player1:%s, player2:%s", NEW_MATCH_URL, p1, p2));
            TwoPlayersRequest playerPair = TwoPlayersRequest.builder()
                    .player1(p1)
                    .player2(p2)
                    .build();
            req.setAttribute("player1", playerPair.getPlayer1());
            req.setAttribute("player2", playerPair.getPlayer2());
            validator.validate(playerPair);
            UUID matchUuid = UUID.randomUUID();
            CurrentMatch newMatch = matchesPersistenceService.newMatch(p1, p2);
            ongoingMatchesService.put(matchUuid, newMatch);
            resp.sendRedirect(String.format("%s?uuid=%s", ROOT_URL + MATCH_SCORE_URL, matchUuid));
        } catch (BadPlayerNameException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("matchesUrl", ROOT_URL + ENDED_MATCHES_URL);
            req.setAttribute("homeUrl", ROOT_URL + WELCOME_URL);
            req.setAttribute("newMatchUrl", ROOT_URL + NEW_MATCH_URL);
            req.getRequestDispatcher("/WEB-INF/views/new-match.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Internal server error");
            req.setAttribute("matchesUrl", ROOT_URL + ENDED_MATCHES_URL);
            req.setAttribute("homeUrl", ROOT_URL + WELCOME_URL);
            req.setAttribute("newMatchUrl", ROOT_URL + NEW_MATCH_URL);
            req.getRequestDispatcher("/WEB-INF/views/new-match.jsp").forward(req, resp);
        }
    }

    private void handlePostMatchScore(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uuid = req.getParameter("uuid");
        String playerNumber = req.getParameter("playerNumber");
        LOG.info(String.format("POST request to %s, playerNumber:%s", MATCH_SCORE_URL + "?uuid=" + uuid, playerNumber));
        ScoreRequest matchRequest = ScoreRequest.builder()
                .playerNumber(playerNumber)
                .build();
        UuidRequest uuidRequest = UuidRequest.builder()
                .uuid(uuid)
                .build();
        validator.validate(matchRequest);
        PlayerNumber scoreWinner = parseNumber(matchRequest.getPlayerNumber());
        CurrentMatch match = ongoingMatchesService
                .get(UUID.fromString(uuidRequest.getUuid()))
                .orElseThrow(() -> new PostScoreException("Match is not found in running matches"));
        CurrentMatch calculatedMatch = matchScoreCalculationService.addScore(match, scoreWinner);
        if (isOver(calculatedMatch)) {
            CurrentMatch removedMatch = ongoingMatchesService.remove(UUID.fromString(uuid))
                    .orElseThrow(() -> new PostScoreException("Match is not found in running matches"));
            Match matchToSave = matchMapper.toMatch(removedMatch);
            matchesPersistenceService.save(matchToSave);
            resp.sendRedirect(ROOT_URL + ENDED_MATCHES_URL);
        } else {
            ongoingMatchesService.put(UUID.fromString(uuid), calculatedMatch);
            resp.sendRedirect(String.format("%s?uuid=%s", ROOT_URL + MATCH_SCORE_URL, uuidRequest.getUuid()));
        }
    }

    private PlayerNumber parseNumber(String number) {
        if (number.equals("1")) {
            return PlayerNumber.ONE;
        } else if (number.equals("2")) {
            return PlayerNumber.TWO;
        } else {
            throw new WrongPlayerNumberException("Number" + number + " is not proper player number");
        }
    }

    private boolean isOver(CurrentMatch match) {
        int p1Sets = Math.toIntExact(match.getSets().stream()
                .filter(set -> set.getWinner().equals(PlayerNumber.ONE))
                .count());
        int p2Sets = Math.toIntExact(match.getSets().stream()
                .filter(set -> set.getWinner().equals(PlayerNumber.TWO))
                .count());
        return p1Sets == 2 || p2Sets == 2;
    }
}
