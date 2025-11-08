const stompClient = new StompJs.Client({
    brokerURL: "ws://localhost:8080/events-livechat/websocket",
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log("Connected: " + frame);
    stompClient.subscribe("/topics/livechat", (message) => {
        console.log(JSON.parse(message.body));

        updateLiveChat(JSON.parse(message.body));
    });
    stompClient.publish({
        destination: "/app/new-message",
        body: JSON.stringify({
            sender: "GUEST",
            chatId: "2aaff332-fb3a-4fa3-8bd9-ef550d25c262",
            senderGuestId: "6ea20085-7477-4c98-981d-64eecc8ff0f7",
            content: $("#message").val(),
        }),
    });
    $("#message").val("");
};

stompClient.onWebSocketError = (error) => {
    console.error("Error with websocket", error);
};

stompClient.onStompError = (frame) => {
    console.error("Broker reported error: " + frame.headers["message"]);
    console.error("Additional details: " + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    stompClient.publish({
        destination: "/app/new-message",
        body: JSON.stringify({
            sender: "GUEST",
            chatId: "2aaff332-fb3a-4fa3-8bd9-ef550d25c262",
            senderGuestId: "6ea20085-7477-4c98-981d-64eecc8ff0f7",
            content: $("#message").val(),
        }),
    });
    $("#message").val("");
}

function updateLiveChat(message) {
    document.getElementById("livechat").replaceChildren();

    message.forEach((element) => {
        $("#livechat").append("<tr><td>" + element.content + "</td></tr>");
    });
}

$(function () {
    $("form").on("submit", (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendMessage());
});

connect();
