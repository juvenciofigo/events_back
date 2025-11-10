const stompClient = new StompJs.Client({
    brokerURL: "ws://localhost:8080/events-livechat/websocket",
});

stompClient.onConnect = async (frame) => {
    setConnected(true);
    console.log("Connected: " + frame);
    await stompClient.subscribe("/topics/livechat", (message) => {
        console.log(JSON.parse(message.body));

        updateLiveChat(JSON.parse(message.body));
    });
    stompClient.publish({
        destination: "/app/new-chat",
        // headers: {
        //     Authorization:
        //         "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJlYzgxMTY0ZS0xZjY5LTQ3YmEtOTVjNy1lMDExZjBhNjgyZWIiLCJyb2xlcyI6WyJDTElFTlQiXSwic3ViIjoianV2ZW5jaW9AZ21haWwuY29tIiwiZXhwIjoxNzYyNjU1MzM4LCJpYXQiOjE3NjI1Njg5Mzh9.E1L6klIROsXhIXVGTNaKNcR6T8B0FJoNsORwzg35pN4",
        // },
        body: JSON.stringify({
            type: "GUESTS",
            eventId: "fe3c85b9-5ee5-4e88-b17c-38d65d7afc8c",
            title: "Convidados",
        }),
    });
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
    if ($("#user").val() == null || $("#user").val() == "") {
        return;
    }
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
            chatId: "2aaff332-fb3a-4fa3-8bd9-ef550d25c262",
            content: $("#message").val(),
            senderRole: "GUEST",
            senderId: $("#user").val(),
        }),
    });
    // 6ea20085-7477-4c98-981d-64eecc8ff0f7
}

function updateLiveChat(message) {
    document.getElementById("livechat").replaceChildren();
    console.log(message);

    message.forEach((element) => {
        if (element.senderId === $("#user").val()) {
            $("#livechat").append(
                `<tr>
                <td class="inicio"> Eu: ${element.content} </td>
            </tr>`
            );
        } else {
            $("#livechat").append(
                `<tr>
                <td class="fim"> ${element.senderName}: ${element.content} </td>
            </tr>`
            );
        }
    });
}

$(function () {
    $("form").on("submit", (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendMessage());
});

connect();
