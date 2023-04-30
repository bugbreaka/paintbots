(ns paintbots.client
  "UI to include Logo toy interpreter client on the page."
  (:require [ripley.html :as h]))

(defn client-head []
  (h/html
   [:script {:src "/client/swipl-bundle.js"}])
  (h/html
   [:script
   "new SWIPL({}).then((ok,_) => {
          P = ok.prolog;
          let l = window.location;
          let url = l.protocol+'//'+l.host+'/client/logo.pl';
          P.consult(url)
      });

      function pb_post(form_data) {
          let fd = form_data.map(c=>{
              let a = c.arguments();
              return encodeURIComponent(a[0]) + \"=\" + encodeURIComponent(a[1]);
          }).join(\"&\");
          let l = window.location;
          let url = l.protocol+'//'+l.host+l.pathname;
          return fetch(url, {
              method: \"POST\",
              headers: {\"Content-Type\": \"application/x-www-form-urlencoded\"},
              body: fd});
      }

      window._promises = {};

      function get_input() {
          document.querySelector('#botname').disabled = true;
          document.querySelector('#regbtn').style.visibility = 'hidden';
          return new Promise((resolve) => { window._promises.input = resolve });
      }

      function send_input() {
          let input = document.querySelector(\"#logo\").value;
          let p = window._promises.input;
          delete window._promises.input;
          p(input);
      }
      function maybe_send_input(event) {
          if(event.code=='Enter' && event.ctrlKey) send_input();
      }
      function register() {
         P.forEach('start_repl.');
      }

      function log(msg) {
        let l = document.querySelector('#logs');
        l.innerHTML += `<div>${msg}</div>`;
        l.scrollTop = l.scrollHeight;
      }

      function botinfo(X,Y,C,Ang) {
        document.querySelector('#X').innerHTML = X;
        document.querySelector('#Y').innerText = Y;
        document.querySelector('#C').innerText = C;
        document.querySelector('#Ang').innerText = Ang;
      }
      function get_bot_name() { return document.querySelector(\"#botname\").value.trim(); }
"]))

(defn client-ui []
  (h/html
   [:div.bot
    [:div
     [:b "Bot name: "]
     [:input#botname {:placeholder "mybot123"}]
     [:button#regbtn.btn.btn-sm {:on-click "register()"} "Register"]]
    [:div.flex.w-full
     [:div.grid.flex-grow.card
      [:textarea#logo {:style "font-family: monospace,monospace;" :rows 7 :cols 80
                       :on-key-press "maybe_send_input(event)"} "repeat 5 [fd 25 rt 144]"]
      [:div
       [:button.btn.btn-sm {:on-click "send_input()"} "Execute"]
       " (ctrl-enter)"]]
     [:div.divider.divider-horizontal]
     [:div#logs.flex-grow.card.font-mono {:style "max-width: 50%; max-height: 200px; overflow-y: scroll;"}]
     ]
    [:div.font-mono
     [:div.inline.mx-2 [:b "X: "] [:span#X "N/A"]]
     [:div.inline.mx-2 [:b "Y: "] [:span#Y "N/A"]]
     [:div.inline.mx-2 [:b "C: "] [:span#C "N/A"]]
     [:div.inline.mx-2 [:b "A: "] [:span#Ang "N/A"]]]]))
