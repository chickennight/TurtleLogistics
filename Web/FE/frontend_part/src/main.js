import { createApp } from "vue";
import App from "./App.vue";
import vuetify from "./plugins/vuetify";
import { loadFonts } from "./plugins/webfontloader";
import store from "./store";
import router from "./router";

// font-awesome과 관련된 import를 정의
import { library } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faUserSecret, faSort, fas } from "@fortawesome/free-solid-svg-icons";

library.add(faUserSecret, faSort, fas);

loadFonts();

const app = createApp(App);
app.component("font-awesome-icon", FontAwesomeIcon);
app.use(router);
app.use(store);
app.use(vuetify);
app.mount("#app");
