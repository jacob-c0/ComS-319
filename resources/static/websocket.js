var ws;

function connect() {
    var username = document.getElementById("username").value;
    var wsserver = document.getElementById("wsserver").value;
    var url = wsserver + username;
    //var url = "ws://echo.websocket.org";

    ws = new WebSocket(url);

    ws.onmessage = function(event) { // Called when client receives a message from the server
        console.log(event.data);
        console.log("HELLO!???");
        const data = JSON.parse(event.data);
        console.log(data["message"]);

        // display on browser
        var log = document.getElementById("log");
        if (data.type === "message")
        {
            log.innerHTML += data.message + "\n";
        }
        else if (data.type === "userUpdate")
        {
            var activeUsers = document.getElementById("activeUsers");
            while (activeUsers.firstChild)
            {
                activeUsers.removeChild(activeUsers.firstChild);
            }
            data["users"].forEach(function(name){
                if (username === name)
                {
                    return; //its actually continue
                }
               const button = document.createElement("button");
               button.textContent = name;
               button.addEventListener("click", function(){
                   log.innerHTML += "[Now talking to " + button.textContent + "]\n";
                   const data = {type:"userClick",content:button.textContent}
                   ws.send(JSON.stringify(data));
               });
               activeUsers.appendChild(button);

            });
            data["inactiveUsers"].forEach(function(name){
                if (username === name)
                {
                    return; //its actually continue
                }
                const button = document.createElement("button");
                button.textContent = name;
                button.disabled = true;
                activeUsers.appendChild(button);

            });
        }
    };

    ws.onopen = function(event) { // called when connection is opened
        var log = document.getElementById("log");
        log.innerHTML += "Connected to " + event.currentTarget.url + "\n";
    };
}

function send() {  // this is how to send messages
    var content = document.getElementById("msg").value;
    const data = {type:"message",content:content}
    ws.send(JSON.stringify(data));
}
