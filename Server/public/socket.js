// const socket = io();

const text = "sended";

const button = document.getElementById("press");

button.addEventListener("click", async () => {
  fetch("http://127.0.0.1:3000/", {
    method: "POST",
    body: JSON.stringify({ text }),
    headers: {
      "Content-Type": "application/json",
    },
  });
});
