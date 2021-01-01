var stompClient = null;
var roomId = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/topic/greetings', function (greeting) {
        //stompClient.subscribe('/topic/greetings', function (greeting) {
        //stompClient.subscribe('/user/queue/reply', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}



function getRoom() {
//alert("test");
//var connection = new http.Connection('http://localhost:8080');
//var response = connection.getSync('/room');
//$("#greetings").append(response.response);

    $.ajax({
        url: "http://localhost:8080/room"
    }).then(function(data) {
       $("#greetings").append(data.roomId);
       console.log("Room" + data.roomId);
       roomId = data.roomId;
    });
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val(), 'room': roomId}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#getRoom" ).click(function() { getRoom(); });
    $( "#send" ).click(function() { sendName(); });
});