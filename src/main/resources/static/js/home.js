$("#footer").hide();
httpCall = function (url, method, data, callback) {
    return $.ajax({
        type: method,
        url: url,
        data: data,
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        success: callback,
        error: callback,
    });
};
var game = null;
fillBoard = function (data) {
    game = data;
    if (data.winner) {
        alert("Finished Win : " + data.winner);
    } else {
        for (let i = 0; i < data.pits.length; i++) {
            $("#" + data.pits[i].id).html(data.pits[i].stones);
        }

        // $("#first-player-big-pit").text(data.pits[6].stones);
        // $("#second-player-big-pit").text(data.pits[13].stones);

        // $("#first-player > span").each(function (i, e) {
        //   console.log(i, e, data.pits[i].stones);
        //   $(e).text(data.pits[i].stones);
        //   $(e).attr("id", i);
        // });
        // $("#second-player   > span").each(function (i, e) {
        //   console.log(i, e, data.pits[7 + i].stones);
        //   $(e).text(data.pits[i + 7].stones);
        //   $(e).attr("id", i + 7);
        // });
    }
};

$(document).ready(function () {
    console.log("init");
    $("#start").bind("click", function () {
        httpCall(
            "http://localhost:9090/api/mancala-game/start/6",
            "GET",
            {},
            function (data) {
                console.log("start game", data);
                $("#start").hide();
                fillBoard(data);
            }
        );
    });
    $("#first-player > span").bind("click", function (e) {
        console.log(e);
        if (game == null) {
            return;
        }
        pitIndex = parseInt($(e.currentTarget).attr("id"));
        httpCall(
            "http://localhost:9090/api/mancala-game/sow",
            "POST",
            JSON.stringify({gameId: game.id, pitId: pitIndex}),
            function (data) {
                if (data.playerTurn != null) {
                    $("#playerTurn").html("player turn : " + data.playerTurn);
                    $("#footer").show();
                }
                console.log("first turn", data);
                if (game.playerTurn != null && game.playerTurn != "PLAYER_A") {
                    alert("Mistake second player Turn!");
                    return;
                }
                fillBoard(data);
            }
        );
    });
    $("#second-player > span").bind("click", function (e) {
        console.log(e);
        if (game == null) {
            return;
        }
        pitIndex = parseInt($(e.currentTarget).attr("id"));
        httpCall(
            "http://localhost:9090/api/mancala-game/sow",
            "POST",
            JSON.stringify({gameId: game.id, pitId: pitIndex}),
            function (data) {
                if (data.playerTurn != null) {
                    $("#playerTurn").html("player turn : " + data.playerTurn);
                    $("#footer").show();
                }
                console.log("second turn", data);
                if (game.playerTurn != null && game.playerTurn != "PLAYER_B") {
                    alert("Mistake first player Turn!");
                    return;
                }
                fillBoard(data);
            }
        );
    });
});
